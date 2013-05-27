package keyframeMotion.util;

import util.RobotConsts;

/** Erzeugt verschiedene Testsequencen, siehe Methodennamen.
 *
 */
public class TestSequencesCreator {

  public static KeyframeSequence createGeneralRangeTest() {

    int transTime1 = 1000;
    int transTime2 = 1000;

    double[] angles1 = new double[RobotConsts.JointsCount];
    double[] angles2 = new double[RobotConsts.JointsCount];

    for (int i = 0; i < RobotConsts.JointsCount; i++) {
      angles1[i] = RobotConsts.getAngleMin(i);
      angles2[i] = RobotConsts.getAngleMax(i);
    }

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(transTime1, angles1));
    ks.addFrame(new Keyframe(transTime2, angles2));

    return ks;
  }

  public static KeyframeSequence createCrouchAndHeadTurnSequence() {

    KeyframeSequence ks = new KeyframeSequence();

    //Hocke als Keyframe:
    double[] crouch = new double[RobotConsts.JointsCount];
    for (int i = 0; i < RobotConsts.JointsCount; i++) {
      crouch[i] = 0;
    }
    crouch[RobotConsts.LeftHipPitch] = 50f;
    crouch[RobotConsts.RightHipPitch] = 50f;
    crouch[RobotConsts.LeftKneePitch] = -100f;
    crouch[RobotConsts.RightKneePitch] = -100f;
    crouch[RobotConsts.LeftFootPitch] = 50f;
    crouch[RobotConsts.RightFootPitch] = 50f;

    ks.addFrame(new Keyframe(700, crouch));

    //Kopf drehen als Keyframe:
    double[] turnHead = new double[RobotConsts.JointsCount];
    for (int i = 0; i < RobotConsts.JointsCount; i++) {
      turnHead[i] = 0;
    }
    turnHead[RobotConsts.NeckYaw] = 30f;

    ks.addFrame(new Keyframe(100, turnHead));

    return ks;
  }

  public static KeyframeSequence createNeckYawMinMax() {
    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.NeckYaw] = RobotConsts.getAngleMin(RobotConsts.NeckYaw);
    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.NeckYaw] = RobotConsts.getAngleMax(RobotConsts.NeckYaw);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }

  public static KeyframeSequence createNeckPitchMinMax() {
    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.NeckPitch] = RobotConsts.getAngleMin(RobotConsts.NeckPitch);
    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.NeckPitch] = RobotConsts.getAngleMax(RobotConsts.NeckPitch);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }

  public static KeyframeSequence createShoulderPitchMinMax() {
    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftShoulderPitch] = RobotConsts.getAngleMin(RobotConsts.LeftShoulderPitch);
    f1[RobotConsts.RightShoulderPitch] = RobotConsts.getAngleMin(RobotConsts.RightShoulderPitch);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftShoulderPitch] = RobotConsts.getAngleMax(RobotConsts.LeftShoulderPitch);
    f2[RobotConsts.RightShoulderPitch] = RobotConsts.getAngleMax(RobotConsts.RightShoulderPitch);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }

  public static KeyframeSequence createShoulderYawMinMax() {
    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftShoulderYaw] = RobotConsts.getAngleMin(RobotConsts.LeftShoulderYaw);
    f1[RobotConsts.RightShoulderYaw] = RobotConsts.getAngleMin(RobotConsts.RightShoulderYaw);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftShoulderYaw] = RobotConsts.getAngleMax(RobotConsts.LeftShoulderYaw);
    f2[RobotConsts.RightShoulderYaw] = RobotConsts.getAngleMax(RobotConsts.RightShoulderYaw);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }

  public static KeyframeSequence createArmRollMinMax() {
    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftArmRoll] = RobotConsts.getAngleMin(RobotConsts.LeftArmRoll);
    f1[RobotConsts.RightArmRoll] = RobotConsts.getAngleMin(RobotConsts.RightArmRoll);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftArmRoll] = RobotConsts.getAngleMax(RobotConsts.LeftArmRoll);
    f2[RobotConsts.RightArmRoll] = RobotConsts.getAngleMax(RobotConsts.RightArmRoll);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }

  public static KeyframeSequence createArmYawMinMax() {
    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftArmYaw] = RobotConsts.getAngleMin(RobotConsts.LeftArmYaw);
    f1[RobotConsts.RightArmYaw] = RobotConsts.getAngleMin(RobotConsts.RightArmYaw);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftArmYaw] = RobotConsts.getAngleMax(RobotConsts.LeftArmYaw);
    f2[RobotConsts.RightArmYaw] = RobotConsts.getAngleMax(RobotConsts.RightArmYaw);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }

  public static KeyframeSequence createHipYawPitchMinMax() {

    double[] f0 = new double[RobotConsts.JointsCount];
    f0[RobotConsts.LeftFootPitch] = -30f;
    f0[RobotConsts.RightFootPitch] = -30f;

    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftHipYawPitch] = RobotConsts.getAngleMin(RobotConsts.LeftHipYawPitch);
    f1[RobotConsts.RightHipYawPitch] = RobotConsts.getAngleMin(RobotConsts.RightHipYawPitch);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftHipYawPitch] = RobotConsts.getAngleMax(RobotConsts.LeftHipYawPitch);
    f2[RobotConsts.RightHipYawPitch] = RobotConsts.getAngleMax(RobotConsts.RightHipYawPitch);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f0));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }
  
  public static KeyframeSequence createHipRollMinMax() {

    double[] f0 = new double[RobotConsts.JointsCount];
    f0[RobotConsts.LeftFootPitch] = -30f;
    f0[RobotConsts.RightFootPitch] = -30f;

    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftHipRoll] = RobotConsts.getAngleMin(RobotConsts.LeftHipRoll);
    f1[RobotConsts.RightHipRoll] = RobotConsts.getAngleMin(RobotConsts.RightHipRoll);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftHipRoll] = RobotConsts.getAngleMax(RobotConsts.LeftHipRoll);
    f2[RobotConsts.RightHipRoll] = RobotConsts.getAngleMax(RobotConsts.RightHipRoll);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f0));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }
  
  public static KeyframeSequence createHipPitchMinMax() {

    double[] f0 = new double[RobotConsts.JointsCount];
    f0[RobotConsts.LeftFootPitch] = -30f;
    f0[RobotConsts.RightFootPitch] = -30f;

    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftHipPitch] = RobotConsts.getAngleMin(RobotConsts.LeftHipPitch);
    f1[RobotConsts.RightHipPitch] = RobotConsts.getAngleMin(RobotConsts.RightHipPitch);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftHipPitch] = RobotConsts.getAngleMax(RobotConsts.LeftHipPitch);
    f2[RobotConsts.RightHipPitch] = RobotConsts.getAngleMax(RobotConsts.RightHipPitch);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f0));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }
  
  public static KeyframeSequence createKneePitchMinMax() {

    double[] f0 = new double[RobotConsts.JointsCount];
    f0[RobotConsts.LeftFootPitch] = -30f;
    f0[RobotConsts.RightFootPitch] = -30f;

    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftKneePitch] = RobotConsts.getAngleMin(RobotConsts.LeftKneePitch);
    f1[RobotConsts.RightKneePitch] = RobotConsts.getAngleMin(RobotConsts.RightKneePitch);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftKneePitch] = RobotConsts.getAngleMax(RobotConsts.LeftKneePitch);
    f2[RobotConsts.RightKneePitch] = RobotConsts.getAngleMax(RobotConsts.RightKneePitch);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f0));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }
  
  public static KeyframeSequence createFootPitchMinMax() {

    double[] f0 = new double[RobotConsts.JointsCount];
    f0[RobotConsts.LeftFootPitch] = -30f;
    f0[RobotConsts.RightFootPitch] = -30f;

    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftFootPitch] = RobotConsts.getAngleMin(RobotConsts.LeftFootPitch);
    f1[RobotConsts.RightFootPitch] = RobotConsts.getAngleMin(RobotConsts.RightFootPitch);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftFootPitch] = RobotConsts.getAngleMax(RobotConsts.LeftFootPitch);
    f2[RobotConsts.RightFootPitch] = RobotConsts.getAngleMax(RobotConsts.RightFootPitch);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f0));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }
  
    public static KeyframeSequence createFootRollMinMax() {

    double[] f0 = new double[RobotConsts.JointsCount];
    f0[RobotConsts.LeftFootPitch] = -30f;
    f0[RobotConsts.RightFootPitch] = -30f;

    double[] f1 = new double[RobotConsts.JointsCount];
    f1[RobotConsts.LeftFootRoll] = RobotConsts.getAngleMin(RobotConsts.LeftFootRoll);
    f1[RobotConsts.RightFootRoll] = RobotConsts.getAngleMin(RobotConsts.RightFootRoll);

    double[] f2 = new double[RobotConsts.JointsCount];
    f2[RobotConsts.LeftFootRoll] = RobotConsts.getAngleMax(RobotConsts.LeftFootRoll);
    f2[RobotConsts.RightFootRoll] = RobotConsts.getAngleMax(RobotConsts.RightFootRoll);

    KeyframeSequence ks = new KeyframeSequence();
    ks.addFrame(new Keyframe(1000, f0));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f1));
    ks.addFrame(new Keyframe(1000, f2));
    ks.addFrame(new Keyframe(1000, f2));
    return ks;
  }
}
