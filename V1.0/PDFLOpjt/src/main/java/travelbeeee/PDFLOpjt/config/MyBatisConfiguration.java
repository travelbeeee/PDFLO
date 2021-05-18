package travelbeeee.PDFLOpjt.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "travelbeeee.PDFLOpjt.repository")
public class MyBatisConfiguration {
}
