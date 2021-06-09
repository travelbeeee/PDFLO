//package travelbeeee.PDFLO_V20.configuration;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//
//public class StorageConfig {
//
//    @Value("{cloud.aws.credentials.accessKey")
//    private String accessKey;
//
//    @Value("{cloud.aws.credentials.secretKey")
//    private String secretKey;
//
//
//    @Value("{cloud.aws.region.static")
//    private String region;
//
//    @Bean
//    private AmazonS3 generateS3Client(){
//        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .withRegion(region).build();
//    }
//}
