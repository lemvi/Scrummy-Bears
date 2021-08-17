package academy.everyonecodes.java.data.DTOs;

import academy.everyonecodes.java.data.Role;

import java.util.Objects;
import java.util.Set;

public class IndividualProfileDTO extends ProfileDTO
{
    private String fullName;
    private int age;

    public IndividualProfileDTO(String username, String email, Set<Role> roles, String fullName)
    {
        super(username, email, roles);
        this.fullName = fullName;
    }

    public IndividualProfileDTO(String username, String postalCode, String city, String street, String streetNumber, String email, String telephoneNumber, String description, Set<Role> roles, double rating, String fullName, int age)
    {
        super(username, postalCode, city, street, streetNumber, email, telephoneNumber, description, roles, rating);
        this.fullName = fullName;
        this.age = age;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IndividualProfileDTO that = (IndividualProfileDTO) o;
        return age == that.age && Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fullName, age);
    }
}
