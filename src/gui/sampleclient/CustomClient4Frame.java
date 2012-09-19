package gui.sampleclient;

import java.util.List;

import ocss.nmea.api.NMEAClient;
import ocss.nmea.api.NMEAEvent;
import ocss.nmea.parser.GSA;
import ocss.nmea.parser.GeoPos;
import ocss.nmea.parser.RMC;
import ocss.nmea.parser.StringParsers;
import ocss.nmea.parser.Wind;


public class CustomClient4Frame extends NMEAClient 
{
  private TablePane tablePane = null;
  
  public CustomClient4Frame(String s, String[] sa)
  {
    super(s, sa);
    init();
  }

  public CustomClient4Frame(String s, String[] sa, TablePane tp)
  {
    super(s, sa);
    this.tablePane = tp;
    init();
  }

  private void init()
  {
//    super.addNMEAListener(new NMEAListener()
//      {
//        public void dataRead(NMEAEvent e)
//        {
//          dataDetectedEvent(e);
//        }
//      });
  }

  public void setTablePanel(TablePane tp)
  { this.tablePane = tp; }
  
  public void dataDetectedEvent(NMEAEvent e)
  {
    String val = e.getContent();
    try { displayValues(val); }
    catch (Exception ex) { ex.printStackTrace(); }
  }

  private void displayValues(String s) throws Exception
  {
//  System.out.println("Displaying [" + s + "]");
    String key = s.substring(3, 6);
    String data = s;
    if (key.equals("GLL"))
    {
      GeoPos gp = (GeoPos)StringParsers.parseGLL(s)[StringParsers.GP_in_GLL];
      if (gp != null)
      {
        data = gp.getLatInDegMinDec() + ", " + gp.getLngInDegMinDec();
        System.out.println("Position:" + data);
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
      System.out.println("Pos:" + rmc.getGp());
    }
    else if (key.equals("GGA"))
    {
      List<Object> ol = StringParsers.parseGGA(data);
      System.out.println("Parsed GGA");
    }
    else
    {
      System.out.println(key + " not parsed here.");
    }
//  this.tablePane.setValue(key, s);
    this.tablePane.setValue(key, data);
  }
}