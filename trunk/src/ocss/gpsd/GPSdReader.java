package ocss.gpsd;

import ocss.gpsd.GPSdUtils.GPSDException;
/**
 * An example
 */
public class GPSdReader
  implements GPSdClientInterface
{
  private static GPSdClient client = null;
  private int nbDataRead = 0;
  
  public GPSdReader()
  {
    super();
  }

  public void tpvRead(String data)
  {
    System.out.println(">> TPV [" + data + "] <<");
    try 
    { System.out.println("Parsed:" + GPSdUtils.parseTPV(data).toString()); } 
    catch (GPSDException gpsde) 
    { 
//    System.err.println(gpsde.getLocalizedMessage());
      gpsde.printStackTrace();
    }
    nbDataRead++;
    if (nbDataRead > 10)
    {
      try
      {
        System.out.println("Now closing the client, thank you.");
        client.closeClient();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String[] args) throws Exception
  {
    GPSdReader reader = new GPSdReader();
    client = new GPSdClient(reader, "localhost", 2947, true);  
    System.out.println("Done reading GPSd");
  }
}
