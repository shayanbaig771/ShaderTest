package eng;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static String readString(Path path){
        StringBuilder total = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile()));

            String line = "";
            while((line = bufferedReader.readLine()) != null){
                total.append(line).append("\n");
            }

            return total.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readBytes(Path path){
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
