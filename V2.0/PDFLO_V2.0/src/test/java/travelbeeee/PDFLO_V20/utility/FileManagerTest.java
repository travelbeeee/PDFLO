package travelbeeee.PDFLO_V20.utility;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileManagerTest {

    @Autowired
    FileManager fileManager;

    @Value("${file.location}")
    private String fileLocation;
    @Test
    public void test() throws Exception{
        // given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        fileManager.fileUpload(inputStream, fileLocation, "Fileeee.txt");
        // when

        // then
    }

}