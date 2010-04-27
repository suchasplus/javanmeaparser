package astro.calc;


// Referenced classes of package astro.calc:
//      Point

public final class GreatCircleWayPoint
{

  public GreatCircleWayPoint(GeoPoint p, Double z)
  {
    this.p = p;
    this.z = z;
  }

  public GeoPoint getPoint()
  {
    return p;
  }

  public Double getZ()
  {
    return z;
  }

  GeoPoint p;
  Double z;
}
