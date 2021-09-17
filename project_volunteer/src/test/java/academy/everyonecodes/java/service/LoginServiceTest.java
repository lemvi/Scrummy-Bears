package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.InvalidLoginCount;
import academy.everyonecodes.java.data.UserEntityBuilder;
import academy.everyonecodes.java.data.repositories.InvalidLoginCountRepository;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.security.UserPrincipal;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import javax.mail.MessagingException;
import java.util.Collection;
import java.util.Optional;

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

	@Autowired
	LoginService loginService;


	// Test Classes for Failed Authentication
	Authentication testFailedAuthentication = new Authentication() {
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public Object getDetails() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			return "Testy";
		}

		@Override
		public boolean isAuthenticated() {
			return false;
		}

		@Override
		public void setAuthenticated(boolean b) throws IllegalArgumentException {

		}

		@Override
		public String getName() {
			return null;
		}
	};

	AuthenticationException authenticationException = new BadCredentialsException("Test Fail Message");

	AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent = new AuthenticationFailureBadCredentialsEvent(testFailedAuthentication, authenticationException);


	// Test Classes for Successful Authentication
	Authentication testSuccessAuthentication = new Authentication() {
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public Object getDetails() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			return new UserPrincipal(new UserEntityBuilder().setUsername("Testy").setPassword("testPassword").setFirstNamePerson(null).setLastNamePerson(null).setOrganizationName(null).setDateOfBirth(null).setPostalCode(null).setCity(null).setStreet(null).setStreetNumber(null).setEmailAddress("test@testmail.com").setTelephoneNumber(null).setDescription(null).setRoles(null).createUser());
		}

		@Override
		public boolean isAuthenticated() {
			return false;
		}

		@Override
		public void setAuthenticated(boolean b) throws IllegalArgumentException {

		}

		@Override
		public String getName() {
			return null;
		}
	};

	AuthenticationSuccessEvent authenticationSuccessEvent = new AuthenticationSuccessEvent(testSuccessAuthentication);



	@Test
	void runFailedAuthenticationProcedure_UserNotFound() {

		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.empty());

		loginService.runFailedAuthenticationProcedure(authenticationFailureBadCredentialsEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verifyNoMoreInteractions(userRepository);
		Mockito.verifyNoInteractions(invalidLoginCountRepository, emailService);
	}



	@Test
	void runFailedAuthenticationProcedure_UserFound_NoInvalidLoginCounter() {

		User testUser = new UserEntityBuilder().createUser();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		// "Testy", "test", "TestFirst", "TestLast", "TestOrganization", null, null, null, null, null, "test@testmail.com", null, null, null);

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 0);


		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(invalidLoginCountRepository.save(testInvalidLoginCount)).thenReturn(testInvalidLoginCount);

		loginService.runFailedAuthenticationProcedure(authenticationFailureBadCredentialsEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 0));
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 1));

		Mockito.verifyNoMoreInteractions(invalidLoginCountRepository, userRepository);
		Mockito.verifyNoInteractions(emailService);
	}



	@Test
	void runFailedAuthenticationProcedure_UserFound_FoundInvalidLoginCounter_DoesNotReach5() {

		User testUser = new UserEntityBuilder().createUser();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 3);


		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.of(testInvalidLoginCount));

		loginService.runFailedAuthenticationProcedure(authenticationFailureBadCredentialsEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 4));

		Mockito.verifyNoMoreInteractions(invalidLoginCountRepository, userRepository);
		Mockito.verifyNoInteractions(emailService);
	}



	@Test
	void runFailedAuthenticationProcedure_UserFound_FoundInvalidLoginCounter_Reaches5() {
		User testUser = new UserEntityBuilder().createUser();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		testUser.setEmailAddress("test@testmail.com");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 4);


		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.of(testInvalidLoginCount));

		loginService.runFailedAuthenticationProcedure(authenticationFailureBadCredentialsEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 5));

		try {
			Mockito.verify(emailService).sendMessageWithAttachment("test@testmail.com", "Incorrect password for 5 times", "Was this you? Someone tried to login with an incorrect password for 5 times. Please reset your Password.", "project_volunteer/src/main/resources/Scrummy Bears Logo.jpg");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		Mockito.verifyNoMoreInteractions(invalidLoginCountRepository, userRepository);
	}



	@Test
	void resetFailedAttemptsIfNecessary_ResetNecessary() {
		User testUser = new UserEntityBuilder().createUser();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		testUser.setEmailAddress("test@testmail.com");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 4);
		InvalidLoginCount testInvalidLoginCountReset = new InvalidLoginCount(testUser, 0);


		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.of(testInvalidLoginCount));
		Mockito.when(invalidLoginCountRepository.save(new InvalidLoginCount(testUser, 0))).thenReturn(testInvalidLoginCountReset);

		loginService.runSuccessfulAuthenticationProcedure(authenticationSuccessEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 0));

		Mockito.verifyNoMoreInteractions(userRepository, invalidLoginCountRepository);
	}

	@Test
	void resetFailedAttemptsIfNecessary_ResetNotNecessary_NoLoginCount() {
		User testUser = new UserEntityBuilder().createUser();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		testUser.setEmailAddress("test@testmail.com");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 0);


		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(invalidLoginCountRepository.save(new InvalidLoginCount(testUser, 0))).thenReturn(testInvalidLoginCount);

		loginService.runSuccessfulAuthenticationProcedure(authenticationSuccessEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);
		Mockito.verify(invalidLoginCountRepository).save(new InvalidLoginCount(testUser, 0));

		Mockito.verifyNoMoreInteractions(userRepository, invalidLoginCountRepository);
	}

	@Test
	void resetFailedAttemptsIfNecessary_ResetNotNecessary_LoginCountIsZero() {
		User testUser = new UserEntityBuilder().createUser();
		testUser.setId(1L);
		testUser.setUsername("Testy");
		testUser.setPassword("test");
		testUser.setEmailAddress("test@testmail.com");

		InvalidLoginCount testInvalidLoginCount = new InvalidLoginCount(testUser, 0);


		// Test Logic
		Mockito.when(userRepository.findByUsername("Testy")).thenReturn(Optional.of(testUser));
		Mockito.when(invalidLoginCountRepository.findById(1L)).thenReturn(Optional.of(testInvalidLoginCount));

		loginService.runSuccessfulAuthenticationProcedure(authenticationSuccessEvent);

		Mockito.verify(userRepository).findByUsername("Testy");
		Mockito.verify(invalidLoginCountRepository).findById(1L);

		Mockito.verifyNoMoreInteractions(userRepository, invalidLoginCountRepository);
	}
}