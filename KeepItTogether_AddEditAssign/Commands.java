import java.io.IOException;
import java.util.Arrays;

public class Commands {

    public void delete(int chore_id) throws IOException {
        Client dbConnection = new Client("86.8.35.12", 58934);
        String[] chore = dbConnection.select("SELECT description, house_id, last_completed FROM Chores WHERE chore_id = " + chore_id);
        if(chore[0].isEmpty()){
            System.out.println("error deleting -> no items returned");
            return;
        }
        else{
            dbConnection.modify("UPDATE Chores SET active = " + 0 + " WHERE chore_id = " + chore_id);
        }

    }

    public String[] returnDelete(int house_id) throws IOException{
        Client dbConnection = new Client("86.8.35.12", 58934);

        String[] chore = dbConnection.select("SELECT chore_id, description FROM Chores WHERE house_id = " + house_id + " AND active = 0");
        if (chore[0].isEmpty()){
            return null;
        }
        else{
            return chore;
        }
    }

    public void recoverDelete (int chore_id) throws IOException{
        Client dbConnection = new Client("86.8.35.12", 58934);
        dbConnection.modify("UPDATE Chores SET active = " + 1 + " WHERE chore_id = " + chore_id);
    }

    public String[] getUsersChores(int user_id) throws IOException{
        Client dbConnection = new Client("86.8.35.12", 58934);
        String[] chores = dbConnection.select("SELECT chore_id FROM ChoreUsers WHERE user_id = " + user_id);
        String[] usersChores = dbConnection.select("SELECT description, last_completed FROM Chores INNER JOIN ChoreUsers ON Chores.chore_id = ChoreUsers.chore_id WHERE ChoreUsers.user_id = " +user_id + " AND Chores.active = 1");
        if (usersChores[0].isEmpty()){
            return null;
        }
        else {
            String[] usersFinalChores = new String[chores.length + usersChores.length];
            for(int i = 0; i < (chores.length); i++){
                usersFinalChores[i*3] = chores[i];
                usersFinalChores[i*3 + 1] = usersChores[i*2];
                usersFinalChores[i*3 + 2] = usersChores[i*2 + 1];
            }
            return usersFinalChores;
        }
    }

    public String[] filter(int house_id) throws IOException{
        Client dbConnection = new Client("86.8.35.12", 58934);
        String[] chores = dbConnection.select("SELECT chore_id, description FROM Chores WHERE house_id = " + house_id + " ORDER BY last_completed DESC");
        if(chores[0].isEmpty()){
            System.out.println("no house with that id");
            return null;
        }
        else{
            return chores;
        }

    }
}