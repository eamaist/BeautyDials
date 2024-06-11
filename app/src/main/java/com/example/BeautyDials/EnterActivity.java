package com.example.BeautyDials;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.BeautyDials.databinding.ActivityEnterBinding;
import android.widget.EditText;
import android.widget.ImageButton;


public class EnterActivity extends AppCompatActivity {
    String userId;
    ActivityEnterBinding binding;
    SharedPreferences saveEnter;
    private EditText passwordEditText;
    private ImageButton togglePasswordVisibilityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent home = new Intent(this, HomeActivity.class);
        Intent registrActivity = new Intent(this, RegistrActivity.class);
        binding.reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registrActivity);
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
                if (binding.login.getText().toString().equals("")) {
                    binding.login.setTextColor(Color.rgb(200, 0, 0));
                    binding.mistake.setText("Не все поля заполнены!");
                    binding.mistake.setVisibility(View.VISIBLE);
                    all = false;
                }
                if (binding.password.getText().toString().equals("")) {
                    binding.password.setTextColor(Color.rgb(200, 0, 0));
                    binding.mistake.setText("Не все поля заполнены!");
                    binding.mistake.setVisibility(View.VISIBLE);
                    all = false;
                }

                if (!all) return;
                binding.mistake.setVisibility(View.INVISIBLE);
                binding.login.setTextColor(Color.rgb(103, 49, 0));
                binding.password.setTextColor(Color.rgb(103, 49, 0));

                if (checkUser()) {
                    binding.mistake.setVisibility(View.INVISIBLE);
                    binding.login.setTextColor(Color.rgb(103, 49, 0));
                    binding.password.setTextColor(Color.rgb(103, 49, 0));

                    saveEnter(true);
                    startActivity(home);
                    finish();
                } else {
                    binding.login.setTextColor(Color.rgb(200, 0, 0));
                    binding.password.setTextColor(Color.rgb(200, 0, 0));
                    binding.mistake.setText("Неверные логин или пароль!");
                    binding.mistake.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void saveEnter(boolean ent) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        Log.d("MyLogs", "user_ID_save = " + userId);
        editor.putString("UserID", userId);
        editor.apply();
    }

    private boolean checkUser() {
        String name = binding.login.getText().toString();
        String password = binding.password.getText().toString();
        SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();
        String[] projection = {
                SQLiteTable.User.COLUMN_USER_LOGIN,
                SQLiteTable.User.COLUMN_ID,
                SQLiteTable.User.COLUMN_PASSWORD
        };
        String selection =
                SQLiteTable.User.COLUMN_USER_LOGIN + " = ? and " +
                        SQLiteTable.User.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {name, password};

        Cursor cursor = database.query(
                SQLiteTable.User.TABLE_NAME,     // Запрашиваемая таблица
                projection,                               // Возвращаемый столбец
                selection,                                // Столбец для условия WHERE
                selectionArgs,                            // Значение для условия WHERE
                null,                                     // не группировать строки
                null,                                     // не фильтровать по группам строк
                null                                      // не сортировать
        );
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            userId = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_ID));
            cursor.close();

            return true;
        }

        cursor.close();
        return false;
    }
}
