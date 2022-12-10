package test.custom.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/endpoint")
public class MyController {

    @GetMapping
    public Map<String, String> helloWorld() {
        return Map.of("hello", "world",
                "isVirtual", Boolean.toString(Thread.currentThread().isVirtual()));
    }

}
