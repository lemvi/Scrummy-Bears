package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProfileDTOServiceTest
{
    @Autowired
    ProfileDTOService profileDTOService;

    @MockBean
    private UserToProfileDTOTranslator userToProfileDTOTranslator;


    @Test
    void get_user_not_null()
    {
        ProfileDTO profileDTO = new ProfileDTO(
            "name",
            "First Last",
            "company",
            69,
            "description"
        );
        User user = new User(
                "name",
                "First",
                "Last",
                "company",
                LocalDate.of(2021, 8, 2),
                "description"
        );
        Mockito.when(userToProfileDTOTranslator.toDTO(user))
                .thenReturn(profileDTO);
        var expected = profileDTO;
        var actual = profileDTOService.get(user);

        Assertions.assertEquals(expected, actual);
    }
}