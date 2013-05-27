package util;

import java.util.HashMap;

/**
 * This class encapsulates the play mode names of the simulation server. 
 */
public class GameStateConsts {
  /**
   * Play modes have the same names as described in the diploma thesis and 
   * in the documentation wiki (date May 2012).
   */
  public static enum PlayMode {
    BeforeKickOff,
    KickOff_Left,
    KickOff_Right,
    PlayOn,
    KickIn_Left,
    KickIn_Right,
    CORNER_KICK_LEFT,
    CORNER_KICK_RIGHT,
    GOAL_KICK_LEFT,
    GOAL_KICK_RIGHT,
    OFFSIDE_LEFT,
    OFFSIDE_RIGHT,
    GameOver,
    Goal_Left,
    Goal_Right,
    FREE_KICK_LEFT,
    FREE_KICK_RIGHT,
    NONE }

  private static HashMap<String, PlayMode> modeMapping = new HashMap<String, PlayMode>();

  static {
    modeMapping.put("beforekickoff",    PlayMode.BeforeKickOff);
    modeMapping.put("kickoff_left",     PlayMode.KickOff_Left);
    modeMapping.put("kickoff_right",    PlayMode.KickOff_Right);
    modeMapping.put("playon",           PlayMode.PlayOn);
    modeMapping.put("kickin_left",      PlayMode.KickIn_Left);
    modeMapping.put("kickin_right",     PlayMode.KickIn_Right);
    modeMapping.put("corner_kick_left", PlayMode.CORNER_KICK_LEFT);
    modeMapping.put("corner_kick_right",PlayMode.CORNER_KICK_RIGHT);
    modeMapping.put("goal_kick_left",   PlayMode.GOAL_KICK_LEFT);
    modeMapping.put("goal_kick_right",  PlayMode.GOAL_KICK_RIGHT);
    modeMapping.put("offside_left",     PlayMode.OFFSIDE_LEFT);
    modeMapping.put("offside_right",    PlayMode.OFFSIDE_RIGHT);
    modeMapping.put("gameover",         PlayMode.GameOver);
    modeMapping.put("goal_left",        PlayMode.Goal_Left);
    modeMapping.put("goal_right",       PlayMode.Goal_Right);
    modeMapping.put("free_kick_left",   PlayMode.FREE_KICK_LEFT);
    modeMapping.put("free_kick_right",  PlayMode.FREE_KICK_RIGHT);
    modeMapping.put("none",             PlayMode.NONE );      
  }
  
  /**
   * Returns the play mode in the enum item type, which is used by classes
   * like GameStatePerceptor.
   * 
   * @param modeAsString The mode as a string. 
   * @return The same playmode as an enum item. 
   */
  public static PlayMode getPlayMode(String modeAsString){
    return modeMapping.get(modeAsString.toLowerCase());
  }
  
}
