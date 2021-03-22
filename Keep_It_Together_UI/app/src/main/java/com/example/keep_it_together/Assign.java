package com.example.keep_it_together;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.IOException;
import java.util.Arrays;

public class Assign {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean chore(String choreId, String userId) throws IOException {
        boolean success = false;
        Client db = new Client("86.9.93.210", 58934);
        String[] users = db.select("SELECT user_id FROM ChoreUsers WHERE chore_id = " + choreId);
        if (!Arrays.stream(users).anyMatch(userId::equals)) {
            success = db.modify("INSERT INTO ChoreUsers(chore_id, user_id) VALUES ('" + choreId + "', '" + userId + "')");
        }
        return success;
    }
}