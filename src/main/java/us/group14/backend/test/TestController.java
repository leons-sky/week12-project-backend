package us.group14.backend.test;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.group14.backend.annotations.ApiMapping;

@ApiMapping("/test")
@RestController
@AllArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping
    public ResponseEntity<Test> test() {
        Test test = new Test("Text");
        testRepository.save(test);
        return ResponseEntity.ok(test);
    }

}
