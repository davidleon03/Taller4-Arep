package org.escuelaing.arep.taller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String... args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, URISyntaxException {

        Map<String, Method> methodsMap = new HashMap<String, Method>();
        String className = args[0];
        Class c = Class.forName(className);
        Method[] declaredMethods = c.getDeclaredMethods();
        for (Method m : declaredMethods) {
            System.out.println(m.getName());
            if (m.isAnnotationPresent(RequestMapping.class)) {
                try {
                    // java -cp .\target\classes\ org.escuelaing.arep.taller.App org.escuelaing.arep.taller.Controller
                    methodsMap.put(m.getAnnotation(RequestMapping.class).value(), m);
                    System.out.println("METODO " + m.getName() + " PATH --> " + m.getAnnotation(RequestMapping.class).value());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + getPort());
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            String possiblePath = null;
            String metodo = null;
            int cont = 0;
            boolean flag = true;
            while ((inputLine = in.readLine()) != null) {
                if (flag) {
                    flag = false;
                    metodo = inputLine.split(" ")[0];
                    String possiblePath2 = inputLine.split(" ")[0];
                    possiblePath = inputLine.split(" ")[1].substring(8);
                    System.out.println("PATH2--->" +possiblePath2);
                    System.out.println("PATH--->" + possiblePath);
                    System.out.println("METODO --->" + metodo);
                }
                if(cont == 0){
                    front(clientSocket);
                    cont +=1;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            if(metodo.equals("POST")){
                System.out.println("Entra al post");
                busqueda(clientSocket, possiblePath);
            }
            /*String content = "ERROR";
            if(methodsMap.containsKey(possiblePath)){
                content = (String) methodsMap.get(possiblePath).invoke(null);
            }
            outputLine = content;
            out.println(outputLine);*/
            out.close();
            in.close();
            clientSocket.close();
        }

        serverSocket.close();
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    public static void front(Socket clientSocket) throws IOException {
        String outputLine;
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n" + htmlForm();
        out.println(outputLine);
    }

    public static void busqueda(Socket clientSocket, String path1) throws IOException {
        String res;
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String path = path1.split(",")[1] + "" + path1.split(",")[0];
        File archivo = new File(path);
        System.out.println("PATH ----> "+path);
        String extencion = path.split("\\.")[1];
        System.out.println(path);
        System.out.println(extencion);
        if(path.split("\\.")[1].equals("png")){
            String extension = "PNG";
            BufferedImage image = ImageIO.read(archivo);
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream writeimg = new DataOutputStream(clientSocket.getOutputStream());
            ImageIO.write(image, extension, ArrBytes);
            writeimg.writeBytes("HTTP/1.1 200 OK \r\n" + "Content-Type: image/png\r\n" + "\r\n");
            writeimg.write(ArrBytes.toByteArray());
        } else if(path.split("\\.")[1].equals("html")){
            BufferedReader in_2 = new BufferedReader(new FileReader(archivo));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n" + lector(in_2);
            out.println(outputLine);
        }
        else if(path.split("\\.")[1].equals("js")){
            BufferedReader in_2 = new BufferedReader(new FileReader(archivo));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: application/javascript\r\n" + "\r\n" + lector(in_2);
            out.println(outputLine);
        }
        else{
            BufferedReader in_2 = new BufferedReader(new FileReader(archivo));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/css\r\n" + "\r\n" + lector(in_2);
            out.println(outputLine);
        }


        in.close();
    }

    public static StringBuilder lector(BufferedReader date) throws IOException {
        StringBuilder cadena = new StringBuilder();
        String line = null;
        while ((line = date.readLine()) != null) {
            //System.out.println("Imprime el Line "+line);
            cadena.append(line);
        }
        return cadena;
    }

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
                "        <form>\n" +
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
}


