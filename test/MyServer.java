package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private final int port;
    private final ClientHandler clientHandler;
    private ServerSocket serverSocket;
    private volatile boolean shouldRun;
    private Thread serverThread;

    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.shouldRun = true;
    }

    public void start() {
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (shouldRun) {
                    Socket socket = serverSocket.accept();
                    clientHandler.handleClient(socket.getInputStream(), socket.getOutputStream());
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        
        // הוספת timeout
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1000 מילישניות = 1 שנייה
                close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void close() {
        shouldRun = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // המתנה לסיום הטרייד של הסרבר לפני החזרת הפונקציה
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
