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
            this.account = "client";
            new Thread(new Runnable(){
                public void run () {
                    try {
                        Object ro = sois.readObject();
                        if(ro instanceof AbstractMessage){
                            if(ro instanceof FileMessage){
                                FileMessage fm = (FileMessage)ro;
                                Files.write(Paths.get("server/repository/"+account+"/"+fm.getFilename()),fm.getData(), StandardOpenOption.CREATE);
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
                }
            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
