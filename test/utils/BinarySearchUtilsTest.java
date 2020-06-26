package utils;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BinarySearchUtilsTest {

    private static final Logger log = Logger.getLogger(BinarySearchUtilsTest.class.getName());

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
        log.info("Running JUnit test cases from class: " + BinarySearchUtilsTest.class);

    }

    @AfterClass
    public static void after() {
        log.info("Testing class " + BinarySearchUtilsTest.class + " has completed.");

    }

    public void reset() {
        tearDown();
        setup();
    }

    @Test
    public void binarySearchTest() throws Exception {
        DoublyLinkedList<String> playlist = PlayListUtils.getPlaylist();
        List<String> playlistArrayList = new ArrayList<String>();
        Iterator<String> iterator = playlist.iterator();
        while(iterator.hasNext()){
            playlistArrayList.add(iterator.next());
        }
        int position = BinarySearchUtils.binarySearch(playlistArrayList, playlistArrayList.get(0));
        assertEquals(0, position);

        reset();
    }
}
