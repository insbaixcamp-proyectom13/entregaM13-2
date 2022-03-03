package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.carrito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.databinding.FragmentCarritoBinding;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters.ReservesAdapter;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;

import java.util.ArrayList;

public class CarritoFragment extends Fragment {

    private CarritoViewModel mViewModel;
    private FragmentCarritoBinding binding;
    private View root;
    private ReservesAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Event> events;

    public static CarritoFragment newInstance() {
        return new CarritoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CarritoViewModel.class);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding = FragmentCarritoBinding.inflate( inflater, container, false);
        root = binding.getRoot();
        linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL,false);
        binding.rvReserves.setLayoutManager(linearLayoutManager);

        mViewModel.getReservesExpirades().observe(getViewLifecycleOwner(), new Observer<ArrayList<Reserva>>() {
            @Override
            public void onChanged(ArrayList<Reserva> reservas) {

            }
        });

        mViewModel.getReservesPendents().observe(getViewLifecycleOwner(), new Observer<ArrayList<Reserva>>() {
            @Override
            public void onChanged(ArrayList<Reserva> reservas) {
                adapter = new ReservesAdapter(reservas, mViewModel.getEvents().getValue());
                binding.rvReserves.setAdapter(adapter);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}