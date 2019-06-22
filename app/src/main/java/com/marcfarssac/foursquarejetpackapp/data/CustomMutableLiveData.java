package com.marcfarssac.foursquarejetpackapp.data;


import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.lifecycle.MutableLiveData;

public class CustomMutableLiveData<T extends BaseObservable> extends MutableLiveData<T> {

    @Override
    public void setValue(T value) {
        super.setValue(value);

        // listen to property changes
        value.addOnPropertyChangedCallback(callback);
    }

    private Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            setValue(getValue());
        }
    };

}
