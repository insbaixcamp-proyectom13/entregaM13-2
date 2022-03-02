package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.carrito;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;

import java.util.ArrayList;

public class CarritoViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Event>> mEventsReservatsPendents;
    private final MutableLiveData<ArrayList<Event>> mEventsReservatsExpirats;

    // TODO: Implement the ViewModel
    public CarritoViewModel() {
        mEventsReservatsExpirats = new MutableLiveData<>();
        mEventsReservatsPendents = new MutableLiveData<>();
    }
}