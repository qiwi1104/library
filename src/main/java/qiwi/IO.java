package qiwi;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IO {
    public static JSONArray readJSONFile(String where) throws IOException {
        Scanner scan = new Scanner(new File(where), StandardCharsets.UTF_8);
        String str = "";
        while (scan.hasNext()) {
            str += scan.nextLine();
        }
        return new JSONArray(str);
    }

    public static void writeJSONToFile(JSONArray what, String where) throws IOException {
        String res = what.toString();
        FileWriter writer = new FileWriter(new File(where), StandardCharsets.UTF_8);
        writer.write(res);
        writer.close();
    }
}
