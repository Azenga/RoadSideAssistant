package com.project.roadsideassistant.ui.fragments.gethelp.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.models.Service;
import com.project.roadsideassistant.data.repositories.ServicesRepository;

import java.util.List;

public class ChooseServiceFragmentViewModel extends ViewModel implements ServicesRepository.OnServicesTaskComplete {

    private ServicesRepository servicesRepository;

    private MutableLiveData<List<Service>> _services = new MutableLiveData<>();

    public ChooseServiceFragmentViewModel() {
        servicesRepository = new ServicesRepository(this);

        servicesRepository.getServices();
    }

    public LiveData<List<Service>> getServices() {
        return _services;
    }

    @Override
    public void showServices(List<Service> services) {
        _services.setValue(services);
    }

    @Override
    public void showError(String message) {
        //Set error message
    }
}
