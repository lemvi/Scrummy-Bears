package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.PasswordDTO;
import academy.everyonecodes.java.service.ForgottenPasswordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ForgottenPasswordEndpointTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ForgottenPasswordService forgottenPasswordService;

	private final String url = "/forgottenpassword";


	@Test
	void requestPasswordResetForUsername() throws Exception {
		String testUsername = "Testy";
		String dynamicUrl = url + "/username";

		mockMvc.perform(post(dynamicUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUsername)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

		Mockito.verify(forgottenPasswordService).requestPasswordResetForUsername(Mockito.any(), Mockito.eq(testUsername));
		Mockito.verifyNoMoreInteractions(forgottenPasswordService);
	}

	@Test
	void activatePasswordToken() throws Exception {
		String testToken = "testtoken";
		String dynamicUrl = url + "/activate/" + testToken;
		mockMvc.perform(get(dynamicUrl)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(forgottenPasswordService).activatePasswordToken(testToken);
		Mockito.verifyNoMoreInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_Okay() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "Testy";
		String testNewPassword = "testPassword";
		String testNewPasswordRepeat = "testPassword";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfTestPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfTestPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(forgottenPasswordService).changePassword(testPasswordDTO);
		Mockito.verifyNoMoreInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_UsernameTooShort() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "";
		String testNewPassword = "testPassword";
		String testNewPasswordRepeat = "testPassword";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfMockPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfMockPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		Mockito.verifyNoInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_UsernameTooLong() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "ThisIsAFarTooLongUsernameInHere";
		String testNewPassword = "testPassword";
		String testNewPasswordRepeat = "testPassword";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfMockPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfMockPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		Mockito.verifyNoInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_PasswordTooShort() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "Testy";
		String testNewPassword = "";
		String testNewPasswordRepeat = "testPassword";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfMockPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfMockPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		Mockito.verifyNoInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_PasswordTooLong() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "Testy";
		String testNewPassword = "ThisIsAReallyFarTooLongPasswordInMyOpinionButWhatDontWeDoForTestingEverythingOfCourseForTheCodeToWork";
		String testNewPasswordRepeat = "testPassword";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfMockPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfMockPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		Mockito.verifyNoInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_RepeatPasswordTooShort() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "Testy";
		String testNewPassword = "testPassword";
		String testNewPasswordRepeat = "";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfMockPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfMockPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		Mockito.verifyNoInteractions(forgottenPasswordService);
	}

	@Test
	void changePassword_RepeatPasswordTooLong() throws Exception {
		String dynamicUrl = url + "/change";

		String testUsername = "Testy";
		String testNewPassword = "testPassword";
		String testNewPasswordRepeat = "ThisIsAReallyFarTooLongPasswordInMyOpinionButWhatDontWeDoForTestingEverythingOfCourseForTheCodeToWork";
		PasswordDTO testPasswordDTO = new PasswordDTO(testUsername, testNewPassword, testNewPasswordRepeat);

		String jsonOfMockPasswordDTO = getJsonOfPasswordDTO(testPasswordDTO);

		mockMvc.perform(post(dynamicUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonOfMockPasswordDTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		Mockito.verifyNoInteractions(forgottenPasswordService);
	}



	private String getJsonOfPasswordDTO(PasswordDTO passwordDTO) throws JsonProcessingException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(passwordDTO);
	}
}