package test1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test1Controller {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
