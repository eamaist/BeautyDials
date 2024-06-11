package com.example.BeautyDials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.BeautyDials.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    public FragmentStateAdapter adapter;
    private ActivityHomeBinding binding;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MyAdapter(this);
        viewPager = binding.page;
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 2:
                        animateIcon(binding.homeIcon, false);
                        animateIcon(binding.mapIcon, false);
                        animateIcon(binding.userIcon, true);
                        break;
                    case 1:
                        animateIcon(binding.homeIcon, false);
                        animateIcon(binding.mapIcon, true);
                        animateIcon(binding.userIcon, false);
                        break;
                    case 0:
                        animateIcon(binding.homeIcon, true);
                        animateIcon(binding.mapIcon, false);
                        animateIcon(binding.userIcon, false);
                        break;
                }
            }
        });

        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, false);

            }
        });

        binding.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, false);

            }
        });

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, false);

            }
        });

        AppCompatImageButton button = findViewById(R.id.aboutAuthor);

        // Добавьте слушатель нажатий для кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutAuthorDialog();
            }
        });


    }





    private void showAboutAuthorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Об авторе");
        builder.setIcon(R.drawable.my_ava);
        String developerInfo = "Майстренко Е.А.\n" +
                "ИКБО-13-22\n" +
                "2024\n" +
                "eamaist@yandex.ru";

        builder.setMessage(developerInfo);
        builder.setPositiveButton("ОК", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void navigateToPersonFragment() {
        viewPager.setCurrentItem(0, false);
    }

    private void showAboutProgDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("О программе");
        builder.setMessage("Год разработки: 2024");
        builder.setPositiveButton("ОК", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void animateIcon(ImageView icon, boolean shouldEnlarge) {
        Animation anim;
        if (shouldEnlarge) {
            anim = AnimationUtils.loadAnimation(this, R.anim.resize_icon);
        } else {
            anim = AnimationUtils.loadAnimation(this, R.anim.shrink_icon);
        }
        icon.startAnimation(anim);
    }

}
