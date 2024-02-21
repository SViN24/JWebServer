package com.jwebserver.jwebserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class JWebServer {
    public static void main(String[] args){
        int port = 8080;

        runServer(port);
    }

    private static void runServer(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server listening to port " + port);

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                handleClientRequest(clientSocket);

                clientSocket.close();
                System.out.println("Client connection closed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket)throws IOException{
        OutputStream outputStream = clientSocket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream,true);

        File file = new File("site/index.html");
        if (file.exists())
        {
            FileInputStream FileInputStream = new FileInputStream(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(FileInputStream));

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            
            String line;
            while((line = reader.readLine()) != null)
            {
                out.println(line);
            }

            reader.close();
        } else {
            String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "404 Not Found: File not found";
            out.println(response);
        }
        out.close();

        outputStream.flush();
    }
}
