package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.ui.registre;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistreViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegistreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}