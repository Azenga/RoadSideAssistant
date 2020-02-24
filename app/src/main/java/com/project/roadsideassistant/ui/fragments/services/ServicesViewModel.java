package com.project.roadsideassistant.ui.fragments.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.models.Service;
import com.project.roadsideassistant.data.repositories.ServicesRepository;

import java.util.List;

public class ServicesViewModel extends ViewModel implements ServicesRepository.OnServicesTaskComplete {

    private static final String TAG = "ServicesViewModel";
    private ServicesRepository servicesRepository;

    private MutableLiveData<List<Service>> _services = new MutableLiveData<>();

    public LiveData<List<Service>> getServices() {
        return _services;
    }

    public ServicesViewModel() {
        servicesRepository = new ServicesRepository(this);
        servicesRepository.getServices();
    }

    @Override
    public void showServices(List<Service> services) {
        Log.d(TAG, "showServices: count: " + services.size());
        _services.setValue(services);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "showError: viewModel: " + message);
    }
}
