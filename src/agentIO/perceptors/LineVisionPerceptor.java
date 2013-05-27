package agentIO.perceptors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.Logger;

/**
 * This class represents the raw value for a line from the vision perceptor, 
 * for a convenient access to line coordinates see package localfieldView.
 */
public class LineVisionPerceptor {
  private final Vector3D startPoint;
  private final Vector3D endPoint;
  
/**
 * This class represents the raw value for a line from the vision perceptor, 
 * for a convenient access to line coordinates see package localfieldView.
 */
  public LineVisionPerceptor(Vector3D start, Vector3D end){
    startPoint = start;
    endPoint = end;
  }
  
/**
 * This class represents the raw value for a line from the vision perceptor, 
 * for a convenient access to line coordinates see package localfieldView.
 */
  public Vector3D getStart(){
    return startPoint;
  }
  
/**
 * This class represents the raw value for a line from the vision perceptor, 
 * for a convenient access to line coordinates see package localfieldView.
 */
  public Vector3D getEnd(){
    return endPoint;
  }
  
/**
 * This class represents the raw value for a line from the vision perceptor, 
 * for a convenient access to line coordinates see package localfieldView.
 */
  @Override
  public String toString(){
    return String.format("line start: " + Logger.polarStr(startPoint) + 
                         " end: " + Logger.polarStr(endPoint));
  }
}
