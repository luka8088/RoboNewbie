package localFieldView;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts;
import static util.Logger.polarStr;

/**
 * This class represents a goal post. 
 * 
 * It should be updated by class LocalFieldView, where the coordinate system is
 * described, too. 
 * 
 * @see LocalFieldView
 */
public class GoalPostModel extends DatedItemModel{
  
  Vector3D coords;
  private FieldConsts.GoalPostID id;
    
  /**
   * Constructor. 
   * 
   * Assumes, that the model is created at the first cycle, when the object is
   * sensed by the vision perceptor. 
   * 
   * @param timeNow Actual server time.
   * @param coords Local coordinates relative to the center of the robots head. 
   */
  public GoalPostModel(Vector3D coords, double timeStamp, FieldConsts.GoalPostID id){
    super(timeStamp);
    this.coords = coords;
    this.id = id;
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
   * Returns the coordinates of the goal post. 
   * @return Local coordinates (see class LocalFieldView).
   */ 
  public Vector3D getCoords(){
    return coords;
  }
  
  /**
   * Returns a string representation of all goal post data. 
   * @return String representation of the goal post. 
   */
  @Override
  public String toString(){
    return String.format("Goal post model - "+ id +", " + super.toString() 
                          + ", coords: " + polarStr(coords));
  }
  
  
  
}
