import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String filename;
    private long size;
    private byte[] data;

    public FileMessage (Path path) throws IOException {
        this.filename = path.getFileName().toString();
        this.size = Files.size(path);
        this.data = Files.readAllBytes(path);
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }
}
