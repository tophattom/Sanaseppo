package sanaseppo.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Jaakko
 */
public class KotusWordParser extends WordParser {

    @Override
    public ArrayList<String> getWords(String filePath) {
        final ArrayList<String> result = new ArrayList<String>();
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler() {
                
                boolean wordNode = false;
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("s")) {
                        wordNode = true;
                    }
                }
                
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (wordNode) {
                        String s = new String(ch, start, length);
                        
                        if (s.length() > 2 && !s.contains("-") && s.length() < 11 && !s.contains(" ") && !s.matches("[0-9]*") && !s.contains("'")) {
                            result.add(s.toLowerCase());
                        }
                    }
                }
            };
            
            saxParser.parse(filePath, handler);
            
            Collections.sort(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
