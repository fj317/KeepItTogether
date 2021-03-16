import java.io.IOException;
import java.util.Arrays;

public class Household {

    public void create(String address, String postcode, int numberOfResidents) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        String[] find = dbConnection.select("SELECT house_id FROM House WHERE address = '" + address + "' AND postcode = '" + postcode + "'");
        if (find[0].isEmpty()) {
            dbConnection.modify("INSERT INTO House (address, postcode, number_of_residents) VALUES ('" + address + "', '" + postcode + "', " + numberOfResidents + ")");
            System.out.println("test");
        }
        else {
            System.out.println("This household has already been created.");
        }
    }

    public void join(int houseID, int userID) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        String[] find = dbConnection.select("SELECT house_id FROM House WHERE house_id = " + houseID);
        if (find[0].isEmpty()) {
            System.out.println("This household does not exist.");
        }
        else {
            dbConnection.modify("UPDATE House SET number_of_residents = number_of_residents + 1 WHERE house_id = " + houseID);
            dbConnection.modify("INSERT INTO HouseUsers (user_id, house_id) VALUES (" + userID + ", " + houseID + ")");
        }
    }

    public void leave(int houseID, int userID) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        dbConnection.modify("UPDATE House SET number_of_residents = number_of_residents - 1 WHERE house_id = " + houseID);
        dbConnection.modify("DELETE FROM HouseUsers WHERE user_id = " + userID);
    }

    public static void main(String[] args) throws IOException {
    }
}
