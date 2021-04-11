import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GetLargeFile {
    public static void main(String[] args) {

    }
    public static String getLargeFile(String pathname){
//        StringBuilder line = new StringBuilder();
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathname), StandardCharsets.UTF_8)) {
            for (String line = null; (line = br.readLine()) != null;) {
                arrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line:
             arrayList) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
