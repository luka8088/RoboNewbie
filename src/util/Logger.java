package util;

import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This class provides convenient logging of debug informations, including
 * formatting output Strings of 3D-vectors.
 * <p/>
 * The output produced by this class is chronological, has timestamps in
 * milliseconds (time 0 at instantiation), and is well formatted in rows of max.
 * 100 letters. So it is designed for printing on a console. <br>
 * Use this class for debug outputs, rather than System.out.print(...) or
 * similar Java standard output methods. In most cases a SimSpark agent cannot
 * use the standard output methods, because they slow down the agent, so the
 * agent isn't synchronous with the server cycle any more. <br>
 * Instead, with this class you can log the desired outputs, and print them on
 * the console after the agent program has finished. So the synchronization wont
 * be affected by the debug outputs.
 * <p/>
 * Recommended usage of this class: <br>
 * 1) Create one instance of this class in the Agent_-class, and (if needed)
 * pass the instance to the constructors of other classes, which will make
 * outputs. This should be done in the constructor of the Agent_-class. <br>
 * 2.a) To make a new entry, use method log(...) . <br>
 * 2.b) Sometimes methods polarStr(...) and cartesianStr(...) may be useful for
 * a nice output of three dimensional vector values. <br>
 * 3) Print all the logged informations at once, when your agent is finishing.
 * This should be done in the main(...)-method of the Agent_-class. <br>
 * An example of the recommended usage you can find in Agent_TestPerceptorInput.
 * A complex example with passing the Logger-object to another class than the
 * Agent_-class can be found in package examples.agentSimpleSoccer. 
 * 
 * @see examples.Agent_TestPerceptorInput
 */
public class Logger {

  private ArrayList<String> loglist;
  private Date startTime = new Date();
  private final static int ROWLENGTH = 100;

  /** Constructor. */
  public Logger() {
    System.out.println("Logger start.");
    loglist = new ArrayList<>();
  }
  
  /** Formats a nice output String for polar coordinates.
   *
   * @param v Must be initialized, null produces a runtime error.
   * @return A String like "dist=19,85 hor=159,15° vert=89,35°", where dist is the length (= norm) of the vector, hor is the horizontal angle (= azimuth) and vert is the vertical angle (= elevator).
   */
  static public String polarStr(Vector3D v) {
    return String.format("dist=%.2f hor=%.2f° vert=%.2f°",
            v.getNorm(),
            Math.toDegrees(v.getAlpha()),
            Math.toDegrees(v.getDelta()));
  }
  
  /** Formats a nice output String for Cartesian coordinates.
   *
   * @param v Must be initialized, null produces a runtime error.
   * @return A String like "x=-0,15 y=0,20 z=25,31".
   */
  static public String cartesianStr(Vector3D v) {
    return String.format("x=%.2f y=%.2f z=%.2f", v.getX(), v.getY(), v.getZ());
  }
    
    /** Makes a new log entry. 
     * 
     * @param s The information that should be logged. Newlines in this String should be done by the characters '\n' or '\r', other newline characters may destroy the formatting. 
     */
    public void log(String s){
      String substring = null;
      if (s != null){

        
        StringBuilder logString = new StringBuilder(); 
        
        double actualMs = (new Date().getTime()) - startTime.getTime();
        actualMs /= 1000; // to get seconds in the output
        
        String timeS = String.format("%.3f ", actualMs);
        logString.append(timeS);
        int spaceCount = timeS.length();
        
        String spaces = new String();
        for (int i = 0; i < spaceCount; i++) 
          spaces += " ";

        int rowLenghtGuard = 0;
        for (int i = 0; i < s.length(); i++){
          
          logString.append(s.charAt(i));

          if (s.charAt(i) == '\n' || s.charAt(i) == '\r') {
            logString.append(spaces);
            rowLenghtGuard = 0;
          } else {
            rowLenghtGuard++;
            if (rowLenghtGuard >= ROWLENGTH) {
              logString.append('\n').append(spaces);
              rowLenghtGuard = 0;
            }
          }
          
        }
        
        loglist.add(logString.toString());
      }
    }
    
  /** Prints all log entries on System.out .
   * <p>
   * The printed entries are not removed from the log memory, so every call of
   * this method will cause an output starting with the very first entry made
   * after the instantiation of the Logger-object. <br>
   * This method is designed to be called just once, at the end of the
   * main(...)-method of the Agent_-class (see comment of class Logger).
   */
  public void printLog() {
    System.out.println("Log begin");
    for (String s : loglist) {
      System.out.println(s);
    }
    System.out.println("Log end");
  }
}