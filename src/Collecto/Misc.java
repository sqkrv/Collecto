package Collecto;

import org.w3c.dom.ls.LSOutput;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Misc {
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    public static String logTime() {
        return "["+currentDateTime()+"] - ";
    }

    public static String currentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        System.out.println(logTime());
    }
}
