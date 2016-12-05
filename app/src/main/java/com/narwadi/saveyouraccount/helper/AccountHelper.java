package com.narwadi.saveyouraccount.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.narwadi.saveyouraccount.MainActivity;
import com.narwadi.saveyouraccount.model.Account;
import com.narwadi.saveyouraccount.model.AccountModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by DEKSTOP on 03/12/2016.
 */
public class AccountHelper {
    private static final String TAG = "AccountHelper";

    private Realm realm;
    private RealmResults<Account> realmResults;
    private Context context;

    /**
     * constructor for created instance realm
     *
     * @param context
     */
    public AccountHelper(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        this.context = context;
    }

    /**
     * add data account
     *
     * @param name
     * @param email
     * @param password
     */
    public boolean store(final String name, final String email, final String password) {
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Account account = new Account();
                    account.setId((int) (System.currentTimeMillis() / 1000));
                    account.setName(name);
                    account.setEmail(email);
                    account.setPassword(password);
                    realm.copyToRealm(account);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    showLog("Added : " + name);
                    showToast(name + " Berhasil disimpan");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    showLog("Failed : " + name);
                    showToast(name + " Gagal disimpan");
                }
            });
        } finally {
            realm.close();
        }

        return true;
    }

    /**
     * method for get all account
     */
    public ArrayList<AccountModel> findAllAccount() {
        ArrayList<AccountModel> data = new ArrayList<>();

        try {
            realmResults = realm.where(Account.class).findAll();
//            realmResults.sort("id", Sort.DESCENDING);

            if (realmResults.size() > 0) {
                showLog("Size : " + realmResults.size());

                for (int i = 0; i < realmResults.size(); i++) {
                    int id = realmResults.get(i).getId();
                    String name = realmResults.get(i).getName();
                    String email = realmResults.get(i).getEmail();
                    String password = realmResults.get(i).getPassword();
                    data.add(new AccountModel(id, name, email, password));
                }
            } else {
                showLog("Size : 0");
                showToast("Database kosong !");
            }
        } finally {
            realm.close();
        }

        return data;
    }

    /**
     * method for get account by id
     *
     * @param id
     */
    public Account findAccount(int id) {
        try {
            Account account = realm.where(Account.class).equalTo("id", id).findFirst();

            return account;

        } finally {
            realm.close();
        }
    }

    /**
     * method update account
     *
     * @param id
     * @param name
     * @param email
     * @param password
     */
    public void update(final int id, final String name, final String email, final String password) {

        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Account account = realm.where(Account.class).equalTo("id", id).findFirst();
                    account.setName(name);
                    account.setEmail(email);
                    account.setPassword(password);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    showLog("Updated : " + name);
                    showToast(name + " Database berhasil di update");
                    goActivity();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    showLog("Failed : " + name);
                    showToast(name + " Database gagal di update");
                }
            });
        } finally {
            realm.close();
        }

    }

    /**
     * method to delete account where id
     *
     * @param id
     */
    public void delete(final int id) {
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Account> accounts = realm.where(Account.class).equalTo("id", id).findAll();
                    // remove data in results
                    accounts.deleteAllFromRealm();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    showLog("Hapus data berhasil.");
                    showToast("Hapus data berhasil.");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    showLog("Hapus data gagal.");
                    showToast("Hapus data gagal.");
                }
            });
        } finally {
            realm.close();
        }

    }

    /**
     * Create log
     *
     * @param s
     */
    private void showLog(String s) {
        Log.d(TAG, s);

    }

    /**
     * Create Toast Information
     */
    private void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    /**
     * Go to activity
     */
    private void goActivity(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
