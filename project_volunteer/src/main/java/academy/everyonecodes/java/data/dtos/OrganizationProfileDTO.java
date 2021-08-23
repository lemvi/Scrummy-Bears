package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Role;

import java.util.Objects;
import java.util.Set;

public class OrganizationProfileDTO extends ProfileDTO
{
    private String organizationName;


    public OrganizationProfileDTO(String username, String email, Set<Role> roles, String organizationName)
    {
        super(username, email, roles);
        this.organizationName = organizationName;
    }

    public OrganizationProfileDTO(String username, String postalCode, String city, String street, String streetNumber, String email, String telephoneNumber, String description, Set<Role> roles, double rating, String organizationName)
    {
        super(username, postalCode, city, street, streetNumber, email, telephoneNumber, description, roles, rating);
        this.organizationName = organizationName;
    }

    public String getOrganizationName()
    {
        return organizationName;
    }

    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrganizationProfileDTO that = (OrganizationProfileDTO) o;
        return Objects.equals(organizationName, that.organizationName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), organizationName);
    }
}
