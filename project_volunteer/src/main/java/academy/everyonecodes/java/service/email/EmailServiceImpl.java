package academy.everyonecodes.java.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final String subject;
    private final String text;
    private final String pathToAttachment;

    public EmailServiceImpl(JavaMailSender emailSender, @Value("${message.subject}") String subject, @Value("${message.text}") String text,
                            @Value("${message.pathToAttachment}") String pathToAttachment) {
        this.emailSender = emailSender;
        this.subject = subject;
        this.text = text;
        this.pathToAttachment = pathToAttachment;
    }

    @Override
    public void sendSimpleMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        //message.setFrom("scrummybears08@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    //not sure about the exception IntelliJ provided here. https://www.baeldung.com/spring-email: says to maybe catch SendFailedException ??
    @Override
    public void sendMessageWithAttachment(String to) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //helper.setFrom("scrummybears08@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);


        File file = new File(pathToAttachment);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        helper.addAttachment("Logo Scrummy Bears", file);

        emailSender.send(message);
    }
}
