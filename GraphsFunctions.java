
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Graphs {

    Client dbConnection;
    String[] users;

    public Graphs(){
        try {
            dbConnection = new Client("86.9.93.210", 58934);
        }catch (IOException e){
            System.out.println("There was a problem with the database");
        }
    }


    public void getClients(int houseId){
        try {
            users = dbConnection.select("SELECT user_id FROM HouseUsers WHERE house_id = " + houseId);
        }catch (IOException e){
            System.out.println("There was a problem with the database or the houseId does not exist");

        }
    }
    public Map<String, Float> transactionsGraphsData(int time){
        Map<String, Float> dictionary = new HashMap<String, Float>();
        LocalDate date = LocalDate.now().minusDays(time);
        for(String user : users){
            try {
                String[] transactions = dbConnection.select("SELECT price FROM Transactions WHERE date > '" + date.toString() + "' AND user_id = " + user);
                float total = 0;
                for(String price: transactions){
                    if(!price.equals("")) {
                        total = total + Float.parseFloat(price);
                    }
                }
                dictionary.put(user, total);
            }catch(IOException e){
                System.out.println("There was a problem with the database");
            }
        }
        return dictionary;
    }

    public Map<String, Integer> choresGraphsData(int time){
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        LocalDate date = LocalDate.now().minusDays(time);
        for(String user : users){
            try {
                String[] chores = dbConnection.select("SELECT COUNT(chore_id) FROM ChoreRecords WHERE date >" + date.toString() + "AND user_id = " + user);
                int total = chores.length;
                dictionary.put(user, total);
            }catch(IOException e){
                System.out.println("There was a problem with the database");
            }
        }
        return dictionary;
    }
    
}
