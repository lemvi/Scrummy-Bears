package academy.everyonecodes.java.service.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException;
}
