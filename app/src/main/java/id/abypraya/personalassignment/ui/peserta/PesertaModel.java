package id.abypraya.personalassignment.ui.peserta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PesertaModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PesertaModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Peserta fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}