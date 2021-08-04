package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    @Test
    void saveUser() {
        User user = new User();

        restTemplate.postForObject("/register", user , User.class);
        Mockito.verify(userService)
                .save(user);
    }
}
