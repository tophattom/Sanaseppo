package sanaseppo.domain;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Jaakko
 */
public class DragTrail {
    
    private LinkedList<LetterCell> trail;
    private StringBuilder word;

    public DragTrail() {
       this.trail = new LinkedList<LetterCell>();
       this.word = new StringBuilder();
    }
    
    public boolean addCell(LetterCell lc) {
        if (!this.trail.contains(lc)) {
            this.trail.add(lc);
            this.word.append(lc.getLetter());
            
            return true;
        }
        
        return false;
    }
    
    public void stepBack() {
        this.trail.pollLast();
        this.word.deleteCharAt(this.word.length() - 1);
    }

    
    public LetterCell getPreviousCell() {
        if (this.trail.size() > 1) {
            Iterator<LetterCell> it = this.trail.descendingIterator();
            
            it.next();
            return it.next();
        }
        
        return null;
    }

    
    public boolean contains(LetterCell lc) {
        return this.trail.contains(lc);
    }
    
    public synchronized void draw(Graphics g, Color color) {
        double l = (double) this.trail.size();
        int i = 0;
        
        for (LetterCell lc : this.trail) {
            i++;
            
            double ratio = ((l - (i - 1)) / l);
            int red = (int) (color.getRed() * ratio);
            int green = (int) (color.getGreen() * ratio);
            int blue = (int) (color.getBlue() * ratio);
            
            lc.draw(g, new Color(red, green, blue));
        }
    }

    public String getWord() {
        return word.toString();
    }
    
    public LetterCell getLastCell() {
        if (this.trail.isEmpty()) {
            return null;
        }
        
        return this.trail.getLast();
    }
}
