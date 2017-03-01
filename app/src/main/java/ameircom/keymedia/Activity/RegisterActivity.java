package ameircom.keymedia.Activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.Models.UserModel;
import ameircom.keymedia.R;
import ameircom.keymedia.AppManger.Config;

public class RegisterActivity extends AppCompatActivity {
    private String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputName, inputPass,inputMatchPass,inputPhone,inputEmail;
    private TextInputLayout inputLayoutName,inputLayoutPhone, inputLayoutPass,inputLayoutMatchPass,inputLayoutEmail;
    private Button btn_register;
    int request = Config.SIGNUP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutPass = (TextInputLayout) findViewById(R.id.input_layout_pass);
        inputLayoutMatchPass = (TextInputLayout) findViewById(R.id.input_layout_confirm_pass);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        inputName = (EditText) findViewById(R.id.input_name);
        inputPass = (EditText) findViewById(R.id.input_pass);
        inputPhone = (EditText) findViewById(R.id.input_phone);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputMatchPass = (EditText) findViewById(R.id.input_confirm_pass);
        btn_register = (Button) findViewById(R.id.btn_register);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPass.addTextChangedListener(new MyTextWatcher(inputPass));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
        inputMatchPass.addTextChangedListener(new MyTextWatcher(inputMatchPass));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });
        try{
            if (getIntent().getExtras().getString("opp").equals("edit")){
                UserModel current = AppController.getInstance().getPrefManager().getUser();
                inputName.setText(current.getName());
                inputPhone.setText(current.getPhone());
                inputEmail.setText(current.getEmail());
                request= Config.UPDATE_USER ;
                btn_register.setText("update");
            }
        }catch (Exception e){
            Log.e("no signup arguments", e.toString() );
        }

    }
    private void register() {
        if (!validateName()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }

        if (!validatePass()) {
            return;
        }
        if (!validateMatchPass()) {
            return;
        }
      
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        final String name = inputName.getText().toString();
        final String pass = inputPass.getText().toString();
        final String phone = inputPhone.getText().toString();
        final String email = inputEmail.getText().toString();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.OPERATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                Log.e(TAG, "REGISTER response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("operation");
                    if (res.equals("done") ){
                        //GO TO LOGIN
                    supportFinishAfterTransition();
                        String message ;
                        if (request == Config.SIGNUP){
                            message = "Sigh up success" ;
                        }else {
                            UserModel current = AppController.getInstance().getPrefManager().getUser();
                            current.setName(name);
                            current.setPhone(phone);
                            current.setEmail(email);
                            AppController.getInstance().getPrefManager().storeUser(current);
                            onBackPressed();
                            message = "update success";
                        }

                        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_LONG).show();

                    }else if(res.equals("not_executed")) {
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
                if (error instanceof TimeoutError){
                    // register();
                }
                if (error instanceof NoConnectionError) {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    String message = getString(R.string.NoConnection);
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            }
        }) {
            //sending your email and pass
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request", String.valueOf(request));
                params.put("name", name);
                params.put("pass", pass);
                params.put("phone", phone);
                params.put("email", email);
                if (request != Config.SIGNUP){
                    params.put("user_id", AppController.getInstance().getPrefManager().getUser().getId());
                    if (AppController.getInstance().getPrefManager().getUser().getPhone().equals(phone)){
                        params.put("check_phone","0");
                    }else {
                        params.put("check_phone","1");
                    }

                    if (AppController.getInstance().getPrefManager().getUser().getEmail().equals(email)){
                        params.put("check_email","0");
                    }else {
                        params.put("check_email","1");
                    }
                }


                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        strReq.setRetryPolicy(policy);
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
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
                case R.id.input_phone:
                    validatePhone();
                    break;
                case R.id.input_pass:
                    validatePass();
                    break;
                case R.id.input_confirm_pass:
                    validateMatchPass();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
               
            }
        }


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



    // Validating email
    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email) ) {
            inputLayoutEmail.setError("Enter valid Email");
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        //not empty and vaild with email patternex@ex.ex
        return  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    // Validating phone
    private boolean validatePhone() {
        if (inputPhone.getText().toString().trim().isEmpty()) {
            incorrectPhone();
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectPhone() {
        inputLayoutPhone.setError(getString(R.string.Enter_valid_PHONE));
        requestFocus(inputPhone);
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

    // Validating Match Pass
    private boolean validateMatchPass() {
        String Pass = inputPass.getText().toString().trim();
        String PassMatch = inputMatchPass.getText().toString().trim();

        if (PassMatch.isEmpty() || !(Pass.equals(PassMatch))) {
            incorrectMatchPass();
            return false;
        } else {
            inputLayoutMatchPass.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectMatchPass() {
        inputLayoutMatchPass.setError(getString(R.string.Enter_Match_PASS));
        requestFocus(inputMatchPass);
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

}