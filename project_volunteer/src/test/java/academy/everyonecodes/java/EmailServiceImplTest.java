package academy.everyonecodes.java;

import academy.everyonecodes.java.service.email.EmailServiceImpl;
import com.icegreen.greenmail.junit4.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EmailServiceImplTest {

    @Autowired
    EmailServiceImpl emailServiceImpl;

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
        JavaMailSender emailSender
        MimeMessage message = emailServiceImpl.createMimeMessage();
        GreenMailUtil.sendMimeMessage("to@localhost", "from@localhost",
                "some subject", "some body");
        assertNotEquals("false", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));
    }

     */
}
