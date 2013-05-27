package keyframeMotion.util;

import java.util.ArrayList;

/** Repräsentiert eine Sequenz von Keyframes im Speicher. 
 * 
 * Dient dazu, die einzelnen Frames nacheinander auszulesen. Speichert also 
 * auch, welches Frame zuletzt ausgelesen wurde.
 *
 * @see Reader-Klassen zum Einlesen einer Sequenz aus einer Datei.
 */
public class KeyframeSequence {
    
    private ArrayList<Keyframe> sequence = new ArrayList();
    private int nextFrameNumber = 0;
    
    /** Gibt das nächste noch nicht ausgegebene Frame zurück.
     * 
     * Nach dem letzten Frame wird null ausgegeben. Nachdem null ausgegeben wurde
     * wird wieder das erste Frame ausgegeben. 
     */
    public Keyframe getNextFrame(){
        Keyframe nextFrame;
        if (nextFrameNumber == sequence.size()){
            nextFrameNumber = 0;
            nextFrame = null;
        }
        else {
            nextFrame = sequence.get(nextFrameNumber);
            nextFrameNumber++;
        }
        return nextFrame;
    }
    
    /**
     * Append a new frame to the end of the sequence.
     * 
     * @param frame The new frame to add. 
     */
    public void addFrame(Keyframe frame){
      if(frame != null)
        sequence.add(frame);
    }
    
    public KeyframeSequence(){        
    }    
}
