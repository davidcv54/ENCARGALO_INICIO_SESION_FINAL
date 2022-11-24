package mx.com.encargalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import mx.com.encargalo.Utils.Util;


public class activity_is_actcrearunacuenta extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btncrearcuenta;
    EditText is_latitud, is_longitud;
    EditText is_edtnombre, is_edtapellido, is_edtcorreo, is_numero_celular, is_documentopersona;
    ImageView img_user;
    ProgressDialog progreso;
    RequestQueue request;
    Spinner is_sptipopersona, is_spcodigodelpais;
    List<String> items;
    String nombre, apellido, email, profile, encodeImageString;
    JsonObjectRequest jsonObjectRequest;
    TextView is_cucbtnterminosycondiciones;
    double latitud, longitud;
    CheckBox is_cucchkbcheckterminos;
    String rolusuario;


    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_actcrearunacuenta);
        btncrearcuenta = findViewById(R.id.is_cucbtnRegistrarse);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        is_edtnombre = findViewById(R.id.is_cucedtnombre);
        is_edtapellido = findViewById(R.id.is_cucedtapellido);
        is_edtcorreo = findViewById(R.id.is_cucedtcorreo);
        is_numero_celular = findViewById(R.id.is_cucedtnumtelefono);
        is_documentopersona = findViewById(R.id.is_cucedtdocumentoidentidad);
        is_sptipopersona = findViewById(R.id.is_sptipopersona);
        is_spcodigodelpais = findViewById(R.id.is_cuccodpais);
        img_user = findViewById(R.id.is_cucimg_user);
        is_cucbtnterminosycondiciones = findViewById(R.id.is_cucbtnterminosycondiciones);
        is_cucchkbcheckterminos = findViewById(R.id.is_cucchkbcheckterminos);


        Intent intent = getIntent();
        this.email = intent.getStringExtra("usuCorreo");
        this.nombre = intent.getStringExtra("name");
        this.profile = intent.getStringExtra("profile");
        this.profile = intent.getStringExtra("profile");
        this.rolusuario = intent.getStringExtra("1");

        Glide.with(this).
                load(profile).
                into(img_user);


        String string = nombre;
        String[] parts = string.split(" ", 2);
        String nomb = parts[0];
        String ape = parts[1];

        is_edtnombre.setText("" + nomb);
        is_edtapellido.setText("" + ape);
        is_edtcorreo.setText("" + email);


        btncrearcuenta = findViewById(R.id.is_cucbtnRegistrarse);
        request = Volley.newRequestQueue(this);
        String[] Persona = {"Persona natural", "Persona jurídica"};
        is_sptipopersona = findViewById(R.id.is_cucspntipopersona);
        ArrayAdapter<String> personaadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Persona);
        is_sptipopersona.setAdapter(personaadapter);
        String[] CodigoPais = {"+52", "+51", "+54", "+1"};
        is_spcodigodelpais = findViewById(R.id.is_cuccodpais);
        ArrayAdapter<String> codigoadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CodigoPais);
        is_spcodigodelpais.setAdapter(codigoadapter);


        is_cucbtnterminosycondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_is_terminosycondiciones termsCondiciones = new fragment_is_terminosycondiciones();
                termsCondiciones.show(getSupportFragmentManager(), "terminos_condiciones");

            }

        });

        is_cucchkbcheckterminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (is_cucchkbcheckterminos.isChecked()) {
                    btncrearcuenta.setEnabled(true);
                } else {
                    btncrearcuenta.setEnabled(false);
                }
            }
        });


        btncrearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = Util.RUTA + "c_consultar_existencia_persona_inicio_sesion.php?sp_idDocumentoPersona=" + is_documentopersona.getText().toString();
                url = url.replace(" ", "%20");
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity_is_actcrearunacuenta.this, "Documento ya vinculado a una cuenta existente.", Toast.LENGTH_SHORT).show();


                        // Intent intent=new Intent(activity_is_actverificacioncodigo.this,activity_is_actverificacioncodigo.class);
                        //intent.putExtra("UsuCorreo", is_edtcorreo.getText().toString());
                        //startActivity(intent);
                        //progreso.hide();

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progreso.hide();
                        if (is_cucchkbcheckterminos.isChecked()

                        ) {
                            camposobligatorios();


                            //    ejecutarServicio(Util.RUTA + "a_registro_usuario_inicio_sesion.php");

                        } else {
                            Toast.makeText(getApplicationContext(), "POR FAVOR ACEPTE LOS TÉRMINOS Y CONDICIONES DE USO", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                request.add(jsonObjectRequest);


            }
        });

    }


    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {


                                try {
                                    Geocoder geocoder = new Geocoder(activity_is_actcrearunacuenta.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    latitud = addresses.get(0).getLatitude();
                                    longitud = addresses.get(0).getLongitude();

                                    //                                 is_latitud.setText(""+latitud);
                                    //                                is_longitud.setText(""+longitud);


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    });


        } else {

            askPermission();


        }


    }

    private void askPermission() {

        ActivityCompat.requestPermissions(activity_is_actcrearunacuenta.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                getLastLocation();

            } else {


                Toast.makeText(activity_is_actcrearunacuenta.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();

            }


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void ejecutarServicio(String URL) {
        int i = is_sptipopersona.getSelectedItemPosition() + 1;
        String po = String.valueOf(i);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "REGISTRO SATISFACTORIO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_is_actcrearunacuenta.this, activity_is_actverificacioncodigo.class);
                intent.putExtra("usuCorreo", is_edtcorreo.getText().toString());
                intent.putExtra("idRolUsuario", rolusuario);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Bitmap bitmap = ((BitmapDrawable) img_user.getDrawable()).getBitmap();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String fotoEnBase64 = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);

                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("sp_usuCorreo", is_edtcorreo.getText().toString());
                parametros.put("sp_usuImagen", fotoEnBase64);
                parametros.put("sp_idRolUsuario", rolusuario);
                parametros.put("sp_idDocumentoPersona", is_documentopersona.getText().toString());
                parametros.put("sp_perNombres", is_edtnombre.getText().toString());
                parametros.put("sp_perApellidos", is_edtapellido.getText().toString());
                parametros.put("sp_perTipo", is_sptipopersona.getSelectedItem().toString());
                parametros.put("sp_perNumeroCelular", is_spcodigodelpais.getSelectedItem().toString() + is_numero_celular.getText().toString());
                parametros.put("sp_perUbiLatitud", String.valueOf(latitud));
                parametros.put("sp_perUbiLongitud", String.valueOf(longitud));

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    public void camposobligatorios() {
        if (is_edtnombre.length() != 0 && is_edtapellido.length() != 0 && is_edtapellido.length() != 0 && is_documentopersona.length() !=0 && is_numero_celular.length() !=0) {
            ejecutarServicio(Util.RUTA + "a_registro_usuario_inicio_sesion.php");
            cargarWebService();
            enviar_correo();

        }
        else{
            Toast.makeText(getApplicationContext(), "TODOS LOS CAMPOS SON OBLIGATORIOS, POR FAVOR COMPLETE", Toast.LENGTH_SHORT).show();
        }

    }
    public void enviar_correo() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("ENVIANDO CÓDIGO AL CORREO ELECTRÓNICO");
        progreso.show();
        String url= Util.RUTA+"c_codigo_verificacion_inicio_sesion.php?sp_codvCorreo="+email;
        url=url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity_is_actcrearunacuenta.this,"Se ha enviado su correo con éxito", Toast.LENGTH_SHORT).show();
                // Intent intent=new Intent(activity_is_actverificacioncodigo.this,activity_is_actverificacioncodigo.class);
                //startActivity(intent);
                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(activity_is_actcrearunacuenta.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);

    }
    public void cargarWebService() {
        //progreso = new ProgressDialog(this);
        //progreso.setMessage("CARGANDO DATOS");
        //progreso.show();
        String url= Util.RUTA+"a_registro_cod_verificacion_usuario_inicio_sesion.php?sp_codvCorreo="+email;
        url=url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity_is_actcrearunacuenta.this,"Se ha enviado su correo con éxito", Toast.LENGTH_SHORT).show();
                // Intent intent=new Intent(activity_is_actverificacioncodigo.this,activity_is_actverificacioncodigo.class);
                //startActivity(intent);
                //progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progreso.hide();
                Toast.makeText(activity_is_actcrearunacuenta.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(jsonObjectRequest);
    }
}









