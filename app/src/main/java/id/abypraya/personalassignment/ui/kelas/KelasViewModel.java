package id.abypraya.personalassignment.ui.kelas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KelasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public KelasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}