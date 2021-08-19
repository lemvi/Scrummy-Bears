package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ResourceInfo;
import academy.everyonecodes.java.data.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
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

	public void writeThings() {
		System.out.println(getAuthorities());
	}

	private final List<ResourceInfo> resourceInfos = List.of(
			new ResourceInfo("Account", "Edit Account", "Make changes in Profile", "/account/{username}", "PUT", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Account", "View Account", "View Account Info", "/account/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))),
			new ResourceInfo("Profile", "View Profile", "View Profile as seen by others", "/profile/{username}", "GET", Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY")))
	);

	/*private String prepareOutput() {


		var x = resourceInfos.stream()
				.filter(element -> element.getAllowedRoles().stream()
						.map(e -> e.getRole())
	}*/

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthentication().getAuthorities();
	}

	private boolean checkIfAuthoritiesAreInAllowedRoles(List<Role> allowedRoles, Collection<? extends GrantedAuthority> authorities) {
		var x = authorities.stream()
				.map(e -> e.getAuthority())
				.collect(Collectors.toList());
		var y = allowedRoles.stream()
				.map(e -> e.getRole())
				.collect(Collectors.toList());
	}

}
