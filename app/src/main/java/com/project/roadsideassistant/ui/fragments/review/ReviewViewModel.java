package com.project.roadsideassistant.ui.fragments.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.models.Message;
import com.project.roadsideassistant.data.repositories.MessageRepository;

public class ReviewViewModel extends ViewModel implements MessageRepository.MessageTaskListener {

    private MessageRepository messageRepository = new MessageRepository(this);

    private MutableLiveData<String> _successMessage = new MutableLiveData<>();
    private MutableLiveData<String> _errorMessage = new MutableLiveData<>();

    public ReviewViewModel() {
    }

    public void addMessage(Message message) {
        messageRepository.add(message);
    }

    public LiveData<String> getSuccessMessage() {
        return _successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    @Override
    public void onComplete(String message) {
        _successMessage.setValue(message);
    }

    @Override
    public void onError(Exception exception) {
        _errorMessage.setValue(exception.getLocalizedMessage());
    }
}
