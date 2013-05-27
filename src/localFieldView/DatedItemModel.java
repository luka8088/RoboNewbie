package localFieldView;

import util.FieldConsts;

/**
 * This class is part of the local field model and represents every item 
 * sensed by the vision perceptor.
 * Every item can have been sensed at a certain time and this class cares for 
 * the time stamp and tells if it is sensed right now. 
 * If an item has not been sensed yet, its timestamp is 0.0, and method
 * isInFOV() will return false. 
 * The time should always be the server time, not the play time.
 */
public abstract class DatedItemModel {
  
  private double timeStamp = 0;
  private boolean inFOVnow = false;

  /**
   * Constructor. 
   * Assumes, that the model is created at the first cycle, when the item is
   * sensed by the vision perceptor. 
   * 
   * @param timeNow Actual server time.
   */
  public DatedItemModel(double timeNow) {
    this.timeStamp = timeNow;
    inFOVnow = true;
  }
    
  /**
   * Sets the timeStamp to the passed time and sets the state to be "in the 
   * Field of View" of the vision perceptor. 
   * This method should be used only if in the actual server cycle a server 
   * message brought new informations about the item. 
   * 
   * @param timeNow Actual server time (not play time). 
   */
  public void setTimeStamp(double timeNow){
    timeStamp = timeNow;
    inFOVnow = true;
  }

  /**
   * States if the item is in the field of view of the vision perceptor during
   * the actual server cycle. 
   * 
   * Since the vision data are valid always for three cycles, this method 
   * returns true, if the item has been updated during this or one of the last 
   * two cycles. 
   * 
   * @return True if the value is up to date, else false. 
   */
  public boolean isInFOVnow() {
    return inFOVnow;
  }

  /**
   * Sets the state of this model. 
   * 
   * @param inFOVnow "True" should be passed, if the item has been updated 
   * during this or one of the last two server cycles, else "false".
   */
  public void setInFOVnow(boolean inFOVnow) {
    this.inFOVnow = inFOVnow;
  }
  
  /**
   * Returns the server time, when the item was updated the last time.
   * 
   * @return Last update time.  
   */
  public double getTimeStamp() {
    return timeStamp;
  }
  
  public String toString(){
    return String.format("update time: "+ getTimeStamp()
                          + ", inFOVnow: " + isInFOVnow());
  }
}
