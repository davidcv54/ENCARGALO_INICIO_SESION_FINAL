package mx.com.encargalo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;
import org.json.JSONArray;
import  org.json.JSONException;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import mx.com.encargalo.tendero.Inicio_sesion.MainActivity;
import mx.com.encargalo.Utils.Util;
import mx.com.encargalo.Utils.is_cls_ActivitysInicioSesion;
import mx.com.encargalo.Utils.is_cls_Constants_InicioSesion;
import mx.com.encargalo.Utils.is_cls_Consulta_Disponibilidad_Red;
import mx.com.encargalo.Utils.is_cls_Session_Inicio_Sesion;

public class activity_is_actiniciarsesionprincipal extends AppCompatActivity {
    JsonObjectRequest jsonObjectRequest;

    RequestQueue request;
    Button btniniciarfb;
    Button btniniciargmail;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 9001;
    ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    String token;
    CallbackManager mCallbackManager;
    String id;
    String device_token, device_UDID;
    String TAG = "FragmentLogin";
    String keyHash = "";
    String emailFB;
    String emailGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_actiniciarsesionprincipal);
        request = Volley.newRequestQueue(this);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        //Init Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),                  //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        btniniciarfb=(Button)findViewById(R.id.is_ispbtnfacebook);
        btniniciarfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();
            }
        });
        btniniciargmail=(Button)findViewById(R.id.btn_google_singin);
        btniniciargmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog();
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Cargando");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            hideProgressDialog();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with FireBase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                        FirebaseUser user = mAuth.getCurrentUser();

                        assert user != null;
                        String personName = user.getDisplayName();

                        if (personName.contains(" ")) {
                            personName = personName.substring(0, personName.indexOf(" "));
                        }
                        String email = user.getEmail();
                        String[] userName = user.getEmail().split("@");

                        if (isNew) {
                            hideProgressDialog();
                            //ShowReferDialog(user.getUid(), userName[0] + id, personName, email, user.getPhotoUrl().toString(), "gmail");
                        } else
                            UserSignUpWithSocialMedia(user.getUid(), "", userName[0] + id, user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString(), "1", "gmail", "idDocumentoPersona");
                    } else {
                        hideProgressDialog();

                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException | FirebaseAuthUserCollisionException invalidEmail) {

                            //setSnackBar(invalidEmail.getMessage(), getString(R.string.ok));
                        } catch (Exception e) {
                            e.printStackTrace();
                            //setSnackBar(e.getMessage(), getString(R.string.ok));
                        }
                    }

                });
    }

    public void UserSignUpWithSocialMedia(final String authId, final String fCode, final String referCode, final String name, final String email, final String profile, final String rolusuario, final String type, final String idDocumentopersona) {
        HashMap<String, String> params = new HashMap<>();
        emailGmail= email;
        params.put(is_cls_Constants_InicioSesion.email, email);
        params.put(is_cls_Constants_InicioSesion.name, name);
        params.put(is_cls_Constants_InicioSesion.PROFILE, profile);
        params.put(is_cls_Constants_InicioSesion.rolusuario, rolusuario);

        String url= Util.RUTA+"/c_existencia_usuario_inicio_sesion.php?sp_usuCorreo="+emailGmail;
        url=url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response ) {

                {

                    try {
                        JSONArray jsonArray = response.getJSONArray("usuario");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject usuario = jsonArray.getJSONObject(i);
                            params.put(is_cls_Constants_InicioSesion.idDocumentoPersona, usuario.getString("idDocumentoPersona"));
                            //String valor=usuario.getString("usuToken");

                            String url= Util.RUTA+"/c_estado_usuario_inicio_sesion.php?sp_codvCorreo="+emailGmail;
                            url=url.replace(" ","%20");

                            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response ) {

                                    {



                                        //is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actverificacioncodigo.class).muestraActividad(params);

                                        is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, MainActivity.class).muestraActividad(params);

                                    }



                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {


                                    is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actverificacioncodigo.class).muestraActividad(params);
                                    //startActivity(intent);

                                }
                            });
                            request.add(jsonObjectRequest);
                            //is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actverificacioncodigo.class).muestraActividad(params);

                            // is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, MainActivity.class).muestraActividad(params);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class).muestraActividad(params);
                //startActivity(intent);

            }
        });
        request.add(jsonObjectRequest);





        //  Activitys.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class).muestraActividad(params);
    }














    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    try {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String personName = user.getDisplayName();
                            if (personName.contains(" ")) {
                                personName = personName.substring(0, personName.indexOf(" "));
                            }
                            String referCode = "";

                            if (user.getEmail() != null) {
                                String[] userName = user.getEmail().split("@");
                                referCode = userName[0];
                            } else {
                                referCode = user.getPhoneNumber();
                            }
                            if (isNew) {
                                hideProgressDialog();
                                //ShowReferDialog(user.getUid(), referCode + id, personName, "" + user.getEmail(), user.getPhotoUrl().toString(), "fb");
                            } else
                                UserSignUpWithSocialMedia(user.getUid(), "", referCode + id, personName, "" + user.getEmail(), user.getPhotoUrl().toString(), "1","fb", "idDocumentoPersona");
                        } else {
                            // If sign in fails, display a message to the user.

                            LoginManager.getInstance().logOut();
                            hideProgressDialog();
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException | FirebaseAuthUserCollisionException invalidEmail) {
                                // setSnackBar(invalidEmail.getMessage(), getString(R.string.ok));
                            } catch (Exception e) {
                                e.printStackTrace();

                                //setSnackBar(e.getMessage(), getString(R.string.ok));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }

    public void facebookLogin() {
        if (is_cls_Consulta_Disponibilidad_Red.isNetworkAvailable(activity_is_actiniciarsesionprincipal.this)) {
            LoginManager.getInstance().logInWithReadPermissions(activity_is_actiniciarsesionprincipal.this,
                    Arrays.asList("public_profile", "email"));


            LoginManager.getInstance().registerCallback(mCallbackManager,
                    new FacebookCallback<LoginResult>() {

                        public void onSuccess(final LoginResult loginResult) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                GraphRequest requestfb = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                                try {
                                                    Log.e(TAG, "id" + user.optString("id"));
                                                    Log.e(TAG, "name" + user.optString("first_name"));
                                                    Log.e(TAG, "name" + user.optString("last_name"));

                                                    String profileUrl = "https://graph.facebook.com/v2.8/" + user.optString("id") + "/picture?width=1920";

                                                    HashMap<String, String> params = new HashMap<>();
                                                    params.put(is_cls_Constants_InicioSesion.email, user.optString("email"));
                                                    String nombre = user.optString("first_name");
                                                    String apellido = user.optString("last_name");
                                                    emailFB = user.optString("email");
                                                    String full = nombre+" "+apellido;
                                                    params.put(is_cls_Constants_InicioSesion.name, full);
                                                    //params.put(Constants.name, user.optString("first_name"+"last_name"));
                                                    params.put(is_cls_Constants_InicioSesion.PROFILE, profileUrl);
                                                    params.put(is_cls_Constants_InicioSesion.rolusuario, "1");
                                                    String url= Util.RUTA+"/c_existencia_usuario_inicio_sesion.php?sp_usuCorreo="+emailFB;
                                                    url=url.replace(" ","%20");

                                                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                                        @Override

                                                        public void onResponse(JSONObject response ) {
                                                            try {
                                                                JSONArray jsonArray = response.getJSONArray("usuario");
                                                                for (int i=0; i<jsonArray.length();i++){
                                                                    JSONObject usuario = jsonArray.getJSONObject(i);
                                                                    //   idDocumentoPersona=usuario.getString("idDocumentoPersona");
                                                                    params.put(is_cls_Constants_InicioSesion.idDocumentoPersona, usuario.getString("idDocumentoPersona"));

                                                                    String url= Util.RUTA+"/c_estado_usuario_inicio_sesion.php?sp_codvCorreo="+emailFB;
                                                                    url=url.replace(" ","%20");

                                                                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response ) {

                                                                            {



                                                                                //is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actverificacioncodigo.class).muestraActividad(params);

                                                                                is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, MainActivity.class).muestraActividad(params);

                                                                            }



                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {


                                                                            is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actverificacioncodigo.class).muestraActividad(params);
                                                                            //startActivity(intent);

                                                                        }
                                                                    });
                                                                    request.add(jsonObjectRequest);



                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, MainActivity.class).muestraActividad(params);
                                                        }

                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                            //Intent intent = new Intent(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class);
                                                            is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class).muestraActividad(params);
                                                            //startActivity(intent);

                                                        }
                                                    });
                                                    request.add(jsonObjectRequest);
                                                    //  Activitys.getSingleton(activity_is_actiniciarsesionprincipal.this   , activity_is_actcrearunacuenta.class).muestraActividad(params);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Log.d("facebookExp", e.getMessage());
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,first_name,last_name,email");
                                requestfb.setParameters(parameters);
                                requestfb.executeAsync();
                                Log.e("getAccessToken", "" + loginResult.getAccessToken().getToken());
                                //SharedHelper.putKey(Login.this, "accessToken", loginResult.getAccessToken().getToken());
//                                        login(loginResult.getAccessToken().getToken(), URLHelper.FACEBOOK_LOGIN, "facebook");
                            }

                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e("exceptionfacebook", exception.toString());
                            // App code
                        }
                    });
        } else {
            //mProgressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity_is_actiniciarsesionprincipal.this);
            builder.setMessage("Check your Internet").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent NetworkAction = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(NetworkAction);

                }
            });
            builder.show();
        }
    }

    public void setSnackBarStatus() {
        final Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.account_deactivate), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.ok), view -> {

            is_cls_Session_Inicio_Sesion.clearUserSession(this);
            mAuth.signOut();
            LoginManager.getInstance().logOut();

        });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
        hideProgressDialog();
    }

    public void setSnackBar(String message, String action) {
        final Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, view -> {
            if (is_cls_Consulta_Disponibilidad_Red.isNetworkAvailable(this)) {
                snackbar.dismiss();
            } else {
                snackbar.show();
            }
        });
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }



    //    private void wsInicioSesionFB(){
//        String url= "http://192.168.101.85/API/c_existencia_usuario.php?sp_usuCorreo="+emailFB;
//        url=url.replace(" ","%20");
//        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response ) {
//                Intent verif = new Intent(activity_is_actiniciarsesionprincipal.this, MainActivity.class);
//                startActivity(verif);
//
//
//
//                // Navigation.findNavController(Activity getContext()).navigate(R.id.nav_mi_tienda);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//
//                Intent intent = new Intent(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class);
//
//                startActivity(intent);
//                //is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class).muestraActividad(params);
//                // Activitys.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class).muestraActividad(params);
//            }
//        });
//        request.add(jsonObjectRequest);
//
//    }
    public void consultarestadocuentagmail(){
        String url= Util.RUTA+"/c_estado_usuario_inicio_sesion.php?sp_codvCorreo="+emailGmail;
        url=url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response ) {

                {



                    //is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actverificacioncodigo.class).muestraActividad(params);

//                         is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, MainActivity.class).muestraActividad(params);

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //is_cls_ActivitysInicioSesion.getSingleton(activity_is_actiniciarsesionprincipal.this, activity_is_actcrearunacuenta.class).muestraActividad(params);
                //startActivity(intent);

            }
        });
        request.add(jsonObjectRequest);
    }

}