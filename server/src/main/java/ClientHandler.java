import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientHandler {
    private Server server;
    private Socket socket;

    private ObjectOutputStream soos;
    private ObjectInputStream sois;

    private String account;

    public ClientHandler(Server server, Socket socket) {

        try {
            this.server = server;
            this.socket = socket;
            this.soos = new ObjectOutputStream(socket.getOutputStream());
            this.sois = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        Object ro = sois.readObject();
                        if (ro instanceof AuthMessage) {
                            AuthMessage am = (AuthMessage)ro;
                            if(am.getLogin().equals("client")&&am.getPassword().equals("password")){
                                this.account = "client";
                                soos.writeObject(new CommandMessage(CommandMessage.CM_AUTH_RIGHT));
                                System.out.println("Client connected");
                                break;
                            } else {
                                soos.writeObject(new CommandMessage(CommandMessage.CM_AUTH_WRONG));
                                break;
                            }
                        }
                    }
                    while (true) {
                        Object ro = sois.readObject();
                        if (ro instanceof AbstractMessage) {
                            if (ro instanceof FileMessage) {
                                FileMessage fm = (FileMessage) ro;
                                Files.write(Paths.get("server/repository/" + account + "/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                                System.out.println("File received");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        sois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        soos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
