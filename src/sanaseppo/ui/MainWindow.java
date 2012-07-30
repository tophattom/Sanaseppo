package sanaseppo.ui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import sanaseppo.domain.GamePanel;

/**
 *
 * @author Jaakko
 */
public class MainWindow implements Runnable {
    
    private JFrame frame;
    
    private GamePanel gp;

    @Override
    public void run() {
        frame = new JFrame("Sanaseppo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(GamePanel.PWIDTH, GamePanel.PHEIGHT));
        
        gp = new GamePanel();
        
        frame.add(gp);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
