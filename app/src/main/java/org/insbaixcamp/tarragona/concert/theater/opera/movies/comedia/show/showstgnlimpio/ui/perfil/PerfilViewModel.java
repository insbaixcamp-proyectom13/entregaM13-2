package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.perfil;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Usuari;

public class PerfilViewModel extends ViewModel {
    private final MutableLiveData<Usuari> mUsuari;
    // TODO: Implement the ViewModel


    public PerfilViewModel() {

        mUsuari = new MutableLiveData<>();
    }
}