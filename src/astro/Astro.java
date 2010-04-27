package astro;

import java.util.Calendar;
import java.util.Date;

import user.util.GeomUtil;

/**
 * Almanac Calculation
 * 
 * Olivier Le Diouris, 2007
 * olivier@lediouris.net
 * 
 * @deprecated
 */
public class Astro
{
  private final static double J1900 = Astro.julianDay(1900, 1, 0.5);

  public static Date easter(int year)
  {
    Date easter = null;
    
    int a    = year % 19;
    double b = year / 100.0;
    int c    = year % 100;
    int d    = (int)(b / 4);
    int e    = (int)(b % 4);
    int f    = (int)((b + 8) / 25);
    int g    = (int)((b - f + 1) / 3);
    int h    = (int)(((19 * a) + b - d - g + 15) % 30);
    double i = (double)c / 4.0;
    int k    = c % 4;
    int l    = (int)((32 + (2 * e) + (2 * i) - h - k) % 7);
    double m = (a + (11 * h) + (22 * l)) / 451;
    int n    = (int)((h + l - (7 * m) + 114) / 31);
    int p    = (int)((h + l - (7 * m) + 114) % 31);
    int day  = p + 1;
    
    int month = n;
    Calendar cal = Calendar.getInstance();
    cal.set(year, month - 1, day, 0,0,0);
    easter = cal.getTime();    
    
    return easter;
  }
  
  public static boolean isLeap(int year)
  {
    boolean leap = false;
    if (year % 4 == 0)
    {
      leap = true;
      if (year % 100 == 0)
      {
        leap = false;
        if (year % 400 == 0)
        {
          leap = true;
        }
      }
    }
    return leap;
  }
  
  /**
   * Number of days since epoch (1990-1-0)
   * 
   * @param y year, like 1900, -2745, etc.
   * @param m month. 1 for january, 12 for december
   * @param d day. 12 means the 12th.
   * @return guess what!
   */
  public static int dateToNumber(int y, int m, int d) 
  {
    int n = 0;
    if (m > 2) 
    {
      n = (int)(30.6 * (m + 1));   
    }
    else
    {
      int f = isLeap(y)?62:63;
      n = f * (m  -1) / 2;
    }
    n += d;
    // TODO manage the year...  
    
    return n;
  }

  public static double julianDay(int y, int m, int d, int h, int mi, double s)
  {
    return julianDay(y, m, (double)d + (hmsToDecHours(h, mi, s) / 24.0));
  }  
  
  /**
   * Julian Date
   * 
   * @param y year, like 1900, -2745, etc.
   * @param m month. 1 for january, 12 for december
   * @param d day. 12.25 means the 12th at 6am.
   * @return guess what!
   */
  public static double julianDay(int y, int m, double d)
  {
    double jd = 0.0D;
    
    if (m == 1 || m == 2)
    {
      y -=  1;
      m += 12;
    }
    int b = 0;
    if (y >= 1582) // Later than 15-Oct-1582
    {
      if ((y == 1582 && m >= 10) || y > 1582)
      {
        if ((y == 1582 && m == 10 && d >= 15.0) || (y > 1582))
        {
          int a = (y/100);
          b = 2 - a + (a/4);
        }
      }
    }
    
    int c = 0;
    if (y < 0)
      c = (int)((365.25 * y) - 0.75);
    else
      c = (int)(365.25 * y);

    int D = (int)(30.6001 * (m + 1));
    jd = b + c + D + d + 1720994.5;
    
    return jd;
  }
  
  public static AstronomicDate julianToDate(double djd)
  {
    double jd = djd + 0.5D;
    long intpart = (int)jd;
    double f = jd - intpart;
    double b = 0.0D;
    if (intpart > 2299160)
    {
      int a = (int)((intpart - 1867216.25)/36524.25);
      b = intpart + 1 + a - ((int)(a / 4));
    }
    else
      b = intpart;
    int c = (int)(b + 1524);
    int d = (int)((c - 122.1)/365.25);
    int e = (int)(365.25 * d);
    int g = (int)((c - e)/30.6001);
    double day = c - e + f - ((int)(30.6001 * g));
    int month  = (g<13.5)?(g - 1):(g - 13);
    int year   = (month > 2.5)?(d - 4716):(d - 4716);
    
    int _day = (int)day;
    double hours = (day - _day) * 24;
    int _hours = (int)hours;
    double min = (hours - _hours) * 60;
    int _min = (int)min;
    double sec = (min - _min) * 60;
//  int _sec = (int)sec;
    
    return new AstronomicDate(year, month, _day, new AstronomicTime(_hours, _min, sec));
  }
  
  public final static String[] weekdays = {"Sunday", 
                                           "Monday", 
                                           "Tuesday", 
                                           "Wednesday", 
                                           "Thursday", 
                                           "Firday", 
                                           "Saturday"};
  
  public static int dayOfWeek(double jd)
  {
    double a = (jd + 1.5) / 7;
    double fpart = (a - (int)a);
    int n = (int)Math.round(fpart * 7);
    return n; // Sunday:0, Monday:1, ..., Saturday:6
  }
  
  public static double hmsToDecHours(int h, int m, double s)
  {
    double ss = s / 60.0;
    double mm = (m + ss) / 60.0;
    double hh = h + mm;
    return hh;
  }
  
  public static AstronomicTime decHoursToHMS(double d)
  {
    int h = (int)d;
    double m = ((d - h) * 60.0);
    int _m = (int)m;
    double s = ((m - _m) * 60); 
    
    return new AstronomicTime(h, _m, s);
  }
  
  public static AstronomicTime UTtoGST(double jd)
  {
    AstronomicDate originalDate = julianToDate(jd);
//  System.out.println("Orig:" + originalDate.toString());
    
    int year = originalDate.getYear();
    int month = originalDate.getMonth();
    int day = originalDate.getDay();
    
    double s = julianDay(year, month, (double)day);
    s -= 2451545.0;
    double t = s / 36525.0;
    double t0 = 6.697374558 + 
                (t * 2400.051336) + 
                (t * t * 0.000025862);
    while (t0 < 24) t0 += 24;
    while (t0 > 24) t0 -= 24;
    
    // HMS to H.hh
    double h = hmsToDecHours(originalDate.getTime().getHours(),
                             originalDate.getTime().getMinutes(),
                             originalDate.getTime().getSeconds());
//  h = hmsToDecHours(14, 36, 51.67);
    h *= 1.002737909;
    h += t0;
    while (h < 24) h += 24;
    while (h > 24) h -= 24;

    AstronomicTime retDate = decHoursToHMS(h);
    return retDate;
  }
  
  public static AstronomicTime GSTtoUT(double jd)
  {
    AstronomicDate originalDate = julianToDate(jd);
    
    int year = originalDate.getYear();
    int month = originalDate.getMonth();
    int day = originalDate.getDay();
    
    double s = julianDay(year, month, (double)day);
    s -= 2451545.0;
    double t = s / 36525.0;
    double t0 = 6.697374558 +
                (t * 2400.051336) + 
                (t * t * 0.000025862);
    while (t0 < 24) t0 += 24;
    while (t0 > 24) t0 -= 24;
    double h = hmsToDecHours(originalDate.getTime().getHours(),
                             originalDate.getTime().getMinutes(),
                             originalDate.getTime().getSeconds());
    h -= t0;
    while (h < 24) h += 24;
    while (h > 24) h -= 24;
    h *= 0.9972695663;
    
    AstronomicTime retDate = decHoursToHMS(h);

    return retDate;
  }
  
  public static AstronomicTime LSTtoGST(double jd, double lng)
  {
    System.out.println("To be implemented");
    return null;
  }
  
  /**
   * returns azimuth & altitude
   */
  public static double[] equatorialToHorizontal(double geographicalLatitude,
                                                double hourAngle,
                                                double declination)
  {
    double phi = Math.toRadians(geographicalLatitude);
    double cosPhi = Math.cos(phi);
    double sinPhi = Math.sin(phi);
    double x = Math.toRadians(hourAngle * 15D);
    double y = Math.toRadians(declination);
    
    double w = (Math.sin(y) * sinPhi) + (Math.cos(y) * cosPhi * Math.cos(x));
    double q = Math.asin(w); 
    
    double a = cosPhi * Math.cos(q);    
    double p = Math.acos((Math.sin(y) - (sinPhi * w)) / a);
    if (Math.sin(x) > 0)
      p = (2 * Math.PI) - p;
    
    return new double[] {Math.toDegrees(p),  // Z
                         Math.toDegrees(q)}; // Alt
  }
  
  /**
   * returns Ha & Decl
   */
  public static double[] horizontalToEquatorial(double geographicalLatitude,
                                                double z,
                                                double alt)
  {
    double phi = Math.toRadians(geographicalLatitude);
    double cosPhi = Math.cos(phi);
    double sinPhi = Math.sin(phi);
    double x = Math.toRadians(z);
    double y = Math.toRadians(alt);
    
    double w = (Math.sin(y) * sinPhi) + (Math.cos(y) * cosPhi * Math.cos(x));
    double q = Math.asin(w); 
    
    double a = cosPhi * Math.cos(q);    
    double p = Math.acos((Math.sin(y) - (sinPhi * w)) / a);
    if (Math.sin(x) > 0)
      p = (2 * Math.PI) - p;
    
    return new double[] {Math.toDegrees(p) / 15D, // Ha
                         Math.toDegrees(q)};      // D
  }
      
  public static double obliquity(double jd)
  {
//  double j1900 = Astro.julianDay(1900, 1, 0.5);
    double t = (jd - J1900) / 36525D;
    double c = (-0.00181 * t * t * t) + (0.00593 * t * t) + (46.845 * t);
    double eps = 23.45229444 - (c / 3600D);
    return eps;
  }

  /* returns G Ecliptic, L Ecliptic */
  public static double[] equ2ecliptic(double jd, double ra, double decl)
  {
    int dir = 1;
    return eclipticUtil(jd, ra * 15, decl, dir);
  }
  
  /* returns RA, Decl */
  public static double[] ecliptic2equ(double jd, double eg, double el)
  {
    int dir = -1;
    double[] data = eclipticUtil(jd, eg, el, dir);
    return new double[] {data[0] / 15D, data[1]};
  }
  
  public static double[] eclipticUtil(double jd, double x, double y, int dir)
  {
    double obl = obliquity(jd);
    
    double cy = Math.cos(Math.toRadians(y));
    double sy = Math.sin(Math.toRadians(y));
    double ty = Math.tan(Math.toRadians(y));
    
    double cx = Math.cos(Math.toRadians(x));
    double sx = Math.sin(Math.toRadians(x));
    
    double seps = Math.sin(Math.toRadians(obl));
    double ceps = Math.cos(Math.toRadians(obl));
    
    double q = Math.asin((sy * ceps) - (cy * seps * sx * dir));
    double p = Math.atan(((sx * ceps) + (ty * seps * dir)) / cx);
    if (cx < 0) p += Math.PI;
    if (p > (2 * Math.PI)) p -= (2 * Math.PI);
    if (p < 0) p += (2 * Math.PI);
    
    return new double[] {Math.toDegrees(p), Math.toDegrees(q)};
  }
  
  public static double[] equ2galactic(double ra, double decl)
  {
    int dir = 1;
    return galacticUtil(ra * 15D, decl, dir);
  }
  
  public static double[] galactic2equ(double g, double l)
  {
    int dir = -1;
    double[] data = galacticUtil(g, l, dir);
    return new double[] {data[0] / 15D, data[1]};
  }
  
  final static double AN  = 5.759587E-1;
  final static double GPR = 3.355395;
  final static double GPD = 4.782202E-1;
  
  public static double[] galacticUtil(double x, double y, int dir)
  {
    double a = Math.toRadians(x) - AN;
    if (dir == 1) 
      a = Math.toRadians(x) - GPR;
    
    double b = Math.sin(a);
    if (dir == 1) 
      b = Math.cos(a);
    
    double sq = (Math.cos(Math.toRadians(y)) * Math.cos(GPD) * b) + (Math.sin(Math.toRadians(y)) * Math.sin(GPD));
    double q = Math.asin(sq);
    double p = 0D, d = 0D;
    if (dir == 1)
    {
      double c = Math.sin(Math.toRadians(y)) - (sq * Math.sin(GPD));
      d = Math.cos(Math.toRadians(y)) * Math.sin(a) * Math.cos(GPD);
      p = Math.atan(c / d) + AN;
    }
    else
    {
      double c = Math.cos(Math.toRadians(y)) * Math.cos(a);
      d = (Math.sin(Math.toRadians(y)) * Math.cos(GPD)) - (Math.cos(Math.toRadians(y)) * Math.sin(GPD) * Math.sin(a));
      p = Math.atan(c / d) + GPR;
    }
    if (d < 0) p += Math.PI;
    if (p < 0) p += (2 * Math.PI);
    if (p > (2 * Math.PI)) p -= (2 * Math.PI);
    
    return new double[] {Math.toDegrees(p), Math.toDegrees(q)};
  }
  
  /**
   * 
   * @param l     Observer's Geographical Latitude
   * @param g     Observer's Geographical Longitude
   * @param jd    Date (julian)
   * @param ra    Right Ascension
   * @param decl  Declination
   * @param vd    Vertical displacement
   * @return      LST rise, LST set, GMT rise, GMT set, Azimuth rise, Azimuth set 
   */
  public static double[] riseAndSet(double lat,  // Phi
                                    double lng,
                                    int year,
                                    int month,
                                    double day,
                                    double ra,
                                    double decl,
                                    double vd) throws Exception
  {
    double a = Math.cos(Math.toRadians(decl)) * Math.cos(Math.toRadians(lat));
    // If a is too small, throw an exception...
    
    double cospsi = Math.sin(Math.toRadians(lat)) / Math.cos(Math.toRadians(decl));
    if (cospsi > 1) cospsi = 1;
    if (cospsi < -1) cospsi = -1;
    double psi = Math.acos(cospsi);

    double dh = Math.toRadians(vd) * Math.sin(psi);
    double y1 = Math.toRadians(decl) + (Math.toRadians(vd) * cospsi);
    double ty = Math.tan(y1);
    double ch = -(Math.sin(Math.toRadians(lat)) * ty) / Math.cos(Math.toRadians(lat));
    
    if (ch < -1) { throw new CircumPolarException("Cos:" + Double.toString(ch)); }
    if (ch > 1)  { throw new NeverRiseException("Cos:" + Double.toString(ch)); }
    
    double h = Math.acos(ch);
    h += dh;
    h *= 3.819718634;
    double lstr = 24D + ra - h;    
    double lsts = ra + h;
    
    double azr = Math.acos(Math.sin(y1) / Math.cos(Math.toRadians(lat)));
    double azs = (2 * Math.PI) - azr;

    if (lstr < 0)   lstr += 24D;
    if (lstr > 24D) lstr -= 24D;

    if (lsts < 0)   lsts += 24D;
    if (lsts > 24D) lsts -= 24D;
    
    if (azr < 0)             azr += (2 * Math.PI);
    if (azr > (2 * Math.PI)) azr -= (2 * Math.PI);
    
    if (azs < 0)             azs += (2 * Math.PI);
    if (azs > (2 * Math.PI)) azs -= (2 * Math.PI);
    
    return new double[] { 
                          lstr, 
                          lsts, 
                          Astro.GSTtoUT(Astro.julianDay(year, month, day + ((lstr - (lng / 15)) / 24D))).getDecimalHours(), 
                          Astro.GSTtoUT(Astro.julianDay(year, month, day + ((lsts - (lng / 15)) / 24D))).getDecimalHours(), 
                          Math.toDegrees(azr), 
                          Math.toDegrees(azs) 
                        };
  }
  
  // TODO verify this one.
  public static double[] precess(double from, double to, double ra, double dec)
  {
    double epoch1900 = Astro.julianDay(1900, 1, 0.5);
    
    double t1 = (from - epoch1900) / 36525D;
    double t2 = (to - epoch1900) / 36525D;
    double nyrs = (to - from) / 365.2425;
    
    double ma = 3.07324 + (1.86E-3 * t1);
    double mb = 3.07324 + (1.86E-3 * t2);
    double na = 20.0468 - (8.5E-3 * t1);
    double nb = 20.0468 - (8.5E-3 * t2);
    
    double m = (ma + mb) / 2D;
    double n = (na + nb) / 2D;
    
    double ddx = (m + (n * Math.cos(Math.toRadians(ra * 15D)) * Math.tan(Math.toRadians(dec)) / 15D)) * 7.272205E-5 * nyrs;
    double ddy = n * Math.cos(Math.toRadians(ra * 15D)) * 4.848137E-6 * nyrs;
    
    double x = Math.toRadians(ra * 15D) + ddx;
    double y = Math.toRadians(dec) + ddy;
    
    if (x > (2 * Math.PI)) x -= (2 * Math.PI);
    if (x < 0) x += (2 * Math.PI);
    
    return new double[] { Math.toDegrees(x / 15D), Math.toDegrees(y) };
  }
  
  public static double[] refractionTrue2App(double lng,
                                            double lat,
                                            double gst,
                                            double pressure,
                                            double tempcelcius,
                                            double ra,
                                            double decl)
  {
    int dir = 1;  
    return refraction(lng, lat, gst, pressure, tempcelcius, ra, decl, dir);
  }
  
  public static double[] refractionApp2True(double lng,
                                            double lat,
                                            double gst,
                                            double pressure,
                                            double tempcelcius,
                                            double ra,
                                            double decl)
  {
    int dir = -1;  
    return refraction(lng, lat, gst, pressure, tempcelcius, ra, decl, dir);
  }
  
  private final static int KELVIN = 273;
  
  // TODO Finish it.
  public static double[] refraction(double lng,
                                    double lat,
                                    double gst,
                                    double pressure,
                                    double tempcelcius,
                                    double x,
                                    double y,
                                    int dir)
  {
    double r  = 0D;
    double y_ = 0D;
    
    if (dir == 1) // true to apparent
    {
      double[] data = Astro.equatorialToHorizontal(lat, x, y);
      double alt   = data[1];
      //  double z     = data[0];
      System.out.println("(Alt:" + GeomUtil.formatDMS(alt) + ")");
      if (Math.toRadians(y) > 2.617994e-1)
        r = dir * 7.888888e-5 * pressure / ((KELVIN + tempcelcius) * Math.tan(Math.toRadians(alt)));
      else
      {
        if (Math.toRadians(alt) < -8.7e-2)
          r = 0D;
        else
        {
          double yd = Math.toRadians(alt) * 5.729578e1;
          double a = ((2e-5 * yd + 1.96e-2) * yd + 1.594e-1) * pressure;
          double b = (KELVIN + tempcelcius) * ((8.45e-2 * yd + 5.05e-1) * yd + 1);
          r = (a / b) * 1.745329e-2 * dir;
        }
      }
      y_ = Math.toRadians(alt) + r;
    }
    else          // apparent to true
    {
      double ha = GeomUtil.ra2ha(x, lng, gst); // GST ?
      double[] data = Astro.horizontalToEquatorial(lat, ha * 15D, y);
      double alt   = data[1];
      //  double z     = data[0];
      System.out.println("(Alt:" + GeomUtil.formatDMS(alt) + ")");
      double y1 = Math.toRadians(alt);
      double r1 = 0D;
      boolean loop = true;
      while (loop)
      {
        y_ = y1 + r1;
        if (Math.toRadians(y) > 2.617994e-1)
          r = dir * 7.888888e-5 * pressure / ((KELVIN + tempcelcius) * Math.tan(Math.toRadians(alt)));
        else
        {
          if (Math.toRadians(alt) < -8.7e-2)
            r = 0D;
          else
          {
            double yd = Math.toRadians(y) * 5.729578e1;
            double a = ((2e-5 * yd + 1.96e-2) * yd + 1.594e-1) * pressure;
            double b = (KELVIN + tempcelcius) * ((8.45e-2 * yd + 5.05e-1) * yd + 1);
            r = (a / b) * 1.745329e-2 * dir;
          }
        }
        double r2 = r;
        if (r2 != 0)
        {
          if (Math.abs(r2 - r1) > 1e-6)
          {
            r1 = r2;             
          }
          else
            loop = false;
        } 
      }
    }
    return new double[] { Math.toDegrees(r), 0.0, 0.0 };
  }
  
  public static double[] nutation(double jd)
  {
//  double j1900 = Astro.julianDay(1900, 1, 0.5);
    double t = (jd - J1900) / 36525D;
//  double t = date / 36525D;
    double t2 = t * t;
    double a = 1.000021358e2 * t;
    double b = 360D * (a - (int)a);
    double ls = 2.796967e2 + 3.03e-4 * t2 + b;
    a = 1.336855231e3 * t;
    b = 360D * (a - (int)a);
    double ld = 2.704342e2 - 1.133e-3 * t2 + b;
    a = 9.999736056e1 * t;
    b = 360D * (a - (int)a);
    double ms = 3.584758e2 - 1.5e-4 * t2 + b;
    a = 1.325552359e7 * t;
    b = 360D * (a - (int)a);
    double md = 2.961046e2 + 9.192e-3 * t2 + b;
    a = 5.372616667 * t;
    b = 360D * (a - (int)a);
    double nm = 2.591833e2 + 2.078e-3 * t2 - b;
    double tls = 2 * Math.toRadians(ls);
    double tnm = 2 * Math.toRadians(nm);
    double tld = 2 * Math.toRadians(ld);
    nm = Math.toRadians(nm);
    ms = Math.toRadians(ms);
    md = Math.toRadians(md);
    
    double dpsi = (-17.2327 - 1.737e-2 * t) * Math.sin(nm) +
                  (-1.2729 - 1.3e-4 * t) * Math.sin(tls) +
                  2.088e-1 * Math.sin(tnm) - 2.037e-1 * Math.sin(tld) +
                  (1.261e-1 - 3.1e-4 * t) * Math.sin(ms) +
                  6.75e-2 * Math.sin(md) -
                  (4.97e-2 - 1.2e-4 * t) * Math.sin(tls + ms) -
                  3.42e-2 * Math.sin(tld - nm) - 2.61e-2 * Math.sin(tld + md) +
                  2.14e-2 * Math.sin(tls - ms) -
                  1.49e-2 * Math.sin(tls - tld + md) 
                  + 1.24e-2 * Math.sin(tls - nm) + 1.14e-2 * Math.sin(tld - md);
    double deps = (9.21 + 9.1e-4 * t) * Math.cos(nm) +
                  (5.522e-1 - 2.9e-4 * t) * Math.cos(tls) -
                  9.04e-2 * Math.cos(tnm) + 8.84e-2 * Math.cos(tld) +
                  2.16e-2 * Math.cos(tls + ms) + 1.83e-2 * Math.cos(tld - nm) +
                  1.13e-2 * Math.cos(tld + md) - 9.3e-3 * Math.cos(tls - ms) -
                  6.6e-3 * Math.cos(tls - nm);
    
    return new double[] {dpsi / 3600D, deps / 3600D};
  }
  
  // TODO finish this one.
  public static double horParallax(double lat, double lng, double eyeHeight, double ehp, double ra, double decl)
  {
    double u = Math.atan(9.96647e-1 * Math.tan(Math.toRadians(lat)));
    double rsp = (9.96647e-1 * Math.sin(u)) + ((eyeHeight / 6378140) * Math.sin(Math.toRadians(lat)));
    double rcp = Math.cos(u) + ((eyeHeight / 6378140) * Math.cos(Math.toRadians(lat)));
    double rp = 1 / Math.sin(Math.toRadians(ehp));
    
    return 0D;
  }
  
  public static double[] anomaly(double ma, double excentricity)
  {
    double ea = 0D, nu = 0D;
    double m = Math.toRadians(ma) - (2 * Math.PI) * (int)(Math.toRadians(ma) / (2 * Math.PI));
    ea = m;
    boolean loop = true;
    while (loop)
    {
      double dla = ea - (excentricity * Math.sin(ea)) - m;
      if (Math.abs(dla) < 1e-6) 
        loop = false;
      else
      {
        dla = dla / (1 - Math.cos(ea));
        ea -= dla;
      }
    }
    
    double tnu2 = Math.sqrt((1 + excentricity) / (1 - excentricity)) * Math.tan(ea / 2);
    nu = 2 * Math.atan(tnu2);
    
    return new double[] { Math.toDegrees(ea), Math.toDegrees(nu) };
  }
  
  public static double sun(double jd)
  {
    double d = 0D;
    
    double t = jd / 36525D;
    
    return d;
  }
  
  public static class RisetException extends Exception
  {
    public RisetException(String str)
    {
      super(str);
    }
  }
  
  public static class CircumPolarException extends RisetException
  {
    public CircumPolarException(String str)
    {
      super(str);
    }
  }
  
  public static class NeverRiseException extends RisetException
  {
    public NeverRiseException(String str)
    {
      super(str);
    }
  }
  
  public static class AstronomicTime
  {
    private int hours;
    private int minutes;
    private double seconds;
    
    public AstronomicTime(int h, int m, double s)
    {
      hours = h;
      minutes = m;
      seconds = s;
    }
    
    public AstronomicTime(double d)
    {
      hours = (int)d;
      minutes = (int)(60 * (d - hours));
      seconds = 3600D * (d - (hours + (minutes / 60D)));
    }
    
    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }
    public double getSeconds() { return seconds; }
    
    public double getDecimalHours()
    {
      double d = (double)hours + (minutes / 60D) + (seconds / 3600D); 
      return d;
    }
    
    public String toString()
    {
      return Integer.toString(hours) + ":" +
             Integer.toString(minutes) + ":" +
             Double.toString(seconds);
    }
  }
  
  public static class AstronomicDate
  {
    private int year;
    private int month;
    private int day;
    private AstronomicTime at;
    
    public AstronomicDate(int y, int m, int d, AstronomicTime at)
    {
      year = y;
      month = m;
      day = d;
      this.at = at;
    }
    
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public AstronomicTime getTime() { return at; }
    
    public String toString()
    {
      return Integer.toString(year) + "-" +
             Integer.toString(month) + "-" +
             Integer.toString(day) + " " +
             at.toString();
    }
  }
}
