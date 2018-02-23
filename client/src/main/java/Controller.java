import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;

public class Controller {
    @FXML
    HBox authBox, commandBox;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    private boolean authorized;

    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    private Socket socket;
    private ObjectOutputStream coos;
    private ObjectInputStream cois;

    private void setAuthorized(boolean authorized) {
        this.authorized = authorized;
        commandBox.setVisible(authorized);
        commandBox.setManaged(authorized);
        authBox.setVisible(!authorized);
        authBox.setManaged(!authorized);
    }

    private void connect(){
        socket = null;
        coos = null;
        cois = null;
        try {
            socket = new Socket(HOST, PORT);
            new Thread(() -> {
                try {
                    coos = new ObjectOutputStream(socket.getOutputStream());
                    cois = new ObjectInputStream(socket.getInputStream());
                    while (true){
                        Object ro = cois.readObject();
                        if(ro instanceof CommandMessage){
                            CommandMessage cm = (CommandMessage) ro;
                            if(cm.getType() == CommandMessage.CM_AUTH_RIGHT){
                                setAuthorized(true);
                                break;
                            } else {
                                //wrong credentials message
                                break;
                            }
                        }
                    }
                    while (true){
                        //useful stuff
                        Object ro = cois.readObject();
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
        FileMessage fm;
        try {
            fm = new FileMessage(Paths.get("client/1.txt"));
            coos.writeObject(fm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnConnect(ActionEvent actionEvent) {
        if(socket == null || socket.isClosed()) connect();
        AuthMessage am;
        try {
            am = new AuthMessage(loginField.getText(),passwordField.getText());
            coos.writeObject(am);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void btnDisconnect(ActionEvent actionEvent) {
//        setAuthorized(false);
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            cois.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            coos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
