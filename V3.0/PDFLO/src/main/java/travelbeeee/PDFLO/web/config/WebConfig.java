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
                .excludePathPatterns("/", "/error/**", "/member/signUp", "/member/login",
                        "/member/logout", "/member/sendMail", "/member/auth", "/member/duplicateCheck", "/images/**",
                        "/js/**", "/css/**", "/img/**")
                .excludePathPatterns("/test/**", "/test");
    }
}
