package id.abypraya.personalassignment.ui.instruktur.integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

public class AddInstrukturActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextInstrukturName, editTextInstrukturEmail, editTextInstrukturPhoneNumber;
    private MaterialButton btnSaveData;
    private AwesomeValidation validation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instruktur);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.ADD_INSTRUKTUR);

        editTextInstrukturName = findViewById(R.id.editTxtInstrukturName);
        editTextInstrukturEmail = findViewById(R.id.editTxtInstrukturEmail);
        editTextInstrukturPhoneNumber = findViewById(R.id.editTxtInstrukturPhone);

        btnSaveData = findViewById(R.id.btnSaveDataInstruktur);
        btnSaveData.setOnClickListener(this);

        validation = new AwesomeValidation(ValidationStyle.BASIC);
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
        ArrayList<String> sendData = new ArrayList<>();
        String name = editTextInstrukturName.getText().toString().trim();
        String email = editTextInstrukturEmail.getText().toString().trim();
        String phoneNumber = editTextInstrukturPhoneNumber.getText().toString().trim();

        sendData.add(name);
        sendData.add(email);
        sendData.add(phoneNumber);
        String showData = utility.showDataPrompt(sendData);

        CustomDialog customDialog = new CustomDialog(AddInstrukturActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.ADD_INSTRUKTUR);

        if(btnSaveData.equals(buttons)){
            checkFormData();
            if (validation.validate()) {
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
        validation.addValidation(this, R.id.editTxtInstrukturName, ContentMenu.REGEX_NAME, R.string.invalid_name);
        validation.addValidation(this, R.id.editTxtInstrukturEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        validation.addValidation(this, R.id.editTxtInstrukturPhone, ContentMenu.REGEX_IDN_PHONENUMBER, R.string.invalid_phone_number);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}