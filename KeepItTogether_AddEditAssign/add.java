import java.io.IOException;
import java.time.LocalDate;

public class add {
	public static void chore(String desc, String houseId, boolean complete, String userId) throws IOException {
		LocalDate date = LocalDate.now();
		Client db = new Client("86.9.93.210", 58934);
		String choreId = "";
		String[] checkArr = db.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'");
		if (checkArr[0].isEmpty()) {
			if (complete) {
				System.out.println("in 1");
				String chores = "INSERT INTO Chores (description, house_id, last_completed) VALUES (";
				chores += "'" + desc + "', '" + houseId + "', '" + date.toString() + "')";
				db.modify(chores);
				choreId = db.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'")[0];
				String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
				choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
				db.modify(choreRecords);
				assign.chore(choreId, userId);
			}
			else {
				System.out.println("in 2");
				String chores = "INSERT INTO Chores (description, house_id, last_completed) VALUES (";
				chores += "'" + desc + "', '" + houseId + "', 'null')";
				System.out.println(chores);
				db.modify(chores);
				assign.chore(choreId, userId);
			}
		}
		else {
			choreId = checkArr[0];
			if (complete) {
				System.out.println("in 3");
				String chores = "UPDATE Chores SET last_completed = '";
				chores += date.toString() + "' WHERE chore_id = " + choreId;
				db.modify(chores);
				String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
				choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
				db.modify(choreRecords);
				assign.chore(choreId, userId);
			}
			else {
				System.out.println("in 4");
				assign.chore(choreId, userId);
			}
		}
	}
	
	public static void bill(String desc, String houseId, String price, String userId, String name) throws IOException {
		LocalDate date = LocalDate.now();
		Client db = new Client("86.9.93.210", 58934);
		String prodId = "";
		String[] checkArr = db.select("SELECT product_id FROM Products WHERE name = '" + name + "'");
		if (checkArr[0].isEmpty()) {
			System.out.println("empty");
			String products = "INSERT INTO Products (name, description) VALUES ('";
			products += name + "', '" + desc + "')";
			db.modify(products);
			prodId = db.select("SELECT product_id FROM Products WHERE name = '" + name + "'")[0];
			String trans = "INSERT INTO Transactions (user_id, house_id, date, product_id, price) VALUES (";
			trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + prodId + ", " + price + ")";
			db.modify(trans);
		}
		else {
			prodId = checkArr[0];
			String trans = "INSERT INTO Transactions (user_id, house_id, date, product_id, price) VALUES (";
			trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + prodId + ", " + price + ")";
			db.modify(trans);
		}
	}
}
