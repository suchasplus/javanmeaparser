package user.util;

import java.io.InputStreamReader;
import java.io.BufferedReader;

public class StaticUtil
{
  private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

  public static String replaceString(String orig, String oldStr, String newStr)
  {
    String ret = orig;
    int indx = 0;
    for (boolean go = true; go;)
    {
      indx = ret.indexOf(oldStr, indx);
      if (indx < 0)
      {
        go = false;
      } 
      else
      {
        ret = ret.substring(0, indx) + newStr + ret.substring(indx + oldStr.length());
        indx += 1 + oldStr.length();
      }
    }
    return ret;
  }

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

  public static String getMonthName(int mNum)
  {
    String month[] = {
      "Jan", "Feb", "Mar", 
      "Apr", "May", "Jun", 
      "Jul", "Aug", "Sep", 
      "Oct", "Nov", "Dec"
    };
    return month[mNum - 1];
  }

  public static String lpad(String str, int len)
  {
    String s;
    for(s = str; s.length() < len; s = " " + s);
    return s;
  }

  public static String rpad(String str, int len)
  {
    String s;
    for(s = str; s.length() < len; s = s + " ");
    return s;
  }
}
