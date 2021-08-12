package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.ProfileDTOService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProfileDTOEndpointTest {

	@Autowired
	TestRestTemplate testRestTemplate;

	@MockBean
	private ProfileDTOService profileDTOService;

	@Autowired
	private MockMvc mvc;

	String url = "/profile/";

	@Test
	@WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
	void getProfile() throws Exception {
		String username = "Test";
		String dynamicUrl = url + username;

		mvc.perform(get(dynamicUrl)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(profileDTOService).get(username);
		Mockito.verifyNoMoreInteractions(profileDTOService);
	}

	@Test
	@WithMockUser(username = "test", password = "test", authorities = {})
	void getProfile_UnauthorizedUser() throws Exception {
		String username = "Test";
		String dynamicUrl = url + username;

		mvc.perform(get(dynamicUrl)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());

		Mockito.verifyNoInteractions(profileDTOService);
	}
}