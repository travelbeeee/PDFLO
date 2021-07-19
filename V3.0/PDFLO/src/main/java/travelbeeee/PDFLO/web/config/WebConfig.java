package travelbeeee.PDFLO.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import travelbeeee.PDFLO.web.intercepter.LoginIntercepter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginIntercepter())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/member/signUp", "/member/login",
                        "/member/authenticateMail", "/member/logout");
    }
}
