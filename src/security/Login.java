package security;

import admin.Manager;
import audioplayer.AudioPlayer;
import config.DBConfig;
import utils.DoublyLinkedList;
import utils.JMCUtils;
import utils.PlayListUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Login {
    private static boolean isLoggedIn = false;
    /**
     * Get the runtime arguments (VM arguments) to call the audio player's main method
     */
    private static RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    private static List<String> arguments = runtimeMxBean.getInputArguments();
    private static String[] args = new String[arguments.size()];

    static {
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.get(i);
        }
    }

    /**
     * Create login, after success: load playlist table and start audio player
     *
     * @return
     */
    public static boolean login() {
        JFrame jFrame = new JFrame("Login");
        JLabel jLabel1, jLabel2;
        jLabel1 = new JLabel("Username");
        jLabel1.setBounds(30, 15, 100, 30);

        jLabel2 = new JLabel("Password");
        jLabel2.setBounds(30, 50, 100, 30);

        JTextField jUsernameField = new JTextField();
        jUsernameField.setBounds(110, 15, 200, 30);

        JPasswordField jPasswordField = new JPasswordField();
        jPasswordField.setBounds(110, 50, 200, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(130, 90, 80, 25);
        loginButton.addActionListener(new ActionListener() {
            // on submitting username and password
            public void actionPerformed(ActionEvent actionEvent) {
                // get username and password entered
                String username = jUsernameField.getText();
                String password = jPasswordField.getText();
                // validate username and password
                if (username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter username");
                } else if (password.equals("")) //If password is null
                {
                    JOptionPane.showMessageDialog(null, "Please enter password");
                } else {
                    // on validation success
                    try {
                        // encode password
                        password = JMCUtils.getInstance().encode(password);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    // get DB connection
                    Connection connection = DBConfig.getInstance();
                    try {
                        // get create statement object
                        Statement statement = connection.createStatement();
                        // use right database
                        statement.executeUpdate("USE JUPITER_MINING_CORPORATION");
                        // the query to select user with entered creds
                        String query = ("SELECT * FROM USERS WHERE USERNAME='" + username + "' AND PASSWORD='" + password + "'");
                        // get prepared statement with scroll insensitivity and concur updatable
                        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        // execute query's prepared statement
                        ResultSet resultSet = preparedStatement.executeQuery();
                        // check if results exist
                        if (resultSet.next() == false) {
                            JOptionPane.showMessageDialog(null, "Wrong Username/Password!"); //Display Message
                        } else {
                            jFrame.dispose();
                            // scroll result set to before first
                            resultSet.beforeFirst();
                            // show manager dashboard if user is found in which case, the result set would have an entry
                            while (resultSet.next()) {
                                Manager.managerDashboard();
                            }
                            // get playlist
                            DoublyLinkedList<String> playlist = PlayListUtils.getPlaylist();
                            playlist = PlayListUtils.validatePlaylist(playlist);
                            // assign playlist to audio player plaulist
                            AudioPlayer.playlist = playlist;
                            // add audio player's main method as runnable for a thread
                            Runnable task = () -> {
                                AudioPlayer.main(args);
                            };
                            // create a thread using the runnable
                            Thread thread = new Thread(task);
                            // start the thread
                            thread.start();
                            // toogle isLoggedIn
                            isLoggedIn = true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // add UI components to jFrame
        jFrame.add(jPasswordField);
        jFrame.add(loginButton);
        jFrame.add(jUsernameField);
        jFrame.add(jLabel1);
        jFrame.add(jLabel2);

        jFrame.setSize(400, 180);
        jFrame.setLayout(null);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        return isLoggedIn;
    }
}
