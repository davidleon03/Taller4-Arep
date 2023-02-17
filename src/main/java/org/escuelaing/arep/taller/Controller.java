package org.escuelaing.arep.taller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
@Component
public class Controller {
    @RequestMapping("/prueba")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
