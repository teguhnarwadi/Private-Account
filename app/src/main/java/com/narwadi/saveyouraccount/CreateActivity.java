package com.narwadi.saveyouraccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.narwadi.saveyouraccount.helper.AccountHelper;

public class CreateActivity extends AppCompatActivity {

    private String name;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_up);
        setSupportActionBar(toolbar);

        final EditText etName = (EditText) findViewById(R.id.editText_name);
        final EditText etEmail = (EditText) findViewById(R.id.editText_email);
        final EditText etPassword = (EditText) findViewById(R.id.editText_password);
        Button btnSave = (Button) findViewById(R.id.button_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().matches("")) {
                    showToast("Enter Name");
                } else if (etEmail.getText().toString().matches("")) {
                    showToast("Enter Email");
                } else if (etPassword.getText().toString().matches("")) {
                    showToast("Enter Password");
                } else {
                    name = etName.getText().toString();
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();

                    AccountHelper account = new AccountHelper(CreateActivity.this);
                    account.store(name, email, password);

                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });
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
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
