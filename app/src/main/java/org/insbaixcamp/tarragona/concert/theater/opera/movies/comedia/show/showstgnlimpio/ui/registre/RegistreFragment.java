package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.registre;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.MainActivity;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data.RealtimeDatabase;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentRegistreBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistreFragment extends Fragment {

    private RegistreViewModel registreViewModel;
    private FragmentRegistreBinding binding;
    private View root;
    private DatabaseReference mDatabase;
    public final String url = "https://showstarragona-default-rtdb.firebaseio.com/";
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registreViewModel =
                new ViewModelProvider(this).get(RegistreViewModel.class);

        binding = FragmentRegistreBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance(url);
        mDatabase = database.getInstance().getReference();

        Button registry = binding.bRegistrar;
        EditText etcorreu = binding.etEmail;
        EditText etcontrasenya = binding.etPassword;
        EditText etnom = binding.etNom;
        EditText etcognom = binding.etCognoms;
        EditText etdni = binding.etDni;


        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ettelefon = binding.etTelefon;

                String correu = etcorreu.getText().toString();
                String contrasenya = etcontrasenya.getText().toString();
                String nom = etnom.getText().toString();
                String cognom = etcognom.getText().toString();
                String dni = etdni.getText().toString();
                int telefon = Integer.parseInt(ettelefon.getText().toString());

                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                Pattern patron = Pattern.compile("[0-9]{7,8}[A-Z a-z]");

                Map<String, Object> dadesUsuari = new HashMap<>();

                if (!correu.isEmpty() &&
                        !contrasenya.isEmpty() &&
                        !nom.isEmpty() &&
                        !cognom.isEmpty() &&
                        !dni.isEmpty()) {

                    Matcher machercorreu = pattern.matcher(correu);
                    Matcher matcherdni = patron.matcher(dni);

                    if (machercorreu.matches() && matcherdni.matches()){

                        dadesUsuari.put("correu",correu);
                        dadesUsuari.put("contrasenya",contrasenya);
                        dadesUsuari.put("nom",nom);
                        dadesUsuari.put("cognom",cognom);
                        dadesUsuari.put("dni",dni);
                        dadesUsuari.put("telefon",telefon);

                        mAuth.createUserWithEmailAndPassword(correu, contrasenya)
                                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Usuari usuari = new Usuari(cognom, contrasenya, correu, dni, nom, telefon);
                                            RealtimeDatabase.getInstance().carregaDadesUsuari(user.getUid());
                                            RealtimeDatabase.getInstance().setRegistrat(usuari);
                                            mDatabase.child("usuaris").child(user.getUid()).setValue(dadesUsuari).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(),"Has sigut registrat correctament",Toast.LENGTH_LONG).show();
                                                        Navigation.findNavController(root).navigate(R.id.nav_home);
                                                    }
                                                }
                                            });

                                        } else {
                                            // If sign in fails, display a message to the user.

                                            Toast.makeText(getContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(),"Introdueix un fomat valid per al Correu o el DNI",Toast.LENGTH_LONG).show();
                    }

                } else {

                    Toast.makeText(getContext(),"Omple tots els camps",Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}