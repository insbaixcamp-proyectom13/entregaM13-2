package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<ArrayList<Event>> mActiveEvents;

    private DatabaseReference myRef;
    private FirebaseDatabase db;
    private Gson gson;
    private ArrayList<Event> events;

    public HomeViewModel() {
        gson = new Gson();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();

        events = new ArrayList<>();
        mText = new MutableLiveData<>();
        mActiveEvents = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        setEvents();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setEvents() {


        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String json = gson.toJson(snapshot.getValue());
                events = gson.fromJson(json, new TypeToken<ArrayList<Event>>(){}.getType());
//                Log.i("json", events.toString());
                try {
                    mActiveEvents.setValue(getActiveEvents(events));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<ArrayList<Event>> getLiveActiveEvents () {
        return mActiveEvents;
    }

    private ArrayList<Event> getActiveEvents(ArrayList<Event> events) throws ParseException {
        ArrayList<Event> activeEvents = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (Event event : events) {
            if (eventIsActive(event, calendar)) {
                activeEvents.add(event);
            }
        }

        return activeEvents;
    }

    private boolean eventIsActive(Event event, Calendar calendar) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        int year = Integer.parseInt(event.getData().substring(6, event.getData().length()).trim());
        int month = Integer.parseInt(event.getData().substring(3, 5).trim())-1;
        int day = Integer.parseInt(event.getData().substring(0, 2).trim());
        int hora = Integer.parseInt(event.getHora().trim().substring(0, 2));
        int minuts = Integer.parseInt(event.getHora().trim().substring(3, event.getHora().length()));

        if (hora == 12) {
            hora = 0;
        }

        //!!Resto un mes porque al añadirlo suma un mes o un año si es 12
        Calendar dataEvent = new GregorianCalendar(year, month, day, hora, minuts);
//        Log.i("data:" + event.getId(), String.valueOf(calendar.compareTo(dataEvent)) + " __ " + format.format(dataEvent.getTime()) + " __ " + format.format(calendar.getTime()));


        if (calendar.compareTo(dataEvent) < 1) {
//            Log.i("data:" + event.getId(), event.getData());
            return true;
        }

        return false;
    }
}