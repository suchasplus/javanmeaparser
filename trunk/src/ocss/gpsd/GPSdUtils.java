package ocss.gpsd;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.HashMap;

import ocss.nmea.parser.GeoPos;
import ocss.nmea.parser.RMC;

public class GPSdUtils
{
  private final static SimpleDateFormat TO_DURATION = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");

  /**
   * TVP Track Velocity Position
   * 
   * @param dataDate UTC Date of the data
   * @param gp Position
   * @param cog Course Over Ground
   * @param sog Speed Orev Ground
   * @return The expected json TVP String, like
   * {"class":"TPV","tag":"MID2","time":"2010-04-30T11:48:20.10Z","ept":0.005,"lat":46.498204497,"lon":7.568061439,"alt":1327.689,"epx":15.319,"epy":17.054,"epv":124.484,"track":10.3797,"speed":0.091,"climb":-0.085,"eps":34.11,"mode":3}
   */
  public static String produceTPV(Date dataDate, GeoPos gp, double cog, double sog)
  {
    String gpsdTPVString = "";
    String time = TO_DURATION.format(dataDate);
    gpsdTPVString = "{\"class\":\"TPV\",\"tag\":\"MID2\",\"time\":\"" + time + 
                    "\",\"ept\":0.00,\"lat\":" + gp.lat + 
                    ",\"lon\":" + gp.lng + 
                    ",\"alt\":0.0,\"epx\":0.0,\"epy\":0.0,\"epv\":0.0,\"track\":" + cog + 
                    ",\"speed\":" + sog + 
                    ",\"climb\":0.0,\"eps\":0.0,\"mode\":3}";
    return gpsdTPVString;
  }
  
  public static RMC parseTPV(String gpsdTPVString) throws GPSDException
  {
    RMC rmc = null;
    if (!gpsdTPVString.trim().startsWith("{") || !gpsdTPVString.trim().endsWith("}"))
      throw new GPSDException("Bad TVP string\n" + gpsdTPVString + "\nShould start and end with curly barces.");
    else
      gpsdTPVString = gpsdTPVString.substring(1, gpsdTPVString.length() - 1);
    String[] splitted = gpsdTPVString.split(",");
    HashMap<String, String> tpvMap = new HashMap<String, String>();
    for (String s : splitted)
    {
      String[] nvp = s.split(":", 2);
      tpvMap.put(dropQuotes(nvp[0]), dropQuotes(nvp[1]));
//    System.out.println(dropQuotes(nvp[0]) + " => " + dropQuotes(nvp[1]));
    }
    if (!"TPV".equals(tpvMap.get("class")))
      throw new GPSDException("Bad class name, expected TPV");
    
    rmc = new RMC();
    try 
    {
      rmc.setGp(new GeoPos(Double.parseDouble(tpvMap.get("lat")),
                           Double.parseDouble(tpvMap.get("lon"))));
      rmc.setCog(Double.parseDouble(tpvMap.get("track")));
      rmc.setSog(Double.parseDouble(tpvMap.get("speed")));
      rmc.setRmcDate(TO_DURATION.parse(tpvMap.get("time")));
    }
    catch (Exception ex)
    {
      throw new GPSDException(ex);
    }
     
    return rmc;
  }
  
  private final static String dropQuotes(String str)
  {
    if (str.startsWith("\""))
      str = str.substring(1);
    if (str.endsWith("\""))
      str = str.substring(0, str.length() - 1);
    return str;
  }
  
  public static class GPSDException extends Exception
  {
    public GPSDException()
    {      
    }
    
    public GPSDException(String cause)
    {
      super(cause);
    }

    public GPSDException(Exception cause)
    {
      super(cause);
    }
  }
  
  /**
   * For tests
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    String tpv = "{\"class\":\"TPV\",\"tag\":\"MID2\",\"time\":\"2010-04-30T11:48:20.10Z\",\"ept\":0.005,\"lat\":46.498204497,\"lon\":7.568061439,\"alt\":1327.689,\"epx\":15.319,\"epy\":17.054,\"epv\":124.484,\"track\":10.3797,\"speed\":0.091,\"climb\":-0.085,\"eps\":34.11,\"mode\":3}";
    System.out.println("Parsing " + tpv);
    RMC rmc = parseTPV(tpv);
    System.out.println(rmc.toString());
    System.out.println("Generating:");
    tpv = produceTPV(new Date(), new GeoPos(37.5, -122.2345), 210d, 5.6);
    System.out.println(tpv);
    
    System.out.println("Done.");
  }
}
