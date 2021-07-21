package travelbeeee.PDFLO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PdfloApplication {
	public static void main(String[] args) {
		SpringApplication.run(PdfloApplication.class, args);
	}
}
