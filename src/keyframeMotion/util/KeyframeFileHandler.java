
package keyframeMotion.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import util.RobotConsts;

/*
 * Liest Keyframe-Dateien aus dem Verzeichnis "[Root]/keyframes", wobei [Root]
 * das Ausführungsverzeichnis des Programms ist.
 * @author nika
 */
public class KeyframeFileHandler {
  
  /*! \brief Liest die Keyframe-Datei zeilenweise in ein KeyframeSequence-Objekt.
   * 
   * Leerzeilen und Kommentarzeilen (fangen mit "//" an) in der Keyframe-Datei
   * werden übersprungen. 
   * 
   * Die Reihenfolge der Winkel innerhalb eines Frames ist in RobotConsts
   * definiert, die selbe wie im SimSpark-Wiki, und muss eingehalten werden.
   * 
   */
  public static KeyframeSequence getSequenceFromFile(String fileName) {
    
    KeyframeSequence ks = new KeyframeSequence();
    
	try {
		BufferedReader in = new BufferedReader(new FileReader("keyframes/" + fileName));
		String line = null;
		while ((line = in.readLine()) != null) {
          if (line.length() > 0 && !line.startsWith("//")){
            Keyframe frame = getFrame(line);
            ks.addFrame(frame);
          }
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
    return ks;
  }
  
  /*! \brief Lädt eine Zeile aus einem Keyframe.txt in ein Array mit Winkeln. 
   * Problem dabei: 
   * Nimmt an, dass eine Zeile immer 22 Joints enthält und diese Joionts durch
   * ein oder mehrere Leerzeichen getrennt sind. 
   * Bei anderen Formatierungen (z.B. mit Tabs, oder wenn die Datei fehlerhaft ist),
   * verhält sich getFrame sehr undefiniert. 
   */
  private static Keyframe getFrame(String line){
    int transitionTime;
    double[] angleDoubles = new double[RobotConsts.JointsCount];
    int jointNum = 0;

    String[] valueStrings = line.split(" ");
    
    transitionTime = Integer.parseInt(valueStrings[0]);
    
    for (int i = 1; i < valueStrings.length; i++){
      if (valueStrings[i].length() > 0){
        if (jointNum < angleDoubles.length){
          angleDoubles[jointNum] = (Double.parseDouble(valueStrings[i]));
          jointNum++;
        }
      }  
    }    
    return new Keyframe(transitionTime, angleDoubles);
  }
  
  /*! \brief Speichert eine Sequenz in einer Datei ab. Wird nur benutzt um den NaoTH-MotionEditor zu debuggen.
   * 
   */
  public static void writeSequenceToFile(KeyframeSequence ks, String fileName){
    try{
      FileWriter writer = new FileWriter("keyframes/" + fileName);
      Keyframe kf = ks.getNextFrame();
      while(kf != null)
      {
        StringBuilder sb = new StringBuilder();
        sb.append(kf.getTransitionTime());
        for (int i = 0; i < RobotConsts.JointsCount; i++)
          sb.append(" ").append(kf.getAngle(i));
        sb.append("\n");
        writer.write(sb.toString());
        kf = ks.getNextFrame();
      }
      writer.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
}
