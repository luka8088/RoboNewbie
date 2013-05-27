package examples.agentKeyframeDeveloper;

import agentIO.PerceptorInput;
import keyframeMotion.KeyframeMotion;

/**
 * Thinking class for Agent_KeyframeDeveloper. 
 * Realizes the following behaviour:
 * Sets the keyframe motion to be tested (it has to be saves as 
 * "[RoboNewbie project folder]/keyframes/test.txt" ) and executes it in an
 * infinite loop. If the robot falls down, because the motion is not stable yet,
 * the thinking sets a motion to stand up again. 
 * 
 * Usage by the agent class:
 * Call method decide() in each server cycle. 
 * Before the call the PerceptionInput object has to be updated and after the call
 * the KeyframeMotion object must execute the choosen movement - this has to be
 * managed by the Agent_KeyframeDeveloper. 
 */
public class KeyDevThinking {
  
  private KeyframeMotion motion;
  private PerceptorInput percIn;
  
  int wait_time = 100;
  int state = 0;
  // 0 = perform motion according to keyframe file test.txt
  // 1 = wait for wait_time cycles: is pose stable?
  // 2 = stand up from back
  // 3 = stand up from front

  /**
   * Constructor. 
   *
   * @param km Has to be initialized already.
   * @param percIn Has to be initalized already. 
   */
  public KeyDevThinking(KeyframeMotion km, PerceptorInput percIn){
    this.motion = km;
    this.percIn = percIn;
  }
  
  /**
   * Decides, whether the robot can continue the test motion or has to stand up
   * first. 
   * 
   * After completing the motion, the agent waits for wait_time cycles in order
   * to test if the reached pose is stable. Then the motion is executed once
   * again. If the agent has fallen down, a stand-up is performed. 
   *
   */  
  public void decide(){     
    
    if (motion.ready()) {
      if (percIn.getAcc().getZ() < 7) {
        if (percIn.getAcc().getY() > 0) {
          state = 2;    // robot lies on the back
        } else {
          state = 3;                               // robot lies on the front
        }
      }
      switch (state) {
        case 0:
          motion.setTest();
          state = 1;
          break;
        case 1:
          if (wait_time == 0) {
            state = 0;
            wait_time = 100;
          } else {
            wait_time--;
          }
          break;
        case 2:
          motion.setStandUpFromBack();
          state = 0;
          break;
        case 3:
          motion.setRollOverToBack();
          state = 0;
          break;
      }
    }
  }
    
}
