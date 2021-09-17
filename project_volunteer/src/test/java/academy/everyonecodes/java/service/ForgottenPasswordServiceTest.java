package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ForgottenPasswordMessages;
import academy.everyonecodes.java.data.PasswordToken;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserEntityBuilder;
import academy.everyonecodes.java.data.dtos.PasswordDTO;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ForgottenPasswordServiceTest {

	@Autowired
	private ForgottenPasswordService forgottenPasswordService;

	@MockBean
	private UserService userService;

	@MockBean
	private EmailServiceImpl emailService;

	@MockBean
	private PasswordTokenService passwordTokenService;

	@MockBean
	private ForgottenPasswordMessages forgottenPasswordMessages;

	private final String emailSubject = "Email Subject";
	private final String emailText = "Email Text";
	private final Long expiryTimeInMinutes = 2L;



	@Test
	void requestPasswordResetForUsername_UserWithNameExists_NoOldTokens() throws MalformedURLException {
		String testUsername = "Testy";
		String testEmailAddress = "test@testmail.test";
		String expectedMessage = "success";
		User testUser = new UserEntityBuilder().createUser();
		testUser.setUsername(testUsername);
		testUser.setEmailAddress(testEmailAddress);
		HttpServletRequest mockRequest = new MockHttpServletRequest();

		Mockito.when(userService.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
		Mockito.when(forgottenPasswordMessages.getResetEmailSuccessfully()).thenReturn(expectedMessage);
		Mockito.when(passwordTokenService.findByUser_usernameAndValid(testUsername)).thenReturn(List.of());

		String resultingMessage = forgottenPasswordService.requestPasswordResetForUsername(mockRequest, testUsername);

		Mockito.verify(userService).findByUsername(testUsername);
		Mockito.verify(passwordTokenService).save(Mockito.any(PasswordToken.class));
		Mockito.verify(passwordTokenService).findByUser_usernameAndValid(testUsername);
		Mockito.verify(emailService).sendSimpleMessage(Mockito.eq(testUser.getEmailAddress()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(forgottenPasswordMessages).getResetEmailSuccessfully();
		Mockito.verifyNoMoreInteractions(userService, passwordTokenService, emailService, forgottenPasswordMessages);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}

	@Test
	void requestPasswordResetForUsername_UserWithNameExists_OneOldToken() throws MalformedURLException {
		String testUsername = "Testy";
		String testEmailAddress = "test@testmail.test";
		String expectedMessage = "success";
		User testUser = new UserEntityBuilder().createUser();
		testUser.setUsername(testUsername);
		testUser.setEmailAddress(testEmailAddress);
		HttpServletRequest mockRequest = new MockHttpServletRequest();

		Mockito.when(userService.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
		Mockito.when(forgottenPasswordMessages.getResetEmailSuccessfully()).thenReturn(expectedMessage);
		Mockito.when(passwordTokenService.findByUser_usernameAndValid(testUsername)).thenReturn(List.of(new PasswordToken()));

		String resultingMessage = forgottenPasswordService.requestPasswordResetForUsername(mockRequest, testUsername);

		Mockito.verify(userService).findByUsername(testUsername);
		Mockito.verify(passwordTokenService).findByUser_usernameAndValid(testUsername);
		Mockito.verify(passwordTokenService, Mockito.times(2)).save(Mockito.any(PasswordToken.class));
		Mockito.verify(emailService).sendSimpleMessage(Mockito.eq(testUser.getEmailAddress()), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(forgottenPasswordMessages).getResetEmailSuccessfully();
		Mockito.verifyNoMoreInteractions(userService, passwordTokenService, emailService, forgottenPasswordMessages);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}

	@Test
	void requestPasswordResetForUsername_NoUserWithNameExists() throws MalformedURLException {
		String testUsername = "Testy";
		String testEmailAddress = "test@testmail.test";
		String expectedMessage = "failure";
		User testUser = new UserEntityBuilder().createUser();
		testUser.setUsername(testUsername);
		testUser.setEmailAddress(testEmailAddress);
		HttpServletRequest mockRequest = new MockHttpServletRequest();

		Mockito.when(userService.findByUsername(testUsername)).thenReturn(Optional.empty());
		Mockito.when(forgottenPasswordMessages.getResetEmailNoUser()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.requestPasswordResetForUsername(mockRequest, testUsername);

		Mockito.verify(userService).findByUsername(testUsername);
		Mockito.verify(forgottenPasswordMessages).getResetEmailNoUser();
		Mockito.verifyNoMoreInteractions(userService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(passwordTokenService, emailService);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}



	@Test
	void activatePasswordToken_TokenFound_TokenValidAndYoungEnough() {
		String testToken = "testToken";
		User testUser = Mockito.mock(User.class);
		PasswordToken testPasswordToken = new PasswordToken(testToken, testUser, LocalDateTime.now());
		String expectedMessage = "success";

		Mockito.when(passwordTokenService.findByToken(testToken)).thenReturn(Optional.of(testPasswordToken));
		Mockito.when(forgottenPasswordMessages.getActivateTokenSuccessfully()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.activatePasswordToken(testToken);

		Mockito.verify(passwordTokenService).findByToken(testToken);
		Mockito.verify(passwordTokenService).save(testPasswordToken);
		Mockito.verify(forgottenPasswordMessages).getActivateTokenSuccessfully();
		Mockito.verifyNoMoreInteractions(passwordTokenService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(userService, emailService);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}

	@Test
	void activatePasswordToken_TokenFound_TokenInvalid() {
		String testToken = "testToken";
		User testUser = Mockito.mock(User.class);
		PasswordToken testPasswordToken = new PasswordToken(testToken, testUser, LocalDateTime.now());
		testPasswordToken.setValid(false);
		String expectedMessage = "Invalid";

		Mockito.when(passwordTokenService.findByToken(testToken)).thenReturn(Optional.of(testPasswordToken));
		Mockito.when(forgottenPasswordMessages.getActivateTokenInvalid()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.activatePasswordToken(testToken);

		Mockito.verify(passwordTokenService).findByToken(testToken);
		Mockito.verify(forgottenPasswordMessages).getActivateTokenInvalid();
		Mockito.verifyNoMoreInteractions(passwordTokenService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(userService, emailService);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}

	@Test
	void activatePasswordToken_TokenFound_TokenTooOld() {
		String testToken = "testToken";
		User testUser = Mockito.mock(User.class);
		LocalDateTime tooOldLocalDateTime = LocalDateTime.of(2021, 9, 1, 1, 12);
		PasswordToken testPasswordToken = new PasswordToken(testToken, testUser, tooOldLocalDateTime);
		String expectedMessage = "Invalid";

		Mockito.when(passwordTokenService.findByToken(testToken)).thenReturn(Optional.of(testPasswordToken));
		Mockito.when(forgottenPasswordMessages.getActivateTokenInvalid()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.activatePasswordToken(testToken);

		Mockito.verify(passwordTokenService).findByToken(testToken);
		Mockito.verify(forgottenPasswordMessages).getActivateTokenInvalid();
		Mockito.verifyNoMoreInteractions(passwordTokenService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(userService, emailService);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}

	@Test
	void activatePasswordToken_NoTokenFound() {
		String testToken = "testToken";
		String expectedMessage = "Failure";

		Mockito.when(passwordTokenService.findByToken(testToken)).thenReturn(Optional.empty());
		Mockito.when(forgottenPasswordMessages.getActivateTokenFailure()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.activatePasswordToken(testToken);

		Mockito.verify(passwordTokenService).findByToken(testToken);
		Mockito.verify(forgottenPasswordMessages).getActivateTokenFailure();
		Mockito.verifyNoMoreInteractions(passwordTokenService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(userService, emailService);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}



	@Test
	void changePassword_TokenFound_PasswordsMatch() {
		String testUsername = "Testy";
		String testPassword = "password";
		String testPasswordRepeat = "password";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testPassword, testPasswordRepeat);
		String testToken = "testToken";
		User testUser = Mockito.mock(User.class);
		PasswordToken testPasswordToken = new PasswordToken(testToken, testUser, LocalDateTime.now());
		String expectedMessage = "PasswordChanged";

		Mockito.when(passwordTokenService.findByUser_usernameAndValidAndActive(testUsername)).thenReturn(Optional.of(testPasswordToken));
		Mockito.when(forgottenPasswordMessages.getChangePasswordSuccessfully()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.changePassword(testPasswordDTO);

		Mockito.verify(passwordTokenService).findByUser_usernameAndValidAndActive(testUsername);
		Mockito.verify(userService).validateAndSaveUser(testUser);
		Mockito.verify(passwordTokenService).save(testPasswordToken);
		Mockito.verify(forgottenPasswordMessages).getChangePasswordSuccessfully();
		Mockito.verifyNoMoreInteractions(passwordTokenService, userService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(emailService);
	}

	@Test
	void changePassword_TokenFound_PasswordsDifferent() {
		String testUsername = "Testy";
		String testPassword = "password";
		String testPasswordRepeat = "somethingelse";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testPassword, testPasswordRepeat);
		String testToken = "testToken";
		User testUser = Mockito.mock(User.class);
		PasswordToken testPasswordToken = new PasswordToken(testToken, testUser, LocalDateTime.now());
		String expectedMessage = "PasswordChanged";

		Mockito.when(passwordTokenService.findByUser_usernameAndValidAndActive(testUsername)).thenReturn(Optional.of(testPasswordToken));
		Mockito.when(forgottenPasswordMessages.getChangePasswordNotEnteredSameTwice()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.changePassword(testPasswordDTO);

		Mockito.verify(passwordTokenService).findByUser_usernameAndValidAndActive(testUsername);
		Mockito.verify(forgottenPasswordMessages).getChangePasswordNotEnteredSameTwice();
		Mockito.verifyNoMoreInteractions(passwordTokenService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(userService, emailService);
	}

	@Test
	void changePassword_NoTokenFound() {
		String testUsername = "Testy";
		String testPassword = "password";
		String testPasswordRepeat = "password";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testPassword, testPasswordRepeat);
		String expectedMessage = "NoValidToken";

		Mockito.when(passwordTokenService.findByUser_usernameAndValidAndActive(testUsername)).thenReturn(Optional.empty());
		Mockito.when(forgottenPasswordMessages.getChangePasswordNoValidToken()).thenReturn(expectedMessage);

		String resultingMessage = forgottenPasswordService.changePassword(testPasswordDTO);

		Mockito.verify(passwordTokenService).findByUser_usernameAndValidAndActive(testUsername);
		Mockito.verify(forgottenPasswordMessages).getChangePasswordNoValidToken();
		Mockito.verifyNoMoreInteractions(passwordTokenService, forgottenPasswordMessages);
		Mockito.verifyNoInteractions(userService, emailService);

		Assertions.assertEquals(expectedMessage, resultingMessage);
	}
}