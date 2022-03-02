package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.event;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentEventBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.PicassoTrustAll;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters.OpinioAdapter;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Opinio;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    private EventViewModel mViewModel;
    private FragmentEventBinding binding;
    private View root;
    private OpinioAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Event selectedEvent;
    private String nomUsuari;
    private String usuari;
    private Gson gson;

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        binding = FragmentEventBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        layoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        binding.incOpinions.recyclerView2.setLayoutManager(layoutManager);
        gson = new Gson();

        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
            String usuari = FirebaseAuth.getInstance().getCurrentUser().getUid();
            getLogedUser(usuari);
        }

        TextView tvTitle = binding.textView2;
        TextView tvDescripcio = binding.tvDescripcio;
        TextView tvDireccio = binding.tvDireccio;
        TextView tvData = binding.tvData;
        TextView tvAforament = binding.tvAforament;
        TextView tvHora = binding.tvHora;
        ImageView ivEvent = binding.ivDetailEvent;

        //Recibimos el evento clickado por los argumentos, y se guardan los datos
        if (getArguments() != null) {
            mViewModel.setEvent((Event) getArguments().getSerializable("event"));
            selectedEvent = (Event) getArguments().getSerializable("event");
            tvTitle.setText(selectedEvent.getNom());
            tvAforament.setText(String.valueOf(selectedEvent.getAforament()));
            tvDescripcio.setText(selectedEvent.getDescripcio());
            tvData.setText(selectedEvent.getData());
            tvHora.setText(selectedEvent.getHora());
            tvDireccio.setText(selectedEvent.getAdreça());
            PicassoTrustAll.getInstance().load(selectedEvent.getImatge()).into(ivEvent);

            //Declara el adaptador i carga las opiniones sobre el
            mViewModel.getOpinions().observe(getViewLifecycleOwner(), new Observer<ArrayList<Opinio>>() {
                @Override
                public void onChanged(ArrayList<Opinio> opinios) {
                    if (!opinios.isEmpty()) {
                        binding.incOpinions.cvEmpty.setVisibility(View.GONE);
                        binding.incOpinions.recyclerView2.setVisibility(View.VISIBLE);
                        adapter = new OpinioAdapter(opinios);
                        binding.incOpinions.recyclerView2.setAdapter(adapter);
                    }
                }
            });

            ImageView ivOpina = binding.incOpinions.ivOpina;
            EditText etOpinio = binding.incOpinions.etOpinio;

            ivOpina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Nomes podran deixar comentaris els usuaris registrats
                    if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                        Toast.makeText(getContext(), "Inicia Sessio per deixar la teva opinio!", Toast.LENGTH_LONG).show();
                    } else {
                        String opinio = etOpinio.getText().toString().trim();
                        int puntuacion = 5;


                        if (!opinio.isEmpty() && opinio != "") {
                            nomUsuari = getLogedUser(usuari);
                            Opinio opinioUsuari = new Opinio(opinio, selectedEvent.getId(), usuari, nomUsuari, puntuacion);
                            etOpinio.setText(null);
                            adapter.notifyDataSetChanged();
                            binding.incOpinions.cvEmpty.setVisibility(View.GONE);
                            binding.incOpinions.recyclerView2.setVisibility(View.VISIBLE);
                            writeOpinio(opinioUsuari);
                        }
                    }

                }
            });
        }

        return root;
    }

    private void writeOpinio(Opinio opinio) {
        FirebaseDatabase.getInstance().getReference().child("opinions").child(String.valueOf(mViewModel.getOpinions().getValue().size())).setValue(opinio);
    }

    private String getLogedUser(String uid) {
        FirebaseDatabase.getInstance().getReference().child("usuaris").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String json = gson.toJson(snapshot.getValue());
                Usuari registred = gson.fromJson(json, Usuari.class);
                nomUsuari = registred.getNom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return nomUsuari;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        // TODO: Use the ViewModel
    }

}