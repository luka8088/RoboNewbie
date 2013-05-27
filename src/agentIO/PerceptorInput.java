package agentIO;

import agentIO.perceptorInputUtil.PerceptorConversionException;
import agentIO.perceptorInputUtil.SymbolNode;
import agentIO.perceptorInputUtil.SymbolTreeParser;
import agentIO.perceptors.*;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.FieldConsts;
import util.GameStateConsts;
import util.RobotConsts;

/**
 * Read perceptor values out of server messages. 
 * 
 * This class receives perceptor values from the simulation server. 
 * 
 * The get...-methods (like getJoint(...), getBall()) return the current 
 * perceptor values (see there for details and see also "Required context in the
 * agent class" below).
 * This class provides just immutable objects as return values. This ensures,
 * that other classes can not falsify the input from the server. 
 * The getters for the vision perceptor values (getBall, getGoalPost, getFlag, getPlayerPositions, getLines) should not be used directly, use class localFieldView.LocalFieldView for that.
 * 
 * Required context in the agent class: 
 * PerceptorInput has to be updated in every server cycle and it is essential
 * for keeping the agent program synchronized with the server. So in the 
 * sense()-method of the agent class the PerceptorInput-method update() must be 
 * the first command.
 * See Agent_BasicStructure, Agent_SimpleWalkToBall and Agent_SimpleSoccer for 
 * examples of correct integration in Agent_-classes. 
 * 
 * The code of this class is partially copied from magmaOffenburg. \n
 * (download source of the magmaOffenburg-project: \n
 * http://robocup.fh-offenburg.de/html/downloads.htm, (14.1.2012) \n
 * Used for this class: src\magma\agent\perception\impl\ServerMessageParser.java).
 */

public class PerceptorInput {

  private ServerCommunication com;
  private String message;
  
  private double serverTime;
  private GameStatePerceptor gameState;
  
  private double[] hingeJoints = new double[RobotConsts.JointsCount];
  private Vector3D gyro;
  private Vector3D acc;
  private ForceResistancePerceptor frLeft, frRight;
  
  private Vector3D ball;
  private LinkedList<LineVisionPerceptor> lines;
  private LinkedList<PlayerVisionPerceptor> players;
  private HashMap<FieldConsts.GoalPostID, Vector3D> goals;
  private HashMap<FieldConsts.FlagID, Vector3D> flags;
  
  private LinkedList<HearPerceptor> hears;
  
  /**
   * Constructor.
   * 
   * @param connectedServer Cares for the connection to the server, so this
   * parameter has to be already initialized before the constructor is called.
   * 
   */
  public PerceptorInput(ServerCommunication sc) {
    com = sc;
    gyro = null;
    acc = null;
    ball = null;
    lines = null;
    frLeft = null;
    frRight = null;
    players = null;
    goals = null;
    flags = null;
  }
  
  /** 
   * Returns the raw coordinates of a goal post relative to the vision perceptor 
   * ("camera").
   * 
   * This value is not accessible in every server cycle, see for details:
   * http://simspark.sourceforge.net/wiki/index.php/Perceptors#Vision_Perceptors
   * 
   * @param id ID of one of the goal posts, mapping for the IDs is given at:
   * http://simspark.sourceforge.net/wiki/index.php/Soccer_Simulation#Field_Dimensions_and_Layout
   * 
   * @return Coordinates in meters and radians, or null if the value is not
   * accessible.
   */
  public Vector3D getGoalPost(FieldConsts.GoalPostID id){
      return goals.get(id);
  }
  
  /** 
   * Returns the raw coordinates of a flag relative to the vision perceptor 
   * ("camera").
   * 
   * This value is not accessible in every server cycle, see for details:
   * http://simspark.sourceforge.net/wiki/index.php/Perceptors#Vision_Perceptors
   * 
   * @param id ID of one of the flags, mapping for the IDs is given at:
   * http://simspark.sourceforge.net/wiki/index.php/Soccer_Simulation#Field_Dimensions_and_Layout
   * 
   * @return Coordinates in meters and radians, or null if the value is not
   * accessible.
   */
  public Vector3D getFlag(FieldConsts.FlagID id){
      return flags.get(id);
  }
  
  /**
   * Returns the value of the force resistance perceptor in the left foot of the
   * robot.
   * 
   * If the left force resistance perceptor detected a collision during this
   * server cycle, this method returns its data. 
   * If the sensor did not detect any collision, the return is null.
   * For more Information see class ForceResistancePerceptor. 
   *
   * @return Force resistance data or null.
   * 
   */
  public ForceResistancePerceptor getFrLeft() {
    return frLeft;
  }

  /** 
   * Returns the value of the force resistance perceptor in the right foot of the
   * robot.
   * 
   * If the right force resistance perceptor detected a collision during this
   * server cycle, this method returns its data. 
   * If the sensor did not detect any collision, the return is null.
   * For more Information see class ForceResistancePerceptor. 
   *
   * @return Force resistance data or null.
   */
  public ForceResistancePerceptor getFrRight(){
    return frRight;
  }
  
  /** 
   * Returns a list of hear perceptor, or null.
   * 
   * If other robots have sent messages, this 
   * method returns the messages. 
   * The list contains just messages sent by other robots then the agents own,
   * so there can be at most two elements in this list:
   * - one sent by any robot of the agents own team, but not by itself
   * - one sent by any robot of the other team.
   * 
   * If there has not been sent any message, the return is null.
   * 
   * @return A list of hear perceptor, or null.
   */
  public LinkedList<HearPerceptor> getHears() {
    if (hears.isEmpty())
      return null;
    else 
      return (LinkedList<HearPerceptor>)hears.clone();
  }
  
  /**
   * Returns the game state value.
   * 
   * @return The current game state.
   */
  public GameStatePerceptor getGameState(){
    return gameState;
  }
  
  /** 
   * Returns the raw coordinates of the ball relative to the vision perceptor 
   * ("camera").
   * 
   * This value is not accessible in every server cycle, see for details:
   * http://simspark.sourceforge.net/wiki/index.php/Perceptors#Vision_Perceptor
   * 
   * @return Coordinates in meters and radians, or null if the value is not
   * accessible.
   */  
  public Vector3D getBall() {
    return ball;
  }
  
  /**
   * Returns the raw coordinates of all detected players relative to the vision 
   * perceptor ("camera").
   * 
   * This value is not accessible in every server cycle, see for details:
   * http://simspark.sourceforge.net/wiki/index.php/Perceptors#Vision_Perceptors
   * 
   * NOTE: The robot may also "see" its own body, especially its arms in the 
   * initial position (when its beamed on the field and has not moved yet).
   * 
   * @return A list of player positions (coordinates in meters and radians) or 
   * null.
   */
  public LinkedList<PlayerVisionPerceptor> getPlayerPositions(){
    if (players.isEmpty())
      return null;
    else 
      return (LinkedList<PlayerVisionPerceptor>)players.clone();
  }
  
  /** 
   * Returns the raw coordinates of all detected lines relative to the vision 
   * perceptor ("camera").
   * 
   * This value is not accessible in every server cycle, see for details:
   * http://simspark.sourceforge.net/wiki/index.php/Perceptors#Vision_Perceptors
   * 
   * @return A list of line positions (coordinates in meters and radians) or 
   * null.
   */
  public LinkedList<LineVisionPerceptor> getLines() {
    if (lines.isEmpty())
      return null;
    else 
      return (LinkedList<LineVisionPerceptor>)lines.clone();
  }
  
  /**
   * Returns the value of the gyrometer perceptor.
   * 
   * The simulated gyrometer is located in the torso of the robot and the values
   * are given in a right handed coordinate system: x-axis points to the robots
   * right, y in front of the robot and z points up. 
   *
   * @return The value of the gyrometer perceptor.
   */
  public Vector3D getGyro() {
    return gyro;
  }

  /**
   * Returns the value of the accelerometer perceptor.
   *
   * The simulated accelerometer is located in the torso of the robot and the values
   * are given in a right handed coordinate system: x-axis points to the robots
   * right, y in front of the robot and z points up. 
   * 
   * @return The value of the accelerometer perceptor.
   */
  public Vector3D getAcc() {
    return acc;
  }

  /**
   * Returns the actual angle of a hinge joint of the robot.
   * 
   * @param i Index of the joint as defined in class RobotConsts in package util
   * (like RobotConsts.RightFootPitch, RobotConsts.LeftArmRoll, ...) . 
   * @return The angle in radians. 
   */ 
  public double getJoint(int i) {
    return hingeJoints[i];
  }

  /**
   * Returns at which server time the actually read server message was sent.
   * 
   * @return Time elapsed since the start of the server until the actually read 
   * server message.
   */
  public double getServerTime() {
    return serverTime;
  }

  /**
   * Returns the raw server message for debugging.
   * 
   * @return The current read server message.
   */
  public String getServerMessage() {
    return message;
  }
  
  /**
   * Returns a debug string with the values of all joint perceptors (in degrees).
   * 
   * @return String with the values of all joint perceptors in degrees.
   */
  public String getJointsDebugString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Percepted joints: \n");
    builder.append(RobotConsts.getAllJointsString(hingeJoints));
    return builder.toString();
  }

  /**
   * Receives the server message and parses it to provide the perceptor values.
   * 
   * This method interacts almost directly (through a method of class 
   * ServerCommunication) with the simulation server. 
   * It waits, until the next server cycle begins, receives the new 
   * incoming server message and initiates the parsing of the message. 
   * After this method is called, all getter methods of this class provide 
   * the current values during the actual server cycle. update() 
   * guarantees actual values, only if it is executed in each cycle.
   * 
   * About parsing internals: 
   * The server message is here transformed from a character sequence into a 
   * symbol tree. Other specialized internal methods read the informations from 
   * the tree leaves into the variables of PerceptorInput.
   */
  public void update() {
    message = com.getServerMessage();
    
    frLeft = null;
    frRight = null;
    
    ball = null;
    lines = new LinkedList<>();
    players = new LinkedList<>();
    goals = new HashMap<>();
    flags = new HashMap<>();
    
    hears = new LinkedList<>();
    
    if (message == null) {
      // nothing to do, might happen at disconnection
      return;
    }

    SymbolTreeParser treeParser = new SymbolTreeParser();
    SymbolNode messageRoot = treeParser.parse(message);

    for (int i = 0; i < messageRoot.children.length; i++) {
      Object node = messageRoot.children[i];

      try {
        parseNode((SymbolNode) node);
      } catch (PerceptorConversionException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Internal method for parsing the server message. 
   * 
   * Perceptor values have a different structure for every type of 
   * information, e.g. the hinge joints have just one angle value but the ball
   * needs a 3D-vector. So the symbol tree, that contains the actual server
   * message has differently structured leaves.
   * This method decides, which specialized parsing method will deal with the 
   * passed information type. 
   * 
   * @param node The root of a part of a server message. 
   * @throws PerceptorConversionException 
   */
  private void parseNode(SymbolNode node)
          throws PerceptorConversionException {
    /*
     * Check message type
     */
    Object child = node.children[0];

    if (child == null || !(child instanceof String)) {
      throw new PerceptorConversionException("Malformed node: "
              + node.toString());
    }

    String type = (String) child;

    	/* Hinge joint perceptor*/
		if (type.equals("HJ"))
			parseHingeJoint(node);

		/* Time perceptor */
		if (type.equals("time"))
            parseTime(node);

        /* Force resistance perceptor */
		if (type.equals("FRP"))
			parseForceResistance(node);

		/* Gyro perceptor*/
		if (type.equals("GYR"))
			parseGyro(node);

		/* Accelerometer perceptor*/
		if (type.equals("ACC"))
			parseAcc(node);

		/* Vision perceptor */
		if (type.equals("See"))
			parseVision(node);

		/* Hear perceptor */
		if (type.equals("hear"))
			parseHear(node);
        
        /* GameState perceptor */
		if (type.equals("GS"))
			parseGameState(node);

  }

  /**
   * Internal method for parsing the server message. 
   * 
   * @param node The root of the part of a server message, that contains data
   * from the vision perceptor. 
   * @throws PerceptorConversionException 
   */
  private void parseVision(SymbolNode node)
          throws PerceptorConversionException {

    // Sanity checks
    if (node.children.length == 0) {
      throw new PerceptorConversionException("Malformed Node: "
              + node.toString());
    }

    try {
      // Parse visible objects
      for (int i = 1; i < node.children.length; i++) {
        Object subitem = node.children[i];

        if (subitem instanceof SymbolNode) {
          SymbolNode subnode = (SymbolNode) subitem;

          // Player object?
          if (!(subnode.children[0] instanceof String)) {
            throw new PerceptorConversionException(
                    "Malformed Node, empty name: " + node.toString());
          } else if (subnode.children[0].equals("P"))
            players.add(parsePlayer((SymbolNode) subnode));
          else if (subnode.children[0].equals("L"))
            lines.add( parseLine((SymbolNode) subnode));
          else if (subnode.children[0].equals("B")) {
            ball = parsePolar((SymbolNode) subnode.children[1]);
          }
          else if (subnode.children[0].toString().startsWith("G")){
            FieldConsts.GoalPostID id = FieldConsts.getGoalPostID(subnode.children[0].toString());
            goals.put(id, parsePolar((SymbolNode) subnode.children[1]));
          }
          else if (subnode.children[0].toString().startsWith("F")){
            FieldConsts.FlagID id = FieldConsts.getFlagID(subnode.children[0].toString());
            flags.put(id, parsePolar((SymbolNode) subnode.children[1]));
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      throw new PerceptorConversionException("Malformed node: "
              + node.toString());
    }
  }
  
  /**
   * Internal method for parsing the server message. 
   * 
   * @param node
   * @return Internal used value.
   * @throws PerceptorConversionException 
   */
  private PlayerVisionPerceptor parsePlayer(SymbolNode node)
			throws PerceptorConversionException
	{
		try {
			/* Parse content */
            String teamName = null;
            String id = null;
			HashMap<String, Vector3D> bodyPartMap = new HashMap<>();

			for (int i = 1; i < node.children.length; i++) {
				if (!(node.children[i] instanceof SymbolNode)) {
					throw new PerceptorConversionException("Malformed node: "
							+ node.toString());
				}

				SymbolNode param = (SymbolNode) node.children[i];

				if (param.children[0].equals("team")) {
					teamName = (String) param.children[1];
				} else if (param.children[0].equals("id")) {
					id = (String) param.children[1];
				} else {
                    String partName = null;
                    Vector3D coords;
					// in case of seeing parts of opponent, we have to look into them
					if (!param.children[0].equals("pol")) {
						partName = (String) param.children[0];
						param = (SymbolNode) param.children[1];
					}

					if (param.children[0].equals("pol")) {
						coords = parsePolar(param);
						bodyPartMap.put(partName, coords);
					}
				}
			}

			return new PlayerVisionPerceptor(teamName, id, bodyPartMap);
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: "
					+ node.toString());
		}
  }
  
  /**
   * Internal method for parsing the server message. 
   * 
   * @param node
   * @return Internal used value.
   * @throws PerceptorConversionException 
   */
  private LineVisionPerceptor parseLine(SymbolNode node)
			throws PerceptorConversionException
	{
		try {
			assert node.children.length == 3 : "Malformed node";
			assert node.children[1] instanceof SymbolNode : "Malformed node: "
					+ node.toString();
			assert node.children[2] instanceof SymbolNode : "Malformed node: "
					+ node.toString();

			Vector3D pol1 = parsePolar((SymbolNode) node.children[1]);
			Vector3D pol2 = parsePolar((SymbolNode) node.children[2]);

			return new LineVisionPerceptor(pol1, pol2);
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: "
					+ node.toString());
		}
	}
  
  /**
   * Internal method for parsing the server message. 
   * 
   * @param node
   * @return Internal used value.
   * @throws PerceptorConversionException 
   */
  private Vector3D parsePolar(SymbolNode node)
          throws PerceptorConversionException
  {
    double distance, azimuth, elevation;
    try {
            if (node.children.length != 4)
                    throw new PerceptorConversionException("Malformed node: "
                                    + node.toString());
            if (!node.children[0].equals("pol"))
                    throw new PerceptorConversionException("Expecting a pol object: "
                                    + node.toString());

            distance    = Double.parseDouble((String) node.children[1]);
            azimuth     = Double.parseDouble((String) node.children[2]);
            elevation   = Double.parseDouble((String) node.children[3]);
            
            return new Vector3D(distance, 
                    new Vector3D(Math.toRadians(azimuth), Math.toRadians(elevation)));
            
    } catch (IndexOutOfBoundsException e) {
            throw new PerceptorConversionException("Malformed node: "
                            + node.toString());

    } catch (NumberFormatException e) {
            // seems that the server sometimes sends NAN, in which case we ignore
            // the reading for position
            e.printStackTrace();
    }
    return null;
  }
  
  /**   
   * Internal method for parsing the server message. 
   * 
   * @param node
   * @throws PerceptorConversionException 
   */
  private void parseHingeJoint(SymbolNode node)
          throws PerceptorConversionException {
    try {
      // if (!(node.children[1] instanceof SymbolNode)
      // || !(node.children[2] instanceof SymbolNode))
      // throw new PerceptorConversionException("Malformed Message: "
      // + node.toString());

      /*
       * Check content
       */
      SymbolNode idNode = (SymbolNode) node.children[1];
      SymbolNode rotationNode = (SymbolNode) node.children[2];

      // if (!((String) nameNode.children[0]).equals("n"))
      // throw new PerceptorConversionException("Malformed Message: "
      // + node.toString() + ": name expected");
      //
      // if (!((String) rotationNode.children[0]).equals("ax"))
      // throw new PerceptorConversionException("Malformed Message: "
      // + node.toString() + ": axis expected");

      hingeJoints[RobotConsts.getPerceptorIndex((String) idNode.children[1])] = 
              Math.toRadians(Double.valueOf((String) rotationNode.children[1]));
    } catch (IndexOutOfBoundsException e) {
      throw new PerceptorConversionException("Malformed node: " + node.toString());
    }
  }

  /**
   * Internal method for parsing the server message. 
   *
   * @param node Symbol tree node
   * @throws PerceptorConversionException If the input string contains illegal
   * data which cannot be converted
   */
  private void parseTime(SymbolNode node)
          throws PerceptorConversionException {
    // Sanity check
    if (node.children.length != 2
            || !(node.children[1] instanceof SymbolNode)) {
      throw new PerceptorConversionException("Malformed time node: "
              + node.toString());
    }

    // Sub-node sanity check
    SymbolNode timeNode = (SymbolNode) node.children[1];
    if (timeNode.children.length != 2 || timeNode.children[1].equals("now")) {
      throw new PerceptorConversionException("Malformed time sub-node: "
              + timeNode.toString());
    }

    try {
      // Sanity checks
      serverTime = Double.parseDouble((String) timeNode.children[1]);

    } catch (Exception e) {
      throw new PerceptorConversionException(
              "Malformed time node, conversion error: " + node.toString());
    }
  }

  /**
   * Internal method for parsing the server message. 
   *
   * Parse a symbol tree node into a gyro perceptor value.
   *
   * @param node Symbol tree node
   * @throws PerceptorConversionException If the input string contains illegal
   * data which cannot be converted
   */
  private void parseGyro(SymbolNode node)
          throws PerceptorConversionException {
    if (!(node.children[1] instanceof SymbolNode)
            || !(node.children[2] instanceof SymbolNode)) {
      throw new PerceptorConversionException("Malformed Message: "
              + node.toString());
    }

    try {
      /*
       * Check content
       */
      SymbolNode rotationNode = (SymbolNode) node.children[2];

      if (!rotationNode.children[0].equals("rt")) {
        throw new PerceptorConversionException("rotation expected: "
                + node.toString());
      }
      gyro = new Vector3D(Double.valueOf((String) rotationNode.children[1]),
              Double.valueOf((String) rotationNode.children[2]),
              Double.valueOf((String) rotationNode.children[3]));
    } catch (IndexOutOfBoundsException e) {
      throw new PerceptorConversionException("Malformed node: "
              + node.toString());
    }
  }

  /**
   * Internal method for parsing the server message. 
   *
   * Parse a symbol tree node into a accelerometer perceptor value.
   *
   * @param node Symbol tree node
   * @throws PerceptorConversionException If the input string contains illegal
   * data which cannot be converted
   */
  private void parseAcc(SymbolNode node)
          throws PerceptorConversionException {
    if (!(node.children[1] instanceof SymbolNode)
            || !(node.children[2] instanceof SymbolNode)) {
      throw new PerceptorConversionException("Malformed Message: "
              + node.toString());
    }

    try {
      /*
       * Check content
       */
      SymbolNode accelerationNode = (SymbolNode) node.children[2];

      if (!accelerationNode.children[0].equals("a")) {
        throw new PerceptorConversionException("rotation expected: "
                + node.toString());
      }

      acc = new Vector3D(Double.valueOf((String) accelerationNode.children[1]),
              Double.valueOf((String) accelerationNode.children[2]),
              Double.valueOf((String) accelerationNode.children[3]));

    } catch (NumberFormatException e) {
      // seems that the server sometimes sends NAN, in which case we ignore
      // the reading for accelerometer
      e.printStackTrace();

    } catch (IndexOutOfBoundsException e) {
      throw new PerceptorConversionException("Malformed node: "
              + node.toString());
    }
  }
  
  /**
   * Internal method for parsing the server message. 
   *
   * Parse a symbol tree node into a force resistance perceptor value.
   *
   * @param node Symbol tree node
   * @throws PerceptorConversionException If the input string contains illegal
   * data which cannot be converted
   */
  private void parseForceResistance(SymbolNode node)
          throws PerceptorConversionException {
    try {
      // Sanity checks
      if (!(node.children[1] instanceof SymbolNode)
              || !(node.children[2] instanceof SymbolNode)
              | !(node.children[3] instanceof SymbolNode)) {
        throw new PerceptorConversionException("Malformed Message: "
                + node.toString());
      }
      /*
       * Check content
       */
      SymbolNode nameNode = (SymbolNode) node.children[1];
      SymbolNode originNode = (SymbolNode) node.children[2];
      SymbolNode forceNode = (SymbolNode) node.children[3];

      if (!nameNode.children[0].equals("n")) {
        throw new PerceptorConversionException("name expected: "
                + node.toString());
      }
      if (!originNode.children[0].equals("c")) {
        throw new PerceptorConversionException("origin expected: "
                + node.toString());
      }
      if (!forceNode.children[0].equals("f")) {
        throw new PerceptorConversionException("force expected: "
                + node.toString());
      }


      // read FRP-values, getrennt nach links und rechts

      Vector3D origin = new Vector3D( Double.valueOf((String) originNode.children[1]),
              Double.valueOf((String) originNode.children[2]),
              Double.valueOf((String) originNode.children[3]));
      Vector3D force = new Vector3D(Double.valueOf((String) forceNode.children[1]),
              Double.valueOf((String) forceNode.children[2]),
              Double.valueOf((String) forceNode.children[3]));
        
      if (nameNode.children[1].equals("lf"))
        frLeft = new ForceResistancePerceptor(origin, force);
      else if (nameNode.children[1].equals("rf"))
        frRight = new ForceResistancePerceptor(origin, force);
      else 
        throw new PerceptorConversionException("malformed message, lf or rf expected: "
                + node.toString());
    } catch (IndexOutOfBoundsException e) {
      throw new PerceptorConversionException("Malformed node: "
              + node.toString());
    }
  }
  
  /**
   * Internal method for parsing the server message. 
   *
   * Parse a symbol tree node into a hear perceptor value.
   * "self"-messages are omitted!
   *
   * @param node Symbol tree node
   * @throws PerceptorConversionException If the input string contains illegal
   * data which cannot be converted
   * 
   */
	private void parseHear(SymbolNode node)
			throws PerceptorConversionException
	{
		double time;
        double direction;
		String heardMessage = "";

		// Sanity checks
		  if (node.children.length < 4) {
      throw new PerceptorConversionException("Malformed hear node: "
              + node.toString());
    }

    try {
      if (!((String) node.children[2]).equals("self")) {
        time = Double.parseDouble((String) node.children[1]);
        direction = Double.parseDouble((String) node.children[2]);

        // Concatenate following nodes
        for (int i = 3; i < node.children.length; i++) {
          if (i > 3) {
            heardMessage += " " + node.children[i];
          } else {
            heardMessage += node.children[i];
          }
        }
        hears.add(new HearPerceptor(time, direction, heardMessage));
      }
    } catch (Exception e) {
      throw new PerceptorConversionException(
              "Malformed hear node, conversion error: " + node.toString());
    }
  }

  /**
   * Internal method for parsing the server message. 
   *
   * Parse a symbol tree node into a hear perceptor value.
   *
   * @param node Symbol tree node
   * @throws PerceptorConversionException If the input string contains illegal
   * data which cannot be converted
   */
	private void parseGameState(SymbolNode node)
			throws PerceptorConversionException
	{
		double time = 0;
        GameStateConsts.PlayMode playMode = null;

		SymbolNode child = null;

		// Evaluate content
		for (int i = 1; i < node.children.length; i++) {
			try {
				child = (SymbolNode) node.children[i];

				String type = (String) child.children[0];
        // Check sub-node type
        switch (type) {
          case "t":
            time = Float.parseFloat((String) child.children[1]);
            break;
          case "pm":
            String pm = ( String) child.children[1];
            playMode = GameStateConsts.getPlayMode(pm);
            break;
          default:
            throw new PerceptorConversionException(
                    "Malformed GameState node, unknown sub-node: "
                            + child.toString());
        }
			} catch (NumberFormatException | PerceptorConversionException e) {
				if (child != null)
					throw new PerceptorConversionException(
							"Malformed GameState node, conversion error: "
									+ child.toString());

				throw new PerceptorConversionException(
						"Malformed GameState node: child was null!");
			}
		}
		gameState = new GameStatePerceptor(time, playMode);
	}
}
