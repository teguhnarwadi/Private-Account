package com.narwadi.saveyouraccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.narwadi.saveyouraccount.helper.UserHelper;
import com.narwadi.saveyouraccount.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUser;
    private EditText etPass;
    private UserHelper userHelper;
    private EditText etRePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_up);
        setSupportActionBar(toolbar);

        etUser = (EditText) findViewById(R.id.editText_username);
        etPass = (EditText) findViewById(R.id.editText_password);
        etRePass = (EditText) findViewById(R.id.editText_password_retype);

        userHelper = new UserHelper(this);
        User user = userHelper.findUser();
        etUser.setText(user.getUsername());
        etPass.setText(user.getPassword());
        etRePass.setText(user.getPassword());

        Button btnSave = (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                String username = etUser.getText().toString();
                String password = etPass.getText().toString();
                String rePassword = etRePass.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    showToast("Enter Username");
                } else if (TextUtils.isEmpty(password)) {
                    showToast("Enter Password");
                } else if (!isPasswordValid(password)) {
                    showToast(getString(R.string.error_invalid_password));
                } else if (TextUtils.isEmpty(rePassword)) {
                    showToast("Enter Confirm Password");
                } else if (!isPasswordValid(rePassword)) {
                    showToast(getString(R.string.error_invalid_password));
                } else if (!isPasswordMatching(password, rePassword)) {
                    showToast("Password not match!");
                } else {
                    userHelper.update(username, password);
                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }


                break;
        }
    }

    private boolean isPasswordMatching(String password, String rePassword) {
        Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rePassword);

        if (!matcher.matches()) {
            // password not match, return false
            return false;
        }

        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the toolbar navigation up
            case android.R.id.home:
                //Navigation icon
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
