package agentIO.perceptors;

/**
 * This class represents a single message received by the hear perceptor. 
 * 
 * The hear perceptor receives broadcasted messages from other robots. As all
 * Perceptor-classes this class is just for reading the perceptor values
 * provided by the server.
 * Instances of this class are immutable.
 */
public class HearPerceptor {
  
  double time;
  double direction; 
  String message;
  
  /**
   * Constructor. 
   * 
   * @param time The time elapsed since the kick off (since the server has been
   * set to the playmode "KickOff_Left" for the first time) until the hear message 
   * has been received. This time value stops during the halftime break. It is
   * given in seconds.
   * @param direction The horizontal direction from which the message has been 
   * received. For more information see method getDirection().
   * @param message Content of the message.
   */
  public HearPerceptor(double time, double direction, String message){
    this.time = time;
    this.direction = direction;
    this.message = message;
  }
  
  /**
   * Returns the play time in seconds, at which the message has been received.
   * 
   * Caution! In the version of SimSpark configured for RoboNewbie, the play
   * time does not start automatically. It can be started manually, as stated
   * in the document titled "How to start". So this method will return 0, until
   * the timer has been started.
   * 
   * @return The time elapsed since the kick off (in RoboNewbie-configuration
   *         since timer has been started manually, in the original configuration
   *         since the server has been set to the play mode "KickOff_Left" for
   *         the first time). This time value stops during the halftime break.   
   */
  double getTime(){
    return time;
  }
  
  /**
   * Returns the direction, from which the message has been received.
   * 
   * The direction is given relative to the robots position, but orientation of
   * the robots head or body is unaccounted for this direction value. 0 degrees
   * always points parallel to the field coordinate system in the direction of
   * the opposite goal.
   * 
   * @return The direction value in degrees. 
   */
  public double getDirection() {
    return direction;
  }

  /**
   * Returns the content of the message.
   * 
   * @return The content of the message. 
   */
  public String getMessage() {
    return message;
  }
  
  /**
   * Returns a textual representation of all information stored in class 
   * variables. 
   * 
   * @return Textual representation of this object.
   */
  @Override
  public String toString(){
    return String.format("hear at time: "+ time + " from direction: " 
            + direction + " message: " + message);
  }
  
  
}
