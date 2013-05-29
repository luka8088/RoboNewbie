package soccer;

import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;
import directMotion.LookAroundMotion;
import java.util.HashMap;
import keyframeMotion.KeyframeMotion;
import localFieldView.BallModel;
import localFieldView.GoalPostModel;
import localFieldView.LocalFieldView;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts;
import util.Logger;
import util.RobotConsts;

/**
 * This agent shows basic concepts of using the RoboNewbie framework and gives
 * examples for interacting with the simulation server and using the classes
 * EffectorOutput and PerceptorInput.
 */
public class robo1 {

  public static void main(String args[]) {
    
    // Change here the class to the name of your own agent file 
    // - otherwise Java will always execute the Agent_BasicStructure.
    robo1 agent = new robo1();
    
    // Establish the connection to the server.
    agent.init();
    
    // Run the agent program synchronized with the server cycle.
    // Parameter: Time in seconds the agent program will run. 
    agent.run(300);

    // The logged informations are printed here, when the agent is not timed 
    // with the server anymore. Printing immediately when informations are 
    // gained during the server cycles could slow down the agent and impede 
    // the synchronization.
    agent.printlog();
    
    System.out.println("Agent stopped.");
  }

  private Logger log;
  private PerceptorInput percIn;
  private EffectorOutput effOut;
  private KeyframeMotion motion;
  BallModel ball;
  private double lookTime;
  private LocalFieldView localView;
  private LookAroundMotion lookAround;
  
  GoalPostModel oppGoalLPost, oppGoalRPost;
  private static final double TOLLERATED_DEVIATION = Math.toRadians(6);
  protected static final double TOLLERATED_DISTANCE = 0.7; // in meters
  protected boolean robotIsWalking;

  /** A player is identified in the server by its player ID and its team name. 
   There are at most two teams an the field, and every agent of a single team 
   must have a unique player ID between 1 and 11. 
   If the identification works right, it is visualized in the monitor: 
   the robots on the field have eather red or blue parts. An unidentified 
   robot has grey parts. 
   Attention! Using an invalid player ID (0 or negative value) leads to 
   undefined behaviour of the agent program. */
  static final String id = "1";
  static final String team = "myT";
  /** The "beam"-coordinates specify the robots initial position on the field.
   The root of the global field coordinate system is in the middle of the 
   field, the system is right-handed. The x-axis points to the opponent goal, 
   so the initial position must have a negative x-value. The robot can be placed
   with an initial orientation given in the variable beamRot, in degrees, 
   counterclockwise relative to the x-axis. */
  static final double beamX =    -2;
  //static final double beamX =    -0.15;
  static final double beamY =     1;
  //static final double beamY =     -0.1;
  static final double beamRot =   0;
    
  /**
   * Initialize the connection to the server, the internal used classes and 
   * their relations to each other, and create the robot at a specified position 
   * on the field. 
   */
  private void init() {

    // connection to the server
    ServerCommunication sc = new ServerCommunication();

    // internal agent classes
    log = new Logger();
    percIn = new PerceptorInput(sc);
    effOut = new EffectorOutput(sc);
    motion = new KeyframeMotion(effOut, percIn, log);
    localView = new LocalFieldView(percIn, log, team, id);
    ball = localView.getBall();
    lookAround = new LookAroundMotion(percIn, effOut, log);

    HashMap<FieldConsts.GoalPostID, GoalPostModel> goalPosts = this.localView.getGoals();
    this.oppGoalLPost = goalPosts.get(FieldConsts.GoalPostID.G1R);
    this.oppGoalRPost = goalPosts.get(FieldConsts.GoalPostID.G2R);
    this.lookTime = LookAroundMotion.LOOK_TIME;
    this.robotIsWalking = false;
    
    // simulated robot hardware on the soccer field
    sc.initRobot(id, team, beamX, beamY, beamRot);
  }
  
  double sss = 1;
  
  double neckYawDegree;
  double neckPitchDegree;	
  double leftShoulderPitchDegree;
  double leftShoulderYawDegree;
  double leftArmRollDegree;
  double leftArmYawDegree;
  double leftHipYawPitchDegree;
  double leftHipRollDegree;	
  double leftHipPitchDegree;	
  double leftKneePitchDegree;
  double leftFootPitchDegree;
  double leftFootRollDegree;
  double rightHipYawPitchDegree;
  double rightHipRollDegree;
  double rightHipPitchDegree;
  double rightKneePitchDegree;	
  double rightFootPitchDegree;
  double rightFootRollDegree;
  double rightShoulderPitchDegree;
  double rightShoulderYawDegree;
  double rightArmRollDegree;
  double rightArmYawDegree;   

  /**
   * Main loop of the agent program, where it is synchronized with the 
   * simulation server. 
   * 
   * @param timeInSec Time in seconds the agent program will run. 
   */
  private void run(int timeInSec) {
    // The server executes about 50 cycles per second. 
    //int cyclesPerSecond = 50;
    int cycles = timeInSec * 50;
    int cycles2sec = 100;
    
    double degree;
    boolean kicked = false;
    
    // do nothing for 2 seconds, just stay synchronized with the server
    for (int i = 0; i < cycles2sec / 4; i++){
      sense();
      act();
    }
    
    
    // Loop synchronized with server.
    for (int i = 0; i < cycles; i++) {
      
      // "Hardware" access to the perceptors (simulated sensors) and processing
      // of the perceptor data. 
      sense();
      
      
      // "Think":
      // Use the perceptor data (simulated sensory data, here gained by percIn) 
      // to control the effectors (simulated motors, here activated by effOut) 
      // accordingly.
      // This agent raises the robots arms to approximately 30°.
      //degree = Math.toDegrees(percIn.getJoint(RobotConsts.RightShoulderYaw));
      
      decide();
      //printlog();
      
      
      //if (motion.ready()) {
        //if (!kicked) {
        //  kicked = true;
        //  motion.setKickTheBall();
        //}
      //  motion.setWalkForward();
      //}

      /*
      if (i == 100) {
        sss = 1.0 / 4;
        
        //leftHipPitchDegree = 25;
        //rightHipPitchDegree = 25;

        leftKneePitchDegree = -75;
        rightKneePitchDegree = -75;

        leftFootPitchDegree = 55;
        rightFootPitchDegree = 55;
      }
      
      if (i == 150) {
        leftHipPitchDegree = 20;
        rightHipPitchDegree = 20;
      }
      
      if (i == 200) {
        sss = 1;
        
        leftHipPitchDegree = 0;
        rightHipPitchDegree = 0;

        leftKneePitchDegree = 0;
        rightKneePitchDegree = 0;

        leftFootPitchDegree = 0;
        rightFootPitchDegree = 0;

        //break;
      }

      if (i == 210) {
        leftFootPitchDegree = -45;
        rightFootPitchDegree = -45;
      }
      
      /*****/
      
      /*
      if (i == 50)
        rightShoulderYawDegree = -40;

      if (i == 75)
        rightShoulderYawDegree = -80;

      if (i == 100)
        rightShoulderYawDegree = -5;
      /**/
        
        
        
      /*
      degree = Math.toDegrees(percIn.getJoint(RobotConsts.RightShoulderYaw));
      
      log.log("diff: " + (rightShoulderYawDegree - degree));
      log.log("abs diff: " + (rightShoulderYawDegree - degree));
      
      if (Math.abs(rightShoulderYawDegree - degree) > 3) {
        double diff = rightShoulderYawDegree - degree;
        double speed;
          speed = 2 * Math.PI;
        if (Math.abs(diff) < 360 / 50) // full turn degree / cycles per second
          speed = speed * (diff / 90); // diff / max turn degree
        if (diff < 0)
          speed = speed * -1;
        effOut.setJointCommand(RobotConsts.RightShoulderYaw, speed);
        //if (diff > 0)
        //  //effOut.setJointCommand(RobotConsts.RightShoulderYaw, 2 * Math.PI);
        //  effOut.setJointCommand(RobotConsts.RightShoulderYaw, (diff / 90) * 2 * Math.PI);
        //else
        //  //effOut.setJointCommand(RobotConsts.RightShoulderYaw, -2 * Math.PI);
        //  effOut.setJointCommand(RobotConsts.RightShoulderYaw, (diff / 90) * 2 * Math.PI);
      } else
        effOut.setJointCommand(RobotConsts.RightShoulderYaw, 0);
      
      /*
      if (degree > -10)
        effOut.setJointCommand(RobotConsts.RightShoulderYaw, -2 * Math.PI);
      else if (degree < -80)
        effOut.setJointCommand(RobotConsts.RightShoulderYaw, 2 * Math.PI);
      /**/
      
      //if (i > 20)
      //  break;
        
      //effOut.setJointCommand(RobotConsts.RightShoulderYaw, -2 * Math.PI);
      //if (i < 10)
      //  effOut.setJointCommand(RobotConsts.RightShoulderYaw, -2 * Math.PI);
      //else if (i < 20)
      //  effOut.setJointCommand(RobotConsts.RightShoulderYaw, 2 * Math.PI);
      //else
      //  break;
      //effOut.setJointCommand(RobotConsts.RightShoulderYaw, 1.0);
      //effOut.setJointCommand(RobotConsts.RightShoulderYaw, -1.0);
      /*
      if (i < 20) {
        effOut.setJointCommand(RobotConsts.RightFootRoll, -1.0);
        effOut.setJointCommand(RobotConsts.LeftFootRoll, -1.0);
      
        effOut.setJointCommand(RobotConsts.RightHipRoll, 1.0);
        effOut.setJointCommand(RobotConsts.LeftHipRoll, 1.0);
      } else {
        effOut.setJointCommand(RobotConsts.RightFootRoll, 0);
        effOut.setJointCommand(RobotConsts.LeftFootRoll, 0);
        effOut.setJointCommand(RobotConsts.RightHipRoll, 0);
        effOut.setJointCommand(RobotConsts.LeftHipRoll, 0);
      }
      //effOut.setJointCommand(RobotConsts.RightKneePitch, -1.0);
      /*
      if (degree < 30) {
        effOut.setJointCommand(RobotConsts.LeftShoulderPitch, 1.0);
        effOut.setJointCommand(RobotConsts.RightShoulderPitch, 2.0);
      } else {
        effOut.setJointCommand(RobotConsts.LeftShoulderPitch, 3.0);
        effOut.setJointCommand(RobotConsts.RightShoulderPitch, 0.0);
      }
      /**/
      
      //degree = Math.toDegrees(percIn.getJoint(RobotConsts.RightShoulderYaw));
      //log.log("reached angle: " + degree);
      
      //autoMove();
      
      // "Hardware" access to the effectors (simulated motors).
      act();
      
    }
  }
  
  int ii = 0;

  public void decide() {
    if (!motion.ready())
      return;

    //log.log("new decision");

    //double serverTime = percIn.getServerTime();

    // if the robot has fallen down
    //if (motion.currentPosture() == "laying-down") {
    //  motion.setStandUp();
    //  return;
    //}
      
    /*
    if (percIn.getAcc().getZ() < 7)
      if (percIn.getAcc().getY() > 0) {
        motion.setStandUpFromBack();
        return;
      } else {
        motion.setRollOverToBack();
        return;
      }
    /**/
    
      
    //motion.setWalkForward();

      //log.log("new decision");

    double serverTime = percIn.getServerTime();
      
    if (motion.currentPosture() == "laying-down") {
      motion.setStandUp();
      return;
    }
    
    /*
    ii++;
    
    if (ii < 4) {
        motion.setWalkForward();
    } else {
        motion.setStopWalking();
        ii = 0;
    }
    
    
    if (true)
      return;
    
    /**/
      
    //log.log("ball.getTimeStamp(): " + ball.getTimeStamp());
    //log.log("(serverTime - ball.getTimeStamp()): " + (serverTime - ball.getTimeStamp()));
    //log.log("lookTime: " + lookTime);
    
      // if the robot has the actual ball coordinates
    if ((serverTime - ball.getTimeStamp()) < lookTime) {
        
        Vector3D ballCoords = ball.getCoords();
//        log.log("2. robot has the actual ball coordinates, horizontal angle: " 
//                + Math.toDegrees(ballCoords.getAlpha()) 
//                + " distance: " + ballCoords.getNorm()) ;

        // if the ball is not in front of the robot
        if (Math.abs(ballCoords.getAlpha()) > TOLLERATED_DEVIATION) {
//          log.log("3. the ball is not in front of the robot. ") ;
          //if (robotIsWalking) {
          if (motion.isWalking()) {    
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
            //if (robotIsWalking) {
            if (motion.isWalking()) {
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
    
  protected void autoMove () {

  autoMoveJoint(RobotConsts.NeckYaw, 241, neckYawDegree);
  autoMoveJoint(RobotConsts.NeckPitch, 91, neckPitchDegree);
  
  // todo: fix maxTurnDegree
  autoMoveJoint(RobotConsts.LeftShoulderPitch, 91, leftShoulderPitchDegree);
  autoMoveJoint(RobotConsts.LeftShoulderYaw, 91, leftShoulderYawDegree);
  autoMoveJoint(RobotConsts.LeftArmRoll, 91, leftArmRollDegree);
  autoMoveJoint(RobotConsts.LeftArmYaw, 91, leftArmYawDegree);
  autoMoveJoint(RobotConsts.LeftHipYawPitch, 91, leftHipYawPitchDegree);
  autoMoveJoint(RobotConsts.LeftHipRoll, 91, leftHipRollDegree);	
  autoMoveJoint(RobotConsts.LeftHipPitch, 91, leftHipPitchDegree);	
  autoMoveJoint(RobotConsts.LeftKneePitch, 91, leftKneePitchDegree);
  autoMoveJoint(RobotConsts.LeftFootPitch, 91, leftFootPitchDegree);
  autoMoveJoint(RobotConsts.LeftFootRoll, 91, leftFootRollDegree);
  autoMoveJoint(RobotConsts.RightHipYawPitch, 91, rightHipYawPitchDegree);
  autoMoveJoint(RobotConsts.RightHipRoll, 91, rightHipRollDegree);
  autoMoveJoint(RobotConsts.RightHipPitch, 91, rightHipPitchDegree);
  autoMoveJoint(RobotConsts.RightKneePitch, 91, rightKneePitchDegree);	
  autoMoveJoint(RobotConsts.RightFootPitch, 91, rightFootPitchDegree);
  autoMoveJoint(RobotConsts.RightFootRoll, 91, rightFootRollDegree);
  autoMoveJoint(RobotConsts.RightShoulderPitch, 91, rightShoulderPitchDegree);
  autoMoveJoint(RobotConsts.RightShoulderYaw, 91, rightShoulderYawDegree);
  autoMoveJoint(RobotConsts.RightArmRoll, 91, rightArmRollDegree);
  autoMoveJoint(RobotConsts.RightArmYaw, 91, rightArmYawDegree);   
  
  /*
    autoMoveJoint();
  
    double degree;

    degree = Math.toDegrees(percIn.getJoint(RobotConsts.RightShoulderYaw));
    
    log.log("diff: " + (rightShoulderYawDegree - degree));
    log.log("abs diff: " + (rightShoulderYawDegree - degree));
    
    if (Math.abs(rightShoulderYawDegree - degree) > 3) {
      double diff = rightShoulderYawDegree - degree;
      double speed;
        speed = 2 * Math.PI;
      if (Math.abs(diff) < 360 / 50) // full turn degree / cycles per second
        speed = speed * (diff / 90); // diff / max turn degree
      if (diff < 0)
        speed = speed * -1;
      effOut.setJointCommand(RobotConsts.RightShoulderYaw, speed);
      //if (diff > 0)
      //  //effOut.setJointCommand(RobotConsts.RightShoulderYaw, 2 * Math.PI);
      //  effOut.setJointCommand(RobotConsts.RightShoulderYaw, (diff / 90) * 2 * Math.PI);
      //else
      //  //effOut.setJointCommand(RobotConsts.RightShoulderYaw, -2 * Math.PI);
      //  effOut.setJointCommand(RobotConsts.RightShoulderYaw, (diff / 90) * 2 * Math.PI);
    } else
      effOut.setJointCommand(RobotConsts.RightShoulderYaw, 0);
    /**/
    
  }
  
  protected void autoMoveJoint (int joint, double maxTurnDegree, double degree) {

    double currentDegree = Math.toDegrees(percIn.getJoint(joint));
    double diff = degree - currentDegree;
    double speed = 0;
    
    if (Math.abs(diff) > 3) {
      speed = 0.2 + sss * 2 * Math.PI; // max speed
      if (Math.abs(diff) < 360 / 50) // full turn degree / cycles per second
        speed = speed * (diff / maxTurnDegree);
      if (diff < 0)
        speed = speed * -1;
    }
    
    effOut.setJointCommand(joint, speed);
    
  }

  /**
   * Update the world and robot hardware informations, that means process 
   * perceptor values provided by the server.
   * 
   * Here is listed a simple sequence of method calls, which are executed in 
   * every server cycle 
   * 1) to synchronize perceptor processing classes with the loop of the simulation 
   * server 
   * 2) to ensure that the agent gets the actual informations about the robot and 
   * the soccer field from the perceptors (simulated sensors).
   */
  private void sense() {
    // Receive the server message and parse it to get the perceptor values. 
    percIn.update();
    // Proceed and store values of the vision perceptor.
    localView.update();
  }
  
  /**
   * Move the robot hardware, that means send effector commands to the server. 
   * 
   * Here is listed a simple sequence of method calls, which are executed in 
   * every server cycle 
   * 1) to calculate the effector commands, if needed.
   * 2) to send the effector commands to the server regularly in every server 
   * cycle. 
   * 
   * Notice: At least the "syn" effector has to be sent in every server cycle.
   * Look up "agent sync mode" for details.
   */
  private void act(){
    motion.executeKeyframeSequence();
    lookAround.look();
    // Send agent message with effector commands to the server.
    effOut.sendAgentMessage();
  }

  /**
   * Print logged informations. 
   */
  private void printlog() {
    log.printLog();
  }
  
}
