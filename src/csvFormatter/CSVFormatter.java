package csvFormatter;

import java.util.Date;

public class CSVFormatter {
    public String song;
    public String date = new Date().toString();

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
