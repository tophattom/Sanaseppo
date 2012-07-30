package sanaseppo.domain;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Jaakko
 */
public class LetterCell {

    private char letter;
    private Rectangle area;
    
    private final Font font = new Font("Helvetica", Font.PLAIN, 40);

    public LetterCell(char letter, int x, int y, int width, int height) {
        this.letter = letter;
        this.area = new Rectangle(x, y, width, height);
    }

    public char getLetter() {
        return letter;
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public Rectangle getArea() {
        return area;
    }

    public void draw(Graphics g, Color color) {
        g.setColor(color);

        g.fillRect(this.area.x, this.area.y, this.area.width, this.area.height);

        g.setColor(Color.WHITE);
        Character l = this.letter;

        g.setFont(this.font);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(l.toString(), g);

        int textW = (int) (rect.getWidth());
        int textH = (int) (rect.getHeight());

        g.drawString(l.toString().toUpperCase(),
                this.area.x + (this.area.width - textW) / 2,
                this.area.y + (this.area.height - textH) / 2 + fm.getAscent());
    }
    
    public int getX() {
        return this.area.x;
    }
    
    public int getY() {
        return this.area.y;
    }
}
