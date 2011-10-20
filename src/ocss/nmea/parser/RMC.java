package ocss.nmea.parser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

import java.io.Serializable;

import java.util.Date;

public class RMC implements Serializable
{
  private GeoPos gp = null;
  private double sog = 0D;
  private double cog = 0D;
  
  private Date rmcDate = null;
  private Date rmcTime = null;
  private double declination = -Double.MAX_VALUE;
  private transient VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);
  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public RMC()
  {
  }

  public void setGp(GeoPos gp)
  {
    this.gp = gp;
  }

  public GeoPos getGp()
  {
    return gp;
  }

  public void setSog(double sog)
  {
    this.sog = sog;
  }

  public double getSog()
  {
    return sog;
  }

  public void setCog(double cog)
  {
    this.cog = cog;
  }

  public double getCog()
  {
    return cog;
  }
  
  public String toString()
  {
    String str = "";
    str = gp.toString() + ", " + "SOG:" + sog + ", COG:" + cog;
    if (rmcDate != null)
      str += (" " + rmcDate.toString() + " ");
    if (declination != -Double.MAX_VALUE)
      str += ("D:" + Double.toString(declination));
    
    return str;
  }

  public void setRmcDate(Date rmcDate)
  {
    this.rmcDate = rmcDate;
  }

  public Date getRmcDate()
  {
    return rmcDate;
  }

  public void setDeclination(double declination)
  {
    this.declination = declination;
  }

  public double getDeclination()
  {
    return declination;
  }

  public void setRmcTime(Date rmcTime)
    throws PropertyVetoException
  {
    Date oldRmcTime = this.rmcTime;
    vetoableChangeSupport.fireVetoableChange("RmcTime", oldRmcTime, rmcTime);
    this.rmcTime = rmcTime;
    propertyChangeSupport.firePropertyChange("RmcTime", oldRmcTime, rmcTime);
  }

  public void addVetoableChangeListener(VetoableChangeListener l)
  {
    vetoableChangeSupport.addVetoableChangeListener(l);
  }

  public void removeVetoableChangeListener(VetoableChangeListener l)
  {
    vetoableChangeSupport.removeVetoableChangeListener(l);
  }

  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    propertyChangeSupport.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    propertyChangeSupport.removePropertyChangeListener(l);
  }

  public Date getRmcTime()
  {
    return rmcTime;
  }
}
