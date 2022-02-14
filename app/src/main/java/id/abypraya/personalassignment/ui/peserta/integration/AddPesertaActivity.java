package id.abypraya.personalassignment.ui.peserta.integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.ui.Component.CustomDialog.CustomDialog;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class AddPesertaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTxtPesertaName, editTxtPesertaEmail, editTxtPesertaPhoneNumber;
    private Spinner editSpPesertaInstansi;
    private MaterialButton btnSaveData;
    private AwesomeValidation validation;
    private String dropDownNameInstansi;
    private String dropDownIdInstansi;
    ArrayList<String> arrListIdInstansi = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_peserta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.ADD_PESERTA);

        editTxtPesertaName = findViewById(R.id.editTxtPesertaName);
        editTxtPesertaEmail = findViewById(R.id.editTxtPesertaEmail);
        editTxtPesertaPhoneNumber = findViewById(R.id.editTxtPesertaPhone);

        editSpPesertaInstansi = findViewById(R.id.editSpPesertaInstansi);

        btnSaveData = findViewById(R.id.btnSaveDataPeserta);
        btnSaveData.setOnClickListener(this);

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        showDropDownData();

    }

    private void showDropDownData() {
        class ShowDropDownData extends AsyncTask<Void, Void, String> implements AdapterView.OnItemSelectedListener {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddPesertaActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiInstansi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.GET);
                String result = handler.sendGetResponse(getApiInstansi);
                return result;
            }

            @Override
            protected void onPostExecute(String dataJson) {
                super.onPostExecute(dataJson);

                JSONObject jsonObjectInstansi = null;
                JSONArray jsonArrInstansi = null;
                ArrayList<String> arrListInstansi = new ArrayList<>();

                loading.dismiss();
                dropDownNameInstansi = dataJson;

                try {
                    jsonObjectInstansi = new JSONObject(dropDownNameInstansi);
                    jsonArrInstansi = jsonObjectInstansi.getJSONArray(Config.TAG_JSON_ARRAY);
                    arrListInstansi.add(ContentMenu.INITIALIZE_DROPDOWN_INSTANSI_NAME);
                    arrListIdInstansi.add("0");
                    for(int i = 0; i < jsonArrInstansi.length(); ++i){
                        JSONObject object = jsonArrInstansi.getJSONObject(i);
                        String instansiName = object.getString(Config.TAG_JSON_INSTANSI_NAMA);
                        String instansiId = object.getString(Config.TAG_JSON_INSTANSI_ID);

                        arrListInstansi.add(instansiName);
                        arrListIdInstansi.add(instansiId);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapterDropDownInstansi = new ArrayAdapter<String>(AddPesertaActivity.this, android.R.layout.simple_spinner_dropdown_item, arrListInstansi);

                adapterDropDownInstansi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                editSpPesertaInstansi.setAdapter(adapterDropDownInstansi);
                editSpPesertaInstansi.setOnItemSelectedListener(this);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(parent.equals(editSpPesertaInstansi)){
                    dropDownIdInstansi = String.valueOf(arrListIdInstansi.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        ShowDropDownData showDropDownData = new ShowDropDownData();
        showDropDownData.execute();
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
        ArrayList<String> sendData = new ArrayList<>();
        String name = editTxtPesertaName.getText().toString().trim();
        String email = editTxtPesertaEmail.getText().toString().trim();
        String phone = editTxtPesertaPhoneNumber.getText().toString().trim();
        String idIns = dropDownIdInstansi;

        sendData.add(name);
        sendData.add(email);
        sendData.add(phone);
        sendData.add(idIns);

        String showData = utility.showDataPrompt(sendData);

        CustomDialog customDialog = new CustomDialog(AddPesertaActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.ADD_PESERTA);
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
        TextView errText = (TextView) editSpPesertaInstansi.getSelectedView();
        String err = String.valueOf(R.string.invalid_instansi);
        validation.addValidation(this, R.id.editTxtPesertaName, ContentMenu.REGEX_NAME, R.string.invalid_name);
        validation.addValidation(this, R.id.editTxtPesertaEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        validation.addValidation(this, R.id.editTxtPesertaPhone, ContentMenu.REGEX_IDN_PHONENUMBER, R.string.invalid_phone_number);

        if (editSpPesertaInstansi.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_INSTANSI_NAME)) {
            errText.setError(err);
            errText.setText(ContentMenu.ERROR_INSTANSI_NAME);
            errText.setTextColor(Color.RED);
        }
    }
}