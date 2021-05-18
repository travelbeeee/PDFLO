package travelbeeee.PDFLOpjt.utility;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileManager {

    public boolean fileUpload(InputStream inputStream, String location, String fileName) {
        try{
            Files.copy(inputStream, Paths.get(location + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean fileDelete(String location, String fileName) {
        File file = new File(location + "/" + fileName);
        if(file.exists()) file.delete();
        return false;
    }

    public byte[] fileDownload(String location, String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(location + "/" + fileName));
    }
}
