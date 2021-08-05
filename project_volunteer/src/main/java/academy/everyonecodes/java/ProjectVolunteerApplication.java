package academy.everyonecodes.java;

import academy.everyonecodes.java.service.email.EmailService;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.MessagingException;

@SpringBootApplication
public class ProjectVolunteerApplication
{
	/*
	private static EmailService emailServiceImpl;

	public ProjectVolunteerApplication(EmailService emailServiceImpl) {
		this.emailServiceImpl = emailServiceImpl;
	}

	 */

	public static void main(String[] args) throws MessagingException {
		SpringApplication.run(ProjectVolunteerApplication.class, args);

		//emailServiceImpl.sendSimpleMessage("scrummybears08@gmail.com");
		//emailServiceImpl.sendMessageWithAttachment("scrummybears08@gmail.com");
	}
}
