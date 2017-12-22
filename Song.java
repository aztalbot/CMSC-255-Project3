/**
 * @author: Andrew Talbot
 * Project #3
 * CMSC 256, Section 901
 * Fall 2017
 *
 * About:
 * Song implements the Comparable class and stored title, artist, and album data for a particular song.
 * The class cannot have null fields.
 *
 */

import java.util.Comparator;

public class Song implements Comparable<Song>, Comparator<Song> {

    private String title, artist, album;

    Song() {
      this("", "", "");
    }

    Song(String title, String artist, String album) {
        // Song cannot have null fields
        if(title == null || artist == null || album == null) {
            throw new IllegalArgumentException();
        }

        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    /**
     * @param other
     * @return int
     *      Compares this song to another song, and returns a positive int if the song is greater than the other,
     *      0 if same, a negative int if less
     *      Compares by album, then by title. (per blackboard discussion board)
     *      A bit of backward logic: the comparison is based on unicode values, and since a letter
     *      earlier in the Alphabet has a lower value, A is less than B, so Song "Alpha" is less than
     *      Song "Beta". So when ordering songs alphabetically, negative results should move the song
     *      to an earlier point in the sorted group.
     */
    @Override
    public int compareTo(Song other) {
        // check type
        if(other == null) {
            throw new NullPointerException();
        }
        // check member values, utilize String compareTo method for simplicity
        int albumCompare, titleCompare;
        albumCompare = this.album.compareTo(other.getAlbum());
        titleCompare = this.title.compareTo(other.getTitle());

        return (albumCompare != 0) ? albumCompare : titleCompare;
    }

    /**
     * Return the result of comparing lhs and rhs.
     * @param song1 first object.
     * @param song2 second object.
     * @return < 0 if lhs is less than rhs,
     *           0 if lhs is equal to rhs,
     *         > 0 if lhs is greater than rhs.
     */
    public int compare(Song song1, Song song2) {
        return song1.compareTo(song2);
    }

    /**
     *
     * @param other
     * @return boolean
     *    true if equal or same reference; false if not a Song, null, or not equal
     */
    @Override
    public boolean equals(Object other) {
        // check if reference to same object
        if(this == other) {
            return true;
        }
        // ensure other is not null
        if(other == null) {
            return false;
        }
        // check type
        if(!(other instanceof Song)) {
            return false;
        }
        // check member values
        Song song = (Song) other;
        boolean artistMatches, albumMatches, titleMatches;
        artistMatches = song.getArtist().equals(this.artist);
        albumMatches = song.getAlbum().equals(this.album);
        titleMatches = song.getTitle().equals(this.title);

        return artistMatches && albumMatches && titleMatches;
    }

    // GETTERS AND SETTERS
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null) throw new IllegalArgumentException();
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        if(artist == null) throw new IllegalArgumentException();
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        if(album == null) throw new IllegalArgumentException();
        this.album = album;
    }
    // END GETTERS AND SETTERS

    @Override
    public String toString() {
        return "Title: " + title + "\nArtist: " + artist + "\nAlbum: " + album;
    }
}
