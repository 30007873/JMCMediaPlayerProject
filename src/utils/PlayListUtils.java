package utils;

import config.DBConfig;
import csvFormatter.CSVFormatter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlayListUtils {
    // playlist directory in project folder
    private static final String FILE_DIRECTORY = "playlist";
    public static DoublyLinkedList<String> playlist = null;

    /**
     * Get playlist as a doubly linked list from the a directory
     * @return
     * @throws MalformedURLException
     */
    public static DoublyLinkedList<String> getPlaylist() throws MalformedURLException {
        // get directory
        File tempDirectory = new File(FILE_DIRECTORY);
        // get files from the directory
        File[] playlistFiles = tempDirectory.listFiles();
        List<String> playlistFilesMp3 = new ArrayList<String>();
        for(int i = 0; i< playlistFiles.length; i++){
            String filename = playlistFiles[i].getName();
            if(filename.substring(filename.length()-4, filename.length()).equalsIgnoreCase(".mp3")){
                playlistFilesMp3.add(filename);
            }
        }
        // Initialize doubly linked list
        playlist = new DoublyLinkedList<String>();
        // add files to the doubly linked list in descending order for a playlist
        // Decending order is used to check if merge sort is working
        for(int i = 0; i < playlistFilesMp3.size(); i++){
            playlist.add(playlistFilesMp3.get(i));
        }
        // return playlist
        return playlist;
    }

    /**
     * Get playlist as an array list from a csv
     * @param filename
     * @return
     * @throws MalformedURLException
     */
    public static List<String> getPlaylistFromCSV(String filename) throws MalformedURLException {
        // create playlist as an array list
        List<String> playlist = new ArrayList<String>();
        // get playlist directory
        File tempDirectory = new File(FILE_DIRECTORY);
        // get file by filename
        File file = new File(tempDirectory.getAbsolutePath() + "/" + filename);
        // get csv spreadsheet
        ArrayList<String[]> spreadsheet = CSVUtils.CSVFile.readSpreadsheet(file.getAbsolutePath());
        // loop through and populate playlist
        for(int i = 1; i < spreadsheet.size(); i++){
            String song = spreadsheet.get(i)[0];
            playlist.add(song);
        }
        // return playlist
        return playlist;
    }

    /**
     * This removes the song that is not present in the DB from the Doubly Linked List
     * @param playlist
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static DoublyLinkedList<String> validatePlaylist(DoublyLinkedList<String> playlist) throws IOException, IllegalAccessException {
        List<String> songsList = new ArrayList<String>();
        Connection connection = DBConfig.getInstance();
        String query = "select * From SONGS";
        try {
            // get create statement
            Statement statement = connection.createStatement();
            // execute to use the right DB
            statement.executeUpdate("USE JUPITER_MINING_CORPORATION");
            // reinitialize create statement
            statement = connection.createStatement();
            // execute query
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                songsList.add(resultSet.getString("SONG_NAME"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Doubly linked List Iterator removes the item
        Iterator<String> iterator = playlist.iterator();
        while(iterator.hasNext()){
            String song = iterator.next();
            if(!songsList.contains(song)){
                iterator.remove();
            }
        }

        for(String song: songsList){
            boolean isPresent = true;
            playlist = PlayListUtils.getFirst(playlist);
            ListIterator<String> listIterator = playlist.iterator();
            while(listIterator.hasNext()){
                String playlistSong = listIterator.next();
                if(song.equalsIgnoreCase(playlistSong)){
                    isPresent = true;
                    break;
                } else {
                    isPresent = false;
                }
            }

            if(!isPresent){
                boolean flag = true;
                playlist.add(song);
            }
        }
        return playlist;
    }

    /**
     * This brings the index back to start
     * @param playlist
     * @return
     */
    public static DoublyLinkedList<String> getFirst(DoublyLinkedList<String> playlist){
        ListIterator<String> iterator = playlist.iterator();
        while(iterator.hasPrevious()){
            iterator.previous();
        }
        return playlist;
    }
}
