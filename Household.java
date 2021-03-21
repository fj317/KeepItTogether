import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Household {

    public void create(String address, String postcode, int userID) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        String[] find = dbConnection.select("SELECT house_id FROM House WHERE address = '" + address + "' AND postcode = '" + postcode + "'");
        if (find[0].isEmpty()) {
            String houseID = randomIdGenerator();
            dbConnection.modify("INSERT INTO House (house_id, address, postcode, number_of_residents) VALUES ('" + houseID + "', '" + address + "', '" + postcode + ", 1)");
            dbConnection.modify("INSERT INTO HouseUsers (user_id, house_id) VALUES (" + userID + ", '" + houseID + "')");
        }
        else {
            System.out.println("This household has already been created.");
        }
    }

    public void join(String houseID, int userID) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        String[] find = dbConnection.select("SELECT house_id FROM House WHERE house_id = '" + houseID.toLowerCase() + "'");
        if (find[0].isEmpty()) {
            System.out.println("This household does not exist.");
        }
        else {
            dbConnection.modify("UPDATE House SET number_of_residents = number_of_residents + 1 WHERE house_id = '" + houseID + "'");
            dbConnection.modify("INSERT INTO HouseUsers (user_id, house_id) VALUES (" + userID + ", '" + houseID + "')");
        }
    }

    public void leave(String houseID, int userID) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        dbConnection.modify("UPDATE House SET number_of_residents = number_of_residents - 1 WHERE house_id = '" + houseID + "'");
        dbConnection.modify("DELETE FROM HouseUsers WHERE user_id = " + userID);
    }
    
    public String randomIdGenerator() throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random rand  = new Random();
        int length = 10;
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(chars[rand.nextInt(36)]);
        }
        String randomID = builder.toString();
        String[] checkUniqueness = dbConnection.select("SELECT house_id FROM House WHERE house_id = '" + randomID + "'");
        if (!checkUniqueness[0].isEmpty()) {
            randomIdGenerator();
        }
        return randomID;
    }

    public static void main(String[] args) throws IOException {
    }
}
