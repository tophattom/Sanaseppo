package sanaseppo.domain;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 *
 * @author Jaakko
 */
public class LetterGrid {

    private int size;
    private LetterCell[][] letters;
    private final String characters = "aaaaaaaaaaaiiiiiiiiiitttttttttnnnnnnnnneeeeeeeesssssssslllllloooookkkkkuuuuuääääämmmvvvrrjjhhyyppdögbfc";
    
    private ArrayList<String> possibleWords;
    private ArrayList<String> gridWords;
    
    private int[] wordAmounts;
    
    private boolean[][] visited;    //For depth first search
    
    private final int cellSize = 100;
    private final int cellMargin = 20;

    public LetterGrid(int size) {
        this.size = size;

        this.letters = new LetterCell[size][size];
        
        this.possibleWords = new ArrayList<String>();
        this.gridWords = new ArrayList<String>();
        
        this.wordAmounts = new int[8];

        this.visited = new boolean[size][size];
    }

    public void initialize(ArrayList<String> dictionary) {
        this.clear();
        
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                char l = this.characters.charAt(random.nextInt(this.characters.length()));
                int tmpX = x * (this.cellSize + this.cellMargin);
                int tmpY = y * (this.cellSize + this.cellMargin);

                this.letters[x][y] = new LetterCell(l, tmpX, tmpY, this.cellSize, this.cellSize);

                sb.append(l);
            }
        }

        findWordsWithGridLetters(sb, dictionary);
        findWordsInGrid(sb);
        
        System.out.println("Words in grid:");
        for (String s : this.gridWords) {
            System.out.println(s);
        }
        System.out.println("Amount: " + this.gridWords.size());
        System.out.println();
        
        System.out.println("Sanamäärät:");
        printWordAmounts();
    }

    private void findWordsWithGridLetters(StringBuilder sb, ArrayList<String> dictionary) {
        for (int i = 0; i < dictionary.size(); i++) {
            StringBuilder tmp = new StringBuilder(sb);
            String s = dictionary.get(i);

            Character firstChar = s.charAt(0);
            int firstLetterIndex = tmp.indexOf(firstChar.toString());
            if (firstLetterIndex == -1) {
                continue;
            } else {
                tmp.deleteCharAt(firstLetterIndex);

                for (int k = 1; k < s.length(); k++) {
                    Character c = s.charAt(k);

                    int index = tmp.indexOf(c.toString());
                    if (index == - 1) {
                        break;
                    } else {
                        tmp.deleteCharAt(index);

                        if (k == s.length() - 1) {
                            this.possibleWords.add(s);
                        }
                    }
                }
            }
        }
    }

    private void findWordsInGrid(StringBuilder sb) {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                DFS("", x, y);
            }
        }
        
        this.cleanupList();
        
        for (String s : this.gridWords) {
            int l = s.length();
            
            this.wordAmounts[l - 3]++;
        }
    }

    private void DFS(String prefix, int x, int y) {
        if (x < 0 || x >= this.size || y < 0 || y >= this.size) {
            return;
        }

        if (this.visited[x][y]) {
            return;
        }

        if (!containsPrefix(prefix)) {
            return;
        }

        this.visited[x][y] = true;

        prefix = prefix + this.letters[x][y].getLetter();
        if (this.possibleWords.contains(prefix)) {
            this.gridWords.add(prefix);
        }

        for (int xx = -1; xx <= 1; xx++) {
            for (int yy = -1; yy <= 1; yy++) {
                DFS(prefix, x + xx, y + yy);
            }
        }

        this.visited[x][y] = false;

    }

    private boolean containsPrefix(String prefix) {
        for (int i = 0; i < this.possibleWords.size(); i++) {
            if (this.possibleWords.get(i).startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
    
    private void cleanupList() {
        HashSet<String> hs = new HashSet<String>();
        hs.addAll(this.gridWords);
        this.gridWords.clear();
        this.gridWords.addAll(hs);
        Collections.sort(this.gridWords);
    }

    public void draw(Graphics g) {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                int tmpX = x * (this.cellSize + this.cellMargin);
                int tmpY = y * (this.cellSize + this.cellMargin);

                this.letters[x][y].draw(g, Color.BLUE);
            }
        }
    }

    public char getCharAt(int mouseX, int mouseY) {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (this.letters[x][y].contains(mouseX, mouseY)) {
                    return this.letters[x][y].getLetter();
                }
            }
        }

        return ' ';
    }

    public boolean contains(int mouseX, int mouseY) {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (this.letters[x][y].contains(mouseX, mouseY)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getGridX(int mouseX) {
        return mouseX / (this.cellSize + this.cellMargin);
    }

    public int getGridY(int mouseY) {
        return mouseY / (this.cellSize + this.cellMargin);
    }

    public LetterCell getCellAt(int x, int y) {
        if (x >= 0 && x < this.size && y >= 0 && y < this.size) {
            return this.letters[x][y];
        }

        return null;
    }
    
    public boolean wordFound(String word) {
        if (Collections.binarySearch(this.gridWords, word) > -1) {
            return true;
        }
        
        return false;
    }
    
    public void printWordAmounts() {
        for (int i = 0; i < this.wordAmounts.length; i++) {
            System.out.println((i + 3) + " kirjainta: " + this.wordAmounts[i]);
        }
    }
    
    public void clear() {
        this.gridWords.clear();
        this.possibleWords.clear();
        this.wordAmounts = new int[8];
        this.letters = new LetterCell[this.size][this.size];
    }
}
