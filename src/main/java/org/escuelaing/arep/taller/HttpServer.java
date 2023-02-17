package org.escuelaing.arep.taller;


import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import java.net.*;
import java.io.*;

public class HttpServer {
    public void run(String[] args) throws IOException, ClassNotFoundException {
        String classname = args[0];
        Class c = Class.forName(classname);
        Method[] methods = c.getMethods();
        for(Method m: methods){

            if(m.isAnnotationPresent(RequestMapping.class)){

            }
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        Socket clientSocket = null;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Title of the document</title>\n"
                    + "</head>"
                    + "<body>"
                    + "My Web Site"
                    + "</body>"
                    + "</html>" + inputLine;
            out.println(outputLine);

            out.close();
            in.close();
        }
        clientSocket.close();
        serverSocket.close();
    }

    /**
     * Funcion generada para realizar la busqueda de archivos
     *
     * @param clientSocket El socket creado
     * @return
     * @throws IOException Exception
     */
    public static void busqueda(Socket clientSocket, String path, String type) throws IOException {
        String res;
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String result = java.net.URLDecoder.decode(path, StandardCharsets.UTF_8);
        File archivo = new File(result);
        if (type.equals("ng")) {
            String extension = "PNG";
            BufferedImage image = ImageIO.read(archivo);
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream writeimg = new DataOutputStream(clientSocket.getOutputStream());
            ImageIO.write(image, extension, ArrBytes);
            writeimg.writeBytes("HTTP/1.1 200 OK \r\n" + "Content-Type: image/png\r\n" + "\r\n");
            writeimg.write(ArrBytes.toByteArray());
        } else if (type.equals("ml")) {
            BufferedReader in_2 = new BufferedReader(new FileReader(archivo));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n" + lector(in_2);
            out.println(outputLine);
        } else if (type.equals("js")) {
            BufferedReader in_2 = new BufferedReader(new FileReader(archivo));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: application/javascript\r\n" + "\r\n" + lector(in_2);
            out.println(outputLine);
        } else {
            BufferedReader in_2 = new BufferedReader(new FileReader(archivo));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/css\r\n" + "\r\n" + lector(in_2);
            out.println(outputLine);
        }

        in.close();
    }

    /**
     * Funcion para leer el archivo
     *
     * @param date
     * @return StringBuilder
     * @throws IOException
     */
    public static StringBuilder lector(BufferedReader date) throws IOException {
        StringBuilder cadena = new StringBuilder();
        String line = null;
        while ((line = date.readLine()) != null) {
            //System.out.println("Imprime el Line "+line);
            cadena.append(line);
        }
        return cadena;
    }

    /**
     * Funcion generada para almacenar el HTML
     *
     * @return String
     */
    public static String htmlForm() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "\n" +
                "        <h1>LECTOR DE ARCHIVOS</h1>\n" +
                "        <form action=\"/hellopost\">\n" +
                "            <label for=\"postname\">Ingrese de la sieguiente manera NOMBRE.(html, css, js, png),PATH</label><br><br><br>\n" +
                "            <input type=\"text\" id=\"postname\" name=\"name\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n" +
                "        </form>\n" +
                "        \n" +
                "        <div id=\"postrespmsg\"></div>\n" +
                "        \n" +
                "        <script>\n" +
                "            function loadPostMsg(name){\n" +
                "                let url = \"\" + postname.value;\n" +
                "\n" +
                "                fetch (url, {method: 'POST'})\n" +
                "                    .then(x => x.text())\n" +
                "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n" +
                "            }\n" +
                "        </script>\n" +
                "    </body>\n" +
                "</html>";
    }

    private static HttpServer instance;

    /**
     * Retorna la instancia del servidor, en caso de que no exista lo crea
     *
     * @return instancia del servidor
     */
    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }
}