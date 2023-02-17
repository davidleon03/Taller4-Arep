package org.escuelaing.arep.taller;

import java.io.IOException;

public class App {
    static HttpServer httpServer = HttpServer.getInstance();
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        httpServer.run(args);
    }
}
