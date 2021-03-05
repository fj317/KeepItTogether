import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class delete {
	public static void delete(String desc, int chore_id) throws IOException {
		String[] chore = dbConnection.select("SELECT description, house_id, last_completed FROM Chores WHERE chore_id = chore_id");
		dbConnection.modify("INSERT INTO ChoreRecover(chore_id, description, house_id, last_completed) VALUES(chore_id, chore[0], chore[1], chore[2])");
		Boolean ans1 = dbConnection.modify("DELETE FROM Chores WHERE chore_id = chore_id");
		Boolean ans2 = dbConnection.modify("DELETE FROM ChoresUsers WHERE chore_id = chore_id");
	}
	
	public static String[] returnDelete(int house_id) throws IOException{
		String[] chore = dbConnection.select("SELECT chore_id,  description FROM ChoresRecover WHERE house_id = house_id");
		return chore;
	}
	
	public static void recoverDelete (int chore_id, int house_id) throws IOException{
		String[] allChores = dbConnection.select("SELECT chore_id,  description FROM ChoresRecover WHERE house_id = house_id");
		String[] chore = dbConnetion.select("SELECT description, house_id, last_completed FROM ChoreRecover WHERE chore_id = allChores[0]");
		dbConnection.modify("DELETE FROM ChoreRecover WHERE chore_id = allChores[0]");
		dbConnection.modify("INSERT INTO Chores(chore_id, description, house_id, last_completed) VALUES(allChores[0], chore[0], chore[1],chore[2])");
	}
	
	public static String[] display(int user_id) throws IOException{
		String[] chore_id = dbConnection.select("SELECT chore_id FROM chore_users WHERE user_id = user_id");
		String[] descriptionComplete;
		descriptionComplete = new String[chore_id.length];
		for (int i = 0; i < chore_id.length; i++) {
			String[] temp = dbConnection.select("SELECT description, last_completed FROM Chores WHERE chore_id = chore_id[i]");
  			temp.add(0, chore_id[i]);
			descriptionComplete = ArrayUtils.add(descriptionComplete, temp);
		}
		return descriptionComplete;
	}
	
	public static String[] filter(int house_id) throws IOException{
		String[] chores = dbConection.select("SELECT chore_id, description FROM Chores WHERE house_id = house_id ORDER BY last_completed DESC");
		return chores;
	}
}
	
