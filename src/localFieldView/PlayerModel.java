package localFieldView;

import java.util.HashMap;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.Logger;
import util.RobotConsts.BodyPartName;

/**
 * This class represents any player other than the agent´s own.  <br> 
 * 
 * The class holds information about the coordinates of a player relative to
 * the agent´s own one (precisely to the center of its head). 
 * It should be updated by class LocalFieldView.  <br> 
 * For the coordinate system see LocalFieldView. For constants see 
 * RobotConsts.BodyPartName.
 * 
 * @see LocalFieldView
 * @see util.RobotConsts.BodyPartName
 */
public class PlayerModel extends DatedItemModel{
  private String team;
  private String ID;
  private HashMap<BodyPartName, Vector3D> bodyParts;
  
  /**
   * Constructor. 
   * 
   * Assumes, that the model is created at the first cycle, when the object is
   * sensed by the vision perceptor. 
   * 
   * @param team Team name of the player.
   * @param id ID of the player.
   * @param timeNow Actual server time.
   * @param bodyParts Map of local coordinates relative to the center of the own
   * robots head. 
   */
  public PlayerModel(String team, String ID, HashMap<BodyPartName, Vector3D> bodyParts, double timeNow){
    super(timeNow);
    this.team = team;
    this.ID = ID;
    this.bodyParts = bodyParts;
  }
 
  /**
   * Set the player data.
   * 
   * @param timeNow Actual server time.
   * @param bodyParts Map of local coordinates relative to the center of the own
   * robots head. 
   */
  public void update(HashMap<BodyPartName, Vector3D> bodyParts, double timeNow){
    super.setTimeStamp(timeNow);
    this.bodyParts = bodyParts;
  }
  
  /**
   * Returns the id of the player.
   * 
   * @return The id of the player.
   */
  public String getID(){
    return ID;
  }
  
  /**
   * Returns the team of the player.
   * 
   * @return The team of the player.
   */
  public String getTeam(){
    return team;
  }
  
  /**
   * Returns the coordinates of the specified body part. 
   * @return Local coordinates (see class LocalFieldView).
   */
  public Vector3D getBodyPart( BodyPartName b){
    return bodyParts.get(b);
  }
  
  /**
   * Returns a string representation of all player data. 
   * @return String representation of the player. 
   */
  @Override
  public String toString(){
    StringBuilder retStr = new StringBuilder();
    retStr.append("Player model - ").append(super.toString()).append(", team: ")
            .append(team).append(", player ID: ").append(ID);
    for (BodyPartName b : BodyPartName.values())
      if (bodyParts.get(b) != null)
        retStr.append('\n').append(b).append(' ').append(Logger.polarStr(bodyParts.get(b)));
    return retStr.toString();
  }
}
