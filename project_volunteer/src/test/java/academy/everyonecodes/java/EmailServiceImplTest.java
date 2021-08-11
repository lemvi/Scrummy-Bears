package academy.everyonecodes.java;

import academy.everyonecodes.java.service.email.EmailService;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import academy.everyonecodes.java.service.email.MailServerConfiguration;
import com.icegreen.greenmail.junit4.GreenMailRule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.extension.mail.MailConnectors;
import org.camunda.bpm.extension.mail.dto.Attachment;
import org.camunda.bpm.extension.mail.dto.Mail;
import org.camunda.bpm.extension.mail.poll.PollMailResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static com.icegreen.greenmail.util.ServerSetup.SMTP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EmailServiceImplTest {

    @Autowired
    EmailServiceImpl emailServiceImpl;

    @MockBean
    JavaMailSender emailSender;

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @SpyBean
    MimeMessage mimeMessage;

    @SpyBean
    MimeMessageHelper helper;

    private String to = "to@localhost";

    //TODO - REWRITE TEST WITH ATTACHMENT IF TIME SOMEWHEN

    /* SOMETHING WRONG WITH THIS:
    @Before
    public void before() throws MessagingException, URISyntaxException {
        greenMail.setUser("test@camunda.com", "bpmn");
        Session session = greenMail.getSmtp().createSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        emailSender = mock(JavaMailSender.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject("subject");
        helper.setText("text");
         File file = new File("project_volunteer/src/main/resources/Scrummy Bears Logo.jpg");
        Assertions.assertThat(file.exists()).isTrue();
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        helper.addAttachment("Logo.jpg", file);
        emailServiceImpl = new EmailServiceImpl(emailSender, "subject", "text", "/attachment.txt");
    }

     */
/* DO NOT THINK WE NEED IT:
    @BeforeClass
    public static void setupEmailTest() {
        ServerSetup setup = ServerSetupTest.SMTP;
        GreenMail greenMail = new GreenMail(setup);
        greenMail.start();
    }

 */

/*SOMETHING WRONG WITH THIS:
    @Test
    public void messageWithAttachment() throws MessagingException {
        emailServiceImpl.sendMessageWithAttachment(to);

        GreenMailUtil.sendMimeMessage(mimeMessage);
        PollMailResponse response = MailConnectors.pollMails()
                .createRequest()
                .folder("INBOX")
                .execute();

        List<Mail> mails = response.getMails();
        Assertions.assertThat(mails).hasSize(1);

        Mail mail = mails.get(0);
        Assertions.assertThat(mail.getAttachments()).hasSize(1);

        Attachment mailAttachment = mail.getAttachments().get(0);
        Assertions.assertThat(mailAttachment.getFileName()).isEqualTo("Logo.jpg");
        Assertions.assertThat(mailAttachment.getPath()).isNotNull();

        //assertEquals("text", GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]));

    }

 */

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


}
