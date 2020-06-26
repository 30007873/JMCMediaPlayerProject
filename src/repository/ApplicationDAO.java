package repository;

import config.DBConfig;
import constants.JMCConstants;
import utils.JMCUtils;
import utils.MergeSortUtils;
import utils.PlayListUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class ApplicationDAO {
    // the playlist directory in the project folder
    private static final String FILE_DIRECTORY = "playlist";
    // temporary directory
    private static File tempDirectory = null;
    // playlist files
    private static File[] playlistFiles = null;
    static {
        // get playlist folder
        tempDirectory = new File(FILE_DIRECTORY);
        // get files from the playlist folder
        playlistFiles = tempDirectory.listFiles();
    }

    /**
     * Create database, and tables with data
     */
    public static void createDBAndTables() {
        try {
            // get DB connection object
            Connection connection = DBConfig.getInstance();
            // get result set catalogs
            ResultSet resultSet = connection.getMetaData().getCatalogs();
            // loop through the result set
            while (resultSet.next()) {
                // get the database name from the result set of catalogs; the position 1 from result set of catalogs has the DB name
                String dbName = resultSet.getString(1);
                // validate database name
                if (dbName.equalsIgnoreCase("JUPITER_MINING_CORPORATION")) {
                    Statement statement = connection.createStatement();
                    // drop the database if it already exists
                    String query = "DROP DATABASE JUPITER_MINING_CORPORATION";
                    // execute query
                    statement.executeUpdate(query);
                }
            }
            // Get the Statement Object for create statement
            Statement statement = connection.createStatement();
            // define create statement or query for DB creation
            String query = "CREATE DATABASE JUPITER_MINING_CORPORATION";
            // execute
            statement.executeUpdate(query);
            // execute to use the created database
            statement.executeUpdate("USE JUPITER_MINING_CORPORATION");
            // query for create users table
            String query1 = "CREATE TABLE USERS(USER_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30), PASSWORD VARCHAR(30))";
            // execute
            statement.executeUpdate(query1);
            // create admin credentials
            String username = JMCConstants.ADMIN_USERNAME;
            String password = JMCUtils.getInstance().encode(JMCConstants.ADMIN_PASSWORD);
            // insert admins into users table
            statement.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD) VALUES('" + username + "', '" + password + "')");
            // create songs table
            statement.executeUpdate("CREATE TABLE SONGS(SONG_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, SONG_NAME VARCHAR(500))");
            // get playlist from csv
            List<String> playlist = PlayListUtils.getPlaylistFromCSV("playlist.csv");
            // get list iterator
            Iterator<String> iterator = playlist.iterator();
            // create playlist String array
            String[] playlistArray = new String[playlist.size()]; // skip header
            int count = 0;
            // loop through
            while(iterator.hasNext()){
//                if(count == 0){
//                    count++;
//                    iterator.next();
//                    continue; // skip header
//                }
                String song = iterator.next();
                playlistArray[count++] = song;
            }
            // use playlist array to run merge sort on it
            MergeSortUtils.mergeSort(playlistArray, 0, playlistArray.length - 1);
            query="INSERT INTO SONGS(SONG_NAME) VALUES ";
            // append each song from the merge sorted playlist to insert into the songs table
            for(int i = 0; i < playlistArray.length; i++){
                String song = playlistArray[i];
                if(i != playlistArray.length -1) {
                    query += "('" + song + "'), ";
                } else {
                    query += "('" + song + "')";
                }
            }
            // execute query to insert merge sorted songs into the songs table
            statement.executeUpdate(query);
            // close the result set
            resultSet.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
