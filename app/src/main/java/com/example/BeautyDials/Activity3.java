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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.BeautyDials.databinding.Activity3Binding;

import java.util.ArrayList;


public class Activity3 extends AppCompatActivity {

    Activity3Binding binding;

    User user;
    MyListAdapter adapter;
    ArrayList<Add> adds = new ArrayList<Add>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", this.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserID", "");
        user = getUser(userId);

        fillData();
        adapter = new MyListAdapter(this, adds);
        ListView list = binding.myAdds;
        list.setAdapter(adapter);

        Intent newAd = new Intent(this, SellActivity.class);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newAd.putExtra("new", "2");
                newAd.putExtra("edit", "1");
                String addId = ((TextView) view.findViewById(R.id.adId)).getText().toString();
                newAd.putExtra("itemId", addId);
                startActivity(newAd);

            }
        });
        binding.newAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAd.putExtra("new", "2");
                newAd.putExtra("edit", "0");
                newAd.putExtra("itemId", "");
                startActivity(newAd);
                finish();
            }
        });

        AppCompatImageButton button = findViewById(R.id.back);

        // Добавьте слушатель нажатий для кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity3.this, HomeActivity.class);
                intent.putExtra("current_item", 2);
                startActivity(intent);


            }
        });
    }


    private void fillData() {
        String[] projection = {
                SQLiteTable.Ads.COLUMN_ID,
                SQLiteTable.Ads.COLUMN_NAME,
                SQLiteTable.Ads.COLUMN_PRICE,
                SQLiteTable.Ads.COLUMN_USER,
                SQLiteTable.Ads.COLUMN_IMAGE
        };
        SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();
        Cursor cursor = database.query(
                SQLiteTable.Ads.TABLE_NAME,     // Запрашиваемая таблица
                projection,                               // Возвращаемый столбец
                null,                                // Столбец для условия WHERE
                null,                            // Значение для условия WHERE
                null,                                     // не группировать строки
                null,                                     // не фильтровать по группам строк
                null                                      // не сортировать
        );
        cursor.moveToFirst();
        for (int i=0; i<cursor.getCount();i++){

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_ID)));
            int price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_PRICE)));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_NAME));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_USER));
            byte[] imageInByte = cursor.getBlob(cursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_IMAGE));
            String[] userProjection = {
                    SQLiteTable.User.COLUMN_NAME
            };
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
            String userName = userCursor.getString(0);
            if (user.id == Integer.parseInt(userId))
                adds.add(new Add(id, price, imageInByte, name, userName));
            userCursor.close();
            cursor.moveToNext();
        }

        cursor.close();
    }

    private Add getAd(String adId) {
        SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();
        String[] adsProjection = {
                SQLiteTable.Ads.COLUMN_NAME,
                SQLiteTable.Ads.COLUMN_PRICE,
                SQLiteTable.Ads.COLUMN_USER,
                SQLiteTable.Ads.COLUMN_IMAGE
        };
        String adsSelection =
                SQLiteTable.Ads.COLUMN_ID + " = ?";
        String[] selectionArgs = {adId};

        Cursor adCursor = database.query(
                SQLiteTable.Ads.TABLE_NAME,     // Запрашиваемая таблица
                adsProjection,                               // Возвращаемый столбец
                adsSelection,                                // Столбец для условия WHERE
                selectionArgs,                            // Значение для условия WHERE
                null,                                     // не группировать строки
                null,                                     // не фильтровать по группам строк
                null                                      // не сортировать
        );
        adCursor.moveToFirst();
        String name = adCursor.getString(adCursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_NAME));
        int price = adCursor.getInt(adCursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_PRICE));
        String userId = adCursor.getString(adCursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_USER));
        byte[] imageInByte = adCursor.getBlob(adCursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_IMAGE));
        User u = getUser(userId);
        return new Add(Integer.parseInt(adId), price, imageInByte, name, u.userName);
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
}