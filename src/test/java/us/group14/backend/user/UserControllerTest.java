package us.group14.backend.user;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import us.group14.backend.utils.TestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {TestUtils.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
//@WebMvcTest
@AllArgsConstructor
class UserControllerTest {
    @LocalServerPort private int port;

    @Autowired
    private TestUtils testUtils;

//    @Autowired
//    public UserControllerTest(TestUtils testUtils, TestRestTemplate restTemplate) {
////        this.userController = userController;
////        this.testUtils = testUtils;
////        this.restTemplate = restTemplate;
//    }

//    @Test
//    void contextLoads(ApplicationContext context) {
//        assertThat(context).isNotNull();
//    }

//    @AfterEach
//    void tearDown() {
////        testUtils.clearDatabase();
//    }
//
//    @Test
//    void getAllUsers() {
//        System.out.println("Hello");
//    }
//
//    @Test
//    void getUser() {
////        User testUser = testUtils.createTestUser();
////
////        ResponseEntity<Object> response = testUtils.request(restTemplate, testUtils.getLink(port, "/user"), HttpMethod.GET, testUtils.getHttpEntity(testUser));
////        System.out.println(response);
//    }
//
//    @Test
//    void addContact() {
//    }
//
//    @Test
//    void getContacts() {
//    }
//
//    @Test
//    void deleteContact() {
//    }
}