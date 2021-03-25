import org.sqlite.SQLiteException;

import javax.naming.InvalidNameException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    Connect dbConnection = new Connect();
    DateFormat df;

    public ClientHandler(Socket socket){
        this.clientSocket = socket;
        dbConnection.Setup();
        df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    }

    public void run(){
        try {
            InputStreamReader r = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader clientIn = new BufferedReader(r);
            PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
            while(true) {
                String userInput = clientIn.readLine();
                if (userInput == null){
                    clientSocket.close();
                }
                else{
                    Date dateobj = new Date();
                    System.out.println("[" + df.format(dateobj) + "] " + userInput);

                    if(userInput.charAt(0) == '0'){
                        ArrayList output = dbConnection.get(userInput.substring(1));
                        clientOut.println(output);
                    }
                    else if(userInput.charAt(0) == '1'){
                        String output = dbConnection.change(userInput.substring(1));
                        clientOut.println(output);
                    }
                    else{
                        clientOut.println("error");
                    }
                }

            }
        }catch(IOException e){
            System.out.println("Client disconnected");
        }catch(SQLException e){
            System.out.println("SQL error");
            System.out.println(e);
        }/*
        catch(NullPointerException e){
            System.out.println(e);
        }*/
        finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
