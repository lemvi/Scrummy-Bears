package academy.everyonecodes.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, @Value("${mail.subject}") String subject, @Value("${mail.text}") String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("${spring.mail.username}");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    //not sure about the exception IntelliJ provided here. https://www.baeldung.com/spring-email: says to maybe catch SendFailedException ??
    @Override
    public void sendMessageWithAttachment(String to, @Value("${mail.subject}") String subject, @Value("${mail.text}") String text, String pathToAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("${spring.mail.username}");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        File file = new File(pathToAttachment);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        helper.addAttachment("Logo Scrummy Bears", file);

        emailSender.send(message);
    }
}
