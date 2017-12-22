import org.junit.Test;

import static org.junit.Assert.*;

public class MySongTest {
    @Test(expected = IllegalArgumentException.class)
    public void getPlaycountTest() throws Exception {
        MySong testSong = new MySong("Title", "Artist", "Album", -5);
        // assertEquals(10, testSong.getPlaycount());
    }

    @Test
    public void setPlaycountTest() throws Exception {
        MySong testSong = new MySong("Title", "Artist", "Album", 0);
        testSong.setPlaycount(10);
        assertEquals(10, testSong.getPlaycount());
    }

    @Test
    public void compareToTest() throws Exception {
        MySong testSong1 = new MySong("Title1", "Artist1", "Album", 0);
        MySong testSong2 = new MySong("Title1", "Artist1", "Album", 10);
        assertTrue(testSong1.compareTo(testSong2) < 0);
    }

    @Test
    public void compareTest() throws Exception {
    }

    @Test
    public void equalsTest() throws Exception {
        MySong testSong1 = new MySong("Title1", "Artist1", "Album", 10);
        MySong testSong2 = new MySong("Title1", "Artist1", "Album", 10);
        assertTrue(testSong1.equals(testSong2));
    }

    @Test
    public void toStringTest() throws Exception {

    }

}