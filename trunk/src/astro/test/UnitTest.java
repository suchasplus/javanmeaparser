package astro.test;

import astro.Astro;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Date;

import user.util.GeomUtil;

public class UnitTest
{
  static boolean wait = false;
  
  private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
  public static String userInput(String prompt)
  {
    String retString = "";
    System.err.print(prompt);
    try
    {
      retString = stdin.readLine();
    }
    catch(Exception e)
    {
      System.out.println(e);
      String s;
      try
      {
        s = userInput("<Oooch/>");
      }
      catch(Exception exception) 
      {
        exception.printStackTrace();
      }
    }
    return retString;
  }

  public static void main2(String[] args)
  {
    System.out.println("--------------");
    System.out.println("Date of easter");
    System.out.println("--------------");
    Date d = Astro.easter(1987);
    System.out.println("Easter 1987:" + d);
    d = Astro.easter(2007);
    System.out.println("Easter 2007:" + d);
    System.out.println("Done");
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("Leap Year");
    System.out.println("--------------");
    System.out.println("Leap 1900=" + Astro.isLeap(1900));
    System.out.println("Leap 2008=" + Astro.isLeap(2008));
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("Date to number");
    System.out.println("--------------");
    System.out.println("21 Apr 2006=" + Astro.dateToNumber(2006, 4, 21));
    System.out.println("17 Feb 1985=" + Astro.dateToNumber(1985, 2, 17));
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 1100 JULDAY");
    System.out.println("--------------");
    System.out.println("Julian Date");
    System.out.println("--------------");
    System.out.println("JD for 1984-Aug-29 = " + Astro.julianDay(1984, 8, 29D));
    
    System.out.println("JD for 1985-Feb-17.25= " + Astro.julianDay(1985, 2, 17.25));
    double dd = Astro.julianDay(1984, 8, 29D);
    System.out.println("JD for 1984-Aug-29= " + dd);
    double dd2 = Astro.julianDay(1900, 1, 0.5);
    System.out.println("JD for 1900-Jan-0.5= " + dd2);
    System.out.println("Diff:" + (dd - dd2));

    double epoch = Astro.julianDay(1990, 1, 0.0);
    double feb_17_1985 = Astro.julianDay(1985, 2, 17.0);
    double diff = feb_17_1985 - epoch;
    System.out.println("1985-Feb-17 vs epoch=" + diff);
    
    Astro.AstronomicDate date = Astro.julianToDate(2446113.75);
    System.out.println("1 - 1985-Feb-17=" + date.toString());
    date = Astro.julianToDate(feb_17_1985);
    System.out.println("2 - 1985-Feb-17=" + date.toString());        
    date = Astro.julianToDate(0.0D);
    System.out.println("JDZero=:" + date.toString());
    
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("Day of Week");
    System.out.println("--------------");
    String dow = Astro.weekdays[Astro.dayOfWeek(Astro.julianDay(1985, 2, 17.0))];
    System.out.println("17 feb 1985=" + dow);
    dow = Astro.weekdays[Astro.dayOfWeek(Astro.julianDay(2007, 3, 6.0))];
    System.out.println("6 mar 2007=" + dow);
    
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("Decimal to HMS");
    System.out.println("--------------");
    dd = Astro.hmsToDecHours(18, 31, 27.0);
    System.out.println("18:31:27=" + dd);    
    Astro.AstronomicTime time = Astro.decHoursToHMS(dd);
    System.out.println(time.toString());
    
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println(" - 1300 GTIME");
    System.out.println("--------------");
    System.out.println("UT to GST");
    System.out.println("--------------");
    date = Astro.julianToDate(2444351.5);
    System.out.println("2444351.5=" + date.toString());
  //  double jd1980_04_22 = julianDay(1980, 4, 22.0);
    double day = 22 + (Astro.hmsToDecHours(14, 36, 51.67) / 24.0);
    time = Astro.UTtoGST(Astro.julianDay(1980, 4, day));
    System.out.println("UT to GST for 1980-Apr-22 14:36:51.67=" + time.toString());
    
    time = Astro.UTtoGST(Astro.julianDay(1984, 8, 31 + (Astro.hmsToDecHours(8, 25, 31) / 24.0)));
    System.out.println("UT to GST for 1984-Aug-31 08:25:31.00=" + time.toString());
    
    System.out.println("UT to GST for 1984-Aug-31 @ 08:25:31  =" + Astro.UTtoGST(Astro.julianDay(1984, 8, 31, 8, 25, 31D)).toString());
    System.out.println("UT to GST for 1901-Nov-14 @ 23:58:00  =" + Astro.UTtoGST(Astro.julianDay(1901, 11, 14, 23, 58, 0D)).toString());
    System.out.println("GST to UT for 1901-Nov-14 @ 00:02:00.0=" + Astro.UTtoGST(Astro.julianDay(1901, 11, 14, 0, 2, 0.0)).toString());
        
    System.out.println("--------------");
    System.out.println("GST to UT");
    System.out.println("--------------");
    day = 22 + (Astro.hmsToDecHours(4, 40, 5.23) / 24.0);
    time = Astro.GSTtoUT(Astro.julianDay(1980, 4, day));
    System.out.println("GST to UT for 1980-Apr-22 04:40:05.23=" + time.toString());
    
    System.out.println("GST to UT for 1984-Aug-31 07:04:19.60=" + 
                       Astro.GSTtoUT(Astro.julianDay(1984, 8, 31, 7, 4, 19.6)).toString());
    System.out.println();                           
    
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 1500 EQHOR ");                       
    System.out.println("--------------");
    System.out.println("Equatorial to Horizontal");
    System.out.println("--------------");
    double position = GeomUtil.sexToDec("51", "15", "00");
    double ha       = GeomUtil.sexToDec("8", "37", "20");
    double decl     = GeomUtil.sexToDec("14", "23", "55");
    double[] result = Astro.equatorialToHorizontal(position, ha, decl);
    System.out.println("From " + GeomUtil.decToSex(position, GeomUtil.SWING, GeomUtil.NS) + " for HA:" + GeomUtil.formatHMS(ha) + " and Decl:" + GeomUtil.formatDMS(decl));
    System.out.println("Z  :" + GeomUtil.formatDMS(result[0]));
    System.out.println("Alt:" + GeomUtil.formatDMS(result[1]));

    position = -GeomUtil.sexToDec("20", "31", "13");
    ha       =  GeomUtil.sexToDec("23", "19", "0");
    decl     = -GeomUtil.sexToDec("43", "00", "00");
    result = Astro.equatorialToHorizontal(position, ha, decl);
    System.out.println("From " + GeomUtil.decToSex(position, GeomUtil.SWING, GeomUtil.NS) + " for HA:" + GeomUtil.formatHMS(ha) + " and Decl:" + GeomUtil.formatDMS(decl));
    System.out.println("Z  :" + GeomUtil.formatDMS(result[0]));
    System.out.println("Alt:" + GeomUtil.formatDMS(result[1]));

    System.out.println("--------------");
    System.out.println("Horizontal to Equatorial");
    System.out.println("--------------");
    position   =  GeomUtil.sexToDec("51", "15", "00");
    double z   =  GeomUtil.sexToDec("310", "15", "33.6");
    double alt = -GeomUtil.sexToDec("10", "58", "20.8");
    result = Astro.horizontalToEquatorial(position, z, alt);
    System.out.println("From " + GeomUtil.decToSex(position, GeomUtil.SWING, GeomUtil.NS) + " for Z:" + GeomUtil.formatDMS(z) + " and Alt:" + GeomUtil.formatDMS(alt));
    System.out.println("Ha  :" + GeomUtil.formatHMS(result[0]));
    System.out.println("Decl:" + GeomUtil.formatDMS(result[1]));

    position = -GeomUtil.sexToDec("20", "31", "13");
    z   =  GeomUtil.sexToDec("161", "23", "19");
    alt =  GeomUtil.sexToDec("65", "56", "6.09");
    result = Astro.horizontalToEquatorial(position, z, alt);
    System.out.println("From " + GeomUtil.decToSex(position, GeomUtil.SWING, GeomUtil.NS) + " for Z:" + GeomUtil.formatDMS(z) + " and Alt:" + GeomUtil.formatDMS(alt));
    System.out.println("Ha  :" + GeomUtil.formatHMS(result[0]));
    System.out.println("Decl:" + GeomUtil.formatDMS(result[1]));

    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 1600 HRANG ");                       
    System.out.println("--------------");
    double longitude = -GeomUtil.sexToDec("10", "0", "0"); // 10 W
    double jday      =  Astro.julianDay(1985, 7, 27, 11, 23, 0);
    // Input RA:
    double ra = GeomUtil.sexToDec("8", "14", "54");    
    ha = GeomUtil.ra2ha(ra, longitude, jday);
    Astro.AstronomicTime at = new Astro.AstronomicTime(ha);
    System.out.println("For RA:" + GeomUtil.formatHMS(ra) + ", Hour Angle:" + at.toString());
    
    // Input Hour Angle
    ha = GeomUtil.sexToDec("22", "48", "27");
    ra = GeomUtil.ha2ra(ha, longitude, jday);
    at = new Astro.AstronomicTime(ra);
    System.out.println("At " + GeomUtil.decToSex(longitude, GeomUtil.SWING, GeomUtil.EW));
    System.out.println("For HA:" + GeomUtil.formatHMS(ha) + ", Right Ascencion:" + at.toString());
    
    longitude =  GeomUtil.sexToDec("0", "0", "0");
    jday      =  Astro.julianDay(1801, 12, 31, 0, 0, 0);
    // Input RA:
    ra = GeomUtil.sexToDec("0", "0", "0");    
    ha = GeomUtil.ra2ha(ra, longitude, jday);
    at = new Astro.AstronomicTime(ha);
    System.out.println("For RA:" + GeomUtil.formatHMS(ra) + ", Hour Angle:" + at.toString());
    
    // Input Hour Angle
    ha = GeomUtil.sexToDec("6", "35", "45");
    ra = GeomUtil.ha2ra(ha, longitude, jday);
    at = new Astro.AstronomicTime(ra);
    System.out.println("At " + GeomUtil.decToSex(longitude, GeomUtil.SWING, GeomUtil.EW));
    System.out.println("For HA:" + GeomUtil.formatHMS(ha) + ", Right Ascencion:" + at.toString());
    
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 1700 OBLIQ ");                       
    System.out.println("--------------");    
    double obliq = Astro.obliquity(Astro.julianDay(1979, 9, 24D));
    System.out.println("Mean Obliquity on 24-Sep-1979 @ 0:0:0 = " + GeomUtil.formatDMS(obliq) + " (" + obliq + ")");
    obliq = Astro.obliquity(Astro.julianDay(1950, 1, 1D));
    System.out.println("Mean Obliquity on  1-Jan-1950 @ 0:0:0 = " + GeomUtil.formatDMS(obliq) + " (" + obliq + ")");
    obliq = Astro.obliquity(Astro.julianDay(2000, 1, 1D));
    System.out.println("Mean Obliquity on  1-Jan-2000 @ 0:0:0 = " + GeomUtil.formatDMS(obliq) + " (" + obliq + ")");
    obliq = Astro.obliquity(Astro.julianDay(2007, 3, 8D));
    System.out.println("Mean Obliquity on  8-Mar-2007 @ 0:0:0 = " + GeomUtil.formatDMS(obliq) + " (" + obliq + ")");

    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 1800 EQECL ");                       
    System.out.println("--------------");    
    jday = Astro.julianDay(1950, 5, 19D);
    ra   = GeomUtil.sexToDec("14", "26", "57");
    decl = GeomUtil.sexToDec("32", "21", "05");
    double[] data = Astro.equ2ecliptic(jday, ra, decl);
    System.out.println("For 19-May-1950, RA:" + GeomUtil.formatDMS(ra) + ", D:" + GeomUtil.formatDMS(decl));
    System.out.println("Ecliptic G:" + GeomUtil.formatDMS(data[0]));
    System.out.println("Ecliptic L:" + GeomUtil.formatDMS(data[1]));
    
    data = Astro.ecliptic2equ(jday, GeomUtil.sexToDec("200", "19", "6.7"), 
                                    GeomUtil.sexToDec("43", "47", "13.8"));
    System.out.println("Right ascencion:" + GeomUtil.formatHMS(data[0]));
    System.out.println("Declination    :" + GeomUtil.formatDMS(data[1]));
    
    System.out.println();
    jday = Astro.julianDay(2004, 5, 28D);
    ra   = GeomUtil.sexToDec("0", "0", "5.5");
    decl = -GeomUtil.sexToDec("87", "12", "12");
    data = Astro.equ2ecliptic(jday, ra, decl);
    System.out.println("For 28-May-2004, RA:" + GeomUtil.formatDMS(ra) + ", D:" + GeomUtil.formatDMS(decl));
    System.out.println("Ecliptic G:" + GeomUtil.formatDMS(data[0]));
    System.out.println("Ecliptic L:" + GeomUtil.formatDMS(data[1]));
    
    data = Astro.ecliptic2equ(jday, GeomUtil.sexToDec("276", "59", "59.2"), 
                                   -GeomUtil.sexToDec("66", "23", "54.7"));
    System.out.println("Right ascencion:" + GeomUtil.formatHMS(data[0]));
    System.out.println("Declination    :" + GeomUtil.formatDMS(data[1]));

    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 1900 EQGAL ");                       
    System.out.println("--------------");    
    data = Astro.equ2galactic(GeomUtil.sexToDec("19", "19", "0"),
                              GeomUtil.sexToDec("66", "30", "0"));
    System.out.println("Galactic G:" + GeomUtil.formatDMS(data[0]));                              
    System.out.println("Galactic L:" + GeomUtil.formatDMS(data[1]));                              

    data = Astro.galactic2equ(GeomUtil.sexToDec("97", "44", "50.2"),
                              GeomUtil.sexToDec("22", "4", "30.5"));
    System.out.println("Right ascension:" + GeomUtil.formatHMS(data[0]));                              
    System.out.println("Declination    :" + GeomUtil.formatHMS(data[1]));                              

    data = Astro.galactic2equ(GeomUtil.sexToDec("187", "20", "12"),
                             -GeomUtil.sexToDec("0", "45", "0"));
    System.out.println("Right ascension:" + GeomUtil.formatHMS(data[0]));                              
    System.out.println("Declination    :" + GeomUtil.formatHMS(data[1]));                              

    data = Astro.equ2galactic(GeomUtil.sexToDec("5", "56", "19"),
                              GeomUtil.sexToDec("22", "14", "13.6"));
    System.out.println("Galactic G:" + GeomUtil.formatDMS(data[0]));                              
    System.out.println("Galactic L:" + GeomUtil.formatDMS(data[1]));                              

    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 2100 RISET ");                       
    System.out.println("--------------");    
    try
    {
      data = Astro.riseAndSet(GeomUtil.sexToDec("52", "15", "0"),  // L
                             -GeomUtil.sexToDec("0", "23", "7"),   // G
                              1987, 10, 4D,                        // Date
                              GeomUtil.sexToDec("12", "16", "0"),  // RA
                              GeomUtil.sexToDec("14", "34", "0"),  // Decl
                              GeomUtil.sexToDec("0", "34", "0"));  // Vertical displacement
      System.out.println("LST rise:" + GeomUtil.formatHMS(data[0]));
      System.out.println("LST set :" + GeomUtil.formatHMS(data[1]));
      System.out.println("GMT rise:" + GeomUtil.formatHMS(data[2]));
      System.out.println("GMT set :" + GeomUtil.formatHMS(data[3]));
      System.out.println("Z rise  :" + GeomUtil.formatDMS(data[4]));
      System.out.println("Z set   :" + GeomUtil.formatDMS(data[5]));
    }
    catch (Exception ex)
    {
      System.out.println(ex.getClass().getName());
      System.out.println(ex.getMessage());
    }

    try
    {
      data = Astro.riseAndSet(GeomUtil.sexToDec("52", "15", "0"),  // L
                             -GeomUtil.sexToDec("0", "23", "7"),   // G
                              1987, 10, 4D,                        // Date
                              GeomUtil.sexToDec("6", "36", "07"),  // RA
                              GeomUtil.sexToDec("87", "21", "10"),  // Decl
                              GeomUtil.sexToDec("0", "0", "0"));  // Vertical displacement
      System.out.println("LST rise:" + GeomUtil.formatHMS(data[0]));
      System.out.println("LST set :" + GeomUtil.formatHMS(data[1]));
      System.out.println("GMT rise:" + GeomUtil.formatHMS(data[2]));
      System.out.println("GMT set :" + GeomUtil.formatHMS(data[3]));
      System.out.println("Z rise  :" + GeomUtil.formatDMS(data[4]));
      System.out.println("Z set   :" + GeomUtil.formatDMS(data[5]));
    }
    catch (Exception ex)
    {
      System.out.println(ex.getClass().getName());
      System.out.println(ex.getMessage());
    }

    try
    {
      data = Astro.riseAndSet(GeomUtil.sexToDec("52", "15", "0"),  // L
                             -GeomUtil.sexToDec("0", "23", "7"),   // G
                              1987, 10, 4D,                        // Date
                              GeomUtil.sexToDec("6", "36", "07"),  // RA
                             -GeomUtil.sexToDec("80", "21", "10"), // Decl
                              GeomUtil.sexToDec("0", "0", "0"));   // Vertical displacement
      System.out.println("LST rise:" + GeomUtil.formatHMS(data[0]));
      System.out.println("LST set :" + GeomUtil.formatHMS(data[1]));
      System.out.println("GMT rise:" + GeomUtil.formatHMS(data[2]));
      System.out.println("GMT set :" + GeomUtil.formatHMS(data[3]));
      System.out.println("Z rise  :" + GeomUtil.formatDMS(data[4]));
      System.out.println("Z set   :" + GeomUtil.formatDMS(data[5]));
    }
    catch (Exception ex)
    {
      System.out.println(ex.getClass().getName());
      System.out.println(ex.getMessage());
    }
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 2300 PRECESS ");                       
    System.out.println("--------------");  
    data = Astro.precess(Astro.julianDay(1950, 1, 0.923),
                         Astro.julianDay(2000, 1, 1.5),
                         GeomUtil.sexToDec("21", "23", "19.27"),
                        -GeomUtil.sexToDec("4", "0", "5"));
    System.out.println("RA at epoch 2: " + GeomUtil.formatHMS(data[0]));                        
    System.out.println("..... and dec: " + GeomUtil.formatDMS(data[1]));
    data = Astro.precess(Astro.julianDay(2000, 1, 1.5),
                         Astro.julianDay(1950, 1, 0.923),                             
                         GeomUtil.sexToDec("21", "25", "55.9"),
                        -GeomUtil.sexToDec("3", "47", "8.1"));
    System.out.println();
    System.out.println("RA at epoch 2: " + GeomUtil.formatHMS(data[0]));                        
    System.out.println("..... and dec: " + GeomUtil.formatDMS(data[1]));
    data = Astro.precess(Astro.julianDay(1950, 1, 1),
                         Astro.julianDay(1979, 1, 6),                             
                         9.178611, //GeomUtil.sexToDec("21", "25", "55.9"),
                         14.390278); //-GeomUtil.sexToDec("3", "47", "8.1"));
    System.out.println();
    System.out.println("RA at epoch 2: " + GeomUtil.formatHMS(data[0]));                        
    System.out.println("..... and dec: " + GeomUtil.formatDMS(data[1]));
//    if (wait) userInput("Hit return");
//    System.out.println("--------------");
//    System.out.println("- 2500 REFRACT ");                       
//    System.out.println("--------------");    
//    data = Astro.refractionTrue2App(GeomUtil.sexToDec("0","10","12"),  // G
//                                    GeomUtil.sexToDec("51","12","13"), // L
//                                 // Astro.UTtoGST(Astro.julianDay(1987,3,23,1,1,24D)).getDecimalHours(),
//                                    Astro.julianDay(1987,3,23,1,1,24D), // Date
//                                    1012D,                              // pressure
//                                    21.7,                               // celcius temp 
//                                    GeomUtil.sexToDec("0","10","29.7"), // ra 
//                                    GeomUtil.sexToDec("40","10","0"));  // decl
//    System.out.println("Alt correction:" + GeomUtil.formatDMS(data[0]));                                    
    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 2700 NUTAT ");                       
    System.out.println("--------------");    
    data = Astro.nutation(Astro.julianDay(1856, 9, 23D));  // date
    System.out.println("On 1856-Sep-23:");
    System.out.println("Nutation in longitude:" + GeomUtil.formatDMS(data[0]));                                    
    System.out.println("Nutation in obliquity:" + GeomUtil.formatDMS(data[1]));                                    
    data = Astro.nutation(Astro.julianDay(2000, 1, 1.5));  // date
    System.out.println("On 2000-Jan-1.5:");
    System.out.println("Nutation in longitude:" + GeomUtil.formatDMS(data[0]));                                    
    System.out.println("Nutation in obliquity:" + GeomUtil.formatDMS(data[1]));                                    

    if (wait) userInput("Hit return");
    System.out.println("--------------");
    System.out.println("- 3100 ANOMALY ");                       
    System.out.println("--------------");    
    data = Astro.anomaly(201.7292, 
                         0.016718);
    System.out.println("Excentric anomaly (deg.) : " + data[0]);
    System.out.println("True anomaly      (deg.) : " + data[1]);
    data = Astro.anomaly(43.7172, 
                         0.965);
    System.out.println("Excentric anomaly (deg.) : " + data[0]);
    System.out.println("True anomaly      (deg.) : " + data[1]);
  }
  
  public static void main(String[] args)
  {
    double d = GeomUtil.sexToDec("37", "46.36");
    System.out.println("Number:" + d);
  }

}
