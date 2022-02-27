package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    public final String url = "https://eventos-tarragona-app-default-rtdb.firebaseio.com/";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewById(R.id.text_home).setVisibility(View.GONE);
        findViewById(R.id.pbHome).setVisibility(View.VISIBLE);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        //Firebase Obten resultados del usuario si esta logeado o no
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(url);
        mDatabase = database.getInstance().getReference();

        //Si el user esta nulo logeara como anonimo
        if (user != null ) {

            getUserInfo();
/*            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            TNom.setText(name);
            TEmail.setText(email);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            if (emailVerified){

                TNom.setText(name);
                TEmail.setText(email);

            } else {

                Toast.makeText(this, "Revisa el teu correu per verificarte", Toast.LENGTH_SHORT).show();
            }*/
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            // String uid = user.getUid();
        } else {

            mAuth.signInAnonymously();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Si viene de menu principal y quiere iniciar sesion
                if(getIntent().getStringExtra("button").equals("login")) {
                    loadHome();
                    navController.navigate(R.id.nav_slideshow);

                //Si viene de menu principal y quiere registrar-se
                } else if (getIntent().getStringExtra("button").equals("registre")) {
                    loadHome();
                    navController.navigate(R.id.nav_gallery);

                } else if (getIntent().getStringExtra("button").equals("omitir")){
                    loadHome();
                }

            }
        }, 2000);
    }

    public void loadHome() {
        setSupportActionBar(binding.appBarMain.toolbar);
        findViewById(R.id.pbHome).setVisibility(View.GONE);
        findViewById(R.id.text_home).setVisibility(View.VISIBLE);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        lockNavigationDrawer();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }
    //Pone la informacion del usuario en el header main
    private void getUserInfo(){
        String id = "";

        try {

            id = mAuth.getCurrentUser().getUid();

        } catch (NullPointerException ex){

            mAuth.signInAnonymously();
        }

        if (mAuth.getCurrentUser()==null){
            Toast.makeText(this, "User nulo", Toast.LENGTH_SHORT).show();
        } else {
            id = mAuth.getCurrentUser().getUid();
        }

/*        Log.i("funciona", mAuth.getCurrentUser().getUid());
        mAuth.signInAnonymously();
        Log.i("funciona", mAuth.getCurrentUser().getUid());*/

        mDatabase.child("Usuaris").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    String name = snapshot.child("Nom").getValue().toString();
                    String email = snapshot.child("Correu").getValue().toString();
                    Uri photoUrl = user.getPhotoUrl();

                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);

                    TextView TNom = headerView.findViewById(R.id.tv_nom);
                    TextView TEmail = headerView.findViewById(R.id.tv_email);
                    ImageView profileImage = headerView.findViewById(R.id.iv_logo);

                    profileImage.setImageURI(photoUrl);
                    TNom.setText(name);
                    TEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}