package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ResourceInfo;
import academy.everyonecodes.java.data.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@ConfigurationProperties("webapptreelist")
public class WebAppTreeService {

	/*private Map<Integer, Employee> employees = new HashMap<>();

	private String[] getPageData() {
		String[] pageDate = {
				"1 Top",
				"2 Account 1",
				"3 Profile 1",
				"4 Search 1",

		};
	}*/

	/*public void writeNodeTree() {
		Node<String>
	}*/

	public String prepareWebAppTree() {
		return prepareOutput();
	}

	/*private final List<ResourceInfo> resourceInfos = List.of(
			new ResourceInfo("Account", "Edit Account", "Make changes in Profile", "/account/{username}", "PUT", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Account", "View Account", "View Account Info", "/account/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Volunteer", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"))),
			new ResourceInfo("Profile", "View Individual", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_INDIVIDUAL"))),
			new ResourceInfo("Profile", "View Company", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_COMPANY")))
	);*/

	private List<ResourceInfo> resourceinfos;
	/*= List.of(

			new ResourceInfo("Account", "View my Account", "View my Account Info", "/account/{username}", "GET", Set.of(new Role(
					"ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),

			new ResourceInfo("Account", "Edit my Account", "Make changes in my Account", "/account/{username}", "PUT", Set.of(new Role(
					"ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),

			new ResourceInfo("Account", "Add Skills", "Add skills to my Account", "/addSkill/{username}", "POST",
					Set.of(new Role("ROLE_VOLUNTEER"))),

			new ResourceInfo("Profile", "View Profile as seen by others", "View my Profile as seen by Others", "/profile/{username}", "GET"
					, Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),

			new ResourceInfo("Search", "Search for activities as Volunteer", "Search for Activities as Volunteer", "/search/{text}", "GET",
					Set.of(new Role("ROLE_VOLUNTEER"))),



			new ResourceInfo("Activities", "Create Activity", "Create a new Activity as Organizer", "/activities", "POST",
					Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),

			new ResourceInfo("Activities", "View all Activities", "View all my Activities as Organizer", "/activities", "GET",
					Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),

			new ResourceInfo("Activities", "Create Draft", "Create a new Draft as Organizer", "/drafts", "POST", Set.of(new Role("ROLE_INDIVIDUAL"),
					new Role("ROLE_COMPANY"))),

			new ResourceInfo("Activities", "View all Drafts", "View all my Drafts as Organizer", "/drafts", "GET", Set.of(new Role("ROLE_INDIVIDUAL")
					, new Role("ROLE_COMPANY"))),

			new ResourceInfo("Activities", "Edit Draft", "Edit created Draft as Organizer", "/drafts", "PUT", Set.of(new Role("ROLE_INDIVIDUAL"),
					new Role("ROLE_COMPANY"))),

			new ResourceInfo("Activities", "Save Draft as Activity", "Save my Draft as Activity as Organizer", "/drafts/{draftId}", "PUT",
					Set.of(new Role(
							"ROLE_INDIVIDUAL"),    new Role("ROLE_COMPANY"))),

			new ResourceInfo("Activities", "View all my Activities", "View all my Activities as Volunteer", "/{username}/activities", "GET",
					Set.of(new Role("ROLE_VOLUNTEER"))),

			new ResourceInfo("Activities", "View all my completed Activities", "View all my completed Activities as Volunteer",
					"/{username" +
							"}/activities/completed", "GET",
					Set.of(new Role("ROLE_VOLUNTEER"))),

			new ResourceInfo("Activities", "View all my pending Activities", "View all my pending Activities as Volunteer",
					"/{username" +
							"}/activities/pending", "GET",
					Set.of(new Role("ROLE_VOLUNTEER"))),

			new ResourceInfo("Activities", "View all my active Activities", "View all my active Activities as Volunteer",
					"/{username" +
							"}/activities/active", "GET",
					Set.of(new Role("ROLE_VOLUNTEER")))
	);*/

	public void setResourceinfos(List<ResourceInfo> resourceinfos) {
		this.resourceinfos = resourceinfos;
	}

	private String prepareOutput() {
		var output = new StringBuilder();
		Collection<? extends GrantedAuthority> authorities = getAuthorities();

		Map<String, List<ResourceInfo>> resourceMap = resourceinfos.stream()
				.filter(element -> checkIfAuthoritiesAreInAllowedRoles(element.getAllowedRoles(), authorities))
				.collect(groupingBy(ResourceInfo::getCategory));

		for (Map.Entry<String, List<ResourceInfo>> mapEntry : resourceMap.entrySet()) {
			output
					.append(mapEntry.getKey())
					.append("\n");
			for (ResourceInfo resourceInfo : mapEntry.getValue()) {
				output
						.append("    ")
						.append(resourceInfo.getName())
						.append("\n");
			}
		}
		return output.toString();
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthentication().getAuthorities();
	}

	private boolean checkIfAuthoritiesAreInAllowedRoles(Set<Role> allowedRoles, Collection<? extends GrantedAuthority> authorities) {
		List<String> authoritiesStringList = authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		List<String> allowedRolesStringList = allowedRoles.stream()
				.map(Role::getRole)
				.collect(Collectors.toList());
		return authoritiesStringList.stream().anyMatch(allowedRolesStringList::contains);
	}
}
