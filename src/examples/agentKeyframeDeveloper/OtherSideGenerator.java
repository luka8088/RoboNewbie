package examples.agentKeyframeDeveloper;

import keyframeMotion.util.Keyframe;
import keyframeMotion.util.KeyframeFileHandler;
import keyframeMotion.util.KeyframeSequence;
import static util.RobotConsts.*;



/**
 * This little program takes a move modeled as a keyframe sequence and
 * generates the same move reflected from one side of the robot to the other, 
 * like generating a step with the right foot from a step with the left foot. 
 * 
 * Having a move made e.g. with the MotionEditor, the same move for the other
 * side. This program takes a keyframe file like "side-step-left.txt" and 
 * generates for the other side move a file like "side-step-left2.txt" . 
 * 
 * The OtherSideGenerator should be started in the root directory of the 
 * RoboNewbie project. The keyframe sequence, which will be reflected, must be
 * in the directory "[RoboNewbie root]/keyframes", the output file will also be
 * there. The filename has to be specified in the class variable 
 * OtherSideGenerator.sequenceForConversion, or passed as the first argument 
 * when starting the program from the command prompt.
 */
public class OtherSideGenerator {
  
  static String sequenceForConversion = "side-step-left-nika.txt";
  
  /**
   * Starts the conversion of a keyframe sequence from one side to the other, 
   * see class description for details. 
   * 
   * @param args Keyframe sequence located in directory 
   * "[RoboNewbie root]/keyframes".
   */
  public static void main(String[] args){
    if (args.length != 0)
      sequenceForConversion = args[0];
      
    KeyframeSequence ks = KeyframeFileHandler.getSequenceFromFile(sequenceForConversion);
    
    ks = generateOtherSide(ks);
    
    sequenceForConversion = sequenceForConversion.replace(".txt", "");
    KeyframeFileHandler.writeSequenceToFile(ks, sequenceForConversion + "2" + ".txt");
  }
  
  /**
   * Generates a new sequence, where the originally left side angles are changed
   * to the right side and vice versa. 
   * 
   * @param ks Keyframe sequence to convert. 
   * @return Keyframe sequence with the move switched to the other side of the 
   * agent. 
   */
  private static KeyframeSequence generateOtherSide(KeyframeSequence ks){
    KeyframeSequence ksOtherSide = new KeyframeSequence();
    
    for (Keyframe kf = ks.getNextFrame(); kf != null; kf = ks.getNextFrame()) {
      double[] newAngles = new double[JointsCount];
      
      newAngles[NeckYaw]            = -kf.getAngle(NeckYaw); 
      newAngles[NeckPitch]          = kf.getAngle(NeckPitch);	
      newAngles[LeftShoulderPitch]  = kf.getAngle(LeftShoulderPitch);
      newAngles[LeftShoulderYaw]    = -kf.getAngle(RightShoulderYaw);
      newAngles[LeftArmRoll ]       = -kf.getAngle(RightArmRoll);
      newAngles[LeftArmYaw ]        = -kf.getAngle(RightArmYaw);
      newAngles[LeftHipYawPitch ]   = kf.getAngle(RightHipYawPitch);
      newAngles[LeftHipRoll]        = -kf.getAngle(RightHipRoll);
      newAngles[LeftHipPitch]       = kf.getAngle(RightHipPitch);
      newAngles[LeftKneePitch]      = kf.getAngle(RightKneePitch);
      newAngles[LeftFootPitch]      = kf.getAngle(RightFootPitch);
      newAngles[LeftFootRoll]       = -kf.getAngle(RightFootRoll);
      newAngles[RightHipYawPitch]   = kf.getAngle(LeftHipYawPitch);
      newAngles[RightHipRoll]       = -kf.getAngle(LeftHipRoll);
      newAngles[RightHipPitch]      = kf.getAngle(LeftHipPitch);
      newAngles[RightKneePitch]     = kf.getAngle(LeftKneePitch);
      newAngles[RightFootPitch]     = kf.getAngle(LeftFootPitch);
      newAngles[RightFootRoll]      = -kf.getAngle(LeftFootRoll);
      newAngles[RightShoulderPitch] = kf.getAngle(LeftShoulderPitch);
      newAngles[RightShoulderYaw]   = -kf.getAngle(LeftShoulderYaw);
      newAngles[RightArmRoll]       = -kf.getAngle(LeftArmRoll);
      newAngles[RightArmYaw ]       = -kf.getAngle(LeftArmYaw);
      
      ksOtherSide.addFrame(new Keyframe(kf.getTransitionTime(), newAngles));
    }
    
    return ksOtherSide;
  }
}
