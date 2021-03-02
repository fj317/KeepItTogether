import java.io.IOException;
import java.time.LocalDate;

public class edit {
	public static void chore(String choreId, String desc, boolean complete, String userId) throws IOException {
		Client db = new Client("86.9.93.210", 58934);
		LocalDate date = LocalDate.now();
		if (complete) {
			String chores = "UPDATE chores SET description = '" + desc + "', last_completed = '" + date.toString() + "' ";
			chores += "WHERE chore_id = " + choreId;
			db.modify(chores);
			String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
			choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
			db.modify(choreRecords);
			assign.chore(choreId, userId);
		}
		else {
			String chores = "UPDATE chores SET description = '" + desc + "' WHERE chore_id = " + choreId;
			db.modify(chores);
			assign.chore(choreId, userId);
		}
	}
	
	public static void bill(int trnsId, String desc, boolean repeat, int prodId, int houseId, float price, boolean complete, int userId) {
		//add to database
	}
}
