import java.io.IOException;
import java.time.LocalDate;

public class edit {
	public static void chore(String choreId, String desc, boolean complete, String userId, String name) throws IOException {
		Client db = new Client("86.9.93.210", 58934);
		LocalDate date = LocalDate.now();
		if (complete) {
			String chores = "UPDATE Chores SET description = '" + desc + "', last_completed = '" + date.toString() + "', ";
			chores += "name = '" + name + "', active = 0 ";
			chores += "WHERE chore_id = " + choreId;
			db.modify(chores);
			String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
			choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
			db.modify(choreRecords);
			assign.chore(choreId, userId);
		}
		else {
			String chores = "UPDATE chores SET description = '" + desc + ", active = 1, name = '" + name;
			chores += "' WHERE chore_id = " + choreId;
			db.modify(chores);
			assign.chore(choreId, userId);
		}
	}
	
	public static void bill(String trnsId, String houseId, String price, String userId, String date, String name, String desc) throws IOException {
		Client db = new Client("86.9.93.210", 58934);
		String trans = "UPDATE Transactions SET user_id = " + userId + ", house_id = '" + houseId;
		trans += "', date = '" + date + "', price = " + price + ", name = '" + name + "', description = '" + desc + "'";
		trans += " WHERE transaction_id = " + trnsId;
		db.modify(trans);
	}
}
