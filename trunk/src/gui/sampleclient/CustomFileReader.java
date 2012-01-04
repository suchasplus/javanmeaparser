package gui.sampleclient;

import ocss.nmea.api.NMEAReader;
import ocss.nmea.api.NMEAEvent;
import java.util.ArrayList;
import java.io.FileInputStream;

import java.util.List;

import ocss.nmea.api.NMEAListener;

/**
 * A Simulator, taking its inputs from a file
 */
public class CustomFileReader extends NMEAReader
{
  String dataFileName = null;
  public CustomFileReader(List<NMEAListener> al, String fName)
  {
    super(al);
    System.out.println("There are " + al.size() + " listener(s)");
    this.dataFileName = fName;
  }
  
  public void read()
  {
    // Simulation
    super.enableReading();
    String fileName = dataFileName;
    try
    {
      FileInputStream fis = new FileInputStream(fileName);
      while (canRead())
      {
        double size = Math.random();
        int dim = (int)(750 * size);
        byte[] ba = new byte[dim];
        int l = fis.read(ba);
//      System.out.println("Read " + l);
        if (l != -1 && dim > 0)
        {
          String nmeaContent = new String(ba);
//        System.out.println("Spitting out [" + nmeaContent + "]");
          fireDataRead(new NMEAEvent(this, nmeaContent));
          try { Thread.sleep(500); } catch (Exception ignore) {}
        }
        else
        {
          System.out.println("===== Reseting Reader =====");
          fis.close();
          fis = new FileInputStream(fileName);
        }
      }
    }
    catch (Exception e)
    {
     e.printStackTrace();
    }
  }
}