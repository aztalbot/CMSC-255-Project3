/**
 * @author: Andrew Talbot
 * Project #3
 * CMSC 256, Section 901
 * Fall 2017
 *
 * See notes below.
 */

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests compareTo and equals methods.
 * Does not test Song, Object comparison in compareTo, since it would cause a compile error
 */
public class SongTest {

    /**
     * Tests empty strings for all fields in both Song objects, should be equal.
     * @throws Exception
     */
    @Test
    public void compareToTest1() throws Exception {
        Song one = new Song("", "", "");
        Song two = new Song("", "", "");
        int actualValue = one.compareTo(two);
        int expectedValue = 0; // which is ("").compareTo("");
        assertEquals(null, expectedValue, actualValue);
    }

    /**
     * Tests a song compared to another that is greater.
     * @throws Exception
     */
    @Test
    public void compareToTest2() throws Exception {
        Song one = new Song("I", "Can't", "Sing");
        Song two = new Song("But", "Beyonce's", "Got pipes");
        int actualValue = one.compareTo(two);
        // the expected value is the output of two strings compared, but specifically here it should be album only
        int expectedValue = ("Sing").compareTo("Got pipes");
        assertEquals(null, expectedValue, actualValue);
    }


    /**
     * Tests a song compared to another that is lesser lexicographically.
     * @throws Exception
     */
    @Test
    public void compareToTest3() throws Exception {
        Song one = new Song("This", "Also Shouldn't", "Test");
        Song two = new Song("The", "Song's", "Title");
        int actualValue = one.compareTo(two);
        // the expected value is the output of two strings compared, but specifically here it should be album only
        int expectedValue = ("Test").compareTo("Title");
        assertEquals(null, expectedValue, actualValue);
    }

    /**
     * Tests a song compared to another that is greater, should return -12.
     * @throws Exception
     */
    @Test
    public void compareToTest4() throws Exception {
        Song one = new Song("This", "Should", "However");
        Song two = new Song("Test", "The Title", "However");
        int actualValue = one.compareTo(two);
        // the expected value is the output of two strings compared, but specifically here it should be title, since album is the same
        int expectedValue = ("This").compareTo("Test");
        assertEquals(null, expectedValue, actualValue);
    }

    /**
     * Should throw NullPointerException when compared to null
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void compareToTest5() throws Exception {
        Song one = new Song("This", "Should", "However");
        one.compareTo(null);
    }

    /**
     * Tests a song compared to another that is equal.
     * @throws Exception
     */
    @Test
    public void compareToTest6() throws Exception {
        Song one = new Song("Same", "Should", "Same");
        Song two = new Song("Same", "The Title", "Same");
        int actualValue = one.compareTo(two);
        int expectedValue = 0; // which is ("Same").compareTo("Same")
        assertEquals(null, expectedValue, actualValue);
    }

    /**
     * Compares two identical Songs. Should be true symmetrically.
     * @throws Exception
     */
    @Test
    public void equalsTest1() throws Exception {
        Song one = new Song("One", "Two", "Three");
        Song two = new Song("One", "Two", "Three");
        assertTrue(one.equals(two));
        assertTrue(two.equals(one));
    }

    /**
     * Compares two identical Songs. Should be true.
     * @throws Exception
     */
    @Test
    public void equalsTest2() throws Exception {
        Song one = new Song("One", "Two", "Three");
        Song two = new Song("One", "Two", "Three");
        Song three = new Song("One", "Two", "Three");
        assertTrue(one.equals(two) && two.equals(three) && one.equals(three));
    }

    /**
     * Compares three identical Songs to show transitive property.
     * @throws Exception
     */
    @Test
    public void equalsTest3() throws Exception {
        Song one = new Song("One", "Two", "Three");
        Song two = new Song("One", "Two", "Three");
        Song three = new Song("One", "Two", "Three");
        assertTrue(one.equals(two) && two.equals(three) && one.equals(three));
    }

    /**
     * Tests consistency. Multiple invocations should return correctly.
     * @throws Exception
     */
    @Test
    public void equalsTest4() throws Exception {
        Song one = new Song("One", "Two", "Three");
        Song two = new Song("One", "Two", "Three");
        Song three = new Song("Three", "Two", "One");
        assertTrue(one.equals(two));
        assertTrue(one.equals(two));
        assertFalse(two.equals(three));
        assertFalse(two.equals(three));
    }

    /**
     * Passing in null as the parameter should return false.
     * @throws Exception
     */
    @Test
    public void equalsTest5() throws Exception {
        Song one = new Song("One", "Two", "Three");
        assertFalse(one.equals(null));
    }

    /**
     * Passing in null as the parameter should return false.
     * @throws Exception
     */
    @Test
    public void equalsTest6() throws Exception {
        Song one = new Song("One", "Two", "Three");
        assertFalse(one.equals(null));
    }

    /**
     * Passing in something other than a Song should return false.
     * @throws Exception
     */
    @Test
    public void equalsTest7() throws Exception {
        Song one = new Song("This", "Is a", "Song");
        String two = "This is not a song";
        assertFalse(one.equals(two));
    }

    /**
     * Compares two different Songs. Should be false.
     * @throws Exception
     */
    @Test
    public void equalsTest8() throws Exception {
        Song one = new Song("One", "Two", "Three");
        Song two = new Song("Three", "Two", "One");
        assertFalse(one.equals(two));
    }

}