package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Role;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class VolunteerProfileDTO extends ProfileDTO
{
    private String fullName;
    private int age;
    private Optional<String> skill;

    public VolunteerProfileDTO(String username, String email, Set<Role> roles, String fullName, int age)
    {
        super(username, email, roles);
        this.fullName = fullName;
        this.age = age;
    }

    public VolunteerProfileDTO(String username, String postalCode, String city, String street, String streetNumber, String email, String telephoneNumber, String description, Set<Role> roles, double rating, String fullName, int age, Optional<String> skill)
    {
        super(username, postalCode, city, street, streetNumber, email, telephoneNumber, description, roles, rating);
        this.fullName = fullName;
        this.age = age;
        this.skill = skill;
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

    public Optional<String> getSkill()
    {
        return skill;
    }

    public void setSkill(Optional<String> skill)
    {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VolunteerProfileDTO that = (VolunteerProfileDTO) o;
        return age == that.age && Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fullName, age);
    }

    @Override
    public String toString() {
        return "VolunteerProfileDTO{" +
                "fullName='" + fullName + '\'' +
                ", age=" + age +
                ", skill=" + skill +
                '}';
    }
}
