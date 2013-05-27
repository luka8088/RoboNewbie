package examples;


import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;
import directMotion.LookAroundMotion;
import java.util.HashMap;
import java.util.LinkedList;
import localFieldView.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts.FlagID;
import util.FieldConsts.GoalPostID;
import util.Logger;
import util.RobotConsts.BodyPartName;

/**
 * This class shows how to use the modeled field data gained from the vision 
 * perceptor and provided by class LocalFieldView (package localFieldView),
 * furthermore it shows the usage of the Logger class (package util).
 * 
 * For the coordinate system of the modeled field items see class
 * LocalFieldView.
 * 
 * Use this class together with Agent_Dummy.java and Agent_Dummy2.java (also
 * in package examples). They are target agents, which are sensed by the vision
 * perceptor and modeled by LocalFieldView for further usage. First start the
 * dummy classes, then this Agent_TestLocalFieldView.
 
 */
public class Agent_TestLocalFieldView{

  public static void main(String args[]) {

    // Change here the class to the name of your own agent file 
    // - otherwise Java will always execute the AgentBasicStructure.
    Agent_TestLocalFieldView agent = new Agent_TestLocalFieldView();

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
  private LocalFieldView localView;
  private Logger log;
  private LookAroundMotion lookAround;

  /** A player is identified in the server by its player ID and its team name. 
   There are at most two teams an the field, and every agent of a single team 
   must have a unique player ID between 1 and 11. 
   If the identification works right, it is visualized in the monitor: 
   the robots on the field have either red or blue parts. An unidentified 
   robot has grey parts. 
   Attention! Using an invalid player ID (0 or negative value) leads to 
   undefined behavior of the agent program. */
  static final String id = "1";
  static final String team = "teamA";
  // The "beam"-coordinates specify the robots initial position on the field.
  // See class ServerCommunication, method initRobot for details.
  static final double beamCoordX =    -1;
  static final double beamCoordY =    0.7;
  static final double beamCoordRot =  -60;

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
    localView = new LocalFieldView(percIn, log, team, id);
    lookAround = new LookAroundMotion(percIn, effOut, log);  

    // robot on the field
    sc.initRobot(id, team, beamCoordX, beamCoordY, beamCoordRot);
  }

  /**
   * Main loop of the agent program, synchronization with the simulation server
   * and reading of perceptor values.
   */
  private void run() {

    // The references to the models or model lists have to be got only once, 
    // because class LocalFieldView keeps the referenced models up to date. 
    BallModel ball = localView.getBall();
    HashMap<GoalPostID, GoalPostModel> goals = localView.getGoals();
    HashMap<FlagID, FlagModel> flags = localView.getFlags();
    LinkedList<PlayerModel> players = localView.getAllPlayers(); 
    LinkedList<LineModel> lines = localView.getLines();
    
    // At the beginning the vision perceptor might not have sensed any player, 
    // (if the agent´s robot is the only one on the field, or the other robots
    // stand behind it and it does not turn its head) so the reference to a 
    // player model can not begained at the initialisation. 
    // But when another player has been sensed once, the reference stays valid
    // and class LocalFieldView updates it. 
    PlayerModel specificPlayer = null;
    
    // The specific coordinates within the items have to be actualized in 
    // every server cycle.
    Vector3D vecBall;
    Vector3D vecGoal;
    Vector3D vecRobotBodyPart;
    Vector3D vecLine;
    
    
    
    for (int i = 0; i < 120; i++) {
      
      sense();
      
      log.log("--------------------------------------------------------------------------------------------------");
      log.log("Cycle "+ i + ", time since start of server: "+ percIn.getServerTime());
      log.log("--------------------------------------------------------------------------------------------------");
 
      // Every model inherits methods to check its actuality from the class
      // DatedItemModel.
      // Here they are shown by the ball model.
      log.log("Every item model");
      log.log("Access to the actuality of an item model, e.g. of the ball. \n"
              + "Is it in the field of view now? "+ ball.isInFOVnow() + '\n'
              + "Timestamp of last sensing: "+ ball.getTimeStamp());
      
// ball model
      log.log("Ball model");
      vecBall = ball.getCoords();
      log.log(ball.toString());
      log.log("Access to value, e.g. cartesian x-value: " + vecBall.getX());
            
// goal post model and flag model
      log.log("Goal post and flag models");
      for (GoalPostID id : GoalPostID.values())
        log.log(goals.get(id).toString());
      vecGoal = goals.get(GoalPostID.G2R).getCoords();
      for (FlagID id : FlagID.values())
        log.log(flags.get(id).toString());
      log.log("Access to details of goal post or flag, e.g. horizontal angle to goal post G2R: "
              + vecGoal.getAlpha());
        
// player model
      // Run Agent_Dummy from package examples.agentTestPerceptorInput to try that 
      // out. Agent_Dummy has to be beamed at coordinates x=-1.0, y=0.0. This
      // agent (TastLocalFieldView) should be beamed at coordinates x=-1, y=0.7,
      // rotation=-60.0. Agent_Dummy and this agent must belong to different 
      // teams.
      log.log("Player models");
      for (PlayerModel pm: players)
        log.log(pm.toString());
      // Get the reference to a player, when it is sensed for the first time.
      if (specificPlayer == null) {
        if (!players.isEmpty())
          specificPlayer = players.getFirst();
        else 
          log.log("Do not see Agent_Dummy (yet).");  
      }
      // Access the data of a sensed player. Keep in mind, that not all
      // parts of the player are visible at any time, depending on the
      // orientations of the agent´s own robot and the sensed player .
      if (specificPlayer != null)
        for (BodyPartName bp : BodyPartName.values()){
          vecRobotBodyPart = specificPlayer.getBodyPart(bp);
          if (vecRobotBodyPart != null){
            log.log("Access to details of robot, e.g. the distance to "+ bp +" of Agent_Dummy: "
                   + vecRobotBodyPart.getNorm() + ", and his team: "+ specificPlayer.getTeam() );
            break;
          }
      }
           
// line model
      log.log("Line models");
      if (!lines.isEmpty()){
        log.log(lines.getFirst().toString());
        vecLine = lines.getFirst().getStart();
        log.log("Access to details of a line, e.g. the distance to a start point: "
                +vecLine.getNorm());
      } else
        log.log("There is not any line in the field of view.");    
      
      
      
      act();
    }
  }

  /**
   * Update the world and robot hardware informations, that means process 
   * perceptor values provided by the server.
   */
  private void sense() {
    percIn.update();
    localView.update();
  }
  
  /**
   * Move the robot hardware, that means send effector commands to the server.
   * Here: Move the head to get different views.
   */ 
  private void act(){
    lookAround.look(); 
    effOut.sendAgentMessage();   
  }
}

