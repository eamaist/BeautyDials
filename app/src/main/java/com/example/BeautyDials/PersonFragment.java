package com.example.BeautyDials;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import de.hdodenhof.circleimageview.CircleImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.BeautyDials.databinding.FragmentPersonBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PersonFragment extends Fragment {
    User user;

    private static final String CHANNEL_ID = "my_channel_id";
    ArrayList<Add> adds = new ArrayList<Add>();
    FragmentPersonBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String CHANNEL_NAME = "My Channel";
    private static final String CHANNEL_DESCRIPTION = "This is my channel";



    PersonFragment(User currentUser){
        this.user = currentUser;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(getLayoutInflater());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserID", "");
        user = getUser(userId);
        Intent exit = new Intent(getActivity(), MainActivity.class);
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                startActivity(exit);
                getActivity().finish();
            }
        });
        binding.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fav = new Intent(getActivity(), Activity2.class);
                startActivity(fav);
            }
        });
        binding.myAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAdd = new Intent(getActivity(), Activity3.class);
                startActivity(myAdd);
            }
        });
        binding.notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNotification();
            }
        });

        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info = new Intent(getActivity(), Activity4.class);
                startActivity(info);
            }
        });

        binding.myName.setText(user.userName);
        // Проверяем, видима ли кнопка myAdd, и перемещаем кнопку exit в зависимости от этого
        if (user.sell) {
            binding.myAdd.setVisibility(View.VISIBLE);
            binding.exit.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        } else {
            binding.myAdd.setVisibility(View.INVISIBLE);
            binding.exit.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0f));
            binding.userStatus.setText("Клиент");
        }


        binding.ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });
        createNotificationChannel();
        return binding.getRoot();

    }




    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Это уведомление!")
                .setContentText("Оно очень важное!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите источник изображения");
        builder.setItems(new CharSequence[]{"Сделать фото", "Выбрать из галереи"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Открыть камеру
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Запрос разрешения, если оно не было предоставлено
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        // Разрешение уже предоставлено, можно открывать камеру
                        openCamera();
                    }
                } else {
                    // Открыть галерею
                    openGallery();
                }
            }
        });
        builder.create().show();

        byte[] imageInByte;
        ImageView imageView = binding.ava;
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            imageInByte = baos.toByteArray();
        }
        else{
            imageInByte = null;
        }
        SQLiteDatabase database = new SQLiteHelper(getActivity()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteTable.Ads.COLUMN_IMAGE, imageInByte);
        Cursor cursor = database.query(SQLiteTable.Ads.TABLE_NAME,
                null, null, null, null, null, null);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserID", "");
        database.update(SQLiteTable.Ads.TABLE_NAME, values, SQLiteTable.Ads.COLUMN_ID + "=" + Integer.parseInt(userId), null);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено, можно открывать камеру
                openCamera();
            } else {
                // Разрешение не предоставлено, показать сообщение пользователю
                // ...
            }
        }

        if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // разрешение предоставлено, можно отправлять уведомления
                } else {
                    // разрешение не предоставлено, можно попросить пользователя еще раз или отказаться от отправки уведомлений
                }
                return;
            }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Получаем сделанное фото из камеры
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Drawable circularDrawable = getResources().getDrawable(R.drawable.ic_circle);
                imageBitmap = getCircleBitmap(imageBitmap, circularDrawable);
                binding.ava.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Получаем выбранное изображение из галереи
                Uri selectedImageUri = data.getData();
                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Drawable circularDrawable = getResources().getDrawable(R.drawable.ic_circle);
                imageBitmap = getCircleBitmap(imageBitmap, circularDrawable);
                binding.ava.setImageBitmap(imageBitmap);
            }
            Bitmap bitmap = ((BitmapDrawable) binding.ava.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageInByte = baos.toByteArray();
            SQLiteDatabase database = new SQLiteHelper(getActivity()).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQLiteTable.User.COLUMN_IMAGE, imageInByte);
            database.update(SQLiteTable.User.TABLE_NAME, values, SQLiteTable.User.COLUMN_ID + "=" + Integer.parseInt(String.valueOf(user.id)), null);
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap, Drawable drawable) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(canvas);

        return output;
    }

    private void fillData() {
        String[] projection = {
                SQLiteTable.Ads.COLUMN_ID,
                SQLiteTable.Ads.COLUMN_NAME,
                SQLiteTable.Ads.COLUMN_PRICE,
                SQLiteTable.Ads.COLUMN_USER,
                SQLiteTable.Ads.COLUMN_IMAGE
        };
        SQLiteDatabase database = new SQLiteHelper(getActivity()).getReadableDatabase();
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
    private User getUser(String userId) {
        SQLiteDatabase database = new SQLiteHelper(getActivity()).getReadableDatabase();
        String[] userProjection = {
                SQLiteTable.User.COLUMN_NAME,
                SQLiteTable.User.COLUMN_SELLER,
                SQLiteTable.User.COLUMN_ID,
                SQLiteTable.User.COLUMN_IMAGE
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
        String name = userCursor.getString(userCursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_NAME));
        int sellerIs = userCursor.getInt(userCursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_SELLER));
        int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(SQLiteTable.User.COLUMN_ID));
        byte[] imageInByte = userCursor.getBlob(userCursor.getColumnIndexOrThrow(SQLiteTable.Ads.COLUMN_IMAGE));
        boolean seller = false;
        if (sellerIs == 1){
            seller = true;
        }

        User user = new User(name, seller);
        user.id = id;
        user.setImage(imageInByte);
        return user;
    }


}
