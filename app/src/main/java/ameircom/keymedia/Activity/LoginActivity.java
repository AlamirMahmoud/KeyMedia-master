package ameircom.keymedia.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.UserModel;
import ameircom.keymedia.R;
import ameircom.keymedia.internal_db.Room_tabel;

public class LoginActivity extends AppCompatActivity {
    private String TAG = LoginActivity.class.getSimpleName();

    private EditText inputName, inputPass;
    private TextInputLayout inputLayoutName, inputLayoutPass;
    private Button btnLogin, btnReg;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputLayoutName = (TextInputLayout) findViewById(R.id.email_login_layout);
        inputLayoutPass = (TextInputLayout) findViewById(R.id.password_login_layout);
        inputName = (EditText) findViewById(R.id.email_login);
        inputPass = (EditText) findViewById(R.id.password_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReg = (Button) findViewById(R.id.btn_register);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPass.addTextChangedListener(new MyTextWatcher(inputPass));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputLayoutName.setErrorEnabled(false);
                inputLayoutPass.setErrorEnabled(false);
               startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });


    }
    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_pass:
                    validatePass();
                    break;
            }
        }


    }

    private void login() {
        if (!validateName()) {
            return;
        }

        if (!validatePass()) {
            return;
        }
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        final String name = inputName.getText().toString();
        final String pass = inputPass.getText().toString();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.OPERATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login response: " + response);
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("operation");
                    if (res.equals("done") ){
                        JSONObject userObj = obj.getJSONObject("user");
                        UserModel user = new UserModel();
                            user.setId(  userObj.getString("userID"));
                               user.setName( userObj.getString("userName"));
                               user.setPhone( userObj.getString("userNumber"));
                                user.setType(userObj.getString("userType"));
                                user.setEmail(userObj.getString("userEmail"));
                        // storing user in shared preferences
                        AppController.getInstance().getPrefManager().storeUser(user);
                        Log.d("FCM", FirebaseInstanceId.getInstance().getToken()+"");
                        if (user.getType().equals("admin"))
                        {
                            admin_chat_subscribe();
                        }
                        else{
                            Room_tabel room_tabel  =  new Room_tabel() ;
                            room_tabel.room_id = user.getId() ;
                            room_tabel.save();
                            FirebaseMessaging.getInstance().subscribeToTopic("room_"+user.getId());
                        }


                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();

                    }else if(res.equals("failed")) {
                        String message =   obj.getString("message");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    login();
                }
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
                params.put("request", String.valueOf(Config.LOGIN));
                params.put("name", name);
                params.put("pass", pass);

                Log.e(TAG, "params: " + params.toString());
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


    // Validating name
    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            incorrectUserName();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectUserName() {
        inputLayoutName.setError(getString(R.string.Enter_valid_Username));
        requestFocus(inputName);
    }

    // Validating Pass
    private boolean validatePass() {
        String Pass = inputPass.getText().toString().trim();

        if (Pass.isEmpty()) {
            incorrectPass();
            return false;
        } else {
            inputLayoutPass.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectPass() {
        inputLayoutPass.setError(getString(R.string.Enter_valid_PASS));
        requestFocus(inputPass);
    }

    private void requestFocus(View view) {
        Log.d("requestFocus",view.requestFocus()+"");
        //foucus on view
        if (view.requestFocus()) {
            /*m7taga sho3'l
          im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Log.d("inputmetod", im.isAcceptingText() + "");
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
*/
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, Config.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Alert Dialog");

                // Setting Dialog Message
                alertDialog.setMessage("This device is not supported. Google Play Services not installed");



                // Setting OK Button
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
            return false;
        }
        return true;
    }

    public  void admin_chat_subscribe(){
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest feedRequest = new StringRequest(Request.Method.POST, Config.OPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    JSONObject resObj = new JSONObject(response);
                    JSONArray feedArr = resObj.getJSONArray("users");
                    for (int i=0 ; i<feedArr.length();i++){
                        JSONObject userObj = feedArr.getJSONObject(i);

                        UserModel user = new UserModel();
                        user.setId(  userObj.getString("userID"));
                        user.setName( userObj.getString("userName"));

                        Room_tabel room_tabel  =  new Room_tabel() ;
                        if (!room_tabel.check(user.getId())){
                        room_tabel.room_id = user.getId() ;
                        room_tabel.name = user.getName();
                        room_tabel.save();
                        FirebaseMessaging.getInstance().subscribeToTopic("room_"+user.getId());
                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("volley","respnse"+response.toString() );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    String message =  getString(R.string.NoConnection);
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }



                Log.e("volley", "error" + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams = new HashMap<>();
                prams.put("request", Config.GET_USERS);
                return prams;
            }
        };
        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        feedRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(feedRequest);
    }


}
