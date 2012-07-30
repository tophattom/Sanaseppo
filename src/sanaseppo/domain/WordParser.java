package sanaseppo.domain;

import java.util.ArrayList;

/**
 *
 * @author Jaakko
 */
public abstract class WordParser {

    /**
     * Returns aplhabetically ordered list of words in the file.
     *
     * @param filePath File to read
     * @return Alphabetically ordered list of words
     */
    public abstract ArrayList<String> getWords(String filePath);

    protected boolean checkWord(String s) {
        if (s.length() > 2 && !s.contains("-") && s.length() < 11 && !s.contains(" ") && !s.matches("[0-9]*") && !s.contains("'")) {
            return true;
        }
        
        return false;
    }
    
    protected String formatWord(String s) {
        String replace = s.replace("š", "sh");
        
        return replace;
    }
}
