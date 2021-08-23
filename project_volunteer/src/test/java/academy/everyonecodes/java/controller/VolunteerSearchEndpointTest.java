package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VolunteerSearchEndpointTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ActivityService activityService;

	String url = "/volunteer/search/";

	@Test
	@WithMockUser(username = "Testy", password = "Irrelevant", authorities = {"ROLE_VOLUNTEER"})
	void getAllActivities_AccessAllowed() throws Exception {
		mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(activityService).getAllActivities();
		Mockito.verifyNoMoreInteractions(activityService);
	}

	@Test
	@WithMockUser(username = "Testy", password = "Irrelevant", authorities = {"ROLE_INDIVIDUAL"})
	void getAllActivities_AccessForbidden_Individual() throws Exception {
		mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());

		Mockito.verifyNoInteractions(activityService);
	}

	@Test
	@WithMockUser(username = "Testy", password = "Irrelevant", authorities = {"ROLE_COMPANY"})
	void getAllActivities_AccessForbidden_Company() throws Exception {
		mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());

		Mockito.verifyNoInteractions(activityService);
	}

	@Test
	@WithMockUser(username = "Testy", password = "Irrelevant", authorities = {"ROLE_DICTATOR"})
	void getAllActivities_AccessForbidden_DictatorOrFictional() throws Exception {
		mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());

		Mockito.verifyNoInteractions(activityService);
	}
}