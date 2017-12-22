/**
 * @author: Andrew Talbot
 * Project #3
 * CMSC 256, Section 901
 * Fall 2017
 *
 * About:
 * Extends the Song class and includes playcount member data field.
 *
 */

public class MySong extends Song {
    int playcount;

    public MySong() {
        this("", "", "", 0);
    }

    public MySong(String title, String artist, String album, int playcount) {
        super(title, artist, album);
        this.setPlaycount(playcount);
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        if(playcount < 0) {
            throw new IllegalArgumentException();
        }
        this.playcount = playcount;
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
        if(!(other instanceof MySong)) {
            return super.compareTo(other);
        } else if(super.compareTo(other) == 0) {
            // check member values, utilize String compareTo method for simplicity
            return this.playcount - ((MySong) other).getPlaycount();
        } else {
            return super.compareTo(other);
        }
    }

    /**
     * Return the result of comparing song1 and song2.
     * Override the compare method in the super class to compare by playcount if instanceof MySong
     * @param song1 first object.
     * @param song2 second object.
     * @return < 0 if lhs is less than rhs,
     *           0 if lhs is equal to rhs,
     *         > 0 if lhs is greater than rhs.
     */
    @Override
    public int compare(Song song1, Song song2) {
        if(song1 instanceof MySong && song2 instanceof MySong) {
            return ((MySong) song2).getPlaycount() - ((MySong) song1).getPlaycount();
        } else {
            return super.compare(song1, song2);
        }
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
        if(!(other instanceof MySong)) {
            return false;
        }
        // check member values
        MySong song = (MySong) other;
        boolean countMatches, songMatches;
        songMatches = super.equals(song);
        countMatches = song.getPlaycount() == this.playcount;

        return countMatches && songMatches;
    }

    @Override
    public String toString() {
        return super.toString() + "\nPlaycount: " + playcount;
    }
}
