package com.narwadi.saveyouraccount.helper;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.narwadi.saveyouraccount.MainActivity;
import com.narwadi.saveyouraccount.model.Account;
import com.narwadi.saveyouraccount.model.User;
import com.narwadi.saveyouraccount.model.UserModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by DEKSTOP on 05/12/2016.
 */
public class UserHelper {
    private static final String TAG = "UserHelper";

    private Realm realm;
    private Context context;
    private String dataUsername;
    private String dataPassword;

    /**
     * constructor for created instance realm
     *
     * @param context
     */
    public UserHelper(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        this.context = context;
    }

    /**
     * add data account
     *
     * @param username
     * @param password
     */
    public boolean store(final String username, final String password) {
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = new User();
//                    user.setId((int) (System.currentTimeMillis() / 1000));
                    user.setId(1);
                    user.setUsername(username);
                    user.setPassword(password);
                    realm.copyToRealm(user);
                }
            });
        } finally {
            realm.close();
        }

        return true;
    }

    public long getCount() {
        long xx = realm.where(User.class).count();
        return xx;
    }

    public boolean login(final String username, final String password) {
        try {
            User user = realm.where(User.class).findFirst();
            dataUsername = user.getUsername();
            dataPassword = user.getPassword();
            if (TextUtils.equals(username, dataUsername)) {
                if (TextUtils.equals(password, dataPassword)) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                } else {
                    showToast("Password not match");
                }
            } else {
                showToast("Username not match");
            }
        } finally {
            realm.close();
        }
        return true;
    }

    /**
     * Create Toast Information
     */
    private void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
