/**
 * @author: Andrew Talbot
 * Project #3
 * CMSC 256, Section 901
 * Fall 2017
 *
 * About:
 * SongReader takes in a full file patch to song data, parses out songs with artist, title, and album,
 * then displays the data. This class uses a LinkedStack to parse the data.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;

public class SongReader {

    // STATIC STACKS -- keep track of tags, data, and songs
    private static final LinkedStack<String> delimiters = new LinkedStack<>();
    private static final LinkedStack<String[]> data = new LinkedStack<>();
    private static final ArrayList<MySong> songs = new ArrayList<>();

    public SongReader() {
        // nothing to initialize, members are final.
    }

    /**
     * Walks through the process of opening and reading the file, then steps
     * through each line and passes the line to process line
     * @return
     *      void
     */
    public static ArrayList<MySong> readFile(String path) {
        // create instance of file class and check for file path's existence
        try {
            Scanner input = openFile(path);
            String line;
            StringBuilder context = new StringBuilder("none"); // track context of data, makes it easier to keep track
            boolean isBalanced = true; // assume balance until unbalanced

            // while data remains in the file
            while (input.hasNext()) {

                line = input.nextLine().trim();
                // System.out.println(line);

                // pass line and associated boolean values to switch block in helper method
                // return false if balanced, if unbalanced, only check for balance when new <song> comes
                if (isStartOfSong(line) || isBalanced) {
                    isBalanced = processLine(line, isOpening(line), isClosing(line), context);
                }

                // check stacks to see if data can be saved to tempSong
                // only do this if the last line was a song closing tag
                if (isBalanced && isEndOfSong(line)) {
                    saveToSong();
                }
            }

            input.close(); // close scanner
            // printSongs();
            return songs;
        } catch (FileNotFoundException noFile) {
            System.out.println("The program was unable to load the file. Please restart.");
            return null;
        }
    }

    /**
     * Interprets the current line read in by Scanner. If a line is invalid,
     * the method returns false, otherwise it returns true (maintains balance)
     * @return
     *      boolean indicating whether the song is still balanced
     */
    private static boolean processLine(String line, boolean isOpening, boolean isClosing, StringBuilder context) {
        boolean isBalanced = true; // we assume balance until it is clearly not
        String currentContext = context.toString();
        try {
            // top of stack tells us what to expect next, also may throw EmptyStackException
            String topOfStack = delimiters.peek();

            // if we get to this line we know the stack is not empty
            if(isOpening) {

                // push any opening tag to stack unless closing tag expected, or top of stack is <song>
                // otherwise set isBalanced to false
                if(isClosing(topOfStack) || isStartOfSong(topOfStack)) {
                    delimiters.push(line);
                    context.replace(0, context.length(), line);
                } else {
                    isBalanced = false;
                }
            } else if(isClosing) {
                // push any closing tag to stack unless it doesn't match, then set isBalanced to false
                // if there is not data in a tag, then set isBalanced to false. If end of the song, then push.
                if(areCounterparts(line, topOfStack) && hasData() || isEndOfSong(line)) {
                    delimiters.push(line);
                    context.replace(0, context.length(), "none");
                } else {
                    isBalanced = false;
                }
            } else { // IT'S DATA !!!
                // push any data to stack as long as top of stack contains a tag other than <song>
                // and the top of stack cannot be a closing tag. Data is associated with the current context.
                if(!isClosing(topOfStack) && !isStartOfSong(topOfStack) && !ignoreLine(currentContext)) {
                    if(currentContext.equalsIgnoreCase("<playcount>")) {
                        try {
                            Integer.valueOf(line);
                            data.push(new String[]{currentContext, line});
                        } catch(NumberFormatException ex) {
                            // The playcount is not an integer and is invalid
                            isBalanced = false;
                        }
                    } else {
                        data.push(new String[]{currentContext, line});
                    }
                } // else ignore the data, this does not make the markup text unbalanced
            }
        } catch (EmptyStackException ex) {
            // we get here when peek() throws an empty stack exception, ie. we have no song yet
            // check if this line is the beginning of a song
            if(isOpening && isStartOfSong(line)) {
                delimiters.push(line);
            } else {
                isBalanced = false;
            }
        }
        // start from a clean slate if not balanced, clear the stacks
        if(!isBalanced) {
            delimiters.clear();
            data.clear();
        }
        return isBalanced;
    }

    /**
     * When called, current data in the data stack is saved to the song stack as song objects
     * @return
     *      void - saves a song object to the song stack
     */
    private static void saveToSong() {
        String album, title, artist, currentTag;
        int playcount = 0;
        album = title = artist = null;
        try {
//            delimiters.pop(); // pop </song> off top of stack

            // loop through stacks until <song> is reached or data is empty
            while (!data.isEmpty()) {
                currentTag = data.peek()[0].toLowerCase(); // peek at last opening tag and set as current tag
                switch (currentTag) {
                    case "<artist>":
                        artist = data.pop()[1];
                        break;
                    case "<album>":
                        album = data.pop()[1];
                        break;
                    case "<title>":
                        title = data.pop()[1];
                        break;
                    case "<playcount>":
                        // must ensure this is an int when balancing, not here when time to save
                        playcount = Integer.valueOf(data.pop()[1]);
                        break;
                    default:
                        data.pop(); // clear unneeded data field
                }
            }
        } catch(EmptyStackException ex) {
            // pop can throw empty stack exception, if that happens clear the stacks, the song will not be saved
            delimiters.clear();
            data.clear();
        }
        if (album != null && artist != null && title != null && playcount >= 0) {
            songs.add(new MySong(title, artist, album, playcount));
        }
        delimiters.clear();
        data.clear();
    }

    /**
     * Checks whether a tag is a closing tag
     * @return
     *      boolean
     */
    private static boolean isClosing(String line) {
        // (?i) indicates case insensitive matching
        String expectedTags = "((song)|(artist)|(album)|(title)|(playcount))";
        return line.matches("(?i)</" + expectedTags + ">");
    }

    /**
     * Checks whether a tag is an opening tag
     * @return
     *      boolean
     */
    private static boolean isOpening(String line) {
        // (?i) indicates case insensitive matching
        String expectedTags = "((song)|(artist)|(album)|(title)|(playcount))";
        return line.matches("(?i)<" + expectedTags + ">");
    }

    /**
     * Checks whether line is "</song>"
     * @return
     *      boolean indicating whether this is a closing song tag
     */
    private static boolean isEndOfSong(String line) {
        return line.equalsIgnoreCase("</song>");
    }

    /**
     * Checks whether line is "<song>"
     * @return
     *      boolean indicating whether this is an opening song tag
     */
    private static boolean isStartOfSong(String line) {
        return line.equalsIgnoreCase("<song>");
    }

    /**
     * Checks whether current line is the counterpart of the top of the stack
     * @return
     *      boolean indicating whether current line is the counterpart of the top of the stack
     */
    private static boolean areCounterparts(String line, String topOfStack) {
        return line.equalsIgnoreCase(topOfStack.substring(0,1) + "/" + topOfStack.substring(1));
    }

    /**
     * Checks both the data and delimiter stacks to ensure there is data for each tag
     * @return
     *      boolean indicating whether all tags currently contain data
     */
    private static boolean hasData() {
        // the last delimiter should equal the context in which the last data element was recorded
        // if not, the current context, when closing, does not have any data
        try {
            return delimiters.peek().equalsIgnoreCase(data.peek()[0]);
        } catch(EmptyStackException ex) {
            return false;
        }
    }

    /**
     * Checks both the data and delimiter stacks to ensure there is data for each tag
     * @return
     *      boolean indicating whether all tags currently contain data
     */
    private static boolean ignoreLine(String currentContext) {
        // the last recorded context should not equal the context in which this data element is being recorded
        // if not, the current context, when closing, does not have any data
        try {
            return data.peek()[0].equalsIgnoreCase(currentContext);
        } catch(EmptyStackException ex) {
            return false;
        }
    }

    /**
     * Gets a file path from the keyboard (alternative prompts depending on entry point)
     * @return
     *      String
     */
    private static String promptForFileName(boolean firstPrompt) {
        String alternatePrompt = (firstPrompt) ? ": " : " (\":q\" to quit): ";
        System.out.println("Enter the full path to the file containing song data" + alternatePrompt);
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    /**
     * Attempts to open the file, prompts for a different file if none exists.
     * User has the option to quit the program.
     * @return
     *      Scanner
     */
    private static Scanner openFile(String pathName) throws FileNotFoundException {
        File file = new File(pathName);
        while (!file.exists()) {
            System.out.println("File note found! Try again.");
            file = new File(promptForFileName(false));
            if(file.getName().equals(":q")) System.exit(0);
        }
        // System.out.println("Showing songs from " + file.getName() + "\n");
        return new Scanner(file);
    }
}
