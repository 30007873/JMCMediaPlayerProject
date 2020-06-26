package utils;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MergeSortUtilsTest {

    private static final Logger log = Logger.getLogger(MergeSortUtilsTest.class.getName());

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        log.info("Setting up...");

    }

    @After
    public void tearDown() {
        log.info("Tearing down...");

    }

    @BeforeClass
    public static void before() {
        log.info("Running JUnit test cases from class: " + MergeSortUtilsTest.class);

    }

    @AfterClass
    public static void after() {
        log.info("Testing class " + MergeSortUtilsTest.class + " has completed.");

    }

    public void reset() {
        tearDown();
        setup();
    }

    @Test
    public void mergeSortTest() throws Exception {
        List<String> playlist = PlayListUtils.getPlaylistFromCSV("test.csv");
        // get list iterator
        Iterator<String> iterator = playlist.iterator();
        // create playlist String array
        String[] playlistArray = new String[playlist.size()-1]; // skip header
        int count = 0;
        // loop through
        while(iterator.hasNext()){
            if(count == 0){
                count++;
                iterator.next();
                continue; // skip header
            }
            String song = iterator.next();
            playlistArray[(count++)-1] = song;
        }
        MergeSortUtils.mergeSort(playlistArray, 0, playlistArray.length - 1);
        assertEquals(playlistArray[0], "Blinding Lights (The Weeknd) 128Kbps-(BigMusic).mp3");
        assertEquals(playlistArray[3], "Wanted (OneRepublic) 320Kbps-(BigMusic).mp3");
        reset();
    }
}
