package com.example.BeautyDials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.BeautyDials.databinding.ActivityRegistrBinding;

public class RegistrActivity extends AppCompatActivity {
    ActivityRegistrBinding binding;
    String userId;
    SharedPreferences saveEnter;
    private EditText passwordEditText;
    private ImageButton togglePasswordVisibilityButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent home = new Intent(this, HomeActivity.class);
        Intent back = new Intent(this, EnterActivity.class);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(back);
                finish();
            }
        });

        passwordEditText = findViewById(R.id.password);
        togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility);

        togglePasswordVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    // Show password
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordVisibilityButton.setImageResource(R.drawable.ic_show_password);
                } else {
                    // Hide password
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordVisibilityButton.setImageResource(R.drawable.ic_hide_password);
                }

                // Move the cursor to the end of the text
                int selection = passwordEditText.getText().length();
                passwordEditText.setSelection(selection);
            }
        });


        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean all = true;
                if (binding.userName.getText().toString().equals("")) {
                    binding.userName.setTextColor(Color.rgb(200,0,0));
                    binding.mistake.setText("Не все поля заполнены!");
                    binding.mistake.setVisibility(View.VISIBLE);
                    all = false;
                }
                if (binding.phone.getText().toString().equals("")){
                    binding.phone.setTextColor(Color.rgb(200,0,0));
                    binding.mistake.setText("Не все поля заполнены!");
                    binding.mistake.setVisibility(View.VISIBLE);
                    all = false;
                }
                if (binding.password.getText().toString().equals("")){
                    binding.password.setTextColor(Color.rgb(200,0,0));
                    binding.mistake.setText("Не все поля заполнены!");
                    binding.mistake.setVisibility(View.VISIBLE);
                    all = false;
                }

                else{
                    binding.userName.setTextColor(Color.rgb(200,0,0));
                    binding.mistake.setText("Такой аккаунт уже существует");
                    binding.mistake.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void saveEnter(boolean ent){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("UserID", userId);
        editor.apply();
    }

    public boolean setUser() {
        if (checkUser()) {
            return false;
        }

        String name = binding.userName.getText().toString();
        String phone = binding.phone.getText().toString();
        String password = binding.password.getText().toString();
        String userMail = binding.userMail.getText().toString(); // Новое значение
        String seller;

        if (binding.seller.isChecked()) {
            seller = "1";
        } else {
            seller = "0";
        }

        SQLiteDatabase database = new SQLiteHelper(this).getWritableDatabase();
        Cursor cursor = database.query(SQLiteTable.User.TABLE_NAME, null, null, null, null, null, null);
        int id_int = cursor.getCount();
        String id = id_int + 1 + "";
        userId = id;
        cursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_ID);

        database.execSQL("INSERT INTO " + SQLiteTable.User.TABLE_NAME + " VALUES (" + id + ", \"" + name + "\", NULL, "
                + seller + ", \"" + phone + "\", \"" + password + "\", \"" + userMail + "\")"); // Обновленный запрос

        return true;
    }

    private boolean checkUser() {
        String name = binding.userName.getText().toString();
        String phone = binding.phone.getText().toString();
        String userMail = binding.userMail.getText().toString(); // Новое значение

        SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();
        String[] projection = {
                SQLiteTable.User.COLUMN_ID
        };
        String selection =
                SQLiteTable.User.COLUMN_NAME + " = ? and " +
                        SQLiteTable.User.COLUMN_PHONE + " = ? and " +
                        SQLiteTable.User.COLUMN_USER_LOGIN + " = ?"; // Новое условие
        String[] selectionArgs = {name, phone, userMail}; // Новый аргумент

        Cursor cursor = database.query(
                SQLiteTable.User.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            userId = cursor.getString(0);
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

}