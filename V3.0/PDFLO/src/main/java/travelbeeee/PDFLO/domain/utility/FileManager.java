package travelbeeee.PDFLO.domain.utility;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.enumType.FileType;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class FileManager {
    @Value("${file.dir}")
    private String filePath;

    @AllArgsConstructor
    class Resize{
        boolean needResize;
        int width, height;
    }

    private HashMap<FileType,String> locationMap = new HashMap<FileType,String>();
    private HashMap<FileType, Resize> resizeMap = new HashMap<>();
    private final Sha256Encryption sha256Encryption;

    @PostConstruct
    private void postConstruct(){
        locationMap.put(FileType.PDF, "PDF/");
        locationMap.put(FileType.PROFILE, "PROFILE/");
        locationMap.put(FileType.THUMBNAIL, "THUMBNAIL/");

        resizeMap.put(FileType.PDF, new Resize(false, 0, 0));
        resizeMap.put(FileType.PROFILE, new Resize(true, 40, 40));
        resizeMap.put(FileType.THUMBNAIL, new Resize(true, 400, 400));
    }

    public FileInformation fileUpload(MultipartFile file, FileType fileType) throws NoSuchAlgorithmException, IOException {
        String fileName = sha256Encryption.sha256(file.getOriginalFilename(), sha256Encryption.makeSalt());
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        String location = locationMap.get(fileType);
        Resize resize = resizeMap.get(fileType);

        file.transferTo(new File(getFullPath(fileName, extension, location)));
        if(resize.needResize){
            resizeImage(getFullPath(fileName, extension, location), resize.width, resize.height);
        }
        return new FileInformation(fileName, location, extension);
    }

    private String getFullPath(String fileName, String extension, String location) {
        return filePath + location + fileName + extension;
    }

    public boolean fileDelete(String location, String fileName, String extension) {
        File file = new File(getFullPath(fileName, extension, location));
        File resizeFile = new File(getFullPath(fileName, extension, location));
        if(file.exists()) file.delete();
        if(resizeFile.exists()) resizeFile.delete();
        return false;
    }

    public byte[] fileDownload(String location, String fileName, String extension) throws IOException {
        return Files.readAllBytes(Paths.get(filePath + location + "/" + fileName + extension));
    }

    public void resizeImage(String oPath, int tWidth, int tHeight){ //원본 경로를 받아와 resize 후 저장한다.
        File oFile = new File(oPath);

        int index = oPath.lastIndexOf(".");
        String ext = oPath.substring(index + 1); // 파일 확장자

        String tPath = oFile.getParent() + File.separator + "resized-" + oFile.getName(); // 썸네일저장 경로
        File tFile = new File(tPath);

        try {
            BufferedImage oImage = ImageIO.read(oFile); // 원본이미지

            BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
            Graphics2D graphic = tImage.createGraphics();
            Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
            graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
            graphic.dispose(); // 리소스를 모두 해제
            ImageIO.write(tImage, ext, tFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
