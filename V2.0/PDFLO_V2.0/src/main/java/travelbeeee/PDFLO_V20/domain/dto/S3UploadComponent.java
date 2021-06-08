package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3UploadComponent {
    private String bucket;
}
