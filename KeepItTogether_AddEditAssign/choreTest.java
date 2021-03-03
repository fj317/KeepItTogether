import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class choreTest {
	public static void main(String[] args) throws IOException {
		LocalDate date = LocalDate.now();
		Client db = new Client("86.9.93.210", 58934);
		add.bill("A test product", "1", "5.55", "1", "Nathan Test");
		String prodId = db.select("SELECT product_id FROM Products WHERE name = 'Nathan Test'")[0];
		System.out.println(prodId);
		System.out.println(Arrays.deepToString(db.select("SELECT name description FROM Products WHERE product_id = " + prodId)));
		String trnsId = db.select("SELECT transaction_id FROM Transactions WHERE product_id = " + prodId)[0];
		System.out.println(Arrays.deepToString(db.select("SELECT transaction_id price user_id FROM Transactions WHERE product_id = " + prodId)));
		edit.bill(trnsId, prodId, "1", "4.44", "2", date.toString());
		edit.product(prodId, "Nathan Test", "A test product with a different description");
		System.out.println(Arrays.deepToString(db.select("SELECT name description FROM Products WHERE product_id = " + prodId)));
		System.out.println(Arrays.deepToString(db.select("SELECT transaction_id price user_id FROM Transactions WHERE product_id = " + prodId)));
	}
}
