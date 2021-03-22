import java.io.IOException;
import java.time.LocalDate;

public class add {
	public static void chore(String desc, String houseId, boolean complete, String userId, String name) throws IOException {
		LocalDate date = LocalDate.now();
		Client db = new Client("86.9.93.210", 58934);
		String choreId = "";
		String[] checkArr = db.select("SELECT chore_id FROM Chores WHERE name = '" + name + "'");
		if (checkArr[0].isEmpty()) {
			if (complete) {
				String chores = "INSERT INTO Chores (description, house_id, last_completed, name, active) VALUES (";
				chores += "'" + desc + "', '" + houseId + "', '" + date.toString() + "', '" + name + "', + 0)";
				db.modify(chores);
				choreId = db.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'")[0];
				String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
				choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
				db.modify(choreRecords);
				assign.chore(choreId, userId);
			}
			else {
				String chores = "INSERT INTO Chores (description, house_id, last_completed, name, active) VALUES (";
				chores += "'" + desc + "', '" + houseId + "', 'null', '" + name + "', 1)";
				db.modify(chores);
				assign.chore(choreId, userId);
			}
		}
		else {
			choreId = checkArr[0];
			if (complete) {
				String chores = "UPDATE Chores SET last_completed = '";
				chores += date.toString() + "', active = 0 WHERE chore_id = " + choreId;
				db.modify(chores);
				String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
				choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
				db.modify(choreRecords);
				assign.chore(choreId, userId);
			}
			else {
				String chores = "UPDATE Chores SET active = 1 WHERE chore_id = " + choreId;
				db.modify(chores);
				assign.chore(choreId, userId);
			}
		}
	}
	
	public static void bill(String desc, String houseId, String price, String userId, String name) throws IOException {
		LocalDate date = LocalDate.now();
		Client db = new Client("86.9.93.210", 58934);
		String trans = "INSERT INTO Transactions (user_id, house_id, date, price, name, description) VALUES (";
		trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + price + ", '" + name + "', '" + desc + "')";
		db.modify(trans);
	}
}
