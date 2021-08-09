package academy.everyonecodes.java;

import academy.everyonecodes.java.service.email.EmailServiceImpl;
import academy.everyonecodes.java.service.email.MailServerConfiguration;
import com.icegreen.greenmail.junit4.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EmailServiceImplTest {

    @Autowired
    EmailServiceImpl emailServiceImpl;

    @Autowired
    JavaMailSender emailSender;

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);


    @Test
    public void testSendSimpleEmail() throws MessagingException {
        GreenMailUtil.sendTextEmailTest("to@localhost", "from@localhost",
                "some subject", "some body");
        assertEquals("some body", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }

    @Test
    public void testSendSimpleEmailFail() throws MessagingException {
        GreenMailUtil.sendTextEmailTest("to@localhost", "from@localhost",
                "some subject", "some body");
        assertNotEquals("false", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }

/*
    @Test
    public void testSendEmailWithAttachments() throws MessagingException {
        //Session smtpSession = greenMail.getSmtp().createSession();
        //MimeMessage msg = new MimeMessage(smtpSession);
       // Mockito.when(emailSender.createMimeMessage()).thenReturn(msg);
        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("to@localhost");
        helper.setSubject("test");
        helper.setText("test");
        String path = "path";
        File file = new File(path);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        helper.addAttachment("test", file);
        GreenMailUtil.sendMimeMessage(msg);
        assertEquals("test", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }

 */


}
