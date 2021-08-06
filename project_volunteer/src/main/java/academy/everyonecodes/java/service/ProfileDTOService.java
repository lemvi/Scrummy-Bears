package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

@Service
public class ProfileDTOService
{
    private final UserToProfileDTOTranslator userToProfileDTOTranslator;

    public ProfileDTOService(UserToProfileDTOTranslator userToProfileDTOTranslator)
    {
        this.userToProfileDTOTranslator = userToProfileDTOTranslator;
    }

    public ProfileDTO get(User user)
    {
        return userToProfileDTOTranslator.toDTO(user);
    }
}
