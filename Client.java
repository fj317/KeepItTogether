import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private Socket server;
    String address;
    int port;
    public Client(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
    }
    public String[] select(String userInput) throws IOException {
        try {
            server = new Socket(address,port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

            serverOut.println("0" + userInput);
            String res = serverIn.readLine();
            String [] resList;
            if(res.charAt(0) == '['){
                resList = stringToList(res);
            }
            else{
                resList = null;
            }

            return resList;

        } catch (IOException e) {
            return null;
        }
        finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean modify(String userInput){
        try {
            server = new Socket(address,port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            System.out.println(e);
        }
        try {
            System.out.println("1");
            PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
            System.out.println("2");
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
            System.out.println("3");

            serverOut.println("1" + userInput);
            String res = serverIn.readLine();
            System.out.println("4");
            if(res.equals("1")){
                return true;
            }
            else{
                return false;
            }

        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String[] stringToList(String string){
        ArrayList<String> arrayItems = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        for(int i = 0; i< string.length(); i++){
            if(string.charAt(i) != '['){
                if(string.charAt(i) == ',' || string.charAt(i) == ']'){
                    arrayItems.add(current.toString());
                    i = i + 1;
                    current = new StringBuilder();
                }
                else{
                    current.append(string.charAt(i));
                }
            }
        }
        String[] items = new String[arrayItems.size()];
        items = arrayItems.toArray(items);
        return items;
    }

}
