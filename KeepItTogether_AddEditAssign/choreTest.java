import java.io.IOException;

public class choreTest {
	public static void main(String[] args) throws IOException {
		add.chore("test2", "0", false, "0");
		Client db = new Client("86.9.93.210", 58934);
		System.out.println(db.select("SELECT chore_id FROM Chores WHERE description = 'test2'")[0]);
		//System.out.println(db.select("SELECT first_name FROM Users")[0]);
	}
}
