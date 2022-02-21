package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Data.MyDatabase;
import Utils.Utilities;

public class HighlightAddActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private ImageView selectedImageView;
    private EditText titleEditText, descriptionEditText;
    private MaterialToolbar toolbar;
    private MyDatabase database;
    private String title, description;
    private double lat, lon;
    private int tourId;
    private String nowDateAfterFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_add);
        database = new MyDatabase(this, 1);
        toolbar = findViewById(R.id.topAppBarHighlightAdd);
        selectedImageView = (ImageView) findViewById(R.id.new_memory_selected_image);
        titleEditText = (EditText) findViewById(R.id.new_highlight_title);
        descriptionEditText = findViewById(R.id.new_highlight_description);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                save();
                return false;
            }
        });
        tourId = getIntent().getIntExtra("TourId",0);
        lat = getIntent().getDoubleExtra("Lat",0);
        lon = getIntent().getDoubleExtra("Lon",0);
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ComponentName a = takePictureIntent.resolveActivity(getPackageManager());
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        else
            Toast.makeText(HighlightAddActivity.this, getResources().getString(R.string.cameraUnavaliable), Toast.LENGTH_LONG).show();
    }

    public void save() {
        Bitmap image = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
        int width = image.getWidth();
        int height = image.getHeight();
        double a = 0.0;
        byte[] b;
        if(width > 1280 || height > 1280){
            int c = Math.max(width, height);
            a = c/1280;
            float scaleWidth = (float)((float)image.getWidth()/a) / image.getWidth();
            float scaleHeight = (float)((float)image.getHeight()/a) / image.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resized = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
            b = getBitmapAsByteArray(resized);
        }
        else{
            b = getBitmapAsByteArray(image);
        }

        title = titleEditText.getText().toString() != "" ? titleEditText.getText().toString() : null;
        description = descriptionEditText.getText().toString() != "" ? descriptionEditText.getText().toString() : null;

        Date nowDate = Utilities.convertToDate(Calendar.getInstance());
        nowDateAfterFormat = Utilities.sdf.format(nowDate);

        database.addHighlight(b,title, description, nowDateAfterFormat, lat, lon, tourId);
        finish();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                selectedImageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImageView.setImageBitmap(imageBitmap);
        }
    }
}