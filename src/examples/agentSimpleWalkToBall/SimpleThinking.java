package examples.agentSimpleWalkToBall;

import keyframeMotion.KeyframeMotion;
import localFieldView.BallModel;
import localFieldView.LocalFieldView;

/**
 * Thinking-Class for Agent_SimpleWalkToBall.
 * This class takes action decisions for a simple scenario: If the ball lies in
 * front of the robot (shifted less than 30° to the left or right side), then 
 * the robot should walk towards it. Else the robot should turn left.
 * 
 * This class uses informations about the ball provided by an object of class
 * LocalFieldView, and robot movements provided by class KeyframeMotion. 
 * Therefore SimpleThinking depends on the functionality of these classes.
 * 
 * Usage by the agent class:
 * Call method decide() in each server cycle. 
 * Before the call the LocalFieldView object has to be updated, and after the call
 * the KeyframeMotion object must execute the chosen movement. 
 * 
 * Class Agent_SimpleWalkToBall gives an example of correct integration and 
 * usage. 
 * 
 */
public class SimpleThinking {

  private static final double TOLERANCE_ANGLE = Math.toRadians(30);
  
  private KeyframeMotion motion;
  private BallModel ball;
  
  private boolean robotIsWalking = false;

  /**
   * Constructor. 
   * 
   * @param localView Has to be already initialized. 
   * @param motion Has to be already initialized. 
   */
  public SimpleThinking(LocalFieldView localView, KeyframeMotion motion) {
    this.ball = localView.getBall();
    this.motion = motion;
  }
    
  /**
   * Decide, whether the robot should walk or turn to reach the ball and set the
   * chosen movement. 
   * If the ball is in front of the robot, it can just walk forward, else it 
   * should turn left, and check its position relative to the ball again. 
   */
  public void decide() {
    
    // Take care not to interrupt an actually executed movement.
    // This has to be checked always when using class KeyframeMotion. 
    if (motion.ready()) {

      // If the ball lies in front of the robot, walk towards it. 
      if (ball.isInFOVnow()
          && (Math.abs(ball.getCoords().getAlpha())) < TOLERANCE_ANGLE) {
        motion.setWalkForward();
        robotIsWalking = true;
      }

      // If the ball lies somewhere else, first stop the walking to
      // prepare for the turning, and then turn left.  
      else if (robotIsWalking) {
        motion.setStopWalking();
        robotIsWalking = false;
      } else {
        motion.setTurnLeft();
      }
    }
    
  }
  
}
