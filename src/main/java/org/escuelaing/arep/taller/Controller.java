package org.escuelaing.arep.taller;

public class Controller {
    @RequestMapping("/hello")
    public static String helloWorld(){
        return "Hello World";
    }

    @RequestMapping("/status")
    public static String serverStatus(){
        return "Running";
    }
}
