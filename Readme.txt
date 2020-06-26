This has been tested with JDK 11 and JavaFX SDA 11.
 
Assuming that XAMPP serves PHPMyAdmin with appropriate accesses to the user to perform the below with PhpMyAdmin running:
Step1: Create a user account in PHPMyAdmin
Step2: Provide the user account credentials in the MySQL connection String in DBConfig.java
Step3: Provide VM arguments to all class with Main method

VM args: 
--module-path "C:\Program Files\Java\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.media

Step4: Add C:\Program Files\Java\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib to project build path library
Step5: Add all jars from /lib located in project home folder into the project build path
Step6: Run the project