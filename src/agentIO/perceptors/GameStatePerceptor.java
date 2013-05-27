
package agentIO.perceptors;

import static util.GameStateConsts.PlayMode;

/**
 * This class represents the data received by the game state perceptor. 
 * 
 * As all Perceptor-classes this class is just for reading the perceptor values
 * provided by the server.
 * Instances of this class are immutable.
 * The game state perceptor informs about the play time and the play mode.
 * 
 * There are also other informations passed by the game state perceptor, 
 * calls "unum" and "team", but their meanings are not documented in the
 * SimSpark wiki, and so they are omitted in the implementation of this class. 
 */
public class GameStatePerceptor {
  
  double playTime;
  PlayMode playMode;

  /**
   * Constructor.
   * 
   * @param playTime The time elapsed since the kick off (since the server has been
   * set to the playmode "KickOff_Left" for the first time) until the game state
   * message has been received. This time value stops during the halftime break. 
   * @param playMode The actual play mode of the simulation server. 
   */
  public GameStatePerceptor(double playTime, PlayMode playMode) {
    this.playTime = playTime;
    this.playMode = playMode;
  }

  /**
   * Returns the play mode. 
   * 
   * @return The actual play mode. 
   */
  public PlayMode getPlayMode() {
    return playMode;
  }

  /**
   * Returns the play time in seconds.
   * 
   * @return The time elapsed since the kick off (since the server has been set 
   * to the playmode "KickOff_Left" for the first time). This time value stops
   * during the halftime break. 
   */
  public double getPlayTime() {
    return playTime;
  }

  /**
   * Returns a textual representation of all information stored in class 
   * variables. 
   * 
   * @return Textual representation of this object.
   */
  @Override
  public String toString() {
    return "game state: play time " + playTime + "s, playmode " + playMode;
  }  
}
