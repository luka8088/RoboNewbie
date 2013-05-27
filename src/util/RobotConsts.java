package util;

import java.util.HashMap;

/** 
 * This class represents server constants concerning the robot. <br>
 * 
 * Whereever in a RoboNewbie agent are needed constants for effector commands or
 * perceptor values, you can find them here. <br>
 * <br>
 * Especially, here is defined the order of hinge joints, like it is used 
 * globally in the framework, and should be kept by all agents. This is done by
 * setting the joint names as constants with an index. See definitions from 
 * NeckYaw to RightArmYaw. <br>
 * Also all agents should use the constant JointsCount, when the number of 
 * joints is needed.<br>
 * Here you can find a convenient method for a command line output of joint 
 * values, too. <br>
 * <br>
 * Please see the comments on constants and methods for all features of this 
 * class.
 * 
 */
public class RobotConsts {
  
   /** Number of hinge joints of the simulated Nao robot. <br>
     * Use this constant for iterations over all joints. 
     */
    public static final int JointsCount = 22; 
    
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int NeckYaw = 0;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int NeckPitch = 1;	
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftShoulderPitch = 2;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftShoulderYaw = 3;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftArmRoll = 4;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftArmYaw = 5;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftHipYawPitch = 6;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftHipRoll = 7;	
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftHipPitch = 8;	
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftKneePitch = 9;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftFootPitch = 10;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int LeftFootRoll = 11;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightHipYawPitch = 12;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightHipRoll = 13;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightHipPitch = 14;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightKneePitch = 15;	
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightFootPitch = 16;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */   
    public static final int RightFootRoll = 17;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightShoulderPitch = 18;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightShoulderYaw = 19;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightArmRoll = 20;
   /** Hinge joint name. <br> 
    * Use this names for all methods accessing single hinge joint effectors or 
    * perceptors. See also other public constants in util.RobotConsts .
    */
    public static final int RightArmYaw = 21;        
    
   /** Enumaration of body part names used by the vision perceptor. <br> 
    * Use this for all methods accessing informations about robots sensed by the
    * vision perceptor.
    * @see localFieldView.LocalFieldView#getAllPlayers()
    * @see localFieldView.PlayerModel
    * @see agentIO.PerceptorInput#getPlayerPositions()
    * @see agentIO.perceptors.PlayerVisionPerceptor
    */
    public static enum BodyPartName{ Head, RLowerArm, LLowerArm, RFoot, LFoot};
        
   /** Returns the corresponding name as value of type BodyPartName. <br> 
    * Use this method, if you have a body part in String representation and need
    * it as value of type BodyPartName. 
    * @param bodyPartString Body part of the robot in String representation.
    * @return If the parameter is a valid body part (also matching the case), 
    * the return is the corresponding body part of type BodyPartName, else null.
    * @see util.RobotConsts.BodyPartName
    */ 
    public static BodyPartName getBodyPartName(String bodyPartString){
      return idMapping.get(bodyPartString);
    }
    
   /** Returns the minimal angle a hinge joint can achieve. <br>
    * 
    * @param i Joint index, represented as a save constant hinge joint name, for 
    * example RobotConsts.NeckPitch . Use just the constants in RobotConsts, 
    * because, if this index is invalid, you get a memory access error.
    * @return Minimal angle the joint can achieve, as stated in the SimSpark 
    * Wiki.
    * @see util.RobotConsts#NeckPitch
    */ 
    public static double getAngleMin(int i){
      return jointAngleMins[i];
    }
    
   /** Returns the maximal angle a hinge joint can achieve. <br>
    * 
    * @param i Joint index, represented as a save constant hinge joint name, for 
    * example RobotConsts.NeckPitch . Use just the constants in RobotConsts, 
    * because, if this index is invalid, you get a memory access error.
    * @return Maximal angle the joint can achieve, as stated in the SimSpark 
    * Wiki.
    * @see util.RobotConsts#NeckPitch
    */ 
    public static double getAngleMax(int i){
      return jointAngleMaxs[i];
    }
    
   /** Returns the effector identifier used in SimSpark messages. <br>
    * 
    * This method should not be used by students/pupils, who lern robotics/AI
    * with RoboNewbie. It is necessary to implement the SimSpark TCP protocol, 
    * and has nothing to do with robotics/AI.  <br>
    * Students, please see util.RobotConsts.NeckPitch, if you need an "ID" of a
    * hinge joint.
    * 
    * @param i Joint index, represented as a save constant hinge joint name, for 
    * example RobotConsts.NeckPitch . Use just the constants in RobotConsts, 
    * because, if this index is invalid, you get a memory access error.
    * @return Effector identifier corresponding to the joint name, example:
    * parameter RobotConsts.NeckPitch results in the return "he2". 
    * @see util.RobotConsts#NeckPitch
    */
    public static String getEffectorID(int i){
        return effectorIDs[i];
    }
    
   /** Returns the perceptor index for parsing SimSpark messages. <br>
    * 
    * This method should not be used by students/pupils, who lern robotics/AI
    * with RoboNewbie. It is necessary to implement the SimSpark TCP protocol, 
    * and has nothing to do with robotics/AI.  <br>
    * Students, please see util.RobotConsts.NeckPitch, if you need an "ID" of a
    * hinge joint. 
    * 
    * @param s A perceptor identifier used in hinge joint perceptor messages, 
    * like "hj2".
    * @return Perceptor index as used everywhere in parsing methods of 
    * RoboNewbie corresponding to the passed identifier. 
    * Like 1 == RobotConsts.NeckPitch for the parameter "hj2" . 
    * @see util.RobotConsts#NeckPitch
    */
    public static int getPerceptorIndex(String s){
        for (int i = 0; i < JointsCount; i++ )
            if(perceptorIDs[i].equals(s))
                return i;
        return -1;
    }
    
  /**  Forms a string with joint angles or commands prepared for a console output. <br>
   *
   * Use this method, if you want to print a debug string for all joints, for
   * example to check all the current angles or current commands etc.
   * 
   * @param jointsData Array with one double value for each joint, no matter, 
   * what this value means (degrees or radians, speed or actual angle). If 
   * there are less then RobotConsts.JointsCount values in the passed array, a 
   * runtime error occurs. If there are more values, they are ignored from the 
   * index JointsCount to the end of the array. The order of joints in the array 
   * must be the same as defined by the joint constants in class RobotConsts 
   * (RobotConst.NeckYaw, ...). The parameter is not checked at all.
   * @return A formatted string with the joint names and corresponding values.
   * @see util.RobotConsts#NeckYaw
   * @see util.RobotConsts#JointsCount
   */
    public static String getAllJointsString(double[] jointsData){
      StringBuilder builder = new StringBuilder();                                     
      String spaceAfterNumber = "      ";
      String spaceAtBeginning = "                  ";
      
      String[] formattedDataOfJoints = new String[JointsCount];
      
      for (int i = 0; i < JointsCount; i++){
        formattedDataOfJoints[i] = String.format("%8.2f%s", jointsData[i], spaceAfterNumber);
      }
      
      builder.append(jointNames[NeckYaw]).append("   ");
      builder.append(formattedDataOfJoints[NeckYaw]);
      builder.append(jointNames[LeftShoulderPitch]).append(" ");
      builder.append(formattedDataOfJoints[LeftShoulderPitch]);
      builder.append(jointNames[RightShoulderPitch]).append(" ");
      builder.append(formattedDataOfJoints[RightShoulderPitch]).append('\n');
      
      builder.append(jointNames[NeckPitch]).append(" ");
      builder.append(formattedDataOfJoints[NeckPitch]);
      builder.append(jointNames[LeftShoulderYaw]).append("   ");
      builder.append(formattedDataOfJoints[LeftShoulderYaw]);
      builder.append(jointNames[RightShoulderYaw]).append("   ");
      builder.append(formattedDataOfJoints[RightShoulderYaw]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftArmRoll]).append("       ");
      builder.append(formattedDataOfJoints[LeftArmRoll]);
      builder.append(jointNames[RightArmRoll]).append("       ");
      builder.append(formattedDataOfJoints[RightArmRoll]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftArmYaw]).append("        ");
      builder.append(formattedDataOfJoints[LeftArmYaw]);
      builder.append(jointNames[RightArmYaw]).append("        ");
      builder.append(formattedDataOfJoints[RightArmYaw]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftHipYawPitch]).append("   ");
      builder.append(formattedDataOfJoints[LeftHipYawPitch]);
      builder.append(jointNames[RightHipYawPitch]).append("   ");
      builder.append(formattedDataOfJoints[RightHipYawPitch]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftHipRoll]).append("       ");
      builder.append(formattedDataOfJoints[LeftHipRoll]);
      builder.append(jointNames[RightHipRoll]).append("       ");
      builder.append(formattedDataOfJoints[RightHipRoll]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftHipPitch]).append("      ");
      builder.append(formattedDataOfJoints[LeftHipPitch]);
      builder.append(jointNames[RightHipPitch]).append("      ");
      builder.append(formattedDataOfJoints[RightHipPitch]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftKneePitch]).append("     ");
      builder.append(formattedDataOfJoints[LeftKneePitch]);
      builder.append(jointNames[RightKneePitch]).append("     ");
      builder.append(formattedDataOfJoints[RightKneePitch]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftFootPitch]).append("     ");
      builder.append(formattedDataOfJoints[LeftFootPitch]);
      builder.append(jointNames[RightFootPitch]).append("     ");
      builder.append(formattedDataOfJoints[RightFootPitch]).append('\n');
      
      builder.append(spaceAtBeginning).append(spaceAfterNumber);
      builder.append(jointNames[LeftFootRoll]).append("      ");
      builder.append(formattedDataOfJoints[LeftFootRoll]);
      builder.append(jointNames[RightFootRoll]).append("      ");
      builder.append(formattedDataOfJoints[RightFootRoll]);
      
      return builder.toString();
    }
     
    
    private static final String[] jointNames = new String[]{
                              "NeckYaw",
                              "NeckPitch",
                              "LeftShoulderPitch",
                              "LeftShoulderYaw",
                              "LeftArmRoll",
                              "LeftArmYaw",
                              "LeftHipYawPitch",
                              "LeftHipRoll",	
                              "LeftHipPitch",
                              "LeftKneePitch",
                              "LeftFootPitch",
                              "LeftFootRoll",
                              "RightHipYawPitch",
                              "RightHipRoll",
                              "RightHipPitch",
                              "RightKneePitch",
                              "RightFootPitch",
                              "RightFootRoll",
                              "RightShoulderPitch",
                              "RightShoulderYaw",
                              "RightArmRoll",
                              "RightArmYaw"};
  
    private static final String[] perceptorIDs = new String[]{
                                                "hj1",  
                                                "hj2",  
                                                "laj1", 
                                                "laj2", 
                                                "laj3", 
                                                "laj4", 
                                                "llj1", 
                                                "llj2", 
                                                "llj3", 
                                                "llj4", 
                                                "llj5", 
                                                "llj6", 
                                                "rlj1", 
                                                "rlj2", 
                                                "rlj3", 
                                                "rlj4", 
                                                "rlj5", 
                                                "rlj6", 
                                                "raj1", 
                                                "raj2", 
                                                "raj3", 
                                                "raj4"};     
    
    private static final String[] effectorIDs = new String[]{
                                                "he1",
                                                "he2",
                                                "lae1",
                                                "lae2",
                                                "lae3",
                                                "lae4",
                                                "lle1",
                                                "lle2",
                                                "lle3",
                                                "lle4",
                                                "lle5",
                                                "lle6",
                                                "rle1",
                                                "rle2",
                                                "rle3",
                                                "rle4",
                                                "rle5",
                                                "rle6",
                                                "rae1",
                                                "rae2",
                                                "rae3",
                                                "rae4"};
    
    // Min and Max values are not validated yet. 
    private static final double[] jointAngleMaxs = new double[]{
                                                      120f, 
                                                      45f,  
                                                      120f, 
                                                      95f,   
                                                      120f, 
                                                      1f,   
                                                      1f,   
                                                      45f,  
                                                      100f,
                                                      1f,
                                                      75f,
                                                      25f,
                                                      1f,
                                                      25f,
                                                      100f,
                                                      1f,
                                                      75f,
                                                      45f,
                                                      120f,
                                                      1f,
                                                      120f,
                                                      90f};
    
    private static final double[] jointAngleMins = new double[]{
                                                      -120f,
                                                      -45f,
                                                      -120f,
                                                      -1f,
                                                      -120f,
                                                      -90f,
                                                      -90f,
                                                      -25f,
                                                      -25f,
                                                      -130f,
                                                      -45f,
                                                      -45f,
                                                      -90f,
                                                      -45f,
                                                      -25f,
                                                      -130f,
                                                      -45f,
                                                      -25f,
                                                      -120f,
                                                      -95f,
                                                      -120f,
                                                      -1f   };
    
    private static HashMap<String, BodyPartName> idMapping = new HashMap<String, BodyPartName>(){
      {
        put("head",      BodyPartName.Head);
        put("rlowerarm", BodyPartName.RLowerArm);
        put("llowerarm", BodyPartName.LLowerArm);
        put("rfoot",     BodyPartName.RFoot);
        put("lfoot",     BodyPartName.LFoot);
      }
    };
    
}
