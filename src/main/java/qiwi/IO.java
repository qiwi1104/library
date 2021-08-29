package qiwi;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IO {
    public static JSONArray readJSONFile(String where) {
        Scanner scan;
        StringBuilder str = new StringBuilder();

        try {
            scan = new Scanner(new File(where), StandardCharsets.UTF_8);

            while (scan.hasNext()) {
                str.append(scan.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONArray(str.toString());
    }

    public static void writeJSONToFile(JSONArray what, String where) {
        String res = what.toString();

        try {
            FileWriter writer = new FileWriter(new File(where), StandardCharsets.UTF_8);
            writer.write(res);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
