package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.InvalidLoginCount;
import academy.everyonecodes.java.data.InvalidLoginCountRepository;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import academy.everyonecodes.java.security.BadCredentialsListener;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import com.sun.mail.util.MailConnectException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

	@Value("${constants.how-many-failed-logins-cause-email-warning}")
	private int MAX_FAILED_ATTEMPTS;

	@MockBean
	UserRepository userRepository;

	@MockBean
	InvalidLoginCountRepository invalidLoginCountRepository;

	@MockBean
	EmailServiceImpl emailService;

	@MockBean
	BadCredentialsListener badCredentialsListener;

	@Autowired
	LoginService loginService;

	@Test
	void increaseFailedAttempts_UserNotFound() {
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.empty());

		loginService.increaseFailedAttempts("Testy");

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verifyNoMoreInteractions(userRepository);
		Mockito.verifyNoInteractions(invalidLoginCountRepository, emailService, badCredentialsListener);
	}

	@Test
	void increaseFailedAttempts_UserFound_NoInvalidLoginCounter() {
		User testUser = new User();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		// "Testy", "test", "TestFirst", "TestLast", "TestCompany", null, null, null, null, null, "test@testmail.com", null, null, null);

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 0);

		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(invalidLoginCountRepository.save(testInvalidLoginCount)).thenReturn(testInvalidLoginCount);

		loginService.increaseFailedAttempts("Testy");

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 0));
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 1));

		Mockito.verifyNoMoreInteractions(invalidLoginCountRepository, userRepository);
		Mockito.verifyNoInteractions(emailService, badCredentialsListener);
	}

	@Test
	void increaseFailedAttempts_UserFound_FoundInvalidLoginCounter_DoesNotReach5() {
		User testUser = new User();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 3);

		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.of(testInvalidLoginCount));

		loginService.increaseFailedAttempts("Testy");

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 4));

		Mockito.verifyNoMoreInteractions(invalidLoginCountRepository, userRepository);
		Mockito.verifyNoInteractions(emailService, badCredentialsListener);
	}

	@Test
	void increaseFailedAttempts_UserFound_FoundInvalidLoginCounter_Reaches5() {
		User testUser = new User();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		testUser.setEmailAddress("test@testmail.com");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 4);

		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.of(testInvalidLoginCount));

		loginService.increaseFailedAttempts("Testy");

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 5));

		try {
			Mockito.verify(emailService).sendMessageWithAttachment("test@testmail.com");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		Mockito.verifyNoMoreInteractions(invalidLoginCountRepository, userRepository);
		Mockito.verifyNoInteractions(badCredentialsListener);
	}

	@Test
	void resetFailedAttemptsIfNecessary() {
	}
}