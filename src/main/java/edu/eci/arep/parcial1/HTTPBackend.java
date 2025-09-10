/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arep.parcial1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static java.nio.file.Files.list;
import static java.util.Collections.list;
import java.util.LinkedList;

/**
 *
 * @author juan.mmendez
 */
public class HTTPBackend {
    private static LinkedList<Integer> numbers = new LinkedList<>();
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
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
        boolean running = true;
        while (running) {
            
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
            if (reqUriString.startsWith("/Backend/add")) {
                outputLine = addMethod(reqUriString, outputLine);
            } else if (reqUriString.startsWith("/Backend/list")) {
                outputLine = listMethod(outputLine);
            } else if (reqUriString.startsWith("/Backend/clear")) {
                outputLine = deleteMethod(outputLine);
            } else if (reqUriString.startsWith("/Backend/stats")) {

            }
            out.println(outputLine);
  
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();

    }
    
    private static String addMethod(String reqUri,String outputLine){
        String number = reqUri.split("=")[1];
        String[] numberList = number.split(",");
        int lenghtList = numberList.length;
        for(int i = 0; i < lenghtList; i++){
            String result = numberList[i];
            if(numberList[i].startsWith("[")){
                result = numberList[i].substring(0);
            }else if(numberList[i].endsWith("]")){
                result = numberList[i].substring(numberList[i].length()-1);
            }
            numbers.add(Integer.valueOf(result));
        }
        int count = numbers.size();
        outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/json \r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "added: " + number +"\r\n"
                    + "count: " + count +"\r\n"
                    + "}";
        return outputLine;
        
    }
    
    private static String listMethod(String outputLine){
        outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/json \r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "values: " + numbers +"\r\n"
                    + "}";
        return outputLine;
    }
    private static String deleteMethod(String outputLine){
        if(!numbers.isEmpty()){
            for (int i = 0; i < numbers.size(); i++) {
                numbers.remove();
            }
        }
        outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/json \r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "message: list_cleared \r\n"
                    + "}";
        return outputLine;
    }
}

