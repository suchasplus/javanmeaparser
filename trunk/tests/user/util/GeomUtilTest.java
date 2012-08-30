package user.util;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class GeomUtilTest
{
  public GeomUtilTest()
  {
  }

  /**
   * @see GeomUtil#getDir(float,float)
   */
  @Test
  public void testGetDirDueN()
  {
    double d = GeomUtil.getDir(0f, 10f);
    assertTrue("(0f, 10f) should return N, returned " + Double.toString(d) + " instead", d == 0d);
  }

  @Test
  public void testGetDirDueS()
  {
    double d = GeomUtil.getDir(0f, -10f);
    assertTrue("(0f, -10f) should return S, returned " + Double.toString(d) + " instead", d == 180d);
  }

  @Test
  public void testGetDirDueE()
  {
    double d = GeomUtil.getDir(10f, 0f);
    assertTrue("(10f, 0f) should return E, returned " + Double.toString(d) + " instead", d == 90d);
  }

  @Test
  public void testGetDirDueW()
  {
    double d = GeomUtil.getDir(-10f, 0f);
    assertTrue("(-10f, 0f) should return W, returned " + Double.toString(d) + " instead", d == 270d);
  }

  @Test
  public void testGetDirNE()
  {
    double d = GeomUtil.getDir(10f, 10f);
    assertTrue("(10f, 10f) should return NE, returned " + Double.toString(d) + " instead", d == 45d);
  }

  @Test
  public void testGetDirSE()
  {
    double d = GeomUtil.getDir(10f, -10f);
    assertTrue("(10f, -10f) should return SE, returned " + Double.toString(d) + " instead", d == 135d);
  }

  @Test
  public void testGetDirSW()
  {
    double d = GeomUtil.getDir(-10f, -10f);
    assertTrue("(-10f, -10f) should return SW, returned " + Double.toString(d) + " instead", d == 225d);
  }

  @Test
  public void testGetDirNw()
  {
    double d = GeomUtil.getDir(-10f, 10f);
    assertTrue("(-10f, 10f) should return NW, returned " + Double.toString(d) + " instead", d == 315d);
  }
}
