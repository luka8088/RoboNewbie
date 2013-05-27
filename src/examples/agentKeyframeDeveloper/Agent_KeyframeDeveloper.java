package examples.agentKeyframeDeveloper;

import util.Logger;
import agentIO.ServerCommunication;
import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import keyframeMotion.KeyframeMotion;

/**
 * Agent for testing of new keyframe sequences - also an example for using 
 * class KeyframeMotion and the accelerometer perceptor. 
 * 
 * Usage:
 * The new keyframe sequence has to be saved as 
 * "[RoboNewbie project folder]/keyframes/test.txt" .
 * Simply run this class, the robot will execute the sequence and if it falls 
 * down, it will stand up and perform the new movement again. 
 * File test.txt can be overwritten while this class is running, the saved 
 * changes are loaded each time before the move is executed. 
 * 
 * @author Nika, Dieter
 */
public class Agent_KeyframeDeveloper {

  /** Creates Agent and calls the routines.
   */
  public static void main(String args[]) {

    Agent_KeyframeDeveloper agent = new Agent_KeyframeDeveloper();
    agent.init();

    //Attention! When the time to run the programm is set too long, RAM (memory) 
    //can be filled up because of generated log strings. 
    //1200sec. = 20min. is completely sercure on my computer.     
    agent.run(1200);
    agent.log.printLog();
    System.out.println("Agent stopped.");
  }
  
  private PerceptorInput percIn;
  private EffectorOutput effOut;
  private KeyframeMotion motion;
  private KeyDevThinking keyDevThinking;
  private Logger log;
  
  static final String ID = "1";
  static final String team = "myT";
  static final double beamCoordX =    -2;
  static final double beamCoordY =     0;
  static final double beamCoordRot =   0;
    
  /** 
   * Initialize the connection to the server, the internal used classes and the
   * robot on the soccer field.
   */
  private void init() {

      // connection to the server
        ServerCommunication sc = new ServerCommunication();

      // internal agent classes
        log = new Logger();
        percIn = new PerceptorInput(sc);
        effOut = new EffectorOutput(sc);
        motion = new KeyframeMotion(effOut, percIn, log);
        keyDevThinking = new KeyDevThinking(motion, percIn);
        
      // robot on the field
        sc.initRobot(ID, team, beamCoordX, beamCoordY, beamCoordRot);
  }
  
  /**
   * Main loop of the agent program, where it is synchronized with the 
   * simulation server. 
   * 
   * @param timeInSec Time in seconds the agent program will run. 
   */
  private void run(int timeInSec) {
    // This is needed to use the accelerometer data. 
    // Several updates of the perceptor input are performed for initialization of 
    // the accelerometer.
    for (int i = 0; i < 100; i++) {
      sense();
      act();
    }    
    
    int loops = timeInSec * 50;
    
    //cycle synchronized with server
    for (int i = 0; i < loops; i++) {
        
      sense();

      think();
      
      act();
    }
  }

  /*
   * Update the world and body information by reading a server message.
   */
  private void sense() {
    percIn.update();
  }
  
  private void think(){
    keyDevThinking.decide();
  }
  
  private void act(){
    motion.executeKeyframeSequence();
    effOut.sendAgentMessage();
  }
}
