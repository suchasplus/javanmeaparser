package ocss.nmea.parser;

import java.io.Serializable;

import java.text.DecimalFormat;

public class Temperature implements Serializable
{
  private double temperature = 0d;
  private static final String UNIT = "\272C";
  private static final DecimalFormat FMT = new DecimalFormat("##0.0");

  public Temperature()
  {
  }

  public Temperature(double temp)
  {
    this.temperature = temp;
  }

  public double getValue()
  {
    return this.temperature;
  }

  public String toString()
  {
    double d = this.temperature;
    return FMT.format(d) + UNIT;
  }
}
