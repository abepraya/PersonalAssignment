package id.abypraya.personalassignment.ui.kelas.integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.ui.Component.CustomDialog.CustomDialog;
import id.abypraya.personalassignment.ui.materi.integration.DetailMateriActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class DetailKelasActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton btnSaveData, btnDeleteData;
    private Spinner editSpMateri, editSpInstruktur;
    private DatePickerDialog.OnDateSetListener onDateSetListenerEntryDate, onDateSetListenerEndDate;
    private EditText dateFormatEntryKelas, dateFormatEndKelas;

    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    private String tempIdInstruktur, tempIdMateri, valIdInstruktur, valIdMateri;
    String spListMateri, spListInstruktur, getId;

    private ArrayList<String> arrListIdMateri = new ArrayList<>();
    private ArrayList<String> arrListNameMateri = new ArrayList<>();
    private ArrayList<String> arrListIdInstruktur = new ArrayList<>();
    private ArrayList<String> arrListNameInstruktur = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kelas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.UPDATE_KELAS);

        dateFormatEntryKelas = findViewById(R.id.datesStartKelas);
        dateFormatEndKelas = findViewById(R.id.datesEndKelas);

        btnSaveData = findViewById(R.id.btnUpdateDatasKelas);
        btnDeleteData = findViewById(R.id.btnDeleteDatasKelas);

        editSpMateri = findViewById(R.id.editSpMateriNames);
        editSpInstruktur = findViewById(R.id.editSpInstrukturNames);

        dateFormatEntryKelas.setOnClickListener(this);
        dateFormatEndKelas.setOnClickListener(this);

        btnSaveData.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);

        Intent receiveIntentParams = getIntent();
        getId = receiveIntentParams.getStringExtra(Config.KLS_ID);

        displayData();

        onDateSetListenerEntryDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                dateFormatEntryKelas.setText(date);
            }
        };

        onDateSetListenerEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                dateFormatEndKelas.setText(date);
            }
        };
    }

    private void displayData() {
        class DisplayData extends AsyncTask<Void, Void, String> implements AdapterView.OnItemSelectedListener {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailKelasActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.GET_DETAIL);

                String getApiUrlInstruktur = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTRUKTUR, Config.GET);
                String getApiUrlMateri = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.GET);

                String getDataListInstruktur = handler.sendGetResponse(getApiUrlInstruktur);
                String getDataListMateri = handler.sendGetResponse(getApiUrlMateri);

                spListInstruktur = getDataListInstruktur;
                spListMateri = getDataListMateri;

                String result = handler.sendGetDetailResponse(getApiKelas, getId);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                displayDetailData(message);

                JSONObject jsonObjectInstruktur = null;
                JSONObject jsonObjectMateri = null;

                JSONArray jsonArrInstruktur = null;
                JSONArray jsonArrMateri = null;

                try {
                    jsonObjectInstruktur = new JSONObject(spListInstruktur);
                    jsonObjectMateri = new JSONObject(spListMateri);

                    jsonArrInstruktur = jsonObjectInstruktur.getJSONArray(Config.TAG_JSON_ARRAY);
                    jsonArrMateri = jsonObjectMateri.getJSONArray(Config.TAG_JSON_ARRAY);

                    for(int i = 0; i < jsonArrInstruktur.length(); ++i){
                        JSONObject object = jsonArrInstruktur.getJSONObject(i);
                        String nameInstruktur = object.getString(Config.TAG_JSON_INS_NAMA);
                        String idInstruktur = object.getString(Config.TAG_JSON_INS_ID);

                        arrListIdInstruktur.add(idInstruktur);
                        arrListNameInstruktur.add(nameInstruktur);
                    }

                    for(int i = 0; i < jsonArrMateri.length(); ++i){
                        JSONObject object = jsonArrMateri.getJSONObject(i);
                        String nameMateri = object.getString(Config.TAG_JSON_MAT_NAMA);
                        String idMateri = object.getString(Config.TAG_JSON_MAT_ID);

                        arrListIdMateri.add(idMateri);
                        arrListNameMateri.add(nameMateri);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                ArrayAdapter<String> adapterInstruktur = new ArrayAdapter<>(DetailKelasActivity.this,android.R.layout.simple_spinner_dropdown_item, arrListNameInstruktur);
                ArrayAdapter<String> adapterMateri = new ArrayAdapter<>(DetailKelasActivity.this,android.R.layout.simple_spinner_dropdown_item, arrListNameMateri);

                adapterInstruktur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterMateri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                editSpMateri.setAdapter(adapterMateri);
                editSpInstruktur.setAdapter(adapterInstruktur);

                int idxInstruktur = Integer.parseInt(tempIdInstruktur);
                int idxMateri = Integer.parseInt(tempIdMateri);

                int insPosition = adapterInstruktur.getPosition(arrListNameInstruktur.get(idxInstruktur - 1));
                int matPosition = adapterMateri.getPosition(arrListNameMateri.get(idxMateri - 1));

                editSpMateri.setSelection(matPosition);
                editSpInstruktur.setSelection(insPosition);

                editSpMateri.setOnItemSelectedListener(this);
                editSpInstruktur.setOnItemSelectedListener(this);

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(parent.equals(editSpMateri)){
                    valIdMateri = String.valueOf(arrListIdMateri.get(position));
                }
                else if(parent.equals(editSpInstruktur)){
                    valIdInstruktur = String.valueOf(arrListIdInstruktur.get(position));
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

            String entryDate, endDate, idMateri, idInstruktur;
            entryDate = object.getString(Config.TAG_JSON_KLS_ENTRY_DATE);
            endDate = object.getString(Config.TAG_JSON_KLS_END_DATE);
            idMateri = object.getString(Config.TAG_JSON_MAT_ID);
            idInstruktur = object.getString(Config.TAG_JSON_INS_ID);

            tempIdInstruktur = idInstruktur;
            tempIdMateri = idMateri;

            dateFormatEntryKelas.setText(entryDate);
            dateFormatEndKelas.setText(endDate);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
        ArrayList<String> sendData = new ArrayList<>();
        ArrayList<String> sendId = new ArrayList<>();
        String entryDate = dateFormatEntryKelas.getText().toString().trim();
        String endDate = dateFormatEndKelas.getText().toString().trim();
        String materi = valIdMateri;
        String instruktur = valIdInstruktur;

        sendData.add(getId);
        sendData.add(entryDate);
        sendData.add(endDate);
        sendData.add(materi);
        sendData.add(instruktur);

        sendId.add(getId);

        String showData = utility.showDataPrompt(sendData);

        if(dateFormatEntryKelas.equals(buttons)){
            DatePickerDialog datePickerDialog = new DatePickerDialog(DetailKelasActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListenerEntryDate, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
        else if(dateFormatEndKelas.equals(buttons)){
            DatePickerDialog datePickerDialog = new DatePickerDialog(DetailKelasActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListenerEndDate, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
        else if(btnSaveData.equals(buttons)){
            if(checkFormData()){
                CustomDialog customDialog = new CustomDialog(DetailKelasActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_UPDATE_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.UPDATE_KELAS);
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
            CustomDialog customDialog = new CustomDialog(DetailKelasActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DELETE_DETAIL,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendId, ContentMenu.DELETE_KELAS);
            customDialog.show();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                customDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
            }
            customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 550);
            customDialog.setCancelable(false);
            customDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    private boolean checkFormData() {
        boolean result = false;
        TextView errTextInstruktur = (TextView) editSpInstruktur.getSelectedView();
        TextView errTextMateri = (TextView) editSpMateri.getSelectedView();
        String errTextEntryDate = dateFormatEntryKelas.getText().toString();
        String errTextEndDate = dateFormatEndKelas.getText().toString();
        String errInstruktur = String.valueOf(R.string.invalid_instruktur);
        String errMateri = String.valueOf(R.string.invalid_materi);

        if(editSpMateri.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_MATERI_NAME)){
            errTextMateri.setError(errMateri);
            errTextMateri.setText(ContentMenu.ERROR_MATERI_NAME);
            errTextMateri.setTextColor(Color.RED);
            result = false;
        }
        else if(editSpInstruktur.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_INSTRUKTUR_NAME)){
            errTextInstruktur.setError(errInstruktur);
            errTextInstruktur.setText(ContentMenu.ERROR_INSTRUKTUR_NAME);
            errTextInstruktur.setTextColor(Color.RED);
            result = false;
        }
        else if(errTextEntryDate.equals("")){
            dateFormatEntryKelas.setError(ContentMenu.ERROR_DATE);
            result = false;
        }
        else if(errTextEndDate.equals("")){
            dateFormatEndKelas.setError(ContentMenu.ERROR_DATE);
            result = false;
        }
        else{
            result = true;
        }
        return result;
    }
}