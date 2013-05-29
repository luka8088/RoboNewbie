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
    agent.run(180);

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
  private static final double TOLLERATED_DEVIATION_TRIGGER = Math.toRadians(30);
  private static final double TOLLERATED_DEVIATION = Math.toRadians(6);
  protected static final double TOLLERATED_DISTANCE = 0.7; // in meters

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
    
    // simulated robot hardware on the soccer field
    sc.initRobot(id, team, beamX, beamY, beamRot);
  }

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
      decide();

      // "Hardware" access to the effectors (simulated motors).
      act();
      
    }
  }
  
  public boolean turningTowardsTheBall = false;

  public void decide() {
    if (!motion.ready())
      return;

    double serverTime = percIn.getServerTime();
      
    if (motion.currentPosture() == "laying-down") {
      motion.setStandUp();
      return;
    }
      
    // if the robot does not have the actual ball coordinates
    // ball not visible?
    if ((serverTime - ball.getTimeStamp()) > lookTime) {
      motion.setTurnLeft();
      return;
    }

    Vector3D ballCoords = ball.getCoords();
//        log.log("2. robot has the actual ball coordinates, horizontal angle: " 
//                + Math.toDegrees(ballCoords.getAlpha()) 
//                + " distance: " + ballCoords.getNorm()) ;
    
    if (Math.abs(ballCoords.getAlpha()) > TOLLERATED_DEVIATION_TRIGGER
        || turningTowardsTheBall) {
      //turningTowardsTheBall = Math.abs(ballCoords.getAlpha()) > Math.toRadians(6);
      //if (turningTowardsTheBall) {
        if (ballCoords.getAlpha() > 0) {
          motion.setTurnLeftSmall();
        } else {
          motion.setTurnRightSmall();
        }
        //return;
      //}
        
      //if (Math.abs(ballCoords.getAlpha()) < 0.1) {
      //  turningTowardsTheBall = false;
      //}
      turningTowardsTheBall =
        Math.abs(ballCoords.getAlpha()) > TOLLERATED_DEVIATION;
      return;
    }
/*
    // if the ball is not in front of the robot
    if (Math.abs(ballCoords.getAlpha()) > TOLLERATED_DEVIATION) {
//          log.log("3. the ball is not in front of the robot. ") ;
      if (motion.isWalking()) {    
        motion.setStopWalking();
      } else {
        if (ballCoords.getAlpha() > 0) {
          motion.setTurnLeftSmall();
        } else {
          motion.setTurnRightSmall();
        }
      }
    } 

    // if the robot is far away from the ball
    else /**/ if (ballCoords.getNorm() > TOLLERATED_DISTANCE) {
//          log.log("3. the robot is far away from the ball.");
      motion.setWalkForward();
    } 

    // if the robot has the actual goal coordinates
    else if ((serverTime - oppGoalLPost.getTimeStamp() < lookTime)
            && (serverTime - oppGoalRPost.getTimeStamp() < lookTime)) {
//          log.log("5. the robot has the actual goal coordinates");

      // if the ball does not lie between the robot and the goal
      if ((oppGoalLPost.getCoords().getAlpha() <= ballCoords.getAlpha())
              || (oppGoalRPost.getCoords().getAlpha() >= ballCoords.getAlpha())) {
//            log.log("6. the ball does not lie between the robot and the goal");
        if (motion.isWalking()) {
          motion.setStopWalking();
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
      }
    } 

    // if the robot cannot sense the goal coordinates from its actual position
    else {
//          log.log("5. goal coordinates missing");
      motion.setTurnLeft();
    }
    
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
