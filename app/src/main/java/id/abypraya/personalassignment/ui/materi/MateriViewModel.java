package id.abypraya.personalassignment.ui.materi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MateriViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MateriViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Peserta fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}