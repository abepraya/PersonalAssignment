package id.abypraya.personalassignment.ui.materi.integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.ui.Component.CustomDialog.CustomDialog;
import id.abypraya.personalassignment.ui.instansi.integration.DetailInstansiActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class DetailMateriActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editMateriName;
    private MaterialButton btnUpdateData, btnDeleteData;

    private AwesomeValidation validation;
    private String getId;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi);

        getSupportActionBar().setTitle(ContentMenu.UPDATE_MATERI);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        btnDeleteData = findViewById(R.id.btnDeleteDataMateri);
        btnUpdateData = findViewById(R.id.btnUpdateDataMateri);
        editMateriName = findViewById(R.id.editTxtMateriName);

        btnUpdateData.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);

        Intent receiveIntentParams = getIntent();
        getId = receiveIntentParams.getStringExtra(Config.MAT_ID);

        displayData();

    }

    private void displayData() {
        class DisplayData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailMateriActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.GET_DETAIL);
                String result = handler.sendGetDetailResponse(getApi, getId);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                displayDetailData(message);
            }
        }
        DisplayData displayData = new DisplayData();
        displayData.execute();
    }

    private void displayDetailData(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject object = result.getJSONObject(0);

            String name;
            name = object.getString(Config.TAG_JSON_MAT_NAMA);

            editMateriName.setText(name);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void checkFormData() {
        validation.addValidation(this, R.id.editTxtMateriName , RegexTemplate.NOT_EMPTY, R.string.invalid_name);
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt;
        ArrayList<String> sendData = new ArrayList<>();
        ArrayList<String> sendId = new ArrayList<>();

        String name = editMateriName.getText().toString().trim();

        sendData.add(getId);
        sendData.add(name);

        sendId.add(getId);

        String showData = utility.showDataPrompt(sendData);

        if(btnUpdateData.equals(buttons)){
            checkFormData();
            drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
            CustomDialog customDialog = new CustomDialog(DetailMateriActivity.this, ContentMenu.PROMPT_CHECK_UPDATE_TITLE, ContentMenu.PROMPT_CHECK_UPDATE_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.UPDATE_MATERI);
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
        else if(btnDeleteData.equals(buttons)){
            drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
            CustomDialog customDialog = new CustomDialog(DetailMateriActivity.this, ContentMenu.PROMPT_CHECK_DELETE_TITLE, ContentMenu.PROMPT_CHECK_DELETE_DETAIL,ContentMenu.PROMPT_CHECK_DELETE_CHECKBOXLIST, drawableImgPrompt, sendId, ContentMenu.DELETE_MATERI);
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