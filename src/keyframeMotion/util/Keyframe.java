package keyframeMotion.util;

import util.RobotConsts;


public class Keyframe {
  
  private final int transitionTime;
  private final double[] angles;
  
  public Keyframe(int tTime, double[] angleArray){
    transitionTime = tTime;
    angles = angleArray;
  }
  
  public int getTransitionTime(){
    return transitionTime;
  }
  
  public double getAngle(int i){
    return angles[i];
  }
  
  public String getDebugString(){
    StringBuilder builder = new StringBuilder();
    
    builder.append("Keyframe:\ntransition time ").append(transitionTime).append("\n");
    builder.append(RobotConsts.getAllJointsString(angles));

    return builder.toString();
  }
  
}
