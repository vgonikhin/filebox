import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8189;

    private ServerSocket serverSocket;

    public Server() {
        System.out.println("Server up");
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Awaiting connections");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler ch = new ClientHandler(this, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
