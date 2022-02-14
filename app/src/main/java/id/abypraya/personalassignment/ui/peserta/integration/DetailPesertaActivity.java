package id.abypraya.personalassignment.ui.peserta.integration;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
import id.abypraya.personalassignment.ui.instruktur.integration.DetailInstrukturActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class DetailPesertaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editPesertasName, editPesertasEmail, editPesertasPhoneNumber;
    private Spinner editSpPesertasInstansi;
    private MaterialButton btnUpdateData, btnDeleteData;

    private ArrayList<String> arrayListInstansi = new ArrayList<>();
    private ArrayList<String> arrayListIdInstansi = new ArrayList<>();
    private ArrayList<String> arrayListDetailInstansi = new ArrayList<>();

    String getId, spListInstansi, valIdInstansi, tempIdInstansi;

    private AwesomeValidation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peserta);

        getSupportActionBar().setTitle(ContentMenu.UPDATE_PESERTA);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validation = new AwesomeValidation(ValidationStyle.BASIC);

        editPesertasName = findViewById(R.id.editTxtPesertasName);
        editPesertasEmail = findViewById(R.id.editTxtPesertasEmail);
        editPesertasPhoneNumber = findViewById(R.id.editTxtPesertasPhone);

        editSpPesertasInstansi = findViewById(R.id.editSpPesertasInstansi);

        btnUpdateData = findViewById(R.id.btnUpdateDataPeserta);
        btnDeleteData = findViewById(R.id.btnDeleteDataPeserta);

        btnUpdateData.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);

        Intent receiveIntentParams = getIntent();
        getId = receiveIntentParams.getStringExtra(Config.PST_ID);

        displayData();
    }

    private void displayData() {
        class DisplayData extends AsyncTask<Void, Void, String> implements AdapterView.OnItemSelectedListener {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailPesertaActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();

                String getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.GET_DETAIL);

                String getApiInstansi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.GET);
                String getInstansiList = handler.sendGetResponse(getApiInstansi);
                spListInstansi = getInstansiList;

                String result = handler.sendGetDetailResponse(getApi, getId);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                displayDetailData(message);

                JSONObject jsonObjectInstansi = null;
                JSONArray jsonArrInstansi = null;

                JSONArray jsonArrDetailInstansi = null;

                try {
                    jsonObjectInstansi = new JSONObject(spListInstansi);
                    jsonArrInstansi = jsonObjectInstansi.getJSONArray(Config.TAG_JSON_ARRAY);

                    for (int i = 0; i < jsonArrInstansi.length(); ++i){
                        JSONObject object = jsonArrInstansi.getJSONObject(i);
                        String instansiName = object.getString(Config.TAG_JSON_INSTANSI_NAMA);
                        String instansiId = object.getString(Config.TAG_JSON_INSTANSI_ID);

                        arrayListInstansi.add(instansiName);
                        arrayListIdInstansi.add(instansiId);
                    }

                    for(int i = 0; i < jsonArrDetailInstansi.length(); ++i){
                        JSONObject object = jsonArrDetailInstansi.getJSONObject(i);
                        String specificInstansiName = object.getString(Config.TAG_JSON_INSTANSI_NAMA_TEMP);

                        arrayListDetailInstansi.add(specificInstansiName);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                ArrayAdapter<String> adapterDropDownInstansi = new ArrayAdapter<String>(DetailPesertaActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListInstansi);

                adapterDropDownInstansi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                editSpPesertasInstansi.setAdapter(adapterDropDownInstansi);
                int idxInstansi = Integer.parseInt(tempIdInstansi);
                int position = adapterDropDownInstansi.getPosition(arrayListInstansi.get(idxInstansi - 1));
                editSpPesertasInstansi.setSelection(position);
                editSpPesertasInstansi.setOnItemSelectedListener(this);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(parent.equals(editSpPesertasInstansi)){
                    valIdInstansi = String.valueOf(arrayListIdInstansi.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

            String name, email, phone, idInstansi;
            name = object.getString(Config.TAG_JSON_PST_NAMA);
            email = object.getString(Config.TAG_JSON_PST_EMAIL);
            phone = object.getString(Config.TAG_JSON_PST_HP);
            idInstansi = object.getString(Config.TAG_JSON_INSTANSI_ID);
            tempIdInstansi = idInstansi;

            editPesertasName.setText(name);
            editPesertasEmail.setText(email);
            editPesertasPhoneNumber.setText(phone);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    private void checkFormData() {
        validation.addValidation(this, R.id.editTxtPesertasPhone , ContentMenu.REGEX_IDN_PHONENUMBER, R.string.invalid_phone_number);
        validation.addValidation(this, R.id.editTxtPesertasName , RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        validation.addValidation(this, R.id.editTxtPesertasEmail ,android.util.Patterns.EMAIL_ADDRESS, R.string.invalid_address);
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt;
        ArrayList<String> sendData = new ArrayList<>();
        ArrayList<String> sendId = new ArrayList<>();

        String name = editPesertasName.getText().toString().trim();
        String email = editPesertasEmail.getText().toString().trim();
        String phone = editPesertasPhoneNumber.getText().toString().trim();
        String idInstansi = valIdInstansi;

        sendData.add(getId);
        sendData.add(name);
        sendData.add(email);
        sendData.add(phone);
        sendData.add(idInstansi);

        sendId.add(getId);

        String showData = utility.showDataPrompt(sendData);

        if(btnUpdateData.equals(buttons)){
            checkFormData();
            drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
            CustomDialog customDialog = new CustomDialog(DetailPesertaActivity.this, ContentMenu.PROMPT_CHECK_UPDATE_TITLE, ContentMenu.PROMPT_CHECK_UPDATE_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.UPDATE_PESERTA);
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
            CustomDialog customDialog = new CustomDialog(DetailPesertaActivity.this, ContentMenu.PROMPT_CHECK_DELETE_TITLE, ContentMenu.PROMPT_CHECK_DELETE_DETAIL,ContentMenu.PROMPT_CHECK_DELETE_CHECKBOXLIST, drawableImgPrompt, sendId, ContentMenu.DELETE_PESERTA);
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