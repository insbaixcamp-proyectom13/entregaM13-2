package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.carrito;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CarritoViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Reserva>> mReservesPendents;
    private final MutableLiveData<ArrayList<Reserva>> mReservesExpirades;
    private final MutableLiveData<ArrayList<Event>> mEvents;
    private DatabaseReference mRef;
    private Calendar calendar;

    public CarritoViewModel() {
        mReservesExpirades = new MutableLiveData<>();
        mReservesPendents = new MutableLiveData<>();
        mEvents = new MutableLiveData<>();
        mRef = FirebaseDatabase.getInstance().getReference();
        calendar = Calendar.getInstance();

        FirebaseDatabase.getInstance().getReference().child("events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String json = new Gson().toJson(task.getResult().getValue());
                    ArrayList<Event> events = new Gson().fromJson(json, new TypeToken<ArrayList<Event>>(){}.getType());
                    mEvents.setValue(events);
                }
            }
        });

        //Lo que hace es consultar en un primer momento los eventos
        mRef.child("reserves").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String json = new Gson().toJson(snapshot.getValue());

                ArrayList<Reserva> reservas = new Gson().fromJson(json, new TypeToken<ArrayList<Reserva>>(){}.getType());
                mReservesPendents.setValue(reservas);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean eventIsActive(Event event, Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        int year = Integer.parseInt(event.getData().substring(6, event.getData().length()).trim());
        int month = Integer.parseInt(event.getData().substring(3, 5).trim())-1;
        int day = Integer.parseInt(event.getData().substring(0, 2).trim());
        int hora = Integer.parseInt(event.getHora().trim().substring(0, 2));
        int minuts = Integer.parseInt(event.getHora().trim().substring(3, event.getHora().length()));

        //!!Resto una hora porque va en el orden de 0-11 (Las 12:00 i 00:00 se representa igual)
        if (hora == 12) {
            hora = 0;
        }

        //!!Resto un mes porque va en el orden de 0-11
        Calendar dataEvent = new GregorianCalendar(year, month, day, hora, minuts);


        if (calendar.compareTo(dataEvent) < 1) {
            return true;
        }

        return false;
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return mEvents;
    }

    public LiveData<ArrayList<Reserva>> getReservesPendents() {
        return mReservesPendents;
    }

    public LiveData<ArrayList<Reserva>> getReservesExpirades() {
        return mReservesExpirades;
    }

}