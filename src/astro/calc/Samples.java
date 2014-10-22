package astro.calc;

public class Samples
{
  // Examples of distances and directions calculations
  public static void main(String[] args)
  {
    GeoPoint from = new GeoPoint(37.501282801564244, -122.48082160949707); // the boat
    GeoPoint to   = new GeoPoint(37.49403906867881,  -122.48468399047852);  // HMB Entrance
    
    double dist = from.gcDistanceBetween(to);
    double heading = GreatCircle.calculateRhumLineRoute(from.degreesToRadians(), 
                                                        to.degreesToRadians());
    dist = GreatCircle.calculateRhumLineDistance(from.degreesToRadians(), 
                                                 to.degreesToRadians());
    System.out.println("Loxo Dist:" + dist + " nm, " + (dist * 1852) + " m, heading " + Math.toDegrees(heading));

    GreatCircle gc = new GreatCircle();
    gc.setStartInDegrees(from);
    gc.setArrivalInDegrees(to);
    gc.calculateGreatCircle(10);
    dist = Math.toDegrees(gc.getDistance()) * 60;
    GreatCircleWayPoint first = gc.getRoute().get(1);
    heading = first.getZ().doubleValue();
    System.out.println("Ortho Dist:" + dist + " nm, " + (dist * 1852) + " m, heading " + heading);
  }
}
