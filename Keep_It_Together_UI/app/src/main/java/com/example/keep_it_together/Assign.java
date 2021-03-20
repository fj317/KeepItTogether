package com.example.keep_it_together;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.IOException;
import java.util.Arrays;

public class Assign {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void chore(String choreId, String userId) throws IOException {
        Client db = new Client("86.9.93.210", 58934);
        String[] users = db.select("SELECT user_id FROM ChoreUsers WHERE chore_id = " + choreId);
        if (!Arrays.stream(users).anyMatch(userId::equals)) {
            db.modify("INSERT INTO ChoreUsers(chore_id, user_id) VALUES (" + choreId + ", " + userId + ")");
        }
    }
}