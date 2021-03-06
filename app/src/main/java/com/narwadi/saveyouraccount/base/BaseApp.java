package com.narwadi.saveyouraccount.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import com.narwadi.saveyouraccount.model.User;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by DEKSTOP on 03/12/2016.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // The Realm file will be located in Context.getFilesDir() with name "default.realm"
        Realm.init(this);
        // Configuration realm
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0) // Version database
                .migration(new dataMigration())
                .build();

        Realm.setDefaultConfiguration(config);
    }

    private class dataMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            // Getting schema
            RealmSchema schema = realm.getSchema();

            // Create new schema if version is 0
            if (oldVersion == 0) {
                schema.create("Accounts")
                        .addField("id", int.class)
                        .addField("name", String.class)
                        .addField("email", String.class)
                        .addField("password", String.class);
                schema.create("Users")
                        .addField("id", int.class)
                        .addField("username", String.class)
                        .addField("password", String.class);
                oldVersion++;
            }
        }
    }

    /**
     * class for seeder data into database
     */
    private class seederData implements Realm.Transaction {

        @Override
        public void execute(Realm realm) {
            User user = new User();
            user.setId(1);
            user.setUsername("admin");
            user.setPassword("password");
            realm.copyToRealm(user);
            Log.d("BaseApp", "Insert");
        }
    }
}
