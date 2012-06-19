package ocss.gpsd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;
import java.net.SocketException;

public class GPSdClient
{
  private GPSdClientInterface parent;
  private String hostName = "";
  private int port = 2497;
  private Socket clientSocket = null;
  BufferedReader inFromServer = null;
  
  private boolean keepReading = true;
  private boolean verbose = false;
  
  public GPSdClient(GPSdClientInterface client, String host, int port) throws Exception
  {
    this(client, host, port, false);
  }
  public GPSdClient(GPSdClientInterface client, String host, int port, boolean v) throws Exception
  {
    this.parent = client;
    this.hostName = host;
    this.port = port;
    this.verbose = v;
    try
    {
      Thread readerThread = new Thread()
        {
          public void run()
          {
            if (verbose)
              System.out.println(".... TPV Reader is starting ....");
            try
            {
              synchronized (this) { this.wait(); }
            }
            catch (Exception ex)
            {
              ex.printStackTrace();
            }
            if (verbose)
              System.out.println(".... Now expecting TPV ....");
            while (keepReading)
            {
              try
              {
                String tpv = inFromServer.readLine();
                parent.tpvRead(tpv);
              }
              catch (SocketException se)
              {
                System.err.println(se.getLocalizedMessage());
              }
              catch (IOException ioe)
              {
                System.err.println(ioe.getLocalizedMessage());
              }
            }
          }
        };
      readerThread.start();
      initiateProtocol();
      synchronized (readerThread) { readerThread.notify(); }
      if (verbose)
        System.out.println(">>>>> Connection established, now expecting TPV strings <<<<<<");
    }
    catch (Exception ex)
    {
      throw ex;
    }
  }

  private void initiateProtocol() throws Exception
  {
    if (verbose)
      System.out.println(".... Initiating protocol ....");
    clientSocket = new Socket(this.hostName, this.port);
    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

    // Step 1
    String query = GPSdUtils.VERSION;
    outToServer.writeBytes(query + '\n');
    String response = inFromServer.readLine();
    // Version comes back
    if (verbose)
      System.out.println(">> Version from Server << " + response);
    // Step 2
    query = "?WATCH={\"json\":false,\"enable\":true,\"class\":\"WATCH\"}";
    outToServer.writeBytes(query + '\n');
    response = inFromServer.readLine();
    // Devices
    if (verbose)
      System.out.println(">> Devices from Server << " + response);
    response = inFromServer.readLine();
    // Watch
    if (verbose)
      System.out.println(">> Watch from server << " + response);
    // Step 3
    query = GPSdUtils.POLL;
    outToServer.writeBytes(query + '\n');
    // Ready for TPV
  }

  public void closeClient() throws Exception
  {
    keepReading = false;
    clientSocket.close();
  }

  public void setVerbose(boolean verbose)
  {
    this.verbose = verbose;
  }
}
