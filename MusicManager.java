/**
 * @author: Andrew Talbot
 * Project #3
 * CMSC 256, Section 901
 * Fall 2017
 *
 * About:
 * Contains main method for the program, manages interactions with the song data.
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class MusicManager {

    // GLOBAL VARIABLES
    private static final String options = "123"; // there are three program options
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<MySong> songs = new ArrayList<>();

    public static void main(String[] args) {
        printHeading();
        args = validateInput(args);

        // COMMAND LINE ARGS
        String songFile, programChoice, artistName;
        songFile = args[0];
        programChoice = args[1];
        artistName = (!programChoice.equals("1")) ? args[2] : null;

        songs = SongReader.readFile(songFile);
        switch(programChoice) {
         case "1":
             // PROGRAM 1
             printTopTen();
             break;
         case "2":
             // PROGRAM 2
             int artistLocation = indexOfArtist(artistName);
             String negate = (artistLocation > -1) ? " " : " not ";
             // disucssion board said to use name of artist as it appears in the data
             String artist = (artistLocation > -1) ? songs.get(artistLocation).getArtist() : artistName;
             System.out.println(
                     artist + " is" + negate + "included in this playlist."
             );
             break;
         case "3":
             // PROGRAM 3
             printSongsByArtist(artistName);
             break;
         default:
             System.out.println("Something went wrong. An appropriate program choice was not entered.");
             System.exit(0);
             break;
        }
        System.exit(0);
        // printSongs();
    }

    /**
     * Displays the top 10 songs with the highest playcount ordered from highest to lowest playcount.
     * If the playlist contains less than 10 songs, it displays all the songs.
     * @params none
     * @return void
     */
    private static void printTopTen() {
        Collections.sort(songs, new MySong());
        printSongs(10);
    }

    /**
     * Displays all songs by a given artist by album and in alphabetical order by title
     * @params none
     * @return void
     */
    private static void printSongsByArtist(String artist) {
        ArrayList<MySong> songsByArtist = new ArrayList<MySong>();

        int artistLocation = indexOfArtist(artist);
        if(artistLocation == -1) {
            System.out.println("There are no songs in this playlist by " + artist + ".");
            System.exit(0);
        }
        while(artistLocation > -1) {
            songsByArtist.add(songs.get(artistLocation));
            artistLocation = indexOfArtist(artist, artistLocation + 1);
        }

        Collections.sort(songsByArtist, new Song());
        songs = songsByArtist;
        printSongs();
    }

    /**
     * Returns -1 if Artist name not found.
     * If the artist exists in the playlist, returns the first index of that artists songs
     * @params String artist
     * @return int
     */
    private static int indexOfArtist(String artist) {
        int i = 0;
        for(MySong song : songs) {
            if(song.getArtist().equalsIgnoreCase(artist)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * OVERLOAD METHOD FOR indexOf, includes starting index param
     * Returns -1 if Artist name not found.
     * If the artist exists in the playlist, returns the first index of that artists songs
     * @params String artist
     * @return int
     */
    private static int indexOfArtist(String artist, int startIndex) {
        MySong song;
        for(int i = startIndex; i < songs.size(); i++) {
            song = songs.get(i);
            if(song.getArtist().equalsIgnoreCase(artist)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ensures valid command line arguments and prompts user to correct.
     * This method does not verify file existence or presence of artist
     * name.
     *
     * @params
     *      Array - args
     * @return
     *      Array - validated args
     */
    private static String[] validateInput(String[] args) {
        if(args.length != 3) {
            int argsCount = args.length; // we will resize, so keep track of number of args
            args = Arrays.copyOf(args, 3);
            // GET 3 ARGS (these are not else-if because one can lead to another)
            if(argsCount < 1) {
                // user did not provide any command line args
                System.out.print("Please enter a file name containing song data: ");
                args[argsCount++] = input.nextLine();
            }
            if(argsCount == 1) {
                // user has only given a file name so far
                args[argsCount++] = getValidChoice("Please enter 1, 2, or 3 to determine program functionality: ");

            }
            if(argsCount == 2 && !args[1].equals("1")) {
                // the user selected a program requiring a third argument but did not provide it
                System.out.println("Please enter an artist name: ");
                args[2] = input.nextLine();
            }
        }
        if(args.length >= 2 && (!options.contains(args[1]) || args[1].equals(""))) {
            // user gave a file name and an invalid option from the command line
            args[1] = getValidChoice("You did not enter a valid program choice. Try again: ");
        }
        if(args.length == 3 && args[1].equals("1")) {
            // make sure the array is length 2 if option 2 is chosen
            args = Arrays.copyOf(args, 2);
        }
        return args;
    }

    /**
     * Helper method for getting a valid choice of program
     * @param message
     * @return String
     */
    private static String getValidChoice(String message) {

        String arg;

        System.out.print(message);
        do {
            arg = input.nextLine();
            if(!options.contains(arg)) {
                System.out.println("You did not enter a valid program choice. Try again: ");
            }
        } while(!options.contains(arg) || arg.equals(""));
        return arg;
    }

    /**
     * Prints all the songs in the list via toString method
     * @return
     *      void, (prints to console)
     */
    private static void printSongs() {
        int x = 1;
        for(MySong song : songs) {
            System.out.println("SONG #" + x++ + "\n" + song.toString() + "\n");
        }
    }

    /**
     * Prints top ten songs in the list via toString method
     * Overload method of printSongs()
     * @return
     *      void, (prints to console)
     */
    private static void printSongs(int top) {
        int x = 1;
        for(MySong song : songs) {
            System.out.println("SONG #" + x++ + "\n" + song.toString() + "\n");
            if(x > top) {
                break;
            }
        }
    }

    /**
     * Prints name, project number, course identifier and current semester to console.
     */
    private static void printHeading() {

        String name, semester, projectNumber, courseID;

        name = "Andrew Talbot";
        projectNumber = "Project #3";
        courseID = "CMSC 256, Section 901";
        semester = "Fall 2017";

        String[] data = {"****** HEADING ******", name, projectNumber, courseID, semester, "====================="};

        System.out.println(String.join("\n", data));
    }
}
