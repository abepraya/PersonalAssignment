package id.abypraya.personalassignment.ui.instansi.integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.ui.Component.CustomDialog.CustomDialog;
import id.abypraya.personalassignment.ui.materi.integration.AddMateriActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class AddInstansiActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton btnSaveData;
    private EditText editTxtInstansiName, editTxtInstansiAddress;
    private AwesomeValidation validation;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instansi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.ADD_INSTANSI);

        btnSaveData = findViewById(R.id.btnSaveDataInstansi);
        btnSaveData.setOnClickListener(this);
        editTxtInstansiName = findViewById(R.id.editTxtInstansiName);
        editTxtInstansiAddress = findViewById(R.id.editTxtInstansiAddress);

        validation = new AwesomeValidation(ValidationStyle.BASIC);
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
        ArrayList<String> sendData = new ArrayList<>();
        String name = editTxtInstansiName.getText().toString().trim();
        String address = editTxtInstansiAddress.getText().toString().trim();

        sendData.add(name);
        sendData.add(address);
        String showData = utility.showDataPrompt(sendData);

        CustomDialog customDialog = new CustomDialog(AddInstansiActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.ADD_INSTANSI);

        if(btnSaveData.equals(buttons)){
            checkFormData();
            if(validation.validate()){
                customDialog.show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    customDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                }
                customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 550);
                customDialog.setCancelable(false);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }
    }

    private void checkFormData() {
        validation.addValidation(this, R.id.editTxtInstansiAddress, RegexTemplate.NOT_EMPTY, R.string.invalid_address);
        validation.addValidation(this, R.id.editTxtInstansiName, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
    }
}