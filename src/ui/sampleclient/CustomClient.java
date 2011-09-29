package ui.sampleclient;

import gui.sampleclient.CustomSerialReader;

import ocss.nmea.api.NMEAClient;
import ocss.nmea.api.NMEAEvent;
import ocss.nmea.api.NMEAListener;

public class CustomClient extends NMEAClient
{
  public CustomClient(String s, String[] sa)
  {
    super(s, sa);
  }
  
  public void dataDetectedEvent(NMEAEvent e)
  {
    System.out.println("Received:" + e.getContent());
  }

  private static CustomClient customClient = null;  
  public static void main(String[] args)
  {
//  String prefix = "II";
//  String[] array = {"HDM", "GLL", "XTE", "MWV", "VHW"};
    String prefix = "GP";
    String[] array = {"GVS", "GLL", "RME", "GSA", "RMC"};
    customClient = new CustomClient(prefix, array);
      
    Runtime.getRuntime().addShutdownHook(new Thread() 
      {
        public void run() 
        {
          System.out.println ("Shutting down nicely.");
          customClient.stopDataRead();
        }
      });    
    customClient.setEOS("\n");
    customClient.initClient();
//  customClient.setReader(new CustomReader(customClient.getListeners()));
    customClient.setReader(new CustomSerialReader(customClient.getListeners()));
    customClient.startWorking();
  }

  private void stopDataRead()
  {
    if (customClient != null)
    {
      for (NMEAListener l : customClient.getListeners())
        l.stopReading(new NMEAEvent(this));
    }
  }
}