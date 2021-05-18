package travelbeeee.PDFLOpjt.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service @RequiredArgsConstructor
public class MailSender {
    private final JavaMailSender javaMailSender;

    public int mailSending(String userMail) throws MessagingException {
        String fromEmail = "sochun2528@gmail.com";
        String toEmail = userMail;
        String title = "회원가입인증메일입니다.";

        Random r = new Random();
        int code = r.nextInt(4589362) + 49311;

        String content = System.getProperty("line.separator")+ //한줄씩 줄간격을 두기위해 작성
                System.getProperty("line.separator")+
                "안녕하세요 회원님 저희 홈페이지를 찾아주셔서 감사합니다"
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                " 인증번호는 " + code + " 입니다. "
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "받으신 인증번호를 홈페이지에 입력해 주시면 다음으로 넘어갑니다."; // 내용

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,
                true, "UTF-8");

        messageHelper.setFrom(fromEmail); // 보내는사람 생략하면 정상작동을 안함
        messageHelper.setTo(toEmail); // 받는사람 이메일
        messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
        messageHelper.setText(content); // 메일 내용

        javaMailSender.send(message);

        return code;
    }
}
