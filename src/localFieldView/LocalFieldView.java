package localFieldView;

import agentIO.PerceptorInput;
import agentIO.perceptors.LineVisionPerceptor;
import agentIO.perceptors.PlayerVisionPerceptor;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts;
import util.FieldConsts.FlagID;
import util.FieldConsts.GoalPostID;
import util.Logger;
import util.RobotConsts;
import util.RobotConsts.BodyPartName;

/**
 * This class represents a simple model of the field situation. 
 * 
 * Local coordinates of every possible field item (goal posts, flags, lines, 
 * the ball and players on the field) are provided here relative to the center
 * of the robots head at every server cycle. 
 * For further details about the field items and related constants (ids of
 * goal posts etc.) see:
 * - in RoboNewbie:
 *   util.FieldConsts.GoalPostID 
 *   util.FieldConsts.FlagID
 *   util.RobotConsts.BodyPartName
 * - in the SimSpark documentation:
 *   http://simspark.sourceforge.net/wiki/index.php/Soccer_Simulation#Field_Dimensions_and_Layout
 *   http://simspark.sourceforge.net/wiki/index.php/Perceptors#Vision_Perceptors
 * 
 * The item coordinates are gained from the vision perceptor values, but the
 * raw vision data may be rotated, if the robots head joints (NeckYaw and 
 * NeckPitch) have been moved. This class provides the data already corrected by
 * the position of the head, not the raw vision data. 
 * 
 * The field items are modeled in "...Model"-classes and they all inherit from 
 * the class DatedItemModel, so they have their individual data access methods 
 * and inherited general methods to check when the data has been gained or if an 
 * item is actually in the field of view of the vision perceptor. See class
 * DatedItemModel and for example BallModel for further details. 
 * 
 * Other classes can access the field items with these steps (usually during 
 * the think()-method of the agent class):
 * The getter methods of this class ( getAllPlayers(), getBall(), getGoals(),
 * getFlags(), getLines()) provide references to the field item models (in 
 * case of the ball) or to lists of the models (for all other items). The 
 * gained references point to the item models, which are instance variables of 
 * LocalFieldView, so for their usage that means:
 * 1. The referenced model objects and lists always contain actual data, the 
 * getters have to be called just once during the whole agent program. The 
 * referenced models are updated automatically, if LocalFieldView is integrated 
 * correctly in the agent class (see "Required context" below). 
 * 2. Use the method isNowInFOV(...) and getTimeStamp() of the referenced 
 * objects to check when the model has been updated the last time.
 * 3. The references should be used ONLY FOR READ ACCESS! If the referenced item
 * models or lists of models are changed external by another class than 
 * LocalFieldView, this leads to inconsistent data of the items. 
 * 
 * Required context in the agent class:
 * The agent class must have an object of class PerceptorInput, and it must be
 * used correctly as explained in the comment on the class.
 * In the sense()-method of the agent class: first call PerceptorInput.update()
 * and after that LocalFieldView.update() .
 * See example Agent_TestLocalFieldView for an example of correct integration
 * and usage. 
 * 
 * Precision of the provided coordinates:
 * As long, as the neck pitch joint is in 0° position, the coordinates provided
 * by LocalFieldView are correct. When the robot is facing down, positive values will 
 * stay positive and negative will also stay negative, but the absolute values
 * are not correct any more. The error gets bigger, with a bigger angle of the
 * neck pitch joint and when the sensed item is far away from the center of 
 * the field of view (especially in horizontal direction). 
 * For our purposes the values are sufficient. 
 *  
 * The coordinate system is right-handed, the x-axis points horizontally in front 
 * of the robot, y-axis to its left. 
 * In polar notation the azimuth (alpha) gets positive at the left side of the
 * x-axis (maximum 60° according to the field of view of the vision perceptor)
 * and negative on the right side (minimum -60°). The elevator (delta) gets 
 * positive above the x-axis (maximum 60°) and negative beneath the x-axis
 * (minimum -60°).
 * All field items have coordinates in instances of class Vector3D from the 
 * library Commons Math 2.2 API. Vector3D provides the data in Cartesian and
 * polar notation. To use the data please refer to the documentation of the 
 * library (also available through code completion).  
 * 
 */
public class LocalFieldView {
  
  private static double UP_TO_DATE_PERIOD = 0.05;
  
  private Logger log; 
  PerceptorInput percIn;
  String ownID, ownTeam;
  
  
  private LinkedList<LineModel> lines;
  
  private HashMap<GoalPostID, GoalPostModel> goals;
  private HashMap<FlagID, FlagModel> flags;
  
  private BallModel ball;
  
  /**
   * The players get a new internal key, the team name appended by the player 
   * number.
   */
  private HashMap<String, PlayerModel> allOtherPlayers;
  private LinkedList<PlayerModel> allOthPlayersList;

  
  
  /**
   * Constructor.
   * 
   * @param percIn Must be initialized, cannot be null.
   */
  public LocalFieldView(PerceptorInput percIn, Logger log, String ownTeam, String ownID) {
    this.percIn = percIn;
    this.log = log;
    this.ownTeam = ownTeam;
    this.ownID = ownID;
    
    lines = new LinkedList<>();
    
    goals = new HashMap<>();
    for (GoalPostID id: GoalPostID.values()){
      goals.put(id, new GoalPostModel(Vector3D.NaN, 0, id));
      goals.get(id).setInFOVnow(false);
    }
    
    flags = new HashMap<>();
    for (FlagID id: FlagID.values()){
      flags.put(id, new FlagModel(Vector3D.NaN, 0, id));
      flags.get(id).setInFOVnow(false);
    }
    
    ball = new BallModel(Vector3D.NaN, 0);
    
    allOtherPlayers = new HashMap<>();
    allOthPlayersList = new LinkedList<>();
    
  }
  
  /**
   * Updates the field item models according to the perceptor values.
   * 
   * See comment on this class for further details.
   * @see LocalFieldView
   */
  public void update(){
    
    double messageTimeStamp = percIn.getServerTime();
    double neckYaw = percIn.getJoint(RobotConsts.NeckYaw);
    double neckPitch = percIn.getJoint(RobotConsts.NeckPitch);
    
    LinkedList<LineVisionPerceptor> lpList = percIn.getLines();
    if(lpList != null){
      lines.clear();
      for(LineVisionPerceptor lp: lpList){
        Vector3D start = correctCoordsByHeadPos(neckYaw, neckPitch, lp.getStart());
        Vector3D end = correctCoordsByHeadPos(neckYaw, neckPitch, lp.getEnd());

        LineModel l = new LineModel(start, end, messageTimeStamp);
        lines.add(l);
      }
    }
    else if ((!lines.isEmpty()) 
            && (messageTimeStamp - lines.getFirst().getTimeStamp() > UP_TO_DATE_PERIOD))
      lines.clear();
    
    Vector3D vec;
    GoalPostModel g;
    for (FieldConsts.GoalPostID id: FieldConsts.GoalPostID.values()){
      vec = percIn.getGoalPost(id);
      g = goals.get(id);
      if (vec != null){
        vec = correctCoordsByHeadPos(neckYaw, neckPitch, vec);
        g.update(vec, messageTimeStamp);
      }
      else if (messageTimeStamp - g.getTimeStamp() > UP_TO_DATE_PERIOD)
        g.setInFOVnow(false);
    }
    
    Vector3D vec2; 
    FlagModel f;
    for (FlagID id: FlagID.values()){
      vec2 = percIn.getFlag(id);
      f = flags.get(id);
      if (vec2 != null){
        vec2 = correctCoordsByHeadPos(neckYaw, neckPitch, vec2);
        f.update(vec2, messageTimeStamp);
      }
      else if (messageTimeStamp - f.getTimeStamp() > UP_TO_DATE_PERIOD)
        f.setInFOVnow(false);
    }
    
    Vector3D vec3 = percIn.getBall();
    if (vec3 != null){
      vec3 = correctCoordsByHeadPos(neckYaw, neckPitch, vec3);
      ball.update(vec3, messageTimeStamp);
    }
    else if (messageTimeStamp - ball.getTimeStamp() > UP_TO_DATE_PERIOD)
      ball.setInFOVnow(false);
    
    LinkedList<PlayerVisionPerceptor> playerPercList = percIn.getPlayerPositions();
    if (playerPercList != null){
      for (PlayerVisionPerceptor pvp: playerPercList){
        String playerKey = pvp.getTeam() + pvp.getID();
        if (!playerKey.equals(ownTeam+ownID)){       
          HashMap<BodyPartName, Vector3D> newBPs = new HashMap<>();
          Vector3D vec4;
          for (BodyPartName id: BodyPartName.values()){
            vec4 = pvp.getBodyPart(id);
            if (vec4 != null){
              vec4 = correctCoordsByHeadPos(neckYaw, neckPitch, vec4);
              newBPs.put(id, vec4);
            } 
          }        
          PlayerModel pm = allOtherPlayers.get(playerKey);
          if (pm != null)
            pm.update(newBPs, messageTimeStamp);        
          else {
            pm = new PlayerModel(pvp.getTeam(), pvp.getID(), newBPs, messageTimeStamp);
            allOtherPlayers.put(playerKey, pm);
            allOthPlayersList.add(pm);
          }
        }
      }
    }
    for( PlayerModel pm: allOtherPlayers.values())
      if (messageTimeStamp - pm.getTimeStamp() > UP_TO_DATE_PERIOD) 
        pm.setInFOVnow(false);    
  }

  /**
   * The returned list holds information about all known players other then the
   * agents own one. 
   * 
   * The referenced values in this list are updated, so this method has to be 
   * called only once. 
   * 
   * If there has not been sensed any player by the vision perceptor yet, the
   * returned list is empty. New players are added as soon, as they appear in 
   * the field of view for the first time. The agent´s own player is not part 
   * of this list. 
   *  
   * @return List of all known players. 
   * @see LocalFieldView
   */
  public LinkedList<PlayerModel> getAllPlayers(){
    return allOthPlayersList;
  }
  
  /**
   * Returns informations about the ball. 
   * 
   * The model is initialized. If the ball has not been sensed by the 
   * vision perceptor yet, it has the timeStamp 0 (and of course it is marked
   * as not "in the field of view" of the vision perceptor).
   * 
   * The referenced value is updated, so this method has to be called only once. 
   * 
   * @return The ball informations. 
   * @see LocalFieldView
   */
  public BallModel getBall(){
    return ball;
  }
  
  /**
   * The returned list holds information about all goal posts. 
   * 
   * The models are initialized. If a goal post has not been sensed by the 
   * vision perceptor yet, it has the timeStamp 0 (and of course it is marked
   * as not "in the field of view" of the vision perceptor).
   * 
   * The referenced values in this list are updated, so this method has to be 
   * called only once. 
   * 
   * @return List of all goal posts. 
   * @see LocalFieldView
   */
  public HashMap<GoalPostID, GoalPostModel> getGoals() {
    return goals;
  }
  
  /**
   * The returned list holds information about all flags. 
   * 
   * The models are initialized. If a flag has not been sensed by the 
   * vision perceptor yet, it has the timeStamp 0 (and of course it is marked
   * as not "in the field of view" of the vision perceptor).
   * 
   * The referenced values in this list are updated, so this method has to be 
   * called only once. 
   * 
   * @return List of all flags . 
   * @see LocalFieldView
   */
  public HashMap<FlagID, FlagModel> getFlags() {
    return flags;
  }
  
  /**
   * The returned list holds all lines from the actual vision value. 
   * 
   * Since the lines haven´t got any identifiers, this list can hold different
   * lines at different times. 
   * 
   * The referenced object is updated, so this method has to be called only 
   * once. 
   * 
   * @return List of all currently sensed lines. 
   * @see LocalFieldView
   */
  public LinkedList<LineModel> getLines(){
    return lines;
  }
  
  
  /**
   * Returns the passed coordinates relative to the center of the agents head 
   * with the x-axis pointing horizontally in front of the robot. 
   * 
   * Corrects the passed coordinates by the passed data about the head position.
   * 
   * @param neckYaw Actual neck yaw angle.
   * @param neckPitch Actual neck pitch angle.
   * @param coords Coordinates to be corrected. 
   * @return Coordinates relative to the center of the agents head with the 
   * x-axis pointing horizontally in front of the robot.
   * 
   */
  private Vector3D correctCoordsByHeadPos(double neckYaw, double neckPitch, Vector3D coords) {
    return new Vector3D( coords.getNorm(),
                new Vector3D (coords.getAlpha() + neckYaw, coords.getDelta() + neckPitch ));
  }
}
