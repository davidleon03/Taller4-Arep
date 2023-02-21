package org.escuelaing.arep.taller;

public class Controller {
    @RequestMapping("/search/png")
    public static String filePng(){
        return "HTTP/1.1 200 OK\r\n"
                + "Access-Control-Allow-Origin: *\r\n"
                + "Content-Type:image/png \r\n"
                + "\r\n";
    }

    @RequestMapping("/search/html")
    public static String fileHtml(){
        return "HTTP/1.1 200 OK\r\n"
                + "Access-Control-Allow-Origin: *\r\n"
                + "Content-Type:text/html \r\n"
                + "\r\n";
    }
    @RequestMapping("/search/css")
    public static String fileCss(){
        return "HTTP/1.1 200 OK\r\n"
                + "Access-Control-Allow-Origin: *\r\n"
                + "Content-Type:text/css \r\n"
                + "\r\n";
    }
    @RequestMapping("/search/js")
    public static String fileJs(){
        return "HTTP/1.1 200 OK\r\n"
                + "Access-Control-Allow-Origin: *\r\n"
                + "Content-Type:text/javascript \r\n"
                + "\r\n";
    }
}
