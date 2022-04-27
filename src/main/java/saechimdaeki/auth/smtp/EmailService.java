package saechimdaeki.auth.smtp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import saechimdaeki.auth.dto.EmailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(EmailMessage emailMessage)  {
        try {
            javaMailSender.send(createMimeMessage(emailMessage));
        }catch (Exception e){
            log.error("error",e);
        }

    }

    private MimeMessage createMimeMessage(EmailMessage emailMessage) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); //파일 보내기위함.

        String htmlMsg = "<p>" + "해당 링크를 눌러서 인증을 완료해주세요 "+ "<a href= https://localhost:8080/check-email-token?token="
            +emailMessage.getBody()+"&email="+emailMessage.getTo()+">여기를 클릭 하시면 인증이 완료됩니다.</a>" + "<p> <img src='cid:mushroom'>";
        helper.setText(htmlMsg, true);
        helper.setTo(emailMessage.getTo());
        helper.setFrom(emailMessage.getFrom());
        helper.setSubject(emailMessage.getSubject());

        File file = new ClassPathResource("static/mushroom.png").getFile();
        FileSystemResource fsr = new FileSystemResource(file);
        helper.addInline("mushroom",fsr);
        return mimeMessage;
    }
}
