package examples.agentSimpleWalkToBall;

import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;
import keyframeMotion.KeyframeMotion;
import localFieldView.LocalFieldView;
import util.Logger;



/**
 * This agent shows the structure of an agent, that does something sensible:
 * it walks into the direction of the ball.
 */
public class Agent_SimpleWalkToBall {

  public static void main(String args[]) {
    
    Agent_SimpleWalkToBall agent = new Agent_SimpleWalkToBall();
    
    agent.init();
    agent.run();
    agent.log.printLog();
    System.out.println("Agent stopped.");
  }
  
  private Logger log;
  private PerceptorInput percIn;
  private EffectorOutput effOut;
  private KeyframeMotion kfMotion;
  private LocalFieldView localView;
  private SimpleThinking simpleThinking;
  
  /** A player is identified in the server by its player ID and its team name. 
   There are at most two teams an the field, and every agent of a single team 
   must have a unique player ID between 1 and 11. 
   If the identification works right, it is visualized in the monitor: 
   the robots on the field have eather red or blue parts. An unidentified 
   robot has grey parts. 
   Attention! Using an invalid player ID (0 or negative value) leads to 
   undefined behaviour of the agent program. */
  static final String id = "1";
  static final String team = "simpleStupid";
   /** The "beam"-coordinates specify the robots initial position on the field.
   The root of the global field coordinate system is in the middle of the 
   field, the system is right-handed. The x-axis points to the opponent goal, 
   so the initial position has a negative x-value to beam the robot on its own
   half. The robot can be placed with a initial orientation given in the 
   variable beamRot, in degrees, counterclockwise relative to the x-axis. */  
  static final double beamX =    -1;
  static final double beamY =     0;
  static final double beamRot =   -90;

  /**
   * Initialize the connection to the server, the internal used classes and 
   * their relations to each other, and create the robot at a specified position 
   * on the field. 
   */
  public void init() {

    log = new Logger();

    ServerCommunication sc = new ServerCommunication();

    percIn = new PerceptorInput(sc);
    effOut = new EffectorOutput(sc);
    kfMotion = new KeyframeMotion(effOut, percIn, log);
    localView = new LocalFieldView(percIn, log, team, id);
    
    simpleThinking = new SimpleThinking(localView, kfMotion);

    sc.initRobot(id, team, beamX, beamY, beamRot);
    
  }
  
  /**
   * Main loop of the agent program, where it is synchronized with the 
   * simulation server. 
   */
  public void run(){
    
    int agentRunTimeInSeconds = 120;
    // The server cycle represents 20ms, so the agent has to execute 50 cycles 
    // to run 1s. 
    int totalServerCycles = agentRunTimeInSeconds * 50;
    
    // This loop synchronizes the agent with the server.
    for (int i = 0; i < totalServerCycles; i++) {
       
      sense();     
      
      think();
      
      act();
    }
  }
  
  /**
   * Update the world and robot hardware informations, that means process 
   * perceptor values provided by the server.
   */
  private void sense() {
    // Receive the server message and parse it to get the perceptor values. 
    percIn.update();
    // Proceed and store values of the vision perceptor.
    localView.update();
  }
  
  /**
   * Decide, what is sensible in the actual situation. 
   * Use the knowledge about the field situation updated in sense(), and choose
   * the next movement - it will be realized in act().
   */
  private void think(){
    // In this case: turn to the ball and walk towards it. 
    simpleThinking.decide();
  }
  
  /**
   * Move the robot hardware, that means send effector commands to the server. 
   */
  private void act(){
    // Calculate effector commands and send them to the server, this method
    // of class KeyframeMotion has to be called in every server cycle. 
    kfMotion.executeKeyframeSequence();
    // Send agent message with effector commands to the server.
    effOut.sendAgentMessage();
  }
}
