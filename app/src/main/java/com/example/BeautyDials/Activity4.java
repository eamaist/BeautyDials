package com.example.BeautyDials;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.BeautyDials.databinding.Activity4Binding;

import java.util.ArrayList;


public class Activity4 extends AppCompatActivity {

    Activity4Binding binding;

    User user;
    MyListAdapter adapter;
    ArrayList<Add> adds = new ArrayList<Add>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new MyListAdapter(this, adds);
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", this.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserID", "");
        user = getUser(userId);


        AppCompatImageButton button = findViewById(R.id.back);

        // Добавьте слушатель нажатий для кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity4.this, HomeActivity.class);
                intent.putExtra("current_item", 2);
                startActivity(intent);


            }
        });

        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.sell) {
                    showInstructionsDialog_Master();}
                else
                    showInstructionsDialog_Client();
            }

        });

    }


    private User getUser(String userId) {
        SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();
        String[] userProjection = {
                SQLiteTable.User.COLUMN_NAME,
                SQLiteTable.User.COLUMN_SELLER,
                SQLiteTable.User.COLUMN_ID
        };
        Log.d("MyLogs", "userId = " + userId);
        String userSelection =
                SQLiteTable.User.COLUMN_ID + " = ?";
        String[] selectionArgs = {userId};


        Cursor userCursor = database.query(
                SQLiteTable.User.TABLE_NAME,     // Запрашиваемая таблица
                userProjection,                               // Возвращаемый столбец
                userSelection,                                // Столбец для условия WHERE
                selectionArgs,                            // Значение для условия WHERE
                null,                                     // не группировать строки
                null,                                     // не фильтровать по группам строк
                null                                      // не сортировать
        );
        userCursor.moveToFirst();
        String name = userCursor.getString(userCursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_NAME));
        int sellerIs = userCursor.getInt(userCursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_SELLER));
        int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_ID));
        boolean seller = false;
        if (sellerIs == 1){
            seller = true;
        }

        User user = new User(name, seller);
        user.id = id;
        return user;
    }

    private void showInstructionsDialog_Master() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Инструкция пользователю");
        builder.setMessage
                (
                        "1. Зайдите в личный кабинет\n" +
                                "2. Нажмите кнопку создания объявления\n" +
                                "3. Добавьте в объявление данные и фото\n" +
                                "4. Сохраните\n" +
                                "5. Для редактирования уже созданного объявления нажмите кнопку в правом верхнем углу\n6. Не забудьте сохранить!");
        builder.setPositiveButton("ОК", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showInstructionsDialog_Client() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Инструкция пользователю");
        builder.setMessage("1. Нажмите на объявление\n" +
                "2. Добавьте в избранное, чтобы не потерять\n" +
                "3. Напишите по указанным контактам");
        builder.setPositiveButton("ОК", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}