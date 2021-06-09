//package travelbeeee.PDFLO_V20.utility;
//
//import com.amazonaws.AmazonClientException;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.transfer.TransferManager;
//import com.amazonaws.services.s3.transfer.Upload;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//@PropertySource("classpath:application-aws.yml")
//public class S3Uploader {
//    private final AmazonS3 amazonS3Client;
//
//    // 버킷 이름 동적 할당
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    // 버킷 주소 동적 할당
//    @Value("${cloud.aws.s3.bucket.url}")
//    private String defaultUrl;
//
//    public String upload(MultipartFile uploadFile) throws IOException {
//        File file = convertMultiPartFileToFile(uploadFile);
//        String fileName = uploadFile.getOriginalFilename();
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file));
//        file.delete();
//
//        return "File Uploaded : " + fileName;
//    }
//
//    private File convertMultiPartFileToFile(MultipartFile file) {
//        File convertedFile = new File(file.getOriginalFilename());
//        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
//            fos.write(file.getBytes());
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//        return convertedFile;
//    }
//}
