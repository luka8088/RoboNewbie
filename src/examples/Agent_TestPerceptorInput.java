package examples;


import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;
import agentIO.perceptors.*;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts;
import util.Logger;
import util.RobotConsts;

/**
 * This class shows how to use the perceptor values provided by class
 * PerceptorInput (package agentIO) and the Logger class (package util).
 *
 * Class PerceptorInput provides the raw values of all perceptors.
 *
 * This class tests all the perceptors. To produce an input for the hear
 * perceptor, start examples.Agent_Dummy.java and examples.Agent_Dummy2.java
 * before you start this class.
 *
 * The vision perceptor is a special case, raw data from it need some further
 * modeling. A convenient access to the vision data provides class
 * localFieldView.LocalFieldView , students learning robotics/AI with RoboNewbie
 * should use this class.
 *
 * If you really need access to raw vision data, just uncomment the outputs in
 * method Agent_TestPerceptorInput.run(). Use as targets the dummy agents
 * mentioned above.
 * 
 * @see agentIO.PerceptorInput
 */
public class Agent_TestPerceptorInput {

  public static void main(String args[]) {

    Agent_TestPerceptorInput agent = new Agent_TestPerceptorInput();

    agent.init();

    agent.run();

    // The logged informations are printed here, when the agent is not timed 
    // with the server anymore. Printing immediately when informations are 
    // gained during the server cycles could slow down the agent and impede 
    // the synchronization.
    agent.log.printLog();
    
    System.out.println("Agent stopped.");
  }
  
  private PerceptorInput percIn;
  private EffectorOutput effOut;
  private Logger log;
  
  /** A player is identified in the server by its player ID and its team name. 
   There are at most two teams an the field, and every agent of a single team 
   must have a unique player ID between 1 and 11. 
   If the identification works right, it is visualized in the monitor: 
   the robots on the field have either red or blue parts. An unidentified 
   robot has grey parts. 
   Attention! Using an invalid player ID (0 or negative value) leads to 
   undefined behavior of the agent program. */
  static final String id = "3";
  static final String team = "teamA";
   /** The "beam"-coordinates specify the robots initial position on the field.
   The root of the global field coordinate system is in the middle of the 
   field, the system is right-handed. The x-axis points to the opponent goal, 
   so the initial position has a negative x-value to beam the robot on its own
   half. The robot can be placed with a initial orientation given in the 
   variable beamRot, in degrees, counterclockwise relative to the x-axis. */
  static final double beamX =    -0.5;
  static final double beamY =     1;
  static final double beamRot =   -90;
  
  /**
   * Initialize the connection to the server, the internal used classes and 
   * their relations to each other, and create the robot at a specified position 
   * on the field. 
   */
  private void init() {

    log = new Logger();

    ServerCommunication sc = new ServerCommunication();

    percIn = new PerceptorInput(sc);
    effOut = new EffectorOutput(sc);

    sc.initRobot(id, team, beamX, beamY, beamRot);
  }

  /**
   * Main loop of the agent program, synchronization with the simulation server
   * and reading of perceptor values.
   */
  private void run() {
    
    for (int i = 0; i < 50; i++) {
      
      sense();

      // server time perceptor
      log.log("--------------------------------------------------------------------------------------------------");
      log.log("Time since start of server: "+ percIn.getServerTime());
      log.log("--------------------------------------------------------------------------------------------------");
      
      // Just for comparing perceptor messages from the server with the values
      // got by PerceptorInput: 
      log.log("Server message:\n" + percIn.getServerMessage());
      
      // To produce a value for the hear perceptor.
      if (i == 5){
        effOut.setSayMessage("(say hellomyself)");
        log.log("say message set.");
      }
      
      // hinge joint perceptors
      log.log(percIn.getJointsDebugString());
      double leftArmRoll = percIn.getJoint(RobotConsts.LeftArmRoll);
      log.log("hinge joint - left arm roll in radians: "+ leftArmRoll 
              +", in degrees: "+ Math.toDegrees(leftArmRoll));
      
      // Accelerometer and gyro rate perceptors.
      // They are both given as Vector3D instances, so they are accessed in the 
      // same way.
      Vector3D acc = percIn.getAcc();
      log.log("Acc test output - "+ Logger.cartesianStr(acc));
      log.log("Acc value access - value in z-direction: "+ acc.getZ());
      
      Vector3D gyro = percIn.getGyro();
      log.log("Gyro test output - "+ Logger.cartesianStr(gyro) +" (value access same as for acc)");
      
      // Force resistance perceptors. 
      ForceResistancePerceptor frLeft = percIn.getFrLeft();
      if (frLeft != null)
        log.log(frLeft.toString());
      ForceResistancePerceptor frRight = percIn.getFrRight();
      if (frRight != null){
        log.log(frRight.toString());
        // Value access: 
        Vector3D frValue = frRight.getForce();
        log.log("Force resistance value access - force: "+ frValue.getNorm()+" Newton" );
        log.log("logger commenting: " + Logger.polarStr(frValue));
      }

      // Hear perceptor
      // Start the dummy agents to produce an input for this perceptor as 
      // mentioned in the comment on this class. 
      LinkedList<HearPerceptor> hearList = percIn.getHears();
      if ( hearList != null)
        for (HearPerceptor h : hearList) {
          log.log(h.toString());
          log.log("Hear value access - " + h.getDirection() + " " + h.getMessage());
        }

      // Game state perceptor
      GameStatePerceptor g = percIn.getGameState();
      log.log(g.toString());
      log.log("Game state value access - "+ g.getPlayTime() + " " + g.getPlayMode());
      
      
      // Vision perceptor
      // The vision is read here just for completeness (see the comment on this
      // class above its definition). A convenient access to the vision data
      // provides class LocalFieldView in package localFieldView. The usage of
      // LocalFieldView is shown in class examples.Agent_TestLocalFieldView . 
      Vector3D ball = percIn.getBall();
      //if (ball != null) 
        //log.log("Ball polar: " + Logger.polarStr(ball));     
      LinkedList<LineVisionPerceptor> lines = percIn.getLines();
//      if (lines != null)
//        for (LineVisionPerceptor l : lines)
//          log.log(l.toString());
      LinkedList<PlayerVisionPerceptor> players = percIn.getPlayerPositions();
      if (players != null)
        for (PlayerVisionPerceptor p : players){
          log.log(p.toString());
          //log.log(Logger.polarStr(p.getBodyPart(PlayerVisionPerceptor.BodyPart.llowerarm)));
        }      
      Vector3D g1r = percIn.getGoalPost(FieldConsts.GoalPostID.G1R);
//      if (g1r != null)
//        log.log("Goal G1R: " + Logger.polarStr(g1r));
      Vector3D g2r = percIn.getGoalPost(FieldConsts.GoalPostID.G2R);
//      if (g2r != null)
//        log.log("Goal G2R: " + Logger.polarStr(g2r));
      Vector3D g1l = percIn.getGoalPost(FieldConsts.GoalPostID.G1L);
//      if (g1l != null)
//        log.log("Goal G1L: " + Logger.polarStr(g1l));
      Vector3D g2l = percIn.getGoalPost(FieldConsts.GoalPostID.G2L);
//      if (g2l != null)
//        log.log("Goal G2L: " + Logger.polarStr(g2l));
      for (FieldConsts.FlagID id:FieldConsts.FlagID.values()){
        Vector3D f = percIn.getFlag(id);
//        if (f != null)
//          log.log("Flag " + id.toString() + ": " + Logger.polarStr(f));
      }
      act();
    }
  }

  /**
   * Update the world and robot hardware informations, that means process 
   * perceptor values provided by the server.
   */
  private void sense() {
    percIn.update();
  }

  /**
   * Move the robot hardware, that means send effector commands to the server. 
   */  
  private void act() {
    effOut.sendAgentMessage();
  }
}

