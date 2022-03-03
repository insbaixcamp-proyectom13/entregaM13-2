package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.MainActivity;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data.FirebaseConnection;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data.RealtimeDatabase;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentEventBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.PicassoTrustAll;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters.OpinioAdapter;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Opinio;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;
    private FragmentEventBinding binding;
    private View root;
    private OpinioAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Event selectedEvent;
    private ArrayList<Opinio> listOpinions;
    private int entradas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        binding = FragmentEventBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        layoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        binding.incOpinions.recyclerView2.setLayoutManager(layoutManager);
        ((MainActivity) getActivity()).hideMainToolbar();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        Toolbar toolbar = binding.toolbarEvent;
        TextView tvDescripcio = binding.tvDescripcio;
        TextView tvDireccio = binding.tvDireccio;
        TextView tvData = binding.tvData;
        TextView tvAforament = binding.tvAforament;
        TextView tvHora = binding.tvHora;
        ImageView ivEvent = binding.ivDetailEvent;
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        entradas = Integer.parseInt(binding.tvEntrades.getText().toString());

        if (getArguments() != null) {
            listOpinions = new ArrayList<>();
            selectedEvent = (Event) getArguments().getSerializable("event");

            if (selectedEvent.getRestants() == 0) {
                binding.lytEntrades.setVisibility(View.GONE);
            } else {
                binding.lytEntrades.setVisibility(View.VISIBLE);
            }

            binding.ctEvent.setExpandedTitleColor(getResources().getColor(R.color.Dark_blue));
            binding.ctEvent.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
            binding.ctEvent.setTitle(selectedEvent.getNom());

            tvAforament.setText(String.valueOf(selectedEvent.getAforament()));
            tvDescripcio.setText(selectedEvent.getDescripcio());
            tvData.setText(selectedEvent.getData());
            tvHora.setText(selectedEvent.getHora());
            tvDireccio.setText(selectedEvent.getAdreça());
            PicassoTrustAll.getInstance().load(selectedEvent.getImatge()).into(ivEvent);

            binding.toolbarEvent.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).showMainToolbar();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_home);

                }
            });

            //Carga los comentarios en tiempo real
            FirebaseDatabase.getInstance().getReference().child("opinions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (listOpinions.isEmpty()) {
                        binding.incOpinions.cvEmpty.setVisibility(View.GONE);
                        binding.incOpinions.recyclerView2.setVisibility(View.VISIBLE);
                        ArrayList<Opinio> opinios =  snapshot.getValue(new GenericTypeIndicator<ArrayList<Opinio>>(){});
                        listOpinions.addAll(opinios);
                        adapter = new OpinioAdapter(getFilteredOpinions(opinios, selectedEvent.getId()));
                        binding.incOpinions.recyclerView2.setAdapter(adapter);

                    } else {
                        binding.incOpinions.cvEmpty.setVisibility(View.GONE);
                        binding.incOpinions.recyclerView2.setVisibility(View.VISIBLE);
                        ArrayList<Opinio> opinios =  snapshot.getValue(new GenericTypeIndicator<ArrayList<Opinio>>(){});
                        listOpinions = new ArrayList<>();
                        listOpinions.addAll(opinios);
                        adapter = new OpinioAdapter(getFilteredOpinions(opinios, selectedEvent.getId()));
                        binding.incOpinions.recyclerView2.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            ImageView ivOpina = binding.incOpinions.ivOpina;
            EditText etOpinio = binding.incOpinions.etOpinio;

            //Se comprueba la validez del comentario i se sube Firebase
            ivOpina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Nomes podran deixar comentaris els usuaris registrats
                    if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                        Toast.makeText(getContext(), "Inicia Sessio per deixar la teva opinio!", Toast.LENGTH_LONG).show();
                    } else {
                        String opinio = etOpinio.getText().toString().trim();
                        int puntuacion = 5;
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (!opinio.isEmpty() && opinio != "") {
                            FirebaseDatabase.getInstance().getReference().child("usuaris").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String json = new Gson().toJson(task.getResult().getValue());
                                        Log.i("json", json);
                                        Usuari usuari = new Gson().fromJson(json, Usuari.class);
                                        Opinio opinioUsuari = new Opinio(opinio, selectedEvent.getId(), uid, usuari.getNom(), puntuacion);
                                        etOpinio.setText(null);
                                        binding.incOpinions.recyclerView2.setAdapter(adapter);
                                        binding.incOpinions.cvEmpty.setVisibility(View.GONE);
                                        binding.incOpinions.recyclerView2.setVisibility(View.VISIBLE);
                                        FirebaseDatabase.getInstance().getReference().child("opinions").child(String.valueOf(listOpinions.size())).setValue(opinioUsuari);

                                    }
                                }
                            });

                        }
                    }

                }
            });
        }

        binding.ivRestaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (entradas > 1) {
                    entradas--;
                    binding.tvEntrades.setText(String.valueOf(entradas));
                }
            }
        });

        binding.ivSumaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (entradas < 20) {
                    if (entradas < selectedEvent.getRestants()) {
                        entradas++;
                        binding.tvEntrades.setText(String.valueOf(entradas));
                    } else {
                        Toast.makeText(getContext(), "No queden mes entrades!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        binding.btComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                    Toast.makeText(getContext(), "Inicia Sessio per reservar", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).showMainToolbar();
                    Navigation.findNavController(root).navigate(R.id.nav_login);
                } else {
                    int restantes = selectedEvent.getRestants();
                    restantes -= Integer.parseInt(binding.tvEntrades.getText().toString());
                    binding.tvEntrades.setText("1");

                    FirebaseDatabase.getInstance().getReference().child("events").child(selectedEvent.getId()+"").child("restants").setValue(restantes);

                    //Una vez restadas las entradas, mandaremos al usuario al carrito para que vea su reserva guardada
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String data = format.format(Calendar.getInstance().getTime());
                    int event = selectedEvent.getId();
                    int pagades = entradas;
                    entradas = 1;

                    //Esto me sirve para subir la reserva con un id
                    FirebaseDatabase.getInstance().getReference().child("reserves").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                String json = new Gson().toJson(task.getResult().getValue());
                                ArrayList<Reserva> reservas = new Gson().fromJson(json, new TypeToken<ArrayList<Reserva>>(){}.getType());
                                Log.i("reserves", reservas.toString());
                                Reserva reserva = new Reserva(uid, data, event, reservas.size(), pagades);
                                FirebaseDatabase.getInstance().getReference().child("reserves").child(reservas.size()+"").setValue(reserva).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ((MainActivity) getActivity()).showMainToolbar();
                                        Toast.makeText(getContext(), "Reserva completada!", Toast.LENGTH_LONG);
                                        Navigation.findNavController(root).navigate(R.id.nav_carrito);
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });
        return root;
    }

    private ArrayList<Opinio> getFilteredOpinions(ArrayList<Opinio> opinios, int id) {
        ArrayList<Opinio> filteredOpinions = new ArrayList<>();

        for (Opinio opinio : opinios) {
            if (id == opinio.getEvent()) {
                filteredOpinions.add(opinio);
            }
        }

        return filteredOpinions;
    }

}