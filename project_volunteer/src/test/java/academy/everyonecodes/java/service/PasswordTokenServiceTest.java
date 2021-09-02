package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.PasswordToken;
import academy.everyonecodes.java.data.repositories.PasswordTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PasswordTokenServiceTest {

	@Autowired
	private PasswordTokenService passwordTokenService;

	@MockBean
	private PasswordTokenRepository passwordTokenRepository;


	@Test
	void save() {
		PasswordToken mockPasswordToken = Mockito.mock(PasswordToken.class);
		passwordTokenService.save(mockPasswordToken);

		Mockito.verify(passwordTokenRepository).save(mockPasswordToken);
		Mockito.verifyNoMoreInteractions(passwordTokenRepository);
	}

	@Test
	void findByToken() {
		String testToken = "testToken";
		passwordTokenService.findByToken(testToken);

		Mockito.verify(passwordTokenRepository).findByToken(testToken);
		Mockito.verifyNoMoreInteractions(passwordTokenRepository);
	}

	@Test
	void findByUser_emailAddressAndValidAndActive() {
		String username = "Testy";
		passwordTokenService.findByUser_usernameAndValidAndActive(username);

		Mockito.verify(passwordTokenRepository).findByUser_usernameAndValidAndActive(username, true, true);
		Mockito.verifyNoMoreInteractions(passwordTokenRepository);
	}

	@Test
	void findByUser_emailAddressAndValid() {
		String username = "Testy";
		passwordTokenService.findByUser_usernameAndValid(username);

		Mockito.verify(passwordTokenRepository).findByUser_usernameAndValid(username, true);
		Mockito.verifyNoMoreInteractions(passwordTokenRepository);
	}
}