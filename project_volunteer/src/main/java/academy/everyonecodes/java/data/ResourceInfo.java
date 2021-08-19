package academy.everyonecodes.java.data;

import java.util.Set;

public class ResourceInfo {

	private String category;
	private String name;
	private String description;
	private String uri;
	private String restMethod;
	private Set<Role> allowedRoles;


	public ResourceInfo(String category, String name, String description, String uri, String restMethod, Set<Role> allowedRoles) {
		this.category = category;
		this.name = name;
		this.description = description;
		this.uri = uri;
		this.restMethod = restMethod;
		this.allowedRoles = allowedRoles;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getRestMethod() {
		return restMethod;
	}

	public void setRestMethod(String restMethod) {
		this.restMethod = restMethod;
	}

	public Set<Role> getAllowedRoles() {
		return allowedRoles;
	}

	public void setAllowedRoles(Set<Role> allowedRoles) {
		this.allowedRoles = allowedRoles;
	}
}
