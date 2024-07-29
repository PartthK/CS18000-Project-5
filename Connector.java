import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project 05 -- Connector.java
 * <p>
 * starts a server thread every time a new client is made
 *
 * @author Group 2 - L32
 * @version December 9, 2023
 */

public class Connector {

    public static void main(String[] args) throws IOException {
        int portNumber = 4242;
        //Starts A serverSocket
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            //Checks for Client Connections
            while (true) {
                Socket clientSocket = serverSocket.accept();

                //Creates a thread for the connecting client
                Server clientHandler = new Server(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
    }
}
