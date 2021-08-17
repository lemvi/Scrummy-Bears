package academy.everyonecodes.java.data.DTOs;

import academy.everyonecodes.java.data.Role;

import java.util.Objects;
import java.util.Set;

public class CompanyProfileDTO extends ProfileDTO
{
    private String companyName;


    public CompanyProfileDTO(String username, String email, Set<Role> roles, String companyName)
    {
        super(username, email, roles);
        this.companyName = companyName;
    }

    public CompanyProfileDTO(String username, String postalCode, String city, String street, String streetNumber, String email, String telephoneNumber, String description, Set<Role> roles, double rating, String companyName)
    {
        super(username, postalCode, city, street, streetNumber, email, telephoneNumber, description, roles, rating);
        this.companyName = companyName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyProfileDTO that = (CompanyProfileDTO) o;
        return Objects.equals(companyName, that.companyName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), companyName);
    }
}
