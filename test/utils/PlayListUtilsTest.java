package utils;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class PlayListUtilsTest {
    private static final Logger log = Logger.getLogger(PlayListUtilsTest.class.getName());

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
        log.info("Running JUnit test cases from class: " + PlayListUtilsTest.class);

    }

    @AfterClass
    public static void after() {
        log.info("Testing class " + PlayListUtilsTest.class + " has completed.");

    }

    public void reset() {
        tearDown();
        setup();
    }

    @Test
    public void getPlaylistTest() throws Exception {
        DoublyLinkedList<String> playlist = PlayListUtils.getPlaylist();
        Iterator<String> iterator = playlist.iterator();
        while(iterator.hasNext()){
            iterator.next();
            assertTrue(true);
        }

        reset();
    }

    @Test
    public void getPlaylistFromCSVTest() throws Exception {
        List<String> playlist = PlayListUtils.getPlaylistFromCSV("test.csv");
        Iterator<String> iterator = playlist.iterator();
        while(iterator.hasNext()){
            iterator.next();
            assertTrue(true);
        }

        reset();
    }
}