package gui.sampleclient;

import java.util.List;

import java.util.Map;

import ocss.nmea.api.NMEAClient;
import ocss.nmea.api.NMEAEvent;
import ocss.nmea.parser.GSA;
import ocss.nmea.parser.GeoPos;
import ocss.nmea.parser.RMC;
import ocss.nmea.parser.SVData;
import ocss.nmea.parser.StringParsers;
import ocss.nmea.parser.UTC;
import ocss.nmea.parser.Wind;

public class CustomClient4Frame extends NMEAClient 
{
  private TablePane tablePane = null;
  
  public CustomClient4Frame(TablePane tp)
  {
    this.tablePane = tp;
    init();
  }

  private void init()
  {
  }

  public void setTablePanel(TablePane tp)
  { this.tablePane = tp; }
  
  public void dataDetectedEvent(NMEAEvent e)
  {
    String val = e.getContent();
    if (StringParsers.validCheckSum(val))
    {
      try { displayValues(val); }
      catch (Exception ex) { ex.printStackTrace(); }
    }
    else
    {
      System.out.println("Invalid Data:" + val);
    }
  }

  private void displayValues(String s) throws Exception
  {
//  System.out.println("Displaying [" + s + "]");
    String key = s.substring(3, 6); // Don't include the prefix
    String data = s;
    if (key.equals("GLL"))
    {
      try
      {
        GeoPos gp = (GeoPos)StringParsers.parseGLL(s)[StringParsers.GP_in_GLL];
        if (gp != null)
        {
          data = gp.getLatInDegMinDec() + ", " + gp.getLngInDegMinDec();
          System.out.println("Position:" + data);
        }
      }
      catch (NullPointerException npe)
      {
        System.out.println("ParseGLL->npe");        
      }
    }
    else if (key.equals("HDM"))
    {
      data = Integer.toString(StringParsers.parseHDM(s));
      System.out.println("Heading:" + data);
    }
    else if (key.equals("MWV"))
    {
      Wind w = StringParsers.parseMWV(s);
      data = w.angle + ", " + w.speed;
      System.out.println("App Wind:" + data);
    }
    else if (key.equals("VHW"))
    {
      data = Double.toString(StringParsers.parseVHW(s)[StringParsers.BSP_in_VHW]);
      System.out.println("Speed:" + data);
    }
    else if (key.equals("DBT"))
    {
      data = Float.toString(StringParsers.parseDBT(s, StringParsers.DEPTH_IN_METERS));
      System.out.println("Depth:" + data);
    }
    else if (key.equals("RMC"))
    {
      RMC rmc = StringParsers.parseRMC(s);
      if (rmc != null)
        System.out.println("Pos:" + rmc.getGp());
    }
    else if (key.equals("GGA"))
    {
      List<Object> al = StringParsers.parseGGA(s);
      System.out.println("--- Parsed GGA ---");
      UTC utc = (UTC)al.get(0);
      GeoPos pos = (GeoPos)al.get(1);
      Integer nbs = (Integer)al.get(2);
      System.out.println("UTC:" + utc.toString());
      System.out.println("Pos:" + pos.toString());
      System.out.println(nbs.intValue() + " Satellite(s) in use");
    }
    else if (key.equals("GSV"))
    {
      Map<Integer, SVData> hm = StringParsers.parseGSV(s);
      if (hm == null)
        System.out.println("GSV wait...");
      else
      {
        System.out.println(hm.size() + " Satellites in view:");
        for (Integer sn : hm.keySet())
        {
          SVData svd = hm.get(sn);
          System.out.println("Satellite #" + svd.getSvID() + " Elev:" + svd.getElevation() + ", Z:" + svd.getAzimuth() + ", SNR:" + svd.getSnr() + "db");
        }
      }
    }
    else if (key.equals("GSA"))
    {
      try
      {
        GSA gsa = StringParsers.parseGSA(s);
        System.out.println("- Mode: " + (gsa.getMode1().equals(GSA.ModeOne.Auto)?"Automatic":"Manual"));
        System.out.println("- Mode: " + (gsa.getMode2().equals(GSA.ModeTwo.NoFix)?"No Fix":(gsa.getMode2().equals(GSA.ModeTwo.TwoD)?"2D":"3D")));
        System.out.println("- Sat in View:" + gsa.getSvArray().size());
      }
      catch (ArrayIndexOutOfBoundsException aioobe)
      {
        System.out.println("ParseGSA->ArrayIndexOutOfBoundsException");
      }
    }
    else
    {
      System.out.println(key + " not parsed here.");
    }
//  System.out.println(key + " [" + s + "]");  
    this.tablePane.setValue(key, s);
  }
}