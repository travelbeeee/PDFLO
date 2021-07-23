package travelbeeee.PDFLO.domain.utility;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileManager {
    @Value("${file.dir}")
    private String fileDir;
    private String resizeName = "resized-";
    private String fileSeperator = "/";
    @AllArgsConstructor
    class Resize{
        boolean needResize;
        int width, height;
    }

    private HashMap<FileType,String> locationMap = new HashMap<FileType,String>();
    private HashMap<FileType, Resize> resizeMap = new HashMap<>();

    @PostConstruct
    private void postConstruct(){
        locationMap.put(FileType.PDF, "pdf/");
        locationMap.put(FileType.PROFILE, "profile/");
        locationMap.put(FileType.THUMBNAIL, "thumbnail/");

        resizeMap.put(FileType.PDF, new Resize(false, 0, 0));
        resizeMap.put(FileType.PROFILE, new Resize(true, 40, 40));
        resizeMap.put(FileType.THUMBNAIL, new Resize( true, 400, 400));
    }

    public FileInformation fileSave(MultipartFile file, FileType fileType) throws NoSuchAlgorithmException, IOException {
        String originFileName = file.getOriginalFilename();
        String extension = getExt(originFileName);
        String saltedFileName = UUID.randomUUID().toString() + "." + extension;
        String location = makeSavePath(locationMap.get(fileType));
        Resize resize = resizeMap.get(fileType);
        log.info("파일 저장 ! fullPath : {}", getFullPath(saltedFileName, location));
        file.transferTo(new File(getFullPath(saltedFileName, location)));
        if(resize.needResize){
            resizeImage(getFullPath(saltedFileName, location), resize.width, resize.height);
        }
        return new FileInformation(originFileName, saltedFileName, location);
    }

    private String makeSavePath(String fileTypePath) {
        Calendar cal = Calendar.getInstance();

        String yearPath = Integer.toString(cal.get(Calendar.YEAR));
        String monthPath = yearPath + fileSeperator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
        String datePath = monthPath + fileSeperator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        File dirPath = new File(fileDir + fileTypePath + datePath);
        if(!dirPath.exists()) {
            dirPath.mkdirs();
        }
        return fileTypePath + datePath + fileSeperator;
    }

    public String getFullPath(String fileName, String location) {
        return fileDir + location + fileName;
    }

    public void resizeImage(String oPath, int tWidth, int tHeight){ //원본 경로를 받아와 resize 후 저장한다.
        File oFile = new File(oPath);

        int index = oPath.lastIndexOf(".");
        String ext = oPath.substring(index + 1); // 파일 확장자

        String tPath = oFile.getParent() + File.separator + resizeName + oFile.getName(); // 썸네일저장 경로
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

    private String getExt(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public boolean fileDelete(String location, String fileName) {
        File file = new File(getFullPath(fileName, location));
        File resizeFile = new File(getFullPath(fileName, resizeName + location));
        if(file.exists()) file.delete();
        if(resizeFile.exists()) resizeFile.delete();
        return false;
    }

    public byte[] fileDownload(String location, String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(getFullPath(fileName, location)));
    }
}
