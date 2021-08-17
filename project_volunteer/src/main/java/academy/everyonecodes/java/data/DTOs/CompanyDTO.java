package academy.everyonecodes.java.data.DTOs;

import academy.everyonecodes.java.data.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

public class CompanyDTO
{
    @NotEmpty
    @Size(min = 1, max = 30, message = "Must have 1-30 characters")
    private String username;

    @NotEmpty
    @Size(min = 1, max = 100, message = "Must have 1-100 characters")
    private String password;

    @NotEmpty
    @Size(min = 1, max = 30, message = "Must have 1-30 characters")
    private String companyName;

    @Size(max = 10, message = "Must have 10 characters")
    private String postalCode;

    @Size(max = 85, message = "Must have 1-85 characters")
    private String city;

    @Size(max = 85, message = "Must have 1-85 characters")
    private String street;

    @Size(max = 20, message = "Must have 1-20 characters")
    private String streetNumber;

    @NotEmpty
    @Size(min = 6, max = 40, message = "Must have 6-40 characters")
    @Email
    private String emailAddress;

    @Size(min = 3, max = 20, message = "Must have 3-20 characters")
    private String telephoneNumber;

    @Size(max = 1000, message = "Maximum of 1000 characters")
    private String description;

    @NotEmpty
    private Set<Role> roles;

    public CompanyDTO()
    {
    }

    public CompanyDTO(String username, String password, String companyName, String emailAddress, Set<Role> roles)
    {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.emailAddress = emailAddress;
        this.roles = roles;
    }

    public CompanyDTO(String username, String password, String companyName, String postalCode, String city, String street, String streetNumber, String emailAddress, String telephoneNumber, String description, Set<Role> roles)
    {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
        this.roles = roles;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getStreetNumber()
    {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber)
    {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDTO that = (CompanyDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(companyName, that.companyName) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(streetNumber, that.streetNumber) && Objects.equals(emailAddress, that.emailAddress) && Objects.equals(telephoneNumber, that.telephoneNumber) && Objects.equals(description, that.description) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, password, companyName, postalCode, city, street, streetNumber, emailAddress, telephoneNumber, description, roles);
    }
}
