package astro.calc;

import java.util.Vector;
import user.util.GeomUtil;

public final class GreatCircle
{
  public static final int TO_NORTH = 0;
  public static final int TO_SOUTH = 1;
  public static final int TO_EAST = 2;
  public static final int TO_WEST = 3;
  private int ewDir;
  private int nsDir;
  private GeoPoint start;
  private GeoPoint arrival;
  private Vector<GreatCircleWayPoint> route;
  private double rv;
  private double dLoxo;
  private static final double TOLERANCE = 1D;

  public GreatCircle()
  {
    start = null;
    arrival = null;
    rv = 0.0D;
    dLoxo = 0.0D;
  }

  public void setStart(GeoPoint p)
  {
    start = p;
  }

  public void setStartInDegrees(GeoPoint p)
  {
    start = new GeoPoint(Math.toRadians(p.getL()),
                         Math.toRadians(p.getG()));
  }

  public void setArrival(GeoPoint p)
  {
    arrival = p;
  }

  public void setArrivalInDegrees(GeoPoint p)
  {
    arrival = new GeoPoint(Math.toRadians(p.getL()),
                           Math.toRadians(p.getG()));
  }

  public GeoPoint getStart()
  {
    return start;
  }

  public GeoPoint getArrival()
  {
    return arrival;
  }

  public int getNS()
  {
    return nsDir;
  }

  public int getEW()
  {
    return ewDir;
  }

  public void calculateGreatCircle(int nbPoints)
  {
    if(arrival.getL() > start.getL())
      nsDir = TO_NORTH;
    else
      nsDir = TO_SOUTH;
    if(arrival.getG() > start.getG())
      ewDir = TO_EAST;
    else
      ewDir = TO_WEST;
    if (Math.abs(arrival.getG() - start.getG()) > Math.PI)
    {
      if (ewDir == TO_EAST)
      {
        ewDir = TO_WEST;
        arrival.setG(arrival.getG() - (2 * Math.PI));
      } 
      else
      {
        ewDir = TO_EAST;
        arrival.setG((2 * Math.PI) + arrival.getG());
      }
    }
    double deltaG = arrival.getG() - start.getG();
    route = new Vector<GreatCircleWayPoint>(nbPoints);
    double interval = deltaG / (double)nbPoints;
    GeoPoint smallStart = start;
    boolean go = true;
    for (double g = start.getG(); route.size() <= nbPoints; g += interval)
    {
      double deltag = arrival.getG() - g;
      double tanStartAngle = Math.sin(deltag) / (Math.cos(smallStart.getL()) * Math.tan(arrival.getL()) - Math.sin(smallStart.getL()) * Math.cos(deltag));
      double smallL = Math.atan(Math.tan(smallStart.getL()) * Math.cos(interval) + Math.sin(interval) / (tanStartAngle * Math.cos(smallStart.getL())));
      double rpG = g + interval;
      if(rpG > Math.PI)
        rpG -= (2 * Math.PI);
      if(rpG < -Math.PI)
        rpG = (2 * Math.PI) + rpG;
      GeoPoint routePoint = new GeoPoint(smallL, rpG);
      double ari = Math.toDegrees(Math.atan(tanStartAngle));
      if(ari < 0.0D)
        ari = Math.abs(ari);
      int _nsDir;
      if(routePoint.getL() > smallStart.getL())
        _nsDir = TO_NORTH;
      else
        _nsDir = TO_SOUTH;
      double arrG = routePoint.getG();
      double staG = smallStart.getG();
      if (sign(arrG) != sign(staG))
      {
        if (sign(arrG) > 0)
          arrG -= (2 * Math.PI);
        else
          arrG = Math.PI - arrG;
      }
      int _ewDir;
      if(arrG > staG)
        _ewDir = TO_EAST;
      else
        _ewDir = TO_WEST;
      double _start = 0.0D;
      if(_nsDir == 1)
      {
        _start = 180D;
        if(_ewDir == 2)
          ari = _start - ari;
        else
          ari = _start + ari;
      } else
      if(_ewDir == 2)
        ari = _start + ari;
      else
        ari = _start - ari;
      for(; ari < 0.0D; ari += 360);
      route.addElement(new GreatCircleWayPoint(smallStart, arrival.equals(smallStart) ? null : new Double(ari)));
      smallStart = routePoint;
    }

  }

  /**
   * 
   * @return in radians
   */
  public double getDistance()
  {
    double cos = Math.sin(start.getL()) * Math.sin(arrival.getL()) + Math.cos(start.getL()) * Math.cos(arrival.getL()) * Math.cos(arrival.getG() - start.getG());
    double dist = Math.acos(cos);
    return dist;
  }

  public double getDistanceInDegrees()
  {
    return Math.toDegrees(getDistance());
  }

  public double getDistanceInNM()
  {
    return (getDistanceInDegrees() * 60D);
  }
  
  /**
   * 
   * @param from in degrees
   * @param to in degrees
   * @return in nautical miles
   */
  public double getDistanceInNM(GeoPoint from, GeoPoint to)
  {
    setStartInDegrees(from);
    setArrivalInDegrees(to);
    return (getDistanceInDegrees() * 60D);
  }
  
  /**
   * 
   * @param from in Radians
   * @param to in Radians
   * @return in miles
   */
  public static double getGCDistance(GeoPoint from, GeoPoint to)
  {
    double cos = Math.sin(from.getL()) * Math.sin(to.getL()) + Math.cos(from.getL()) * Math.cos(to.getL()) * Math.cos(to.getG() - from.getG());
    double dist = Math.acos(cos);
    return Math.toDegrees(dist) * 60D;
  }

  /**
   * 
   * @param from in Degrees
   * @param to in Degrees
   * @return in miles
   */
  public static double getGCDistanceInDegrees(GeoPoint from, GeoPoint to)
  {
    double cos = Math.sin(Math.toRadians(from.getL())) * Math.sin(Math.toRadians(to.getL())) + Math.cos(Math.toRadians(from.getL())) * Math.cos(Math.toRadians(to.getL())) * Math.cos(Math.toRadians(to.getG()) - Math.toRadians(from.getG()));
    double dist = Math.acos(cos);
    return Math.toDegrees(dist) * 60D;
  }

  public void calculateRhumLine()
  {
    if (arrival.getL() > start.getL())
      nsDir = TO_NORTH;
    else
      nsDir = TO_SOUTH;
    double arrG = arrival.getG();
    double staG = start.getG();
    if (sign(arrG) != sign(staG) && Math.abs(arrG - staG) > Math.PI)
    {  
      if (sign(arrG) > 0)
        arrG -= (2 * Math.PI);
      else
        arrG = Math.PI - arrG;
    }
    if (arrG - staG > 0.0D)
      ewDir = TO_EAST;
    else
      ewDir = TO_WEST;
    double deltaL = Math.toDegrees((arrival.getL() - start.getL())) * 60D;
    double radianDeltaG = arrival.getG() - start.getG();
    if (Math.abs(radianDeltaG) > Math.PI)
      radianDeltaG = (2 * Math.PI) - Math.abs(radianDeltaG);
    double deltaG = Math.toDegrees(radianDeltaG) * 60D;
    if(deltaG < 0.0D)
      deltaG = -deltaG;
    double startLC = Math.log(Math.tan((Math.PI / 4D) + start.getL() / 2D));
    double arrLC = Math.log(Math.tan((Math.PI / 4D) + arrival.getL() / 2D));
    double deltaLC = 3437.7467707849396D * (arrLC - startLC);
    if (deltaLC != 0d)
      rv = Math.atan(deltaG / deltaLC);
    else if (radianDeltaG > 0d)
      rv = (Math.PI / 2D);
    else
      rv = (3 * Math.PI / 2D);
    if (deltaL != 0d)
      dLoxo = deltaL / Math.cos(rv); 
    else
      dLoxo = deltaG * Math.cos(start.getL()); // TASK Make sure that's right...
    if (dLoxo < 0.0D)
      dLoxo = -dLoxo;
    if (rv < 0.0D)
      rv = -rv;
    if (ewDir == TO_EAST)
    {
      if (nsDir != TO_NORTH)
        rv = Math.PI - rv;
    } 
    else if (deltaLC != 0d)
    {
      if (nsDir == TO_NORTH)
        rv = (2 * Math.PI) - rv;
      else
        rv = Math.PI + rv;
    }
    while (rv >= (2 * Math.PI))
      rv -= (2 * Math.PI);
  }

  /**
   * Points coordinates in Radians
   */
  public static double calculateRhumLineDistance(GeoPoint f, GeoPoint t)
  {
    int _nsDir = 0;
    if(t.getL() > f.getL())
      _nsDir = TO_NORTH;
    else
      _nsDir = TO_SOUTH;
    double arrG = t.getG();
    double staG = f.getG();
    if(sign(arrG) != sign(staG) && Math.abs(arrG - staG) > Math.PI)
      if(sign(arrG) > 0)
        arrG -= (2 * Math.PI);
      else
        arrG = Math.PI - arrG;
    int _ewDir;
    if(arrG - staG > 0.0D)
      _ewDir = TO_EAST;
    else
      _ewDir = TO_WEST;
    double deltaL = Math.toDegrees((t.getL() - f.getL())) * 60D;
    double radianDeltaG = t.getG() - f.getG();
    if(Math.abs(radianDeltaG) > Math.PI)
      radianDeltaG = (2 * Math.PI) - Math.abs(radianDeltaG);
    double deltaG = Math.toDegrees(radianDeltaG) * 60D;
    if(deltaG < 0.0D)
      deltaG = -deltaG;
    double startLC = Math.log(Math.tan((Math.PI / 4D) + f.getL() / 2D));
    double arrLC = Math.log(Math.tan((Math.PI / 4D) + t.getL() / 2D));
    double deltaLC = 3437.7467707849396D * (arrLC - startLC);
    double _rv = 0.0D;
    if(deltaLC != (double)0)
      _rv = Math.atan(deltaG / deltaLC);
    else
    if(radianDeltaG > (double)0)
      _rv = (Math.PI / 2D);
    else
      _rv = (3 * Math.PI / 2D);
    double _dLoxo = deltaL / Math.cos(_rv);
    if(_dLoxo < 0.0D)
      _dLoxo = -_dLoxo;
    if(_rv < 0.0D)
      _rv = -_rv;
    if (_ewDir == TO_EAST)
    {
      if (_nsDir != TO_NORTH)
        _rv = Math.PI - _rv;
    } 
    else if(deltaLC != (double)0)
    {
      if(_nsDir == TO_NORTH)
        _rv = (2 * Math.PI) - _rv;
      else
        _rv = Math.PI + _rv;
    }
    for(; _rv >= (2 * Math.PI); _rv -= (2 * Math.PI));
    return _dLoxo;
  }

  /**
   * Points coordinates in Radians
   */
  public static double calculateRhumLineRoute(GeoPoint f, GeoPoint t)
  {
    int _nsDir = 0;
    if(t.getL() > f.getL())
      _nsDir = TO_NORTH;
    else
      _nsDir = TO_SOUTH;
    double arrG = t.getG();
    double staG = f.getG();
    if(sign(arrG) != sign(staG) && Math.abs(arrG - staG) > Math.PI)
      if(sign(arrG) > 0)
        arrG -= (2 * Math.PI);
      else
        arrG = Math.PI - arrG;
    int _ewDir;
    if(arrG - staG > 0.0D)
      _ewDir = TO_EAST;
    else
      _ewDir = TO_WEST;
    double deltaL = Math.toDegrees((t.getL() - f.getL())) * 60D;
    double radianDeltaG = t.getG() - f.getG();
    if(Math.abs(radianDeltaG) > Math.PI)
      radianDeltaG = (2 * Math.PI) - Math.abs(radianDeltaG);
    double deltaG = Math.toDegrees(radianDeltaG) * 60D;
    if(deltaG < 0.0D)
      deltaG = -deltaG;
    double startLC = Math.log(Math.tan((Math.PI / 4D) + f.getL() / 2D));
    double arrLC = Math.log(Math.tan((Math.PI / 4D) + t.getL() / 2D));
    double deltaLC = 3437.7467707849396D * (arrLC - startLC);
    double _rv = 0.0D;
    if(deltaLC != (double)0)
      _rv = Math.atan(deltaG / deltaLC);
    else if (radianDeltaG > (double)0)
      _rv = (Math.PI / 2D);
    else
      _rv = (3 * Math.PI / 2D);
    double _dLoxo = deltaL / Math.cos(_rv);
    if(_dLoxo < 0.0D)
      _dLoxo = -_dLoxo;
    if(_rv < 0.0D)
      _rv = -_rv;
    if(_ewDir == TO_EAST)
    {
      if(_nsDir != TO_NORTH)
        _rv = Math.PI - _rv;
    } 
    else if(deltaLC != (double)0)
    {
      if(_nsDir == TO_NORTH)
        _rv = (2 * Math.PI) - _rv;
      else
        _rv = Math.PI + _rv;
    }
    for(; _rv >= (2 * Math.PI); _rv -= (2 * Math.PI));
    return _rv;
  }

  private static int sign(double d)
  {
    if(d == 0.0D)
      return 0;
    return d >= 0.0D ? 1 : -1;
  }

  public double getRhumbLineDistance()
  {
    return dLoxo;
  }

  public double getRhumbLineRoute()
  {
    return rv;
  }

  public Vector<GreatCircleWayPoint> getRoute()
  {
    return route;
  }

  /**
   *
   * @param from GeopPoint, L & G in Radians
   * @param dist distance in nm
   * @param route route in degrees
   * @return DR Position, L & G in Radians
   */
  public static GeoPoint dr(GeoPoint from, double dist, double route)
  {
    double deltaL = Math.toRadians(dist / 60D) * Math.cos(Math.toRadians(route));
    double l2 = from.getL() + deltaL;
//  double lc1 = Math.log(Math.tan((Math.PI / 4D) + from.getL() / 2D));
//  double lc2 = Math.log(Math.tan((Math.PI / 4D) + l2 / 2D));
//  double deltaLc = lc2 - lc1;
//  double deltaG = deltaLc * Math.tan(Math.toRadians(route));
    double deltaG = Math.toRadians(dist / (60D * Math.cos((from.getL() + l2)/2D))) * Math.sin(Math.toRadians(route)); // 2009-mar-10
    double g2 = from.getG() + deltaG;
    return new GeoPoint(l2, g2);
  }

  public static void main(String[] args)
  {
    GeoPoint dr = dr(new GeoPoint(Math.toRadians(45D), Math.toRadians(-130D)), 55, 270);
    System.out.println("Reaching " + new GeoPoint(Math.toDegrees(dr.getL()), Math.toDegrees(dr.getG())).toString());
    System.out.println("Done.");
  }

  public static void main2(String args[])
  {
    GeoPoint start = new GeoPoint(Math.toRadians(GeomUtil.sexToDec("37", "38")), Math.toRadians(-GeomUtil.sexToDec("122", "46")));
//  GeoPoint p = dr(start, 30D, 230D);
    GeoPoint p = new GeoPoint(Math.toRadians(GeomUtil.sexToDec("20", "00")), Math.toRadians(-GeomUtil.sexToDec("150", "00")));
    System.out.println("Arriving:" + GeomUtil.decToSex(Math.toDegrees(p.getL()), GeomUtil.SWING, GeomUtil.NS) + ", " + GeomUtil.decToSex(Math.toDegrees(p.getG()), GeomUtil.SWING, GeomUtil.EW));
    GreatCircle test = new GreatCircle();
    test.setStart(start);
    test.setArrival(p);
    test.calculateGreatCircle(20);
    double gcDist = Math.toDegrees(test.getDistance()) * 60.0;
    test.calculateRhumLine();
    double dist = test.getRhumbLineDistance();
    double route = test.getRhumbLineRoute();
    System.out.println("Dist:" + dist + " (" + gcDist + "), route:" + Math.toDegrees(route));
    
    System.out.println("-------------");
    test = new GreatCircle();
    test.setStart(new GeoPoint(Math.toRadians(47.67941), Math.toRadians(-3.368855)));
    test.setArrival(new GeoPoint(Math.toRadians(47.666931), Math.toRadians(-3.39822)));
    test.calculateRhumLine();
    dist = test.getRhumbLineDistance();
    route = test.getRhumbLineRoute();
    System.out.println("Dist:" + dist + " (" + gcDist + "), route:" + Math.toDegrees(route));
    
  }
}
