package ocss.nmea.parser;

import java.io.Serializable;

import java.text.DecimalFormat;

public class UTC implements Serializable
{
  private int h, m;
  private float s;
  public UTC(int h, int m, float s)
  {
    this.h = h;
    this.m = m;
    this.s = s;
  }

  public int getH()
  {
    return h;
  }

  public int getM()
  {
    return m;
  }

  public float getS()
  {
    return s;
  }
  
  private DecimalFormat df2 = new DecimalFormat("00");
  private DecimalFormat df23 = new DecimalFormat("00.000");
  public String toString()
  {
    return df2.format(h) + ":" + df2.format(m) + ":" + df23.format(s);
  }
}
