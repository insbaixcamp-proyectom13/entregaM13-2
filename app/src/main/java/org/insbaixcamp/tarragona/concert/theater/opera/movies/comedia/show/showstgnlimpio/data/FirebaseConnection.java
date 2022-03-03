package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.data;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Opinio;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

import java.util.ArrayList;

public class FirebaseConnection {

    FirebaseDatabase db;
    DatabaseReference myRef;
    Gson gson;
    String jsonValues;
    ArrayList<Event> listEvents;
    ArrayList listOpinions;
    ArrayList listOpinionsAll;
    ArrayList listReserves;
    Usuari dadesUsuari;
    FireStoreResults fireStoreResults;

    public DatabaseReference getDatabaseReference() {
        return this.myRef;
    }

    public ArrayList getListEvents() {
        return listEvents;
    }

    public ArrayList getListReserves() {
        return listReserves;
    }

    public Usuari getDadesUsuari() {
        return dadesUsuari;
    }

    public ArrayList getListOpinions() {
        return listOpinions;
    }

    public ArrayList getListOpinionsAll() {
        return listOpinionsAll;
    }

    public FirebaseConnection () {
        db = FirebaseDatabase.getInstance();
        gson = new Gson();
        myRef = db.getReference();
        listEvents = new ArrayList<>();
        listOpinions = new ArrayList<>();
        listReserves = new ArrayList<>();
        dadesUsuari = new Usuari();
        listOpinionsAll = new ArrayList();
    }

    public void getEventOpinions(int idEvent, final FireStoreResults results) {

        myRef.child("opinions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                String string = gson.toJson(task.getResult().getValue());
                ArrayList<Opinio> opinions = gson.fromJson(string, new TypeToken<ArrayList<Opinio>>(){}.getType());
                ArrayList<Opinio> filteredOpinions = new ArrayList<>();

                for (Opinio opinion : opinions) {
                    if (opinion.getEvent() == idEvent) {
                        filteredOpinions.add(opinion);
                    }
                }

                listOpinions = new ArrayList(filteredOpinions);

                results.onResultGet(filteredOpinions);
            }
        });

    }



    public void getUserData(String UID, final FireStoreResults resul) {

        myRef.child("usuaris").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String string = gson.toJson(task.getResult().getValue());
                Usuari user;

                user = gson.fromJson(string, Usuari.class);
                dadesUsuari = user;
                ArrayList<Usuari> users = new ArrayList<>();
                users.add(user);
                resul.onResultGet(users);
            }
        });

    }

    public void getOpinions(final FireStoreResults resul) {

        myRef.child("opinions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {

                String string = gson.toJson(task.getResult().getValue());
                ArrayList<Opinio> opinions = gson.fromJson(string, new TypeToken<ArrayList<Opinio>>(){}.getType());
                listOpinions = opinions;

                resul.onResultGet(opinions);
            }
        });

    }

    public void postListener(ChildEventListener listener) {
        myRef.addChildEventListener(listener);
    }

    public void getUserReserves (String UID, final FireStoreResults resul) {

        myRef.child("reserves").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                String string = gson.toJson(task.getResult().getValue());
                ArrayList<Reserva> reserves = gson.fromJson(string, new TypeToken<ArrayList<Reserva>>(){}.getType());
                ArrayList<Reserva> filteresReserves = new ArrayList<>();

                for (Reserva reserva : reserves) {
                    if (reserva.getClient().equals(UID)) {
                        filteresReserves.add(reserva);
                    }
                }

                listReserves = new ArrayList(filteresReserves);

                resul.onResultGet(filteresReserves);
            }
        });

    }

    public void getReserves(final FireStoreResults resul) {

        myRef.child("reserves").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {

                String string = gson.toJson(task.getResult().getValue());
                ArrayList<Reserva> reserves = gson.fromJson(string, new TypeToken<ArrayList<Reserva>>(){}.getType());
                listReserves = reserves;

                resul.onResultGet(reserves);
            }
        });

    }


    public void getEvents (final FireStoreResults resul) {

        myRef.child("events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {

                String string = gson.toJson(task.getResult().getValue());
                ArrayList<Event> events = gson.fromJson(string, new TypeToken<ArrayList<Event>>(){}.getType());
                listEvents = events;

                resul.onResultGet(events);
            }
        });

    }

    public void writeOpinion(String opinio, int id, String usuari, String nomUsuari, int puntuacion, final OnCompleteListener listener) {
        Opinio opinion = new Opinio(opinio, id, usuari, nomUsuari, puntuacion);
        getOpinions(new FireStoreResults() {
            @Override
            public void onResultGet(ArrayList list) {
                myRef.child("opinions").child(String.valueOf(list.size())).setValue(opinion);
            }
        });

    }

    public void getReservedEvents(final FireStoreResults results) {

        ArrayList<Event> reservedEvents = new ArrayList<>();
        ArrayList<Reserva> filteresReserves = new ArrayList<>();

        myRef.child("reserves").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Event getEvent(int id) {

        Event newEvent = null;
        for (Event event : listEvents) {
            if (event.getId() == id) {
                newEvent = event;
            }
        }
        return newEvent;
    }

    public interface FireStoreResults {
        public void onResultGet(ArrayList list);
    }

}
