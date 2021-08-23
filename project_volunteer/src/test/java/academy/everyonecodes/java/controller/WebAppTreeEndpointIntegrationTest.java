package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.ResourceInfo;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.service.WebAppTreeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebAppTreeEndpointIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@SpyBean
	private WebAppTreeService webAppTreeService;

	private final String url = "/webapptree";

	private final List<ResourceInfo> testResourceInfos = List.of(
			new ResourceInfo("Profile", "View Volunteer", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"))),
			new ResourceInfo("Profile", "View Individual", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_INDIVIDUAL"))),
			new ResourceInfo("Profile", "View Company", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_COMPANY")))
	);



	@Test
	@WithMockUser(username = "Testy", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
	void getWebAppTree_AsIndividual() throws Exception {
		String expected = "Profile" + "\n" + "    " + "View Individual" + "\n";
		var result = mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String resultString = result.getResponse().getContentAsString();
		Assertions.assertEquals(expected, resultString);
	}

	@Test
	@WithMockUser(username = "test", password = "pw", authorities = {"ROLE_VOLUNTEER"})
	void getWebAppTree_AsVolunteer() throws Exception {
		String expected = "Profile" + "\n" + "    " + "View Volunteer" + "\n";
		var result = mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String resultString = result.getResponse().getContentAsString();
		Assertions.assertEquals(expected, resultString);
	}

	@Test
	@WithMockUser(username = "Test", password = "test", authorities = {"ROLE_COMPANY"})
	void getWebAppTree_AsCompany() throws Exception {
		String expected = "Profile" + "\n" + "    " + "View Company" + "\n";
		var result = mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String resultString = result.getResponse().getContentAsString();
		Assertions.assertEquals(expected, resultString);
	}

	@Test
	@WithMockUser(username = "Testy", password = "pw", authorities = {"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL"})
	void getWebAppTree_AsIndividualAndVolunteer() throws Exception {
		String expected = "Profile" + "\n" + "    " + "View Volunteer" + "\n" + "    " + "View Individual" + "\n";
		var result = mockMvc.perform(get(url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String resultString = result.getResponse().getContentAsString();
		Assertions.assertEquals(expected, resultString);
	}
}