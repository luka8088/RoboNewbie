package examples;

import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;

/**
 * A dummy agent for reading the hear perceptor and the vision perceptor with 
 * Agent_TestPerceptorInput and Agent_TestLocalFieldView .
 */
public class Agent_Dummy {

  public static void main(String args[]) {

    Agent_Dummy agent = new Agent_Dummy();

    agent.init();
    
    agent.run();
  }
  
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
  static final String ID = "2";
  static final String team = "teamA";
   /** The "beam"-coordinates specify the robots initial position on the field.
   The root of the global field coordinate system is in the middle of the 
   field, the system is right-handed. The x-axis points to the opponent goal, 
   so the initial position has a negative x-value to beam the robot on its own
   half. The robot can be placed with a initial orientation given in the 
   variable beamRot, in degrees, counterclockwise relative to the x-axis. */
  static final double beamX =    -1;
  static final double beamY =     0;
  static final double beamRot =   180;

  /**
   * Initialize connection to the server, the internal used classes and the 
   * robot on the field.
   */
  private void init() {

    ServerCommunication sc = new ServerCommunication();

    percIn = new PerceptorInput(sc);
    effOut = new EffectorOutput(sc);

    sc.initRobot(ID, team, beamX, beamY, beamRot);
  }

  private void run() {
    for (int i = 0; i < 2000; i++) {
      sense();
      if ((i % 4) == 0) 
        effOut.setSayMessage("hello-from-teamA");
      act();
    }
  }

  private void sense() {
    percIn.update();
  }

  private void act() {
    effOut.sendAgentMessage();
  }
}
