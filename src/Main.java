import repository.ApplicationDAO;
import security.Login;

import javax.swing.*;
import java.awt.*;

public class Main {
    // start of application
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // info icon for dialog box
                Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");
                // drop down options
                Object[] options = {"Create DB", "Login"};
                // show input dialog with dropdown options
                String action = (String) JOptionPane.showInputDialog(null,
                        "Select an action:", "ShowInputDialog",
                        JOptionPane.PLAIN_MESSAGE, infoIcon, options, "Numbers");
                // on option select
                switch(action) {
                    case "Create DB" : {
                        // create DB, tables and populate with data
                        ApplicationDAO.createDBAndTables();
                        break;
                    }
                    case "Login" : {
                        // login
                        boolean isLoggedIn = Login.login();
                        break;
                    }
                }
            }
        });
    }
}
