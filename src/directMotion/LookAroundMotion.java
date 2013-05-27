package directMotion;

import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import util.Logger;
import static util.RobotConsts.NeckPitch;
import static util.RobotConsts.NeckYaw;


/**
 * Special motion for permanently turning the robots head.
 * 
 * This motion will turn the robots head down to about 30°, back into the 
 * initial upright position, then left to about 60°, right to -60° and back to 
 * the initial position. This is repeated infinitely. 
 * With the look around motion the agent can utilize the vision perceptor to get
 * a field of view of amost 360° (just an angle of less than 5° behind the
 * robot is omitted). 
 * When using this class together with class LocalFieldView pay attention to the
 * restrictions of LocalFieldView (see the comment on that class).
 * 
 * Usage of this class: 
 * Just call the method look() in every server cycle. This method sets the 
 * actual commands for the neck effectors. 
 * When accessing the values of the vision perceptor, the accessing class can 
 * check, if the needed values where sensed during the last run of the head. Use
 * the provided variable LOOK_TIME for that. 
 * 
 * Required context in the agent class: 
 * An PerceptorInput instance and an EffectorOutput instance have to synchronize 
 * the agent with the server correctly. The EffectorOutput object must send the
 * effector commands after look() has been called. See Agent_SimpleSoccer in 
 * package examples.agentSimpleSoccer for an example of correct integration in 
 * an agent program. 
 * 
 * Using LookAroundMotion together with other motion classes: 
 * The agent class should take care, that look() is the last called method, that 
 * sets the neck joint commands. Otherwise the looking motion can be interrupted. 
 * An example shows the correct cooperation with another motion class: 
 * Agent_SimpleSoccer in package examples.agentSimpleSoccer.
 */
public class LookAroundMotion {

  /**
   * Time needed for one complete looking around move in seconds. 
   * The move will take at most this time. 
   * The value has been gained by observing the server time, when the move has
   * been executed. 
   */
  public static final double LOOK_TIME = 1.8;
  
  private enum HeadPose{ DOWN, LEFT, RIGHT, D_CENTER_L, R_CENTER_D};
  private static final double VELOCITY = Math.PI;
  
  PerceptorInput percIn;
  EffectorOutput effOut;
  Logger log;
  HeadPose nextPose;
  
  /**
   * Constructor.
   * 
   * @param percIn Has to be already initialized.
   * @param effOut Has to be already initialized.
   * @param log Has to be already initialized.
   */
  public LookAroundMotion(PerceptorInput percIn, EffectorOutput effOut, Logger log) {
    this.percIn = percIn;
    this.effOut = effOut;
    this.log = log;
    this.nextPose = HeadPose.DOWN;            
  }

  /**
   * Sets the neck joint commands to start/continue the turning move. 
   */
  public void look() {
    double yaw = Math.toDegrees(percIn.getJoint(NeckYaw));
    double pitch = Math.toDegrees(percIn.getJoint(NeckPitch));

        switch(nextPose){
          case DOWN:       
            if ( (pitch <= -40) ) 
              nextPose = HeadPose.D_CENTER_L;
            else 
              effOut.setJointCommand(NeckPitch, -VELOCITY);
            break;
          case D_CENTER_L:
            if ( Math.abs(pitch) <= 5)
            {effOut.setJointCommand(NeckPitch, 0);
              nextPose = HeadPose.LEFT;
            }
            else 
              effOut.setJointCommand(NeckPitch, VELOCITY);
            break;
          case LEFT:
            if ( (yaw >= 54) ) 
              nextPose = HeadPose.RIGHT;
            else 
              effOut.setJointCommand(NeckYaw, VELOCITY);
            break;
          case RIGHT:
            if ( yaw <= -54 )
              nextPose = HeadPose.R_CENTER_D;
            else 
              effOut.setJointCommand(NeckYaw, -VELOCITY);
            break;
          case R_CENTER_D:
            if ( Math.abs(yaw) <= 5 ) 
            {effOut.setJointCommand(NeckYaw, 0);   
              nextPose = HeadPose.DOWN;}
            else 
              effOut.setJointCommand(NeckYaw, VELOCITY);
            break;
        }
  }
 
}
