package localFieldView;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts;
import static util.Logger.polarStr;

/**
 * This class represents a flag. 
 * It should be updated by class LocalFieldView, where the coordinate system is
 * described, too. 
 * 
 * @see LocalFieldView
 */
public class FlagModel extends DatedItemModel {
    Vector3D coords;
  private FieldConsts.FlagID id;
    
  /**
   * Constructor. 
   * 
   * Assumes, that the model is created at the first cycle, when the object is
   * sensed by the vision perceptor. 
   * 
   * @param timeNow Actual server time.
   * @param coords Local coordinates relative to the center of the robots head. 
   */
  public FlagModel(Vector3D coords, double timeStamp, FieldConsts.FlagID id){
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
   * Returns the coordinates of the flag. 
   * @return Local coordinates (see class LocalFieldView).
   */
  public Vector3D getCoords(){
    return coords;
  }
  
  /**
   * Returns a string representation of all flag data. 
   * @return String representation of the flag. 
   */
  @Override
  public String toString(){
    return String.format("Flag model - "+ id +", " + super.toString() 
                          + ", coords: " + polarStr(coords));
  }
  
  
  
}
