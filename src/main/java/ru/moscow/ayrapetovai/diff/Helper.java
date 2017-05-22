package ru.moscow.ayrapetovai.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by artem on 30.01.17.
 */
public class Helper {
    public static String readSourceFile(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder sb = new StringBuilder();
            String s = null;
            do {
                s = br.readLine();
                if (s != null) {
                    sb.append(s).append("\n");
                }
            } while (s != null);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try { br.close(); } catch (Exception ignore) {}
            }
        }
        return "";
    }
}
