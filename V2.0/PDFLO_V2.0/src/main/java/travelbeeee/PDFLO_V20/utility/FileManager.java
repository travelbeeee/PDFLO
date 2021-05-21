package travelbeeee.PDFLO_V20.utility;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.enumType.FileType;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class FileManager {

    @Value("${file.location}")
    private String rootLocation;

    private HashMap<FileType,String> locationMap = new HashMap<FileType,String>();
    private final Sha256Encryption sha256Encryption;

    @PostConstruct
    private void postConstruct(){
        locationMap.put(FileType.PDF, "/PDF");
        locationMap.put(FileType.PROFILE, "/PROFILE");
        locationMap.put(FileType.THUMBNAIL, "/THUMBNAIL");
    }

    public FileInformation fileUpload(MultipartFile file, FileType fileType) throws NoSuchAlgorithmException {
        String fileName = sha256Encryption.sha256(file.getOriginalFilename(), sha256Encryption.makeSalt());
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        String location = rootLocation + locationMap.get(fileType);

        try{
            Files.copy(file.getInputStream(), Paths.get(location + "/" + fileName + extension), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            return null;
        }
        return new FileInformation(fileName, location, extension);
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
