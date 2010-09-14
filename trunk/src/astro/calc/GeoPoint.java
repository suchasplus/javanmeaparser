package astro.calc;

import java.io.Serializable;

import user.util.GeomUtil;

public final class GeoPoint
        implements Serializable
{
  double latitude;
  double longitude;

  public GeoPoint(double l, double g)
  {
    latitude = l;
    longitude = g;
  }

  public double getL()
  {
    return latitude;
  }

  public double getG()
  {
    return longitude;
  }

  public void setL(double l)
  {
    latitude = l;
  }

  public void setG(double g)
  {
    longitude = g;
  }

  public boolean equals(GeoPoint p)
  {
    String g = GeomUtil.decToSex(longitude, 1, 2);
    String gp = GeomUtil.decToSex(p.getG(), 1, 2);
    String l = GeomUtil.decToSex(latitude, 1, 1);
    String lp = GeomUtil.decToSex(p.getL(), 1, 1);
    return g.equals(gp) && l.equals(lp);
  }

  /**
   * In nautical miles
   * @param target
   * @return
   */
  public double orthoDistanceBetween(GeoPoint target)
  {
    GreatCircle gc = new GreatCircle();
    gc.setStart(new GeoPoint(Math.toRadians(this.getL()), Math.toRadians(this.getG())));
    gc.setArrival(new GeoPoint(Math.toRadians(target.getL()), Math.toRadians(target.getG())));
    gc.calculateGreatCircle(1);
    double d = Math.toDegrees(gc.getDistance());
    
    return d * 60D;
  }
  
  /**
   * In nautical miles
   * @param target
   * @return
   */
  public double gcDistanceBetween(GeoPoint target)
  {    
    double d = GreatCircle.getGCDistanceInDegrees(this, target);    
    return d;
  }
  
  /**
   * In nautical miles
   * @param target
   * @return
   */
  public double loxoDistanceBetween(GeoPoint target)
  {
    GreatCircle gc = new GreatCircle();
    gc.setStart(new GeoPoint(Math.toRadians(this.getL()), Math.toRadians(this.getG())));
    gc.setArrival(new GeoPoint(Math.toRadians(target.getL()), Math.toRadians(target.getG())));
    gc.calculateRhumLine();
    double d = gc.getRhumbLineDistance();
    
    return d;
  }
  
  public String toString()
  {
    String str = GeomUtil.decToSex(this.latitude, GeomUtil.SWING, GeomUtil.NS) + " / " +
                 GeomUtil.decToSex(this.longitude, GeomUtil.SWING, GeomUtil.EW);
    return str;
  }
  
  public static void main(String[] args)
  {
    GeoPoint p1 = new GeoPoint(37, -122);
    GeoPoint p2 = new GeoPoint(38, -121);    
    System.out.println("Ortho:" + p1.orthoDistanceBetween(p2));
    System.out.println("Loxo :" + p1.loxoDistanceBetween(p2));    
    System.out.println("-----------------------------------");
    
    p1 = new GeoPoint(62,  153);
    p2 = new GeoPoint(62, -135);    
    System.out.println("Ortho:" + p1.orthoDistanceBetween(p2));
    System.out.println("Loxo :" + p1.loxoDistanceBetween(p2));    
    System.out.println("-----------------------------------");
    
    p1 = new GeoPoint(28, -139);
    p2 = new GeoPoint(26,  147);    
    System.out.println("Ortho:" + p1.orthoDistanceBetween(p2));
    System.out.println("Loxo :" + p1.loxoDistanceBetween(p2));
    System.out.println("GC   :" + p1.gcDistanceBetween(p2));
    System.out.println("-----------------------------------");
    
    long before = System.currentTimeMillis();
    double d = 0D;
    for (int i=0; i<10000; i++)
      d = p1.loxoDistanceBetween(p2);
    long after = System.currentTimeMillis();
    System.out.println("10000 Loxo :" + Long.toString(after - before) + " ms.");
    
    before = System.currentTimeMillis();
    d = 0D;
    for (int i=0; i<10000; i++)
      d = p1.orthoDistanceBetween(p2);
    after = System.currentTimeMillis();
    System.out.println("10000 Ortho:" + Long.toString(after - before) + " ms.");
    
    before = System.currentTimeMillis();
    d = 0D;
    for (int i=0; i<10000; i++)
      d = p1.gcDistanceBetween(p2);
    after = System.currentTimeMillis();
    System.out.println("10000 GC   :" + Long.toString(after - before) + " ms.");
    System.out.println("-----------------------------------");
  }  
}
