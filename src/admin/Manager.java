package admin;

import audioplayer.AudioPlayer;
import config.DBConfig;
import csvFormatter.CSVFormatter;
import net.proteanit.sql.DbUtils;
import utils.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Manager {

    /**
     * Create manager dashboard
     */
    public static void managerDashboard() {
        // create jframe
        JFrame jFrame = new JFrame("Manager Functions");
        // set exit on close
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // create playlist button
        JButton playlistButton = new JButton("Playlist");
        // set button bounds and dimentions
        playlistButton.setBounds(25, 25, 110, 40);
        // add button action listener
        playlistButton.addActionListener(new ActionListener() {
                                             // on button action
                                             public void actionPerformed(ActionEvent actionEvent) {
                                                 // create jframe
                                                 JFrame jFrame = new JFrame("Songs Available");
                                                 // get DB connection
                                                 Connection connection = DBConfig.getInstance();
                                                 // define query
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
                                                     // create jtable
                                                     JTable songTable = new JTable();
                                                     // populate jtable with query results
                                                     songTable.setModel(DbUtils.resultSetToTableModel(resultSet));
                                                     // add scroll pane to the table
                                                     JScrollPane scrollPane = new JScrollPane(songTable);
                                                     // set additional UI attribs to jframe
                                                     jFrame.add(scrollPane);
                                                     jFrame.setSize(800, 400);
                                                     jFrame.setVisible(true);
                                                     jFrame.setLocationRelativeTo(null);
                                                 } catch (SQLException ex) {
                                                     JOptionPane.showMessageDialog(null, ex);
                                                 }
                                             }
                                         }
        );

        // Add add button
        JButton addSongButton = new JButton("Add");
        addSongButton.setBounds(148, 25, 110, 40);
        addSongButton.addActionListener(new ActionListener() {
            // on add button action

            /**
             * ADD Button
             *
             * @param actionEvent
             */
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame jFrame = new JFrame("Enter Song Details");
                JLabel jLabel;
                jLabel = new JLabel("Song Name");
                jLabel.setBounds(30, 15, 100, 30);

                JTextField songTextField = new JTextField();
                songTextField.setBounds(110, 15, 250, 30);

                JButton addSongButton = new JButton("Submit");
                addSongButton.setBounds(130, 130, 80, 40);
                addSongButton.addActionListener(new ActionListener() {
                    // on song add, enter song details
                    public void actionPerformed(ActionEvent actionEvent) {
                        String songName = songTextField.getText();
                        // get DB connection and insert song into DB
                        Connection connection = DBConfig.getInstance();

                        try {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("USE JUPITER_MINING_CORPORATION");
                            statement.executeUpdate("INSERT INTO SONGS(SONG_NAME) VALUES ('" + songName + "')");
                            JOptionPane.showMessageDialog(null, "Song added!");
                            jFrame.dispose();
                        } catch (SQLException e1) {
                            JOptionPane.showMessageDialog(null, e1);
                        }

                        try {
                            // get create statement
                            Statement statement = connection.createStatement();
                            // execute to use the right DB
                            statement.executeUpdate("USE JUPITER_MINING_CORPORATION");
                            // reinitialize create statement
                            statement = connection.createStatement();
                            // execute query
                            String query = "select * From SONGS";
                            ResultSet resultSet = statement.executeQuery(query);
                            boolean isDeleted = false;
                            while (resultSet.next()){
                                CSVUtils csvUtils = new CSVUtils();
                                CSVFormatter csvFormatter = new CSVFormatter();
                                csvFormatter.setSong(resultSet.getString("SONG_NAME"));
                                try {
                                    isDeleted = csvUtils.editCSVPlaylist(csvFormatter, "playlist.csv", isDeleted);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                jFrame.add(addSongButton);
                jFrame.add(jLabel);
                jFrame.add(songTextField);
                jFrame.setSize(400, 250);
                jFrame.setLayout(null);
                jFrame.setVisible(true);
                jFrame.setLocationRelativeTo(null);
            }
        });

        // Add remove button
        JButton removeSongButton = new JButton("Remove");
        removeSongButton.setBounds(270, 25, 110, 40);
        removeSongButton.addActionListener(new ActionListener() {
            // on remove buttom action, enter song details

            /**
             * REMOVE Button
             *
             * @param actionEvent
             */
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame jFrame = new JFrame("Enter Song Details");
                JLabel jLabel;
                jLabel = new JLabel("Song Name");
                jLabel.setBounds(30, 15, 100, 30);

                JTextField songTextField = new JTextField();
                songTextField.setBounds(110, 15, 250, 30);

                JButton removeSongButton = new JButton("Submit");
                removeSongButton.setBounds(130, 130, 80, 40);
                removeSongButton.addActionListener(new ActionListener() {
                    // on song details submitted, get DB connection and insert into DB
                    public void actionPerformed(ActionEvent actionEvent) {
                        String songName = songTextField.getText();

                        Connection connection = DBConfig.getInstance();

                        try {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("USE JUPITER_MINING_CORPORATION");
                            statement.executeUpdate("DELETE FROM SONGS WHERE SONG_NAME='" + songName + "'");

                            jFrame.dispose();

                        } catch (SQLException e1) {
                            // TODO Auto-generated catch block
                            JOptionPane.showMessageDialog(null, e1);
                        }

                        // define query
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
                            boolean isDeleted = false;
                            while (resultSet.next()){
                                String requiredSong = resultSet.getString("SONG_NAME");
                                CSVFormatter csvFormatter = new CSVFormatter();
                                csvFormatter.setSong(requiredSong);
                                CSVUtils csvUtils = new CSVUtils();

                                isDeleted = csvUtils.editCSVPlaylist(csvFormatter, "playlist.csv", isDeleted);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Iterator<String> iterator = PlayListUtils.playlist.iterator();
                        while(iterator.hasNext()){
                            String song = iterator.next();
                            if(song.equals(songName)){
                               iterator.remove();
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Song removed!");
                    }
                });

                jFrame.add(removeSongButton);
                jFrame.add(jLabel);
                jFrame.add(songTextField);
                jFrame.setSize(400, 250);
                jFrame.setLayout(null);
                jFrame.setVisible(true);
                jFrame.setLocationRelativeTo(null);
            }
        });

        // add search button
        JButton searchSongButton = new JButton("Search");
        searchSongButton.setBounds(25, 100, 110, 40);
        searchSongButton.addActionListener(new ActionListener() {
            // on search button action, enter song details
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame jFrame = new JFrame("Enter Song Details");
                JLabel jLabel, l2, l3;
                jLabel = new JLabel("Song Name");
                jLabel.setBounds(30, 15, 100, 30);

                JTextField songTextField = new JTextField();
                songTextField.setBounds(110, 15, 250, 30);

                JButton searchSongButton = new JButton("Submit");
                searchSongButton.setBounds(130, 130, 80, 40);
                searchSongButton.addActionListener(new ActionListener() {
                    // on song details submit, search playlist and show UI
                    public void actionPerformed(ActionEvent actionEvent) {
                        String songName = songTextField.getText().trim();
                        try {
                            DoublyLinkedList playlist = PlayListUtils.getPlaylist();
                            Iterator<String> iterator = playlist.iterator();
                            String[] playlistArray = new String[playlist.size()];
                            int count = 0;
                            while (iterator.hasNext()) {
                                String song = iterator.next().toString();
                                playlistArray[count++] = song;
                            }
                            MergeSortUtils.mergeSort(playlistArray, 0, playlistArray.length - 1);
                            List<String> playlistArrayList = new ArrayList<String>();
                            for (int i = 0; i < playlistArray.length; i++) {
                                playlistArrayList.add(playlistArray[i]);
                            }
                            int result = BinarySearchUtils.binarySearch(playlistArrayList, songName);
                            if (result != -1) {
                                JOptionPane.showMessageDialog(null, "Song " + songName + " found in position: " + result);
                            } else {
                                JOptionPane.showMessageDialog(null, "Song not found!\nThe song file needs to be in the playlist folder.");
                            }

                            iterator = playlist.iterator();
                            boolean found = false;
                            while(iterator.hasNext()){
                                String song = iterator.next();
                                if(song.equalsIgnoreCase(songName)){
                                    found = true;
                                    break;
                                }
                            }

                            if(found){
                                AudioPlayer.playCustom(songName);
                            } else {
                                JOptionPane.showMessageDialog(null, "Song not found!\nThe song file needs to be in the playlist folder.");
                            }

                            jFrame.dispose();

                        } catch (MalformedURLException e1) {
                            JOptionPane.showMessageDialog(null, e1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                jFrame.add(searchSongButton);
                jFrame.add(jLabel);
                jFrame.add(songTextField);
                jFrame.setSize(400, 250);
                jFrame.setLayout(null);
                jFrame.setVisible(true);
                jFrame.setLocationRelativeTo(null);
            }
        });

        // add all UI elements to the jframe
        jFrame.add(addSongButton);
        jFrame.add(removeSongButton);
        jFrame.add(searchSongButton);
        jFrame.add(playlistButton);
        jFrame.setSize(400, 180);
        jFrame.setLayout(null);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
    }
}
