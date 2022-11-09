package com.example.logbook03;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button forward_button, backward_button;
    private int index = 0;
    Button add_button;
    private EditText add_link_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyDatabaseHelper helper = new MyDatabaseHelper(this);
        imageView = findViewById(R.id.imageView);
        backward_button = findViewById(R.id.backward_button);
        forward_button = findViewById(R.id.forward_button);

//        helper.addImage("https://hips.hearstapps.com/hmg-prod/images/heisenberg-1580845638.jpg");
//        helper.addImage("https://cdn.wionews.com/sites/default/files/2022/08/27/290896-aaron-paul-jesse-pinkman.PNG");

        showImage();
        add_link_input = findViewById(R.id.add_link_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(v -> {

            if (add_link_input.getText().toString().length() == 0) {
                add_link_input.setError("You must enter something!");

            } else {

                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.addImage(add_link_input.getText().toString().trim());
                Toast.makeText(MainActivity.this, "Image added successfully!", Toast.LENGTH_SHORT).show();
                add_link_input.getText().clear();
                Glide.with(MainActivity.this)
                        .load(getLastImageUrl())
                        .centerCrop()
                        .into(imageView);
            }

        });
        backward_button.setOnClickListener(view -> {
            MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
            Cursor cursor = db.readAllImages();
            if (index == 0){
                cursor.moveToLast();
                index = cursor.getPosition();
            } else {
                index--;
                cursor.moveToPosition(index);

            }
            String url = cursor.getString(1);
            Glide.with(MainActivity.this)
                    .load(url)
                    .centerCrop()
                    .into(imageView);

        });

        forward_button.setOnClickListener(view -> {
            MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
            Cursor cursor = db.readAllImages();
            cursor.moveToLast();
            int last_index = cursor.getPosition();
            if (index == last_index){
                cursor.moveToFirst();
                index = cursor.getPosition();
            } else {
                index++;
                cursor.moveToPosition(index);

            }
            String url = cursor.getString(1);
            Glide.with(MainActivity.this)
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        });
    }

    String getFirstImageUrl(){
        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
        Cursor cursor = db.readAllImages();
        cursor.moveToFirst();
        String url = cursor.getString(1);
        index = cursor.getPosition();
        return url;
    }

    String getLastImageUrl(){
        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
        Cursor cursor = db.readAllImages();
        cursor.moveToLast();
        String url = cursor.getString(1);
        index = cursor.getPosition();
        return url;
    }

    private void showImage(){
        Glide.with(MainActivity.this)
                .load(getFirstImageUrl())
                .centerCrop()
                .into(imageView);
    }

}