package utils;

import config.DBConfig;
import csvFormatter.CSVFormatter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVUtils {
    private static final String FILE_DIRECTORY = "logs";

    public static class CSVFile {
        public final static ArrayList<String[]> rowsList = new ArrayList<String[]>();
        public static String[] row;
        public static int count;
        public static List<String> headers = null;

        /**
         * Read spreadsheet by filename
         *
         * @param fileName
         * @return
         */
        public static ArrayList<String[]> readSpreadsheet(String fileName) {
            try {
                CSVParser parser = new CSVParser(new FileReader(fileName), CSVFormat.DEFAULT);
                List<CSVRecord> csvRecords = parser.getRecords();
                int totalRecords = csvRecords.size();
                // add header indexFrom CSV
                headers = parser.getHeaderNames();
                String[] headersArray = new String[headers.size()];
                for (int i = 0; i < headers.size(); i++) {
                    headersArray[i] = headers.get(i);
                }
                rowsList.add(headersArray);
                // add rows indexFrom CSV
                for (int i = 0; i < totalRecords; i++) {
                    CSVRecord record = csvRecords.get(i);
                    row = new String[record.size()];
                    count = 0;
                    record.forEach(e -> {
                        row[count] = e;
                        count++;
                    });

                    rowsList.add(row);
                    System.out.println(Arrays.toString(row));
                }
            } catch (Exception e) {
                System.out.println("File not found:" + e.getMessage());
            }
            return rowsList;
        }
    }

    /**
     * Write spreadsheet by formatter and directory
     *
     * @param csvFormatter
     * @param object
     * @param <E>
     * @return
     * @throws Exception
     */
    public <E> Class<?> createAndWriteCSV(E csvFormatter, Class<? extends Object> object) throws Exception {
        String COMMA_DELIMITER = ",";
        String NEW_LINE = "\n";
        Field[] headers = object.getDeclaredFields();
        File tempDirectory = new File(FILE_DIRECTORY);
        // create csv in directory
        File file = new File(tempDirectory + "/" + object.getSimpleName() + ".csv");
        if (file.exists()) {
            System.out.println("File already exists.");
        }


        FileWriter fileWriter = new FileWriter(file, true);

        // check if new file is created
        if (file.createNewFile()) {
            System.out.println("File created at: " + tempDirectory);
            // add headers if a new file is created
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].getName().toUpperCase();
                fileWriter.append(header);
                if (i != headers.length - 1) {
                    fileWriter.append(COMMA_DELIMITER);
                } else {
                    fileWriter.append(NEW_LINE);
                }
            }
        }
        // add values
        E e = csvFormatter;
        Field[] fields = e.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            String value = (String) fields[j].get(e);
            fileWriter.append(value);
            if (j != headers.length - 1) {
                fileWriter.append(COMMA_DELIMITER);
            } else {
                fileWriter.append(NEW_LINE);
            }
        }
        // flush entries from buffer to the file
        fileWriter.flush();
        // close the file writer
        fileWriter.close();
        // return the object
        return object;
    }

    public <E> boolean editCSVPlaylist(CSVFormatter csvFormatter, String filename, boolean isDeleted) throws IOException, IllegalAccessException {
        String COMMA_DELIMITER = ",";
        String NEW_LINE = "\n";
        Field[] headers = csvFormatter.getClass().getDeclaredFields();
        File tempDirectory = new File("playlist");
        // create csv in directory
        File file = new File(tempDirectory + "/" + filename);
        if (file.exists() && !isDeleted) {
            System.out.println("File already exists.");
            file.delete();
            System.out.println("File deleted.");
            isDeleted = true;
            file.createNewFile();

            System.out.println("File created at: " + tempDirectory);
        }
        FileWriter fileWriter = new FileWriter(file, true);
        // add values
        String value = csvFormatter.getSong();
        fileWriter.append(value);
        fileWriter.append(NEW_LINE);
        // flush entries from buffer to the file
        fileWriter.flush();
        // close the file writer
        fileWriter.close();
        // return the object
        return isDeleted;
    }
}
