package utils;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class JMCUtilsTest {

    private static final Logger log = Logger.getLogger(JMCUtilsTest.class.getName());

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
        log.info("Running JUnit test cases from class: " + JMCUtilsTest.class);

    }

    @AfterClass
    public static void after() {
        log.info("Testing class " + JMCUtilsTest.class + " has completed.");

    }

    public void reset() {
        tearDown();
        setup();
    }

    @Test
    public void encodeTest() throws Exception {
        JMCUtils jmcUtils = JMCUtils.getInstance();
        String string = "hello0";
        String encodedString = jmcUtils.encode(string);
        assertTrue(!string.equals(encodedString));

        reset();
    }

    @Test
    public void decodeTest() throws Exception {
        JMCUtils jmcUtils = JMCUtils.getInstance();
        String string = "hello0";
        String encodedString = jmcUtils.encode(string);
        assertTrue(string.equals(jmcUtils.decode(encodedString)));
        reset();
    }

    @Test
    public void getInstance() throws Exception {
        JMCUtils jmcUtils = JMCUtils.getInstance();
        if(jmcUtils != null && jmcUtils instanceof JMCUtils){
            assertTrue(true);
        }
        reset();
    }
}
