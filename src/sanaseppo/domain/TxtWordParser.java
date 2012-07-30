package sanaseppo.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaakko
 */
public class TxtWordParser extends WordParser {

    @Override
    public ArrayList<String> getWords(String filePath) {
        ArrayList<String> result = new ArrayList<String>();

        try {
            Scanner reader = new Scanner(new File(filePath));

            while (reader.hasNextLine()) {
                String s = reader.nextLine();
                
                if (checkWord(s)) {
                    result.add(formatWord(s));
                }
            }

            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TxtWordParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        Collections.sort(result);
        return result;
    }
}
