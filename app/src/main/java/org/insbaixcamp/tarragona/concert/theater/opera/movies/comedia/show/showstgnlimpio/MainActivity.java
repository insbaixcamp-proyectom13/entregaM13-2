package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data.RealtimeDatabase;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.ActivityMainBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.PicassoTrustAll;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PicassoTrustAll.getInstance(this);
        findViewById(R.id.recyclerView).setVisibility(View.GONE);
        RealtimeDatabase.getInstance();

        //Accions per a donar feedback a l'usuari
        findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().hide();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        drawer = binding.drawerLayout;
        navigationView = (NavigationView) binding.navView;
        lockNavigationDrawer();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_carrito, R.id.nav_perfil)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //El boton de cerrar sesion se pone a la escucha de los clicks
        MenuItem logout = navigationView.getMenu().findItem(R.id.nav_logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null && !FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                    FirebaseAuth.getInstance().signOut();
                    FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateUI(new Usuari());
                                Toast.makeText(MainActivity.this, "Sessio tancada!", Toast.LENGTH_LONG).show();
                                drawer.closeDrawer(GravityCompat.START);
                                navController.navigate(R.id.nav_login);
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Encara no has iniciar sessio!", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        //Redireccion del usuario al fragmento requerido
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Si viene de menu principal y quiere iniciar sesion
                if(getIntent().getStringExtra("button").equals("login")) {
                    navController.navigate(R.id.nav_login);
                    getSupportActionBar().show();
                    findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);

                //Si viene de menu principal y quiere registrar-se
                } else if (getIntent().getStringExtra("button").equals("registre")) {
                    navController.navigate(R.id.nav_registre);
                    getSupportActionBar().show();
                    findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);

                } else {
                    getSupportActionBar().show();
                    findViewById(R.id.pbHome).setVisibility(View.GONE);
                    findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
                }

            }
        }, 1300);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void lockNavigationDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void updateUI(Usuari currentUser) {
        View header = navigationView.getHeaderView(0);
        TextView tvNom = header.findViewById(R.id.tvName);
        Menu menu = navigationView.getMenu();
        Usuari user = RealtimeDatabase.getInstance().getRegistrat();

        //Los datos si el usuario fuese anonimo, se esconde el
        //boton de cerrar sesion y otros como el del carrito y perfil
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
            ImageView iv = header.findViewById(R.id.ivUserImg);
            iv.setImageResource(R.drawable.teatro);
            tvNom.setText("Anonymous");
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_carrito).setVisible(false);
            menu.findItem(R.id.nav_perfil).setVisible(false);

        //En caso que el usuario este registrado se mostraran los
        //botones necesarios y se consultaran los datos de FireBaseDB
        } else {

            ImageView iv = header.findViewById(R.id.ivUserImg);
            iv.setImageResource(R.drawable.user);

            FirebaseDatabase.getInstance().getReference().child("usuaris").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String string = new Gson().toJson(snapshot.getValue());
                    Usuari registrat = new Gson().fromJson(string, Usuari.class);
                    if (registrat != null)
                        tvNom.setText(registrat.getDni());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_carrito).setVisible(true);
            menu.findItem(R.id.nav_perfil).setVisible(true);

        }

    }
}