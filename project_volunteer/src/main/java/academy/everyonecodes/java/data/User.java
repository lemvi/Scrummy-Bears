package academy.everyonecodes.java.data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class User
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotEmpty
    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String username;            // possibilities min./max., no special chars

    @NotEmpty
    @Size(min=1, max=100, message = "Must have 1-30 characters")
    private String password;

    @NotEmpty
    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String firstNamePerson;

    @NotEmpty
    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String lastNamePerson;

    @Size(min=1, max=30, message = "Must have 1-30 characters")
    private String companyName;

    //@Size(min=10,max=10, message = "Must have format: DD/MM/YYYY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Size(min=1, max=10, message = "Must have 1-10 characters")
    private String postalCode;

    @Size(min=1, max=85, message = "Must have 1-85 characters")
    private String city;

    @Size(min=1, max=85, message = "Must have 1-20 characters")
    private String street;

    @Size(min=1, max=20, message = "Must have 1-20 characters")
    private String streetNumber;

    @NotEmpty
    @Size(min=6, max=40, message = "Must have 1-40 characters")
    @Email
    private String emailAddress;

    @Size(min=3, max=20, message = "Must have 1-20 characters")
    private String telephoneNumber;

    @Size(max=1000, message = "Maximum of 1000 characters")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User() {
    }

    public User(String username, String password, String firstNamePerson, String lastNamePerson, String companyName, LocalDate dateOfBirth, String postalCode, String city, String street, String streetNumber, String emailAddress, String telephoneNumber, String description, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstNamePerson = firstNamePerson;
        this.lastNamePerson = lastNamePerson;
        this.companyName = companyName;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
        this.description = description;
        this.roles = roles;
    }

    public User(String username, String firstNamePerson, String lastNamePerson, String companyName, LocalDate dateOfBirth, String description) {
        this.username = username;
        this.firstNamePerson = firstNamePerson;
        this.lastNamePerson = lastNamePerson;
        this.companyName = companyName;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
    }

    public User(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstNamePerson() {
        return firstNamePerson;
    }

    public String getLastNamePerson() {
        return lastNamePerson;
    }

    public String getCompanyName() {
        return companyName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstNamePerson(String firstNamePerson) {
        this.firstNamePerson = firstNamePerson;
    }

    public void setLastNamePerson(String lastNamePerson) {
        this.lastNamePerson = lastNamePerson;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getFirstNamePerson(), user.getFirstNamePerson()) && Objects.equals(getLastNamePerson(), user.getLastNamePerson()) && Objects.equals(getCompanyName(), user.getCompanyName()) && Objects.equals(getDateOfBirth(), user.getDateOfBirth()) && Objects.equals(getPostalCode(), user.getPostalCode()) && Objects.equals(getCity(), user.getCity()) && Objects.equals(getStreet(), user.getStreet()) && Objects.equals(getStreetNumber(), user.getStreetNumber()) && Objects.equals(getEmailAddress(), user.getEmailAddress()) && Objects.equals(getTelephoneNumber(), user.getTelephoneNumber()) && Objects.equals(getDescription(), user.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getFirstNamePerson(), getLastNamePerson(), getCompanyName(), getDateOfBirth(), getPostalCode(), getCity(), getStreet(), getStreetNumber(), getEmailAddress(), getTelephoneNumber(), getDescription());
    }
}
