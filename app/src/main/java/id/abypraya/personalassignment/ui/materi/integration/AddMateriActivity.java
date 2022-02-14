package id.abypraya.personalassignment.ui.materi.integration;

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
import id.abypraya.personalassignment.ui.peserta.integration.AddPesertaActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class AddMateriActivity extends AppCompatActivity implements View.OnClickListener {
    private AwesomeValidation validation;
    private EditText editTextMateriName;
    private MaterialButton btnSaveData;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_materi);

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        editTextMateriName = findViewById(R.id.editTxtMateri);
        btnSaveData = findViewById(R.id.btnSaveDataMateri);

        getSupportActionBar().setTitle(ContentMenu.ADD_MATERI);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSaveData.setOnClickListener(this);
    }

    private void checkFormData() {
        validation.addValidation(this, R.id.editTxtMateri, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
        ArrayList<String> sendData = new ArrayList<>();
        String name = editTextMateriName.getText().toString().trim();

        sendData.add(name);
        String showData = utility.showDataPrompt(sendData);

        CustomDialog customDialog = new CustomDialog(AddMateriActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.ADD_MATERI);

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
}