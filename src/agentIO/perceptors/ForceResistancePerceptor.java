package agentIO.perceptors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.Logger;

/**
 * This class represents the force resistance perceptors in the robots feet. 
 * 
 * As all Perceptor-classes this class is just for reading the perceptor values
 * provided by the server.
 * Instances of this class are immutable.
 */
public class ForceResistancePerceptor {
  
  private final Vector3D pointOfOrigin;
  private final Vector3D force;

  /** Constructor.
   * @param orig Origin of the force, that is the coordinates, where on the foot
   *             the force is applied. The coordinate system for this value is
   *             not described. It is just sure, that the root point is the
   *             center of the sole of foot and the z-axis points up.
   * @param forc Force, that is the direction of the applied force and the force
   *             value as the length of the direction vector.
   */
  public ForceResistancePerceptor(Vector3D orig, Vector3D forc){
    pointOfOrigin = orig;
    force = forc;
  }
  
  /**
   * Returns, where on the sole of foot the force is applied.
   * 
   * The coordinate system for this value is not described. It is just sure, that
   * the root point is the center of the sole of foot and the z-axis points up. 
   * 
   * @return Coordinates, where on the foot the force is applied. 
   */
  public Vector3D getOrigin(){
    return pointOfOrigin;
  }
  
  /**
   * Returns the force applied to the foot.
   * 
   * The coordinate system for this value is not described. It is just sure, that
   * the root point is the center of the sole of foot and the z-axis points up. 
   * 
   * @return Direction of the applied force and the value as the length of the 
   * direction vector. 
   */
  public Vector3D getForce(){
    return force;
  }
  
  /**
   * Returns the value of the force perceptor in a string. 
   * 
   * @return The value of the force perceptor in a string. 
   */
  @Override
  public String toString(){
    return String.format("FR origin: " + Logger.cartesianStr(pointOfOrigin) +
            " force: " + Logger.cartesianStr(force));
  }
}
