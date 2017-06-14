package com.example.andrey.academiaand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.andrey.academiaand.adapter.AcademiaAdapter;
import com.example.andrey.academiaand.factory.SharedPreferencesActivity;
import com.example.andrey.academiaand.model.Academia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.TypeVariable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String result;
    private Activity activity;
    private List<Academia> academias;

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        activity = this;

        lista = (ListView) findViewById(R.id.lv_academias);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listarAcademias();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_listar) {
            // Handle the camera action
        } else if (id == R.id.nav_mapa) {
            startActivity(new Intent(MenuActivity.this, MapsActivity.class));
        } else if (id == R.id.nav_sair) {
            SharedPreferencesActivity.remove(MenuActivity.this, Constantes.TOKEN);
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public synchronized void listarAcademias() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constantes.URL_SERVICE)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                result = e.toString();
                Log.i(Constantes.LOG_TAG, result);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                result = response.body().string();
                Log.d("RESULT", result);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tratarRespostaAcademias(result);
                    }
                });
                
            }
        });
    }

    private void tratarRespostaAcademias(String result) {

        Gson gson = new Gson();

        academias = gson.fromJson(result, new TypeToken< List<Academia> >() {}.getType());
        lista.setAdapter(new AcademiaAdapter(academias, MenuActivity.this));

    }
}
