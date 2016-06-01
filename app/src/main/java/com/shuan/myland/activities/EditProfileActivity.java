package com.shuan.myland.activities;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.R;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Common mApp;
    private ImageButton edit;
    private CircleImageView usrImg;
    private final int GALLERY = 1;
    private final int PHOTO = 2;
    private final int CROP = 3;
    private Uri picUri;
    private File pic;
    private String Path,imgName,updateImg;
    private RelativeLayout col1, col2,row;
    private ScrollView scroll;
    private php call = new php();
    private EditText usrName, phNo, emailId, oldPass, newPass, confrmPass,Addr;
    private FancyButton submit, update;
    private ProgressDialog pDialog;
    private HttpUrlConnectionParser parser=new HttpUrlConnectionParser();
    private HashMap<String,String> profileData;
    private DisplayImageOptions options;
    private String name,phno,email,addr,img,level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        name=getIntent().getStringExtra("name");
        phno=getIntent().getStringExtra("phno");
        email=getIntent().getStringExtra("email");
        addr=getIntent().getStringExtra("addr");
        level=getIntent().getStringExtra("level");
        img=getIntent().getStringExtra("img");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usrImg = (CircleImageView) findViewById(R.id.user_img);
        edit = (ImageButton) findViewById(R.id.edit);
        col1 = (RelativeLayout) findViewById(R.id.col1);
        col2 = (RelativeLayout) findViewById(R.id.col2);
        usrName = (EditText) findViewById(R.id.usr_name);
        phNo = (EditText) findViewById(R.id.ph_no);
        emailId = (EditText) findViewById(R.id.email_id);
        Addr= (EditText) findViewById(R.id.addr);
        oldPass = (EditText) findViewById(R.id.old_pass);
        newPass = (EditText) findViewById(R.id.new_pass);
        confrmPass = (EditText) findViewById(R.id.confrm_pass);
        submit = (FancyButton) findViewById(R.id.submit);
        update = (FancyButton) findViewById(R.id.update);
        row= (RelativeLayout) findViewById(R.id.row);

        if(level!=""){
            if (level.equalsIgnoreCase("silver")) {
                row.setBackgroundColor(getResources().getColor(R.color.silver));
            } else if (level.equalsIgnoreCase("gold")) {
                row.setBackgroundColor(getResources().getColor(R.color.gold));
            } else {
                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }else {
            if(mApp.getSharedPreferences().getString(Common.USR_LEVEL,"").equalsIgnoreCase("silver")){
                row.setBackgroundColor(getResources().getColor(R.color.silver));
            }else if(mApp.getSharedPreferences().getString(Common.USR_LEVEL,"").equalsIgnoreCase("gold")){
                row.setBackgroundColor(getResources().getColor(R.color.gold));
            }else {
                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }

        if(name!=""){usrName.setText(name);}else {usrName.setText(mApp.getSharedPreferences().getString(Common.USR_FULLNAME, ""));}

        if(phno!=""){phNo.setText(phno);}else {phNo.setText(mApp.getSharedPreferences().getString(Common.USR_PHNO, ""));}

        if(email!=""){emailId.setText(email);}else {emailId.setText(mApp.getSharedPreferences().getString(Common.USR_EMAIL, ""));}

        if(addr!=""){Addr.setText(addr);}else{Addr.setText(mApp.getSharedPreferences().getString(Common.USR_ADDR, ""));}

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.user)
                .showImageForEmptyUri(R.drawable.user)
                .showImageOnFail(R.drawable.user)
                .build();

        if(img!=""){setImage(img, usrImg);}else{setImage(mApp.getSharedPreferences().getString(Common.USR_IMG, ""), usrImg);}


        newPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!oldPass.getText().toString().equalsIgnoreCase(mApp.getSharedPreferences().getString(Common.USR_PASS, ""))) {
                        oldPass.setError("Enter Correct Old Password");
                    }
                }
                return false;
            }
        });

        newPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oldPass.getText().toString().equalsIgnoreCase(mApp.getSharedPreferences().getString(Common.USR_PASS, ""))) {
                    oldPass.setError("Enter Correct Old Password");
                }
            }
        });


        confrmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (confrmPass.getText().toString().equalsIgnoreCase(confrmPass.getText().toString())) {
                    confrmPass.setError("Password Not Match");
                }else{}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scroll = (ScrollView) findViewById(R.id.scroll);
        col2.requestFocus();
        col1.setOnClickListener(this);
        edit.setOnClickListener(this);
        submit.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    private void setImage(String img, CircleImageView usrImg) {
        ImageLoader.getInstance().displayImage(img, usrImg, options, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                loadImg();
                break;
            case R.id.col1:
                if (col2.isShown()) {
                    col2.setVisibility(View.GONE);
                } else {
                    col2.setVisibility(View.VISIBLE);
                    col2.scrollTo(0,20);
                    scroll.scrollTo(0,20);
                }
                break;
            case R.id.submit:
                new updateData().execute();
                break;
            case R.id.update:
                new updatePass().execute();
                break;

        }
    }

    private void loadImg() {
        final String[] action = {"From Camera", "From Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Select Picture Using");
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itm) {
                if (action[itm].equalsIgnoreCase("From Camera")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    pic = new File(Environment.getExternalStorageDirectory(),
                            "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    picUri = Uri.fromFile(pic);

                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);

                    cameraIntent.putExtra("return-data", true);
                    startActivityForResult(cameraIntent, PHOTO);

                } else {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra("return-data", true);
                    startActivityForResult(photoPickerIntent, GALLERY);
                }

            }
        }).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO && resultCode == RESULT_OK) {
            CropImage();
        } else if (requestCode == GALLERY) {
            if (data != null) {
                picUri = data.getData();
                CropImage();
            }
        } else if (requestCode == CROP) {
            if (data != null) {

                Bundle extras = data.getExtras();

                // get the cropped bitmap
                Bitmap photo = extras.getParcelable("data");
                usrImg.setImageBitmap(photo);

                if (saveFile(photo)) {
                    new updateProfile().execute();
                }


            }
        }
    }

    private boolean saveFile(Bitmap photo) {
        Bitmap b2 = null;
        File destination;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b2 = Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), false);
        b2.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        destination = new File(Environment.getExternalStorageDirectory(),
                "IMG_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path = destination.getAbsolutePath();
        imgName=destination.getName();
        if (Path != null) {
            return true;
        } else {
            return false;
        }

    }

    private void CropImage() {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(picUri, "image/*");

            intent.putExtra("crop", "true");
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 4);
            intent.putExtra("scaleUpIfNeeded", true);

            intent.putExtra("return-data", true);

            startActivityForResult(intent, CROP);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }


    public class updateProfile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                String fileName = Path;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(fileName);

                if (!sourceFile.isFile()) {

                    Log.e("uploadFile", "Source File not exist :" + fileName);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL(call.update_pro_pic);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("uploaded_file", fileName);


                        dos = new DataOutputStream(conn.getOutputStream());


                        dos.writeBytes(twoHyphens + boundary + lineEnd);


                        dos.writeBytes(twoHyphens + boundary + lineEnd);

                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];
                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        }

                        // send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();


                        Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                        if (serverResponseCode == 200) {
                            profileData=new HashMap<String,String>();
                            profileData.put("id", mApp.getSharedPreferences().getString(Common.USR_ID, ""));
                            profileData.put("path", imgName.toString());
                            try{
                                final JSONObject json=parser.makeHttpUrlConnection(call.update_pro_pic_path,profileData);
                                int succ=json.getInt("success");
                                if(succ==0){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Profile Picture Can't update. Try Again...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    JSONArray jsonArray=json.getJSONArray("update");
                                    JSONObject child=jsonArray.getJSONObject(0);
                                    updateImg=child.optString("pro_pic");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            mApp.getSharedPreferences().edit().putString(Common.USR_IMG,updateImg).commit();
                                            Toast.makeText(getApplicationContext(), "Profile Picture updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (Exception e){}



                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();


                    } catch (MalformedURLException ex) {

                        ex.printStackTrace();

                        runOnUiThread(new Runnable() {
                            public void run() {


                                Toast.makeText(EditProfileActivity.this,
                                        "MalformedURLException", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                    } catch (final Exception e) {


                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(EditProfileActivity.this,
                                        "Got Exception : see logcat ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                }
            } catch (Exception e) {
            }

            return null;
        }
    }


    public class updateData extends AsyncTask<String, String, String> {

        String usr=usrName.getText().toString();
        String ph=phNo.getText().toString();
        String email=emailId.getText().toString();
        String addr=Addr.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Update Your Info...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            profileData=new HashMap<String,String>();
            profileData.put("id",mApp.getSharedPreferences().getString(Common.USR_ID,""));
            profileData.put("usr", usr.toString());
            profileData.put("ph", ph.toString());
            profileData.put("email", email.toString());
            profileData.put("addr",addr.toString());

            try{
                JSONObject json=parser.makeHttpUrlConnection(call.update_pro,profileData);
                int succ=json.getInt("success");
                if(succ==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Profile Can't update. Try Again...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Profile updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }catch (Exception e){}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.cancel();
        }
    }

    public class updatePass extends AsyncTask<String, String, String> {
        String pass=confrmPass.getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Update Your Password...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            profileData=new HashMap<String,String>();
            profileData.put("id",mApp.getSharedPreferences().getString(Common.USR_ID,""));
            profileData.put("pass", pass.toString());
            try{

                JSONObject json=parser.makeHttpUrlConnection(call.update_pro_pass,profileData);
                int succ=json.getInt("success");
                if(succ==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Password Can't update. Try Again...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mApp.getSharedPreferences().edit().putString(Common.USR_PASS,confrmPass.getText().toString()).commit();
                            oldPass.setText("");
                            newPass.setText("");
                            confrmPass.setText("");
                            Toast.makeText(getApplicationContext(), "Password updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }catch (Exception e){}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.cancel();
        }
    }
}
