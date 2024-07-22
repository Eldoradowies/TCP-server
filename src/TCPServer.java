import java.io.*; // Import classes for handling input and output
import java.net.ServerSocket; // Import the ServerSocket class
import java.net.Socket; // Import the Socket class

public class TCPServer {
    public static void main(String[] args) {
        int port = 65432; // The port number on which the server will listen for connections

        // Create a ServerSocket to listen on the specified port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP server is listening on port " + port);

            // Continuously listen for client connections
            while (true) {
                Socket socket = serverSocket.accept(); // Accept a new client connection
                System.out.println("New client connected");

                // Create a new thread to handle the client connection
                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

// Class to handle client connections in separate threads
class ServerThread extends Thread {
    private Socket socket;

    // Constructor to initialize the socket
    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    // The run method contains the code to be executed by the thread
    public void run() {
        // Try-with-resources to automatically close resources
        try (InputStream input = socket.getInputStream(); // Get the input stream from the socket
             BufferedReader reader = new BufferedReader(new InputStreamReader(input)); // Create a BufferedReader to read from the input stream
             OutputStream output = socket.getOutputStream(); // Get the output stream from the socket
             PrintWriter writer = new PrintWriter(output, true)) { // Create a PrintWriter to write to the output stream

            String text;
            // Continuously read messages from the client
            while ((text = reader.readLine()) != null) {
                System.out.println("Message received: " + text); // Print the received message to the console
                writer.println("Server: " + text); // Send a response back to the client
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close(); // Ensure the socket is closed when done
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
