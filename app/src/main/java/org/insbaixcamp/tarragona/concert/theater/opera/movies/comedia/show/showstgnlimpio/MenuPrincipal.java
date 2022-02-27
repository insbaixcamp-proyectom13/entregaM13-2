package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Realizar el login(anonimo) y recojer los datos necesarios del usuario(UID)
        String uid = "FirebaseUID";

        //Recojer los datos de las shared preferences
        SharedPreferences preferences = getSharedPreferences("shows", Context.MODE_PRIVATE);
        String stringPreferences = preferences.getString("usuari", "");

        //Si las shared preferences no estan vacias, mandamos directamente a la lista de eventos
        if (!stringPreferences.trim().equals("") && stringPreferences != null) {
            Log.i("shared", stringPreferences);
            navigateActivity("omitir");

        }

        //Si estan vacias ponemos los botones a escuchar y la vista que pertoca
        setContentView(R.layout.activity_menu_principal);

        Button btOmitir = findViewById(R.id.btOmetreSplash);
        btOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                escriureSharedPreferences(preferences, uid);
                navigateActivity("omitir");
            }
        });

        Button btLogin = findViewById(R.id.btIniciSessioSplash);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                escriureSharedPreferences(preferences, uid);
                navigateActivity("login");
            }
        });

        Button btRegisButton = findViewById(R.id.btRegistreSplash2);
        btRegisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                escriureSharedPreferences(preferences, uid);
                navigateActivity("registre");
            }
        });

    }

    private void escriureSharedPreferences(SharedPreferences preferences, String uid) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuari", uid);
        editor.commit();

    }

    public void navigateActivity(String string) {
        Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
        intent.putExtra("button", string);
        startActivity(intent);
        finish();
    }
}