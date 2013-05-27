package agentIO;

import java.util.HashMap;
import util.RobotConsts;

/** 
 * Send effector commands for the joints and the say effector in agent messages 
 * to the simulation server.
 * 
 * This class should realize the SimSpark syntax of the joint and say effector
 * commands, so programmers could concentrate on the content of the commands. 
 * 
 * Usage of this class and required context in the agent class: 
 * 1) Call the set...-methods to define the hinge joint and say effector 
 * commands that should be sent to the server. These methods normally will be 
 * used by all classes, which realize the robot´s movements. Is a setter called
 * more than one time for a specific effector, then the last set value 
 * overrides the former ones. 
 * 2) Use the method sendAgentMessage() to send the commands defined with the
 * setters. This method should be executed once in each server cycle and
 * called inside the act()-method of the agent class. 
 * Only the commands that where set like in 1) are sent by the method 
 * sendAgentMessage(). If there has not been defined any command for an 
 * effector, then EffectorOutput also does not send any. 
 * 
 * See classes Agent_BasicStructure, Agent_SimpleWalkToBall and 
 * Agent_SimpleSoccer for usage examples. 
 * 
 * Neccessary synchronization, when the server runs in Agent Sync Mode:
 * Method sendAgentMessage() MUST be called exactly once in every server cycle, 
 * because it sends the "sync" command. 
 * 
 * It is possible to set the commands and send them more than one time during a 
 * server cycle, but the server will execute just one command for each effector.
 * Therefore the synchronization with the server (during the act()-method of the 
 * agent class) as decribed above is strongly recommended. 
 * 
 * Remember that a hinge joint executes a sent command until it gets a new one. 
 *  
 */
public class EffectorOutput {

  ServerCommunication sc;

  HashMap<Integer, Double> jointCommands;
  String sayMessage;
  
  /**
   * Constructor.
   * 
   * @param connectedServer Cares for the connection to the server, so this
   * parameter has to be already initialized before the constructor is called.
   * 
   */
  public EffectorOutput(ServerCommunication connectedServer) {
    sc = connectedServer;
    jointCommands = new HashMap<>();
    sayMessage = null;           
  }

  /**
   * This method sends the set effector commands (for the hinge joints and the 
   * say effector) to the server. 
   * 
   * Here all previously set effector commands are sent. 
   * The commands must have been set before using the methods 
   * setAllJointCommands(), setJointCommand(...) or setSayMessage(...) .
   * Care for synchronization with the simulation server like written above in
   * the comment on class EffectorOutput.
   * 
   * sendAgentMessage() works when the server runs in the normal mode and when it
   * runs in agent sync mode. The message "(syn)" is always appended to the 
   * agent message, so the sent message content is "[passed agent message](syn)". 
   * When the server runs in the normal mode, the syn command is ignored, when 
   * it runs an gents sync mode, this command signalizes to the server, that the 
   * agent is ready for the next server cycle.
   * 
   * @see Comment on this class.  
   */
  public void sendAgentMessage() {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < RobotConsts.JointsCount; i++) {
      if (jointCommands.containsKey(i))
        builder.append('(').append(RobotConsts.getEffectorID(i)).append(' ')
                .append(jointCommands.get(i)).append(')');
    }
    if (sayMessage != null)
      builder.append("(say "+ sayMessage +")");
            
    builder.append("(syn)");
    
    sc.sendAgentMessage(builder.toString());
    jointCommands.clear();
    sayMessage = null;
  }

  /**
   * Sets a joint command to be sent to the server. 
   * 
   * This method just sets the command value, and does not send it to the 
   * simulation server. Before the jointCommands have been sent, the command can 
   * be overwritten by calling this method again or setAllJointCommands(...). 
   * 
   * To send the jointCommands, call method sendAgentMessage().
   * 
   * @param jointNo Index number of the hinge joint, as defined by the constants
   * in class RobotConsts (like RobotConsts.RightFootPitch, 
   * RobotConsts.LeftArmRoll, ...) .
   * @param command Velocity for the joint "motor", given in radians per second.
   */
  public void setJointCommand(int jointNo, double command){
    jointCommands.put(jointNo, command);
  }
  
  /**
   * Sets the say effector message to be sent to the server. 
   * 
   * This method just sets the command value, and does not send it to the 
   * simulation server. To send it call method sendAgentMessage().
   * If setSayMessage(...) is called again before the message is sent, the 
   * second call overwrites the message. 
   * 
   * @param message The message, which should be broadcasted with the say 
   * effector (max. 20 characters).
   */
  public void setSayMessage(String message){
    sayMessage = message;
  }
 
  /**
   * Sets joint commands for all joint effectors to be sent to the server.
   * 
   * This method just sets the command values, and does not send them to the 
   * simulation server. Before the jointCommands have been sent, they can be 
   * overwritten by calling this method again or setJointCommand(...). 
   * 
   * To send the jointCommands, call method sendAgentMessage().
   * 
   * @param jointCommands Holds 22 "motor" velocities, one for every joint, in 
   * the order specified by class RobotConsts in package util. The unit of a 
   * command is radians per second. If there are less then 22 values in the 
   * passed array, a runtime error occurs. If there are more values, they are 
   * ignored from the index 22 to the end of the array. The order of joints in 
   * the array must be the same as defined by the joint constants in class 
   * RobotConsts (RobotConst.NeckYaw, ...). The parameter is not checked at all. 
   * 
   * @see setJointCommand(...), sendAgentMessage(), comment on this class.
   * @see util.RobotConsts#NeckYaw
   * @see util.RobotConsts#JointsCount
   */
  public void setAllJointCommands(double[] commands) {
    for (int i = 0; i < RobotConsts.JointsCount; i++)
      jointCommands.put(i, commands[i]);
  }
  
}
