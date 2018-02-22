import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;

public class Controller {
    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    private Socket socket;
    private ObjectOutputStream coos;
    private ObjectInputStream cois;

    public void connect(){
        socket = null;
        coos = null;
        cois = null;
        try {
            socket = new Socket(HOST, PORT);
            new Thread(() -> {
                try {
                    coos = new ObjectOutputStream(socket.getOutputStream());
                    cois = new ObjectInputStream(socket.getInputStream());
                    while (true){}
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        cois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        coos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSendFile(ActionEvent actionEvent) {
        FileMessage fm = null;
        try {
            fm = new FileMessage(Paths.get("client/1.txt"));
            coos.writeObject(fm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnConnect(ActionEvent actionEvent) {
        connect();
    }
}
