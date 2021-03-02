import java.io.IOException;
import java.util.Arrays;

public class assign {
	public static void chore(String choreId, String userId) throws IOException {
		Client db = new Client("86.9.93.210", 58934);
		String[] users = db.select("SELECT user_id FROM ChoreUsers WHERE chore_id = " + choreId);
		if (!Arrays.stream(users).anyMatch(userId::equals)) {
			db.modify("INSERT INTO ChoreUsers(chore_id, user_id) VALUES (" + choreId + ", " + userId + ")");
		}
	}
	
	public static void bill(int trnsId, int houseId, int userId) {
		//add to database
	}
}
