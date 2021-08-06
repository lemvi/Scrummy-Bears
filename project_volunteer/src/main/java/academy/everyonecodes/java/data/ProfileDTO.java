package academy.everyonecodes.java.data;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

public class ProfileDTO
{
    private String username;
    private String fullName;
    private String companyName;
    private Integer age;
    private String description;

    public ProfileDTO()
    {
    }

    public ProfileDTO(String username, String fullName, String companyName, Integer age, String description)
    {
        this.username = username;
        this.fullName = fullName;
        this.companyName = companyName;
        this.age = age;
        this.description = description;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDTO that = (ProfileDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(fullName, that.fullName) && Objects.equals(companyName, that.companyName) && Objects.equals(age, that.age) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, fullName, companyName, age, description);
    }
}
