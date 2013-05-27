package examples;

import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;
import util.Logger;
import util.RobotConsts;

/**
 * This agent shows basic concepts of using the RoboNewbie framework and gives
 * examples for interacting with the simulation server and using the classes
 * EffectorOutput and PerceptorInput.
 */
public class Agent_BasicStructure {

  public static void main(String args[]) {
    
    // Change here the class to the name of your own agent file 
    // - otherwise Java will always execute the Agent_BasicStructure.
    Agent_BasicStructure agent = new Agent_BasicStructure();
    
    // Establish the connection to the server.
    agent.init();
    
    // Run the agent program synchronized with the server cycle.
    // Parameter: Time in seconds the agent program will run. 
    agent.run(12);

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
  static final double beamX =    -1;
  static final double beamY =     0;
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
    int cycles = timeInSec * 50;
    int cycles2sec = 100;
    
    double degree;
    
    // do nothing for 2 seconds, just stay synchronized with the server
    for (int i = 0; i < cycles2sec; i++){
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
      degree = Math.toDegrees(percIn.getJoint(RobotConsts.LeftShoulderPitch));
      if (degree < 30) {
        effOut.setJointCommand(RobotConsts.LeftShoulderPitch, 1.0);
        effOut.setJointCommand(RobotConsts.RightShoulderPitch, 1.0);
      } else {
        effOut.setJointCommand(RobotConsts.LeftShoulderPitch, 0.0);
        effOut.setJointCommand(RobotConsts.RightShoulderPitch, 0.0);
      }
      
      log.log("reached angle: " + degree);
      
      
      // "Hardware" access to the effectors (simulated motors).
      act();
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
