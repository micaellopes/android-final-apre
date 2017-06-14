package com.example.andrey.academiaand;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrey.academiaand.factory.SharedPreferencesActivity;
import com.example.andrey.academiaand.model.ResultLogin;
import com.example.andrey.academiaand.model.Usuario;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;



public class LoginActivity extends AppCompatActivity {


    EditText et_Email;
    EditText et_Senha;
    CheckBox checkBox;
    Button btn_Entrar;
    private String result;
    LoginActivity login;
    public boolean conectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        login = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_Email = (EditText) findViewById(R.id.et_Email);
        et_Senha = (EditText) findViewById(R.id.et_Senha);
        btn_Entrar = (Button) findViewById(R.id.btn_Entrar);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

//        A ÚNICA PARTE QUE FALTOU NA APRESENTAÇÃO FOI ESSA \/
//        VALIDA SE EXISTE TOKEN NO SHAREDPREFERENCES
          if (SharedPreferencesActivity.get(LoginActivity.this, Constantes.TOKEN, null) != null ) {
              startActivity(new Intent(LoginActivity.this, MenuActivity.class));
              finish();
        }
    }



    public String md5Generator(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte vetor[] = messageDigest.digest(password.getBytes("UTF-8"));
        StringBuilder hashString = new StringBuilder();
        for (byte b : vetor) {
            hashString.append(String.format("%02X", 0xFF & b));
        }
        String passwordEncrypt = hashString.toString();
        return passwordEncrypt;
    }


    public String parseUserToJSON(){
        String email = et_Email.getText().toString();
        String password = et_Senha.getText().toString();
        Usuario usuario = new Usuario();

        String passwordEncrypt = null;
        try {
            passwordEncrypt = md5Generator(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        usuario.setEmail(email);
        usuario.setPassword(passwordEncrypt);

        Gson gson = new Gson();
        String usuarioJSON = gson.toJson(usuario);

        return usuarioJSON;
    }

    public void fazerLogin(View v){
        checkConnection();

        if(!conectado){
            Toast.makeText(this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
        } else{
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .url(Constantes.URL_LOGIN)
                    .post(RequestBody.create(JSON,parseUserToJSON()))
                    .build();

            client.newCall(request).enqueue(new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    String result = e.toString();
                    Log.i(Constantes.LOG_TAG, result);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        result = response.body().string();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    login.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            responseLogin();
                        }
                    });

                }
            });
        }
    }


    private void responseLogin(){
        Gson gson = new Gson();
        ResultLogin json = gson.fromJson(this.result, ResultLogin.class);

        if(json.getToken().equals("")){
            Toast.makeText(LoginActivity.this, "Login ou senha inválidos!", Toast.LENGTH_LONG).show();
        }else{
            if(checkBox.isChecked()){
                SharedPreferencesActivity.set(LoginActivity.this, Constantes.TOKEN, json.getToken());
                String savedToken = SharedPreferencesActivity.get(LoginActivity.this, Constantes.TOKEN, "defaultValue");
                Log.d("TOKEN SALVO!", savedToken);
            }
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        }

    }

    public void checkConnection() {
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
    }

}
