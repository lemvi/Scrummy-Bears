package academy.everyonecodes.java.data.DTOs;

import academy.everyonecodes.java.data.Role;

import java.util.Objects;
import java.util.Set;

public abstract class ProfileDTO
{
    private String username;
    private String postalCode;
    private String city;
    private String street;
    private String streetNumber;
    private String email;
    private String telephoneNumber;
    private String description;
    private Set<Role> roles;

    private double rating;

    public ProfileDTO()
    {
    }

    public ProfileDTO(String username, String email, Set<Role> roles)
    {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public ProfileDTO(String username, String postalCode, String city, String street, String streetNumber, String email, String telephoneNumber, String description, Set<Role> roles, double rating)
    {
        this.username = username;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
        this.roles = roles;
        this.rating = rating;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDTO that = (ProfileDTO) o;
        return Double.compare(that.rating, rating) == 0 && Objects.equals(username, that.username) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(streetNumber, that.streetNumber) && Objects.equals(email, that.email) && Objects.equals(telephoneNumber, that.telephoneNumber) && Objects.equals(description, that.description) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, postalCode, city, street, streetNumber, email, telephoneNumber, description, roles, rating);
    }
}
