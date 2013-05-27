package util;

import java.util.HashMap;

/**
 * This class represents server constants concerning field data. <br>
 *
 * These constants are needed, when the vision perceptor is used for sensing the
 * positions of flags and goal posts relative to the robot.
 */
public class FieldConsts {

  /**
   * Enumeration of all goal post ids.
   */
  public static enum GoalPostID {
    G1L, G1R, G2L, G2R
  }

  /**
   * Enumeration of all flag ids.
   */
  public static enum FlagID {
    F1L, F1R, F2L, F2R
  }

  /**
   * Returns the corresponding ID as value of type GoalPostID. <br> Use this
   * method, if you have a goal post in String representation and need it as
   * value of type GoalPostID.
   *
   * @param s Goal post in String representation.
   * @return If the parameter is a valid goal post (also matching the case), the
   * return is the corresponding goal post of type BodyPartName, else null.
   * @see util.FieldConsts.GoalPostID
   */
  public static GoalPostID getGoalPostID(String s) {
    return goalPostIDs.get(s);
  }

  /**
   * Returns the corresponding ID as value of type FlagID. <br> 
   * Use this method, if you have a flag in String representation and need it as
   * value of type FlagID.
   * @param s Flag in String representation.
   * @return If the parameter is a valid flag (also matching the case), the
   * return is the corresponding flag of type BodyPartName, else null.
   * @see util.FieldConsts.FlagID
   */
  public static FlagID getFlagID(String s) {
    return flagNames.get(s);
  }
  
  
  private static HashMap<String, GoalPostID> goalPostIDs = new HashMap<String, GoalPostID>() {
    {
      put("G1L", GoalPostID.G1L);
      put("G1R", GoalPostID.G1R);
      put("G2L", GoalPostID.G2L);
      put("G2R", GoalPostID.G2R);
    }
  };
  
  private static HashMap<String, FlagID> flagNames = new HashMap<String, FlagID>() {
    {
      put("F1L", FlagID.F1L);
      put("F1R", FlagID.F1R);
      put("F2L", FlagID.F2L);
      put("F2R", FlagID.F2R);
    }
  };
}
