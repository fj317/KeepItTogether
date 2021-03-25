import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ServerSocket serverSocket;
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while(true){
            System.out.println("listening");
            new ClientHandler(serverSocket.accept()).start();
        }
    }

}