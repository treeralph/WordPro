package com.example.wordpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.SelectCategoryDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddContentActivity extends AppCompatActivity {

    private String TAG = "AddContentActivity";
    private int IMAGE_SELECT_REQUEST_CODE = 10;

    TextView imageButton;
    ImageView imageView;
    EditText titleEditText;
    TextView categorySelectButton;
    TextView fromLanguageSelectButton;
    TextView toLanguageSelectButton;
    EditText searchEditText;
    ImageView searchButton;
    RecyclerView recyclerView;
    CardView makeContentButton;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        activityViewInitializer();
    }

    private void activityViewInitializer(){

        imageButton = findViewById(R.id.AddContentActivityImageButton);
        imageView = findViewById(R.id.AddContentActivityImageView);
        titleEditText = findViewById(R.id.AddContentActivityTitleEditText);
        categorySelectButton = findViewById(R.id.AddContentActivityCategorySelectTextView);
        fromLanguageSelectButton = findViewById(R.id.AddContentActivityFromLanguageSelectTextView);
        toLanguageSelectButton = findViewById(R.id.AddContentActivityToLanguageSelectTextView);
        searchEditText = findViewById(R.id.AddContentActivitySearchEditText);
        searchButton = findViewById(R.id.AddContentActivitySearchButton);
        recyclerView = findViewById(R.id.AddContentActivityRecyclerView);
        makeContentButton = findViewById(R.id.AddContentActivityMakeContentButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_SELECT_REQUEST_CODE);
            }
        });

        categorySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCategoryDialog selectCategoryDialog = new SelectCategoryDialog(AddContentActivity.this,
                        SelectCategoryDialog.AddContentActivity_CategorySelect_CODE,
                        new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                String target = (String) object;
                                categorySelectButton.setText(target);
                                categorySelectButton.setTextColor(Color.BLACK);
                            }
                        });
                selectCategoryDialog.setCanceledOnTouchOutside(true);
                selectCategoryDialog.setCancelable(true);
                selectCategoryDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                selectCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                selectCategoryDialog.show();
            }
        });

        fromLanguageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCategoryDialog selectCategoryDialog = new SelectCategoryDialog(AddContentActivity.this,
                        SelectCategoryDialog.AddContentActivity_LanguageSelect_CODE,
                        new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                String target = (String) object;
                                fromLanguageSelectButton.setText(target);
                                fromLanguageSelectButton.setTextColor(Color.BLACK);
                            }
                        });
                selectCategoryDialog.setCanceledOnTouchOutside(true);
                selectCategoryDialog.setCancelable(true);
                selectCategoryDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                selectCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                selectCategoryDialog.show();
            }
        });

        toLanguageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCategoryDialog selectCategoryDialog = new SelectCategoryDialog(AddContentActivity.this,
                        SelectCategoryDialog.AddContentActivity_LanguageSelect_CODE,
                        new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                String target = (String) object;
                                toLanguageSelectButton.setText(target);
                                toLanguageSelectButton.setTextColor(Color.BLACK);
                            }
                        });
                selectCategoryDialog.setCanceledOnTouchOutside(true);
                selectCategoryDialog.setCancelable(true);
                selectCategoryDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                selectCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                selectCategoryDialog.show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        makeContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_SELECT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                try {
                    Log.d(TAG, "onActivityResult: try statement");
                    InputStream in = null;
                    in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    imageView.setImageBitmap(img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}