package localFieldView;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import static util.Logger.polarStr;

/**
 * This class represents a line.  
 * 
 * It should be updated by class LocalFieldView, where the coordinate system is
 * described, too. 
 * 
 * @see LocalFieldView
 */
public class LineModel extends DatedItemModel{
  
  private final Vector3D startPoint;
  private final Vector3D endPoint;
  
  /**
   * Constructor. 
   * 
   * Assumes, that the model is created at the first cycle, when the object is
   * sensed by the vision perceptor. 
   * 
   * @param timeNow Actual server time.
   * @param start Local coordinates relative to the center of the robots head. 
   * @param end Local coordinates relative to the center of the robots head.
   */
  public LineModel(Vector3D start, Vector3D end, double timeStamp){
    super(timeStamp);
    startPoint = start;
    endPoint = end;
  }
  
  /**
   * Returns the coordinates of the start point of the line. 
   * @return Local coordinates (see class LocalFieldView).
   */
  public Vector3D getStart(){
    return startPoint;
  }
  
  /**
   * Returns the coordinates of the end point of the line. 
   * @return Local coordinates (see class LocalFieldView).
   */
  public Vector3D getEnd(){
    return endPoint;
  }
  
  /**
   * Returns a string representation of all line data. 
   * @return String representation of the flag. 
   */
  @Override
  public String toString(){
    return String.format("Line model - " + super.toString() 
                          + "\nstart coords: " + polarStr(startPoint) + 
                         ", end coords: " + polarStr(endPoint));
  }
  
}
