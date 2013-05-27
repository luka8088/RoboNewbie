package examples.agentSimpleSoccer;

import agentIO.PerceptorInput;
import directMotion.LookAroundMotion;
import java.util.HashMap;
import keyframeMotion.KeyframeMotion;
import localFieldView.BallModel;
import localFieldView.GoalPostModel;
import localFieldView.LocalFieldView;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts.GoalPostID;
import util.Logger;

/**
 * This class implements a simple behavior to get the ball to the goal. 
 *
 * The robot can be beamed to a custom place on the soccer field. It will
 * turn to the ball, walk to it, get in a position directed towards the ball and
 * the goal and then push the ball.
 * 
 * The behavior works in details as follows: 
 * The decision of a new move is made every time, when the robot does not 
 * execute any motion (but the head motion to sense the field objects). It 
 * depends on conditions, that must be fulfilled sequentially. 
 * 1) Is the robot upright? If is has fallen down, it should perform a stand up
 * motion.
 * 2) Does the robot have the actual ball coordinates relative to itself? If 
 * not, the robot should turn to change its position to get a new perspective on 
 * the field. 
 * 3) Is the ball exactly in front of the robot? If not, turn to the ball. 
 * 4) Is the ball near enough to push it? If not, walk forward to it. 
 * 5) Does the robot have the actual goal coordinates relative to itself? If
 * not, it should turn as in 2) .
 * 6) Does the ball lie between the robot and the goal? If not do side steps to
 * reach this position.
 * 7) If all of the conditions above are fulfilled, then the robot is in a 
 * good position to push the ball forward towards the goal. 
 * 
 * Usage of this class:
 * Just call method decide() in every server cycle in the act()-method of the
 * Agent_-class.
 *
 * Required context in the agent class:
 * This class depends on the classes PerceptorInput, LocalFieldView,
 * KeyframeMotion, LookAroundMotion and Logger so all of these classes have to
 * be correctly synchronized with the simulation server in the Agent_-class, as
 * stated in the comments on the classes. To sense the robot position relative
 * to the ball and the goal the agent should use the class LookAroundMotion,
 * because this class assumes that the the robot checks the field around itself
 * periodically by turning the head to sense the coordinates of the ball and the
 * opponent goal.
 * A correct example gives Agent_SimpleSoccer of package
 * examples.agentSimpleSoccer.
 */
public class SoccerThinking {

  Logger log;
  PerceptorInput percIn;
  LocalFieldView localView;
  KeyframeMotion motion;
  BallModel ball;
  GoalPostModel oppGoalLPost, oppGoalRPost;
  private static final double TOLLERATED_DEVIATION = Math.toRadians(6);
  private static final double TOLLERATED_DISTANCE = 0.7; // in meters
  private double lookTime;
  private boolean robotIsWalking;

  /**
   * Constructor.
   *
   * @param All passed object must be already initalized, and correctly synchronized with the simulation server. None of the arguments can be null.
   */
  public SoccerThinking(PerceptorInput percIn, LocalFieldView localView,
          KeyframeMotion kfMotion, Logger log) {
    this.percIn = percIn;
    this.localView = localView;
    this.motion = kfMotion;
    //this.motion.setLogging(true);
    this.log = log;
    this.ball = this.localView.getBall();
    HashMap<GoalPostID, GoalPostModel> goalPosts = this.localView.getGoals();
    this.oppGoalLPost = goalPosts.get(GoalPostID.G1R);
    this.oppGoalRPost = goalPosts.get(GoalPostID.G2R);
    this.lookTime = LookAroundMotion.LOOK_TIME;
    this.robotIsWalking = false;
  }

  /**
   * Evaluate the actual situation the player is in, and set appropriate 
   * motions.
   * The idea for the soccer behavior is described in the comment above the 
   * definition of this class. 
   */
  public void decide() {
    if (motion.ready()) {
      
      //log.log("new decision");

      double serverTime = percIn.getServerTime();
      
      // if the robot has fallen down
      if (percIn.getAcc().getZ() < 7) {
        if (percIn.getAcc().getY() > 0) {
          motion.setStandUpFromBack();
        } else {
          motion.setRollOverToBack();
        }
      } 
      
      // if the robot has the actual ball coordinates
      else if ((serverTime - ball.getTimeStamp()) < lookTime) {
        
        Vector3D ballCoords = ball.getCoords();
//        log.log("2. robot has the actual ball coordinates, horizontal angle: " 
//                + Math.toDegrees(ballCoords.getAlpha()) 
//                + " distance: " + ballCoords.getNorm()) ;

        // if the ball is not in front of the robot
        if (Math.abs(ballCoords.getAlpha()) > TOLLERATED_DEVIATION) {
//          log.log("3. the ball is not in front of the robot. ") ;
          if (robotIsWalking) {
            motion.setStopWalking();
            robotIsWalking = false;
          } else {
            if (ballCoords.getAlpha() > 0) {
              motion.setTurnLeftSmall();
            } else {
              motion.setTurnRightSmall();
            }
          }
        } 
        
        // if the robot is far away from the ball
        else if (ballCoords.getNorm() > TOLLERATED_DISTANCE) {
//          log.log("3. the robot is far away from the ball.");
          motion.setWalkForward();
          robotIsWalking = true;
        } 
        
        // if the robot has the actual goal coordinates
        else if ((serverTime - oppGoalLPost.getTimeStamp() < lookTime)
                && (serverTime - oppGoalRPost.getTimeStamp() < lookTime)) {
//          log.log("5. the robot has the actual goal coordinates");

          // if the ball does not lie between the robot and the goal
          if ((oppGoalLPost.getCoords().getAlpha() <= ballCoords.getAlpha())
                  || (oppGoalRPost.getCoords().getAlpha() >= ballCoords.getAlpha())) {
//            log.log("6. the ball does not lie between the robot and the goal");
            if (robotIsWalking) {
              motion.setStopWalking();
              robotIsWalking = false;
            } else {
              if (oppGoalLPost.getCoords().getAlpha() <= ballCoords.getAlpha()) {
                motion.setSideStepLeft();
              } else {
                motion.setSideStepRight();
              }
            }
          } 
          
          // if the robot is in a good dribbling position
          else {
//            log.log("7. the robot is in a good dribbling position");
            motion.setWalkForward();
            robotIsWalking = true;
          }
        } 
        
        // if the robot cannot sense the goal coordinates from its actual position
        else {
//          log.log("5. goal coordinates missing");
          motion.setTurnLeft();
        }
      } 
      
      // if the robot cannot sense the ball coordinates from its actual position
      else {
        motion.setTurnLeft();
//        log.log("1. ball coordinates missing");
      }
    }
  }
}
