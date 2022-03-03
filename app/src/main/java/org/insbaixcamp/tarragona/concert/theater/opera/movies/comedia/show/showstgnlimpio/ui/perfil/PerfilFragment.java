package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.MainActivity;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentPerfilBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentPerfilBinding binding;
    private View root;
    private DatabaseReference mDatabase;
    private int telefon = 0;
    private FirebaseAuth mAuth;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();

        EditText etCanviaNom = binding.etCanviaNom;
        EditText etCanviaCognoms = binding.etCanviaCognoms;
        EditText etCanviaTelefon = binding.etCanviaTelefon;
        EditText etCanviaCorreu = binding.etCanviaCorreu;
        Button bDesarCanvis = binding.bDesarCanvis;

        mDatabase.child("usuaris").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            mAuth.updateCurrentUser(mAuth.getCurrentUser());
                            etCanviaNom.setText(snapshot.child("nom").getValue().toString());
                            etCanviaCognoms.setText(snapshot.child("cognom").getValue().toString());
                            etCanviaTelefon.setText(snapshot.child("telefon").getValue().toString());
                            telefon = Integer.parseInt(snapshot.child("telefon").getValue().toString());
                            etCanviaCorreu.setText(snapshot.child("correu").getValue().toString());
                            Usuari usuari = new Usuari();
                            usuari.setNom(etCanviaNom.getText().toString());
                            ((MainActivity) getActivity()).updateUI(usuari);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        bDesarCanvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                String nom = etCanviaNom.getText().toString();
                String cognom = etCanviaCognoms.getText().toString();
                String correu = etCanviaCorreu.getText().toString();

                if(!etCanviaTelefon.getText().toString().isEmpty()){
                    telefon = Integer.valueOf(etCanviaTelefon.getText().toString());
                }

                if (!correu.isEmpty() && !nom.isEmpty() && !cognom.isEmpty()) {
                    Matcher machercorreu = pattern.matcher(correu);
                    if (machercorreu.matches()){
                        Map<String, Object> usuariMap = new HashMap();
                        usuariMap.put("nom", nom);
                        usuariMap.put("cognom", cognom);
                        usuariMap.put("telefon", telefon);
                        usuariMap.put("correu", correu);
                        mDatabase.child("usuaris").child(id).updateChildren(usuariMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "S'han desat els canvis correctament", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "El format del correu no és vàlid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Hi ha camps sense emplenar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

}