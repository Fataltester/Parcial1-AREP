/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arep.parcial1;
import java.net.*;
import java.io.*;
import java.util.LinkedList;
/**
 *
 * @author juan.mmendez
 */
public class HTTPFacade {
        private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https:localhost:36000";

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(37000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        
        Socket clientSocket = null;
        boolean running = true;
        while (running){
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine = null;
            boolean first = true;
            String reqUriString = null;
            while ((inputLine = in.readLine()) != null) {
                if (first) {
                    System.out.println("URI: " + inputLine);
                    reqUriString = inputLine.split(" ")[1];
                }
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
                first = false;

            }
            System.out.println("URI: " + reqUriString);
            if (reqUriString.startsWith("/Client")) {
                facade(reqUriString);
            } 
            out.println(outputLine);
            out.close();
            in.close();
        }
        
        clientSocket.close();
        serverSocket.close();

    }
    public static void facade(String path) throws IOException {
        
        URL obj = new URL(GET_URL+path);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
    }
    
}
