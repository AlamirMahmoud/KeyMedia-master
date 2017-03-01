package ameircom.keymedia.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ameircom.keymedia.Activity.transition.FabTransform;
import ameircom.keymedia.AppManger.AndroidMultiPartEntity;
import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.UserModel;
import ameircom.keymedia.R;
import ameircom.keymedia.internal_db.Room_tabel;

public class Add_productActivity extends AppCompatActivity {
    View add_img , progressBarView ;
    ImageView  uploaded_img ;
    String uploaded_img_name = "" ;
    TextView txtPercentage;
    ProgressBar progressBar ;
    protected int RESULT_LOAD_IMAGE=1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 262 ;

    View main_container ;
    EditText title ;
    String type ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) no_storage_permission();

        setContentView(R.layout.activity_add_product);

        main_container = findViewById(R.id.add_product_main_con);
        title =(EditText) findViewById(R.id.add_prod_title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            FabTransform.setup(this, main_container);}
        type = getIntent().getExtras().getString("type");
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        add_img = findViewById(R.id.add_img);
        uploaded_img = (ImageView) findViewById(R.id.picked_img);
    }

    public void pic_img(View view) {
        get_img();
    }
    void get_img(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            cursor.close();

            Log.e("picturePath",picturePath + "");
            new ResizeImage().execute(picturePath);

        }
    }

    public void add_product(View view) {
            if (!validateImg()) {
                return;
            }

            findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config.OPERATION, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e("upload response:", " " + response);


                            findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                            supportFinishAfterTransition();



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof NoConnectionError) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        String message =   getString(R.string.NoConnection);
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                //sending your email and pass
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("request", String.valueOf(Config.ADD_PROD));
                    params.put("title", title.getText().toString());
                    params.put("img", uploaded_img_name);
                    params.put("type", type);

                    Log.e("add product", "params: " + params.toString());
                    return params;
                }
            };
            int socketTimeout = 10000; // 10 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            strReq.setRetryPolicy(policy);
            //Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);



    }



    public class ResizeImage extends AsyncTask<String, Integer, String> {
        protected String  filePath,fileName ;
        long totalSize = 0;
        Boolean ok = false ;
        Bitmap resized_bitmap ;


        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        // Decode image in background.
        @Override
        protected String doInBackground(String... params) {
            filePath = params[0];
            File uploadFile = null ;
            String response = null;
            try {
                if (!type.equals(String.valueOf(Config.pa360)))
                resized_bitmap =  decodeSampledBitmapFromPath(filePath, 1080, 1920) ;
                else
                    resized_bitmap =  decodeSampledBitmapFromPath(filePath, 2048, 2048) ;

                uploadFile = bitmaptofile(resized_bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (uploadFile == null){
                Log.e("category","file"+"null");
                uploadFile = new File(filePath);
            }
            fileName = uploadFile.getName();
            response = uploadFile(uploadFile);


            return    response ;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(String result) {
            showAlert(result);
            progressBarView.setVisibility(View.GONE);
        }

        protected File  bitmaptofile(Bitmap bitmap) throws IOException {
            File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
            File outputFile = File.createTempFile(currentDateFormat(),".jpg" , outputDir);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputFile;
        }
        /**
         * Method to show alert dialog
         * */
        protected void showAlert(String message) {
            if (ok){
                String filePathName = new File(filePath).getName();
                Log.e("category","filename  "+fileName +"file path   " + filePathName);
                uploaded_img_name = fileName;
                uploaded_img.setImageBitmap(resized_bitmap);
                validateImg();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_productActivity.this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }

        @SuppressWarnings("deprecation")
        protected String uploadFile(File sourceFile) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.OPERATION);
            Log.e("category","url"+httppost.getURI());
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                Log.e("transferd" , num +"");
                                int precentage = (int) ((num / (float) totalSize) * 100);
                                if (precentage <=99){
                                    publishProgress(precentage);
                                }else publishProgress(99);
                            }
                        });


                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("request", new StringBody(Config.UPLOAD_IMG));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    ok=true ;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

    }

    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    public static Bitmap decodeSampledBitmapFromPath( String FilePath,
                                                      int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( FilePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile( FilePath, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.e("sapleSize" , inSampleSize + "");
        return inSampleSize;
    }
    protected boolean validateImg() {
        if ( uploaded_img_name.equals("null") || uploaded_img_name.isEmpty() ){
            findViewById(R.id.img_err).setVisibility(View.VISIBLE);
            return false ;
        }else {
            findViewById(R.id.img_err).setVisibility(View.GONE);
        }
        return true ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //  no_gps_permition();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Add_productActivity.this, "Permission needed to run your app", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void no_storage_permission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {



            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);


        }
    }
}
