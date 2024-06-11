package com.example.BeautyDials;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import com.example.BeautyDials.databinding.Activity2Binding;


public class Activity2 extends AppCompatActivity {

    Activity2Binding binding;

    User user;
    MyListAdapter adapter;
    ArrayList<Add> adds = new ArrayList<Add>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new MyListAdapter(this, adds);
        ListView list = binding.listView;
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", this.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserID", "");
        user = getUser(userId);
        if (fillData()) {
            list.setAdapter(adapter);
            list.setVisibility(View.VISIBLE);
            binding.sadSmile.setVisibility(View.INVISIBLE);
            binding.sadText.setVisibility(View.INVISIBLE);
            Intent newAd = new Intent(this, SellActivity.class);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    newAd.putExtra("new", "1");
                    newAd.putExtra("edit", "1");
                    String addId = ((TextView) view.findViewById(R.id.adId)).getText().toString();
                    newAd.putExtra("itemId", addId);
                    startActivity(newAd);
                }
            });
        }
        else{
            list.setVisibility(View.INVISIBLE);
            binding.sadSmile.setVisibility(View.VISIBLE);
            binding.sadText.setVisibility(View.VISIBLE);

        }

        AppCompatImageButton button = findViewById(R.id.back);

        // Добавьте слушатель нажатий для кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, HomeActivity.class);
                intent.putExtra("current_item", 2);
                startActivity(intent);


            }
        });

    }


    private boolean fillData() {
        SQLiteDatabase database = new SQLiteHelper(this).getReadableDatabase();
        String[] adProjection = {
                SQLiteTable.Favourites.COLUMN_AD,
                SQLiteTable.Favourites.COLUMN_USER
        };
        String adSelection =
                SQLiteTable.Favourites.COLUMN_USER + " = ?";
        String[] selectionArgs = {String.valueOf(user.id)};


        Cursor adCursor = database.query(
                SQLiteTable.Favourites.TABLE_NAME,     // Запрашиваемая таблица
                adProjection,                               // Возвращаемый столбец
                adSelection,                                // Столбец для условия WHERE
                selectionArgs,                            // Значение для условия WHERE
                null,                                     // не группировать строки
                null,                                     // не фильтровать по группам строк
                null                                      // не сортировать
        );
        adCursor.moveToFirst();
        String adId, userId;
        for (int i=0; i<adCursor.getCount();i++){
            adId = adCursor.getString(adCursor.getColumnIndexOrThrow(SQLiteTable.Favourites.COLUMN_AD));
            userId = adCursor.getString(adCursor.getColumnIndexOrThrow(SQLiteTable.Favourites.COLUMN_USER));
            adds.add(getAd(adId));
            adCursor.moveToNext();
        }
        if (adds.size()==0){return false;}
        return true;
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