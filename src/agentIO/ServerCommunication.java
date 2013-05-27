package agentIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Connection to the SimSpark server, receiving and sending of messages via
 * TCP.
 * <p>
 * This class encapulates the network protocol of SimSpark. For users of
 * RoboNewbie it is not necessary, to know anything about that protocol.
 * <p>
 * Just use this class as follows: <br>
 * An agent has always exactly one instance of this class. <br>
 * 1) The constructor establishes a connection to the server at the address
 * 127.0.0.1 (localhost). (If the agent should connect to a server with another
 * IP, change the variable "host" of this class.) <br>
 * 2) Pass the instance of this class to the constructors of PercepTorInput and
 * EffectorOutput. <br>
 * 3) Init the robot with method initRobot(...). <br>
 * 4) Avoid using methods getServerMessage() and sendAgentMessage(...), if you
 * don´t know exactly, what you are doing. Use classes PerceptorInput and
 * EffectorOutput!
 * <p>
 * The typical usage of this class together with classes PerceptorInput and
 * EffectorOutput is shown in Agent_BasicStructure in package examples.
 * <p>
 * <p/>
 * Information for developers of RoboNewbie (students actually learning robotics
 * dont need that):
 * <p/>
 * This class realises the sending and receiving of SimSpark messages.
 * <p/>
 * During every cycle of the main agent loop the actual server message has to be
 * read by calling the method getServerMessage(). This synchronizes the agent
 * with the server, and should be done by an object of class PerceptorInput. So
 * the call stack would be:<br>
 * Agent_-class.sense() <br>
 * -> PerceptorInput.update() <br>
 * -> ServerConnection.getServerMessage()
 * <p/>
 * With sendMessage() an agent message can be sent. Also once during the main
 * agent loop, this should be done by class EffectorOutput. Call stack: <br>
 * Agent_-class.act() <br>
 * -> EffectorOutput.sendAgentMessage(); <br>
 * -> ServerConnection.sendAgentMessage();
 * <p/>
 * The source code is partly copied from the RoboCup-Team magmaOffenburg. <br>
 * http://robocup.fh-offenburg.de/html/downloads.htm , downloaded at 14.1.2012. <br>
 * Path in the source directory: <br>
 * src\magma\agent\connection\impl\ServerConnection.java
 * <p/>
 */
public class ServerCommunication{

  private String host = "127.0.0.1";
  private int port = 3100;
  private DataInputStream in;
  private DataOutputStream out;
  private Socket socket;

  /**
   * Constructor, establishes the TCP-connection to the server.
   */
  public ServerCommunication() {

    //////////////////////  Verbindung zum Server aufbauen
    //
    try {
      socket = new Socket(host, port);
      socket.setTcpNoDelay(true);

      in = new DataInputStream(socket.getInputStream()); //new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());

      System.out.println("Connection to: " + host + ":" + port);

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (ConnectException e) {
      System.out.println(host
              + ":"
              + port
              + " refused the connection. Is rcssserver3d running? Are you using an IPv6-enabled"
              + " system and the host name translates to an IPv6 address?");
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
    
  /**
   * This method initializes the robot on the soccer field.
   * <p/>
   * The robot is initialized as a player of a certain team with a
   * certain player number, and it is beamed at its initial position on the
   * field. <br>
   * After that this method receives some first messages from the server,
   * because the gyrometer and the accelerometer need the first server cycles
   * to even out.
   * <p/>
   * Caution: When the server runs in agent sync mode, this mathod cannot be
   * debugged line by line. It has to be executed as a whole, and after that
   * every breakpoit is possible.
   * @param robotID       The ID the robot should have, this is a string
   *                      containing just a number from 1 to 11, like: "3".
   * @param team          The custom team name of the robot. All agents in one
   *                      team (playing to the same side of the field) must have
   *                      exactly the same team. The server allows agents of two
   *                      teams on the field. If an agent tries to initialize its
   *                      robot with a third team name, this will not work.
   * @param beamCoordsX   X-coordinate for the inital position.
   * @param beamCoordsY   Y-coordinate for the inital position.
   * @param beamCoordsRot Angle to the direction, which the robot should face at
   *                      its initial position.
   */
  public void initRobot(String robotID, String team, double beamCoordsX, double beamCoordsY, double beamCoordsRot) {
    sendAgentMessage("(scene rsg/agent/nao/nao.rsg)(syn)");
    getServerMessage();
    sendAgentMessage("(init (unum " + robotID + ")(teamname " + team + "))(syn)");
    getServerMessage();
    sendAgentMessage("(beam " + beamCoordsX + " " + beamCoordsY + " " + beamCoordsRot + ")(syn)");

    for (int i = 0; i < 100; i++) {
      getServerMessage();
      sendAgentMessage("(syn)");
    }
  }

  /**
   * Sends an agent message to the server.
   * <p/>
   * This method formats an agent message (String of SimSpark effector messages)
   * according to the network protocoll and sends it to the server.
   * <p/>
   * The content of the agent message is not validated.
   * @param msg Agent message with effector commands.
   */
  public void sendAgentMessage(String msg) {
    //System.out.println("Sende Nachricht:" + msg);
    byte[] body = msg.getBytes();

    //Kommentar der Autoren von magma aus Offenburg:
    // FIXME: this is to compensate a server bug that clients responding too
    // quickly get problems
    // long runtime = 0;
    // boolean slowedDown = false;
    // long slowDownTime = 0;
    // int minWaitTime = 1000000;
    // do {
    // runtime = System.nanoTime() - startTime;
    // if (runtime < minWaitTime && !slowedDown) {
    // slowDownTime = minWaitTime - runtime;
    // slowedDown = true;
    // }
    // } while (runtime < minWaitTime);
    // if (slowedDown) {
    // logger.log(Level.FINE, "slowedDown sending message by: {0}",
    // slowDownTime);
    // }

    // Header der Nachricht, gibt die Länge der Nachricht an, auf diese Weise:
    // "The length prefix is a 32 bit unsigned integer in network order, i.e. big 
    // endian notation with the most significant bits transferred first." 
    // (Zitat aus http://simspark.sourceforge.net/wiki/index.php/Network_Protocol, stand 14.1.2012)
    int len = body.length;
    int byte0 = (len >> 24) & 0xFF;
    int byte1 = (len >> 16) & 0xFF;
    int byte2 = (len >> 8) & 0xFF;
    int byte3 = len & 0xFF;

    try {
      out.writeByte((byte) byte0);
      out.writeByte((byte) byte1);
      out.writeByte((byte) byte2);
      out.writeByte((byte) byte3);
      out.write(body);
      out.flush();
    } catch (IOException e) {
      System.out.println("Error writing to socket. Has the server been shut down?");
    }
  }

  /**
   * Receives a server message and returns it.
   * <p/>
   * This method listens (blocking) for the next SimSpark message from the
   * server, removes the header concerning the SimSpark network protocol and
   * returns the server message (String of perceptor messages). <br>
   * If the server has sent more then one message since the last call of this
   * method, the oldest is returned, that means the messages are provided
   * always in chronological order.
   * <p/>
   * @return The raw server message (String of concatenated perceptor messages).
   */
  public String getServerMessage() {
    String msg = "keine Nachricht";
    byte[] result;
    int length;

    // System.out.println("Probiere Nachricht zu bekommen.");

    try {
      int byte0 = in.read();
      int byte1 = in.read();
      int byte2 = in.read();
      int byte3 = in.read();
      length = byte0 << 24 | byte1 << 16 | byte2 << 8 | byte3; // analyzes
      // the header
      int total = 0;

      if (length < 0) {
        // server was shutdown
        System.out.println("Server ist down.");
      }

      result = new byte[length];
      while (total < length) {
        total += in.read(result, total, length - total);
      }

      msg = new String(result, 0, length, "UTF-8");
    } catch (IOException e) {
      System.out.println("Error when reading from socket. Has the server been shut down?");
      return null;
    }

    return msg;
  }
}