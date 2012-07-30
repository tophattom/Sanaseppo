package sanaseppo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import sanaseppo.ui.MainWindow;

/**
 *
 * @author Jaakko
 */
public class Sanaseppo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainWindow());
    }

    private static void WriteWordList(ArrayList<String> words) {
        try {
            PrintWriter fw = new PrintWriter(new File("data/wordlist.txt"));

            for (String s : words) {
                fw.println(s);
            }
            
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Sanaseppo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
