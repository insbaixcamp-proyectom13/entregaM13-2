package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Opinio;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

import java.util.ArrayList;

public class RealtimeDatabase {

    private static RealtimeDatabase instance;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private ArrayList<Event> listEvents;
    private ArrayList<Reserva> listReserves;
    private FirebaseUser usuariRegistrat;
    private Usuari registrat;
    Gson gson;

    public static RealtimeDatabase getInstance() {

        if (instance == null) {
            return new RealtimeDatabase();
        }

        return instance;
    }

    public RealtimeDatabase() {

        gson = new Gson();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
//        listEvents = carregaEvents();
//        listOpinions = carregaOpinions();
//        listReserves = carregaReserves();
        usuariRegistrat = FirebaseAuth.getInstance().getCurrentUser();
        if (usuariRegistrat != null) {
            registrat = carregaDadesUsuari(usuariRegistrat.getUid());
        }

    }

    public void getCorreuRegistrat(final OnUserLoaded loaded) {
        loaded.onLoadedData(carregaDadesUsuari(FirebaseAuth.getInstance().getUid()).getCorreu());
    }

    public Usuari getRegistrat() {
        return registrat;
    }

    public void setRegistrat(Usuari registrat) {
        this.registrat = registrat;
    }

    //Crea una instancia con los datos del usuario relevantes en la bd
    public Usuari carregaDadesUsuari(String uid) {

        if (!usuariRegistrat.isAnonymous()) {
            myRef.child("usuaris").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String string = gson.toJson(snapshot.getValue());
                    registrat = gson.fromJson(string, Usuari.class);
//                    dadesUsuari.setReservesUsuari(getReservesUsuaris());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            return new Usuari();
        }

        return registrat;
    }

    //Filtra todas las reservas devolviendo aquellas
    //que coincidan el cliente con el uid FirebaseAuth
    public ArrayList<Reserva> getReservesUsuaris() {
        ArrayList<Reserva> reservesUsuari = new ArrayList<>();

        for (Reserva reserva : listReserves) {
            if (reserva.getClient().equals(usuariRegistrat.getUid())) {
                reservesUsuari.add(reserva);
            }
        }

        return reservesUsuari;
    }

    public interface OnUserLoaded {
        public void onLoadedData(String correu);
    }

}
