package localFieldView;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import static util.Logger.polarStr;

/**
 * This class represents the ball. 
 * 
 * It should be updated by class LocalFieldView, where the coordinate system is
 * described, too. 
 * 
 * @see LocalFieldView
 */
public class BallModel extends DatedItemModel {
    private Vector3D coords;
    
  /**
   * Constructor. 
   * 
   * Assumes, that the model is created at the first cycle, when the object is
   * sensed by the vision perceptor. 
   * 
   * @param timeNow Actual server time.
   * @param coords Local coordinates relative to the center of the robots head. 
   */
  public BallModel(Vector3D coords, double timeStamp){
    super(timeStamp);
    this.coords = coords;
  }
  
  /**
   * Sets the model data. 
   * 
   * @param coords Local coordinates (see class LocalFieldView).
   * @param timeStamp Actualization time, should be actual server time. 
   */
  public void update(Vector3D coords, double timeStamp){
    super.setTimeStamp(timeStamp);
    this.coords = coords;
  }
  
  /**
   * Returns the coordinates of the ball. 
   * @return Local coordinates (see class LocalFieldView).
   */
  public Vector3D getCoords(){
    return coords;
  }
  
  /**
   * Returns a string representation of all ball data. 
   * @return String representation of the ball. 
   */
  @Override
  public String toString(){
    return String.format("Ball model - " + super.toString() 
                          + ", coords: " + polarStr(coords));
  }
  
}