package audioplayer;

import csvFormatter.CSVFormatter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.CSVUtils;
import utils.DoublyLinkedList;
import utils.PlayListUtils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AudioPlayer extends Application {
    /**
     * Get the runtime arguments (VM arguments)
     */
    private static RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    private static List<String> arguments = runtimeMxBean.getInputArguments();
    private static String[] args = new String[arguments.size()];
    // the playlist directory in the project folder
    private static final String FILE_DIRECTORY = "playlist";
    // media view
    final MediaView view = new MediaView();
    // Playlist
    public static DoublyLinkedList<String> playlist;
    // current song
    private static String songPlaying = new String();
    // play status
    private boolean isPlaying = false;
    private static MediaPlayer player = null;
    ObservableList observableList = null;

    /**
     * On player start
     *
     * @param stage
     * @throws MalformedURLException
     */
    @Override
    public void start(Stage stage) throws Exception {
        Iterator<String> iterator = playlist.iterator();
        // if playlist has next and nothing is being played
        if (iterator.hasNext() && !isPlaying) {
            // get song to play
            songPlaying = iterator.next();
            // play
            play(songPlaying, stage);
            // toogle play status
            isPlaying = true;
        }
    }

    /**
     * Play media by filename
     *
     * @param mediaFile
     * @param stage
     * @return
     * @throws MalformedURLException
     */
    public Group play(String mediaFile, Stage stage) throws Exception {
        Group group = new Group();
        Label label = new Label("Now playing: " + songPlaying);
        label.setLayoutX(20);
        label.setLayoutY(20);

        ListView<String> songs = new ListView<String>();
        observableList = FXCollections.observableArrayList();
        ListIterator<String> listIterator = PlayListUtils.getFirst(playlist).iterator();
        while(listIterator.hasNext()) {
            String song = (String) listIterator.next();
            if (song.equalsIgnoreCase(songPlaying)) {
                song = "Now playing: " + song;
            } else {
                if(song.contains("Now playing: ")){
                    song = song.split("Now playing: ")[1];
                }
            }
            observableList.add(song);
        }

        songs.setItems(observableList);
        GridPane gridPane1 = new GridPane();
        gridPane1.setHgap(20);
        gridPane1.setVgap(20);
        gridPane1.addRow(1, label);
        gridPane1.addRow(2, songs);

        HBox hBox = new HBox(gridPane1);
        group.getChildren().add(view);
        group.getChildren().add(hBox);
        Scene scene = new Scene(group, 360, 460, Color.WHITE);
        stage.setScene(scene);
        stage.show();
        // get playlist directory
        File tempDirectory = new File(FILE_DIRECTORY);
        // write the current song and time to csv
        CSVFormatter csvFormatter = new CSVFormatter();
        csvFormatter.setSong(songPlaying);
        CSVUtils csvUtils = new CSVUtils();
        csvUtils.createAndWriteCSV(csvFormatter, CSVFormatter.class);
        File file = null;
        // get file to play
        file = new File(tempDirectory.getAbsolutePath() + "/" + mediaFile);
        // create media
        Media media = new Media(file.toURI().toURL().toExternalForm());
        // create media player
        player = new MediaPlayer(media);
        // add media player to UI
        view.setMediaPlayer(player);
        // play
        player.play();
        // on end of playing
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.stop();
                isPlaying = false;
                // check for next song in the playlist
                Iterator<String> iterator = playlist.iterator();
                if (iterator.hasNext()) {
                    songPlaying = iterator.next();
//                    stage.close();
                    try {
                        for(int i = 0; i < observableList.size(); i++){
                            String song = (String) observableList.get(i);
                            if(song.equalsIgnoreCase(songPlaying)){
                                song = "Now playing: " + song;
                            } else {
                                if(song.contains("Now playing: ")) {
                                    song = song.split("Now playing: ")[1];
                                }
                            }
                            observableList.remove(i);
                            observableList.add(i, song);
                        }

                        // play next song
                        play(songPlaying, stage);
                        // toggle play status
                        isPlaying = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        });
        // return UI group
        return group;
    }

    public static void stopCurrent() throws Exception {
        player.stop();
    }

    public static void playCustom(String song) throws Exception {
        if (player != null) {
            player.stop();
        }
        songPlaying = song;
        List<String> playlist = PlayListUtils.getPlaylistFromCSV("playlist.csv");
        File tempDirectory = new File(FILE_DIRECTORY);
        File file = new File(tempDirectory.getAbsolutePath() + "/" + song);
        Media media = new Media(file.toURI().toURL().toExternalForm());
        player = new MediaPlayer(media);
        player.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}