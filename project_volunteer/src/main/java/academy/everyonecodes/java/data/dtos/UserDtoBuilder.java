package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Role;

import java.util.Set;

public interface UserDtoBuilder {
    UserDtoBuilder setUsername(String username);

    UserDtoBuilder setPassword(String password);

    UserDtoBuilder setPostalCode(String postalCode);

    UserDtoBuilder setCity(String city);

    UserDtoBuilder setStreet(String street);

    UserDtoBuilder setStreetNumber(String streetNumber);

    UserDtoBuilder setEmailAddress(String emailAddress);

    UserDtoBuilder setTelephoneNumber(String telephoneNumber);

    UserDtoBuilder setDescription(String description);

    UserDtoBuilder setRoles(Set<Role> roles);
}
