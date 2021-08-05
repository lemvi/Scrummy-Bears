package academy.everyonecodes.java.service.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {

    public void sendSimpleMessage(String to);

    public void sendMessageWithAttachment(String to) throws MessagingException;
}
