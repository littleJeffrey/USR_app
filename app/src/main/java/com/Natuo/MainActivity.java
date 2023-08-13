package com.Natuo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.os.Bundle;

import android.content.ContentResolver;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.squareup.picasso.Picasso;


import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.lang.String;
import java.util.List;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity  {

    private Button btn_upload_crop, btn_camera, btn_upload_insect, btn_open_folder, btn_upload_culture;
    private ImageButton btn_speak;
    private TextView textView_Latitude, textView_Longitude;
    private ImageView imageView_Camera;
    private Switch humidity_switch, degree_switch;
    private EditText editText_humidity, editText_degree, text_information;
    private  Uri mImageUri;
    private RadioGroup radioGroup1;
    private RadioButton radButton1, radButton2, radButton3, radButton4;
    private CheckPermission checkPermission = new CheckPermission(this);
    private DoubleCheck doubleCheck = new DoubleCheck();
    //private GPS gps;
    private String filename;
    private boolean humity_upload = false;
    private boolean degree_upload = false;
    private boolean notNull = false;
    private String currentAbsolutePath;
    private static final int TAKE_PHOTO = 100;
    private static final int UPLOAD_INSECT = 101;
    private static final int UPLOAD_CROP = 102;
    private static final int UPLOAD_CULTURE = 103;
    private static final int OPENFOLDER = 99;
    private String tribe_name = "山美部落";


    private File photoFile = null;


    private static String UPLOAD_URL = "http://140.134.26.3/nantou/insert_image.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission.check();


        //gps = new GPS(this, textView_Latitude, textView_Longitude);
        //gps.getGPS();
        tribe_name = "";

        btn_camera = (Button)findViewById(R.id.button_camera);
        btn_upload_crop = (Button)findViewById(R.id.button_upload_crop);
        btn_upload_insect = (Button)findViewById(R.id.button_upload_insect);
        btn_open_folder = (Button) findViewById(R.id.openFolder);
        btn_upload_culture = (Button) findViewById(R.id.button_culture);

        imageView_Camera = (ImageView)findViewById(R.id.Image_view_camera);

        editText_humidity = (EditText)findViewById(R.id.editText_humidity);
        humidity_switch = (Switch)findViewById(R.id.switch_humidity);

        degree_switch = (Switch)findViewById(R.id.switch_degree);
        editText_degree = (EditText)findViewById(R.id.editText_degree);

        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1) ;
        radButton1 = (RadioButton)findViewById(R.id.radioButton1);
        radButton2 = (RadioButton)findViewById(R.id.radioButton2);
        radButton3 = (RadioButton)findViewById(R.id.radioButton3);
        radButton4 = (RadioButton)findViewById(R.id.radioButton4);

        text_information = (EditText) findViewById(R.id.text_information);
        btn_speak = (ImageButton) findViewById(R.id.button_speak);

        btn_camera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //File ImageFile = null;
                try {
                    photoFile = createImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }

                if (photoFile != null){
                    /*
                    if (Build.VERSION.SDK_INT >= 24){
                        mImageUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                    }else{
                        mImageUri = Uri.fromFile(photoFile);
                    }*/
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo take on " + System.currentTimeMillis());
                    mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, TAKE_PHOTO);

                }



            }



        });

        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, 0);
                }
                catch(ActivityNotFoundException e)
                {
                    String appPackageName = "com.google.android.googlequicksearchbox";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,   Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                    startActivity(browserIntent);

                }

            }
        });

        checkListener();
        buttonListener();
        setRadioGroup1Listener();
    }


    private void buttonListener(){

        btn_upload_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveInternet()){
                    uploadImage(UPLOAD_CROP);
                }else {
                    Toast.makeText(MainActivity.this,"請連接網路", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_upload_insect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveInternet()) {
                    uploadImage(UPLOAD_INSECT);
                }else{
                    Toast.makeText(MainActivity.this,"請連接網路", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_open_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                intent.setAction(Intent.ACTION_PICK);

                startActivityForResult(intent, OPENFOLDER);
            }
        });

        btn_upload_culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveInternet()) {
                    uploadImage(UPLOAD_CULTURE);
                }else{
                    Toast.makeText(MainActivity.this,"請連接網路", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setRadioGroup1Listener(){
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radButton1.getId()){
                    tribe_name = "山美部落";
                    Toast.makeText(MainActivity.this, "山美部落", Toast.LENGTH_SHORT).show();
                }else if (checkedId == radButton2.getId()){
                    tribe_name = "眉溪部落";
                    Toast.makeText(MainActivity.this, "眉溪部落", Toast.LENGTH_SHORT).show();
                }else if (checkedId == radButton3.getId()){
                    tribe_name = "古樓部落";
                    Toast.makeText(MainActivity.this, "古樓部落", Toast.LENGTH_SHORT).show();
                }else if (checkedId == radButton4.getId()){
                    tribe_name = "清流部落";
                    Toast.makeText(MainActivity.this, "清流部落", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean checkUpload(){
        String humity_string = editText_humidity.getText().toString();
        String degree_string = editText_degree.getText().toString();


        if(!doubleCheck.checkNotNull(humity_string) && !doubleCheck.checkNotNull(degree_string)){
            Toast.makeText(this, "溫度或濕度不能為空白", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!doubleCheck.checkDegreeCondition(Double.valueOf(degree_string))) {
            Toast.makeText(this, "溫度必須大於0或,小於40", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!doubleCheck.checkHumityCondition(Double.valueOf(humity_string))){
            Toast.makeText(this, "濕度必須大於50或,小於100", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!humity_upload) {
            Toast.makeText(this, "請鎖定濕度確認鍵", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!degree_upload) {
            Toast.makeText(this, "請鎖定溫度確認鍵", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(tribe_name == "") {
            Toast.makeText(this, "請選擇部落", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return true;
    }

    public void checkListener(){
        humidity_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(buttonView.isChecked()){
                    editText_humidity.setFocusable(false);
                    humity_upload = true;
                }
                else{
                    editText_humidity.setFocusable(true);
                    editText_humidity.setFocusableInTouchMode(true);
                    editText_humidity.requestFocus();
                    humity_upload = false;
                }
            }
        });
        degree_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(buttonView.isChecked()){
                    editText_degree.setFocusable(false);
                    degree_upload = true;
                }
                else{
                    editText_degree.setFocusable(true);
                    editText_degree.setFocusableInTouchMode(true);
                    editText_degree.requestFocus();
                    degree_upload = false;
                }
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path;
        path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    protected void uploadImage(int upload_number){
        if(upload_number == UPLOAD_CROP ||(upload_number == UPLOAD_INSECT)){
            if (!checkUpload()) return;
        }


        String caption = null;
        if(upload_number == UPLOAD_CROP){
            caption = "crop";

        }else if(upload_number == UPLOAD_INSECT){
            caption = "insect";
        }else if(upload_number == UPLOAD_CULTURE){
            caption = "culture";
        }

        if(mImageUri != null) {

            String path = getPath(mImageUri);
            try {
                MultipartUploadRequest upload = new MultipartUploadRequest(this, UPLOAD_URL)
                        .setMethod("POST")
                        .addFileToUpload(path, "image")
                        .addParameter("degree", editText_degree.getText().toString())
                        .addParameter("humidity", editText_humidity.getText().toString())
                        .addParameter("caption", caption)
                        .addParameter("tribe", tribe_name)
                        .addParameter("information", String.valueOf(text_information.getText()))
                        .setMaxRetries(2);

                upload.startUpload();
                iniTializeAll();
                makeText(this, "圖片已上傳", Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap image;

        if ((requestCode == TAKE_PHOTO)) {
            //Bitmap image = (Bitmap) data.getExtras().get("data");

            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                imageView_Camera.setImageBitmap(image);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        }else if (requestCode == OPENFOLDER){

            mImageUri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                imageView_Camera.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                String spokenText = results.get(0);
                Toast.makeText(this,spokenText,Toast.LENGTH_SHORT).show();
                text_information.setText(spokenText);
            }
        }


    }

    protected void iniTializeAll(){
        notNull = false;
        degree_switch.setChecked(false);
        humidity_switch.setChecked(false);

        editText_degree.setFocusable(true);
        editText_degree.setFocusableInTouchMode(true);
        editText_degree.requestFocus();
        degree_upload = false;

        editText_humidity.setFocusable(true);
        editText_humidity.setFocusableInTouchMode(true);
        editText_humidity.requestFocus();
        humity_upload = false;

        Picasso.with(this).load((Uri) null).into(imageView_Camera);
        editText_degree.setText("");
        editText_humidity.setText("");
        text_information.setText("");

        radioGroup1.clearCheck();
    }

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMddmmss").format(new Date(System.currentTimeMillis()));
        String imageFileName = "JPEG_" + timeStamp ;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(imageFileName ,".jpg", storageDir);
        currentAbsolutePath = image.getAbsolutePath();
        return image;
    }

    private boolean haveInternet(){
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()){
            result = false;
        }else{
            if (!info.isAvailable()){
                result = false;
            }else {
                result = true;
            }
        }
        return  result;
    }



}
