package id.abypraya.personalassignment.ui.kelas.integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.ui.Component.CustomDialog.CustomDialog;
import id.abypraya.personalassignment.ui.peserta.integration.AddPesertaActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class AddKelasActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText dateFormatEntryKelas, dateFormatEndKelas;
    private Spinner editSpMateri, editSpInstruktur;
    private MaterialButton btnSaveData;
    private DatePickerDialog.OnDateSetListener onDateSetListenerEntryDate, onDateSetListenerEndDate;

    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    private String tempDataJsonInstruktur, JSON_STRING_MATERI, JSON_STRING_INSTRUKTUR;
    String spIdMateri, spIdInstruktur;

    private ArrayList<String> listIdMateri = new ArrayList<>();
    private ArrayList<String> listIdInstruktur = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kelas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.ADD_KELAS);

        dateFormatEntryKelas = findViewById(R.id.dateStartKelas);
        dateFormatEndKelas = findViewById(R.id.dateEndKelas);

        btnSaveData = findViewById(R.id.btnSaveDataKelas);

        editSpMateri = findViewById(R.id.editSpMateriName);
        editSpInstruktur = findViewById(R.id.editSpInstrukturName);

        dateFormatEntryKelas.setOnClickListener(this);
        dateFormatEndKelas.setOnClickListener(this);

        btnSaveData.setOnClickListener(this);

        showDropDownData();

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

    private void showDropDownData() {
        class ShowDropDownData extends AsyncTask<Void, Void, String> implements AdapterView.OnItemSelectedListener {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddKelasActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiMateri = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.GET);
                String getApiInstruktur = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTRUKTUR, Config.GET);
                String resultMateri = handler.sendGetResponse(getApiMateri);
                String resultInstruktur = handler.sendGetResponse(getApiInstruktur);
                tempDataJsonInstruktur = resultInstruktur;
                return resultMateri;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();

                JSON_STRING_MATERI = message;
                JSON_STRING_INSTRUKTUR = tempDataJsonInstruktur;

                JSONObject jsonObjectMateri = null;
                JSONObject jsonObjectInstruktur = null;

                ArrayList<String> listNameInstruktur = new ArrayList<>();
                ArrayList<String> listNameMateri = new ArrayList<>();

                try {
                    jsonObjectMateri = new JSONObject(JSON_STRING_MATERI);
                    jsonObjectInstruktur = new JSONObject(JSON_STRING_INSTRUKTUR);
                    JSONArray jsonArrayMateri = jsonObjectMateri.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArrayInstruktur = jsonObjectInstruktur.getJSONArray(Config.TAG_JSON_ARRAY);

                    listNameInstruktur.add(ContentMenu.INITIALIZE_DROPDOWN_INSTRUKTUR_NAME);
                    listIdMateri.add("0");
                    listNameMateri.add(ContentMenu.INITIALIZE_DROPDOWN_MATERI_NAME);
                    listIdInstruktur.add("0");

                    for (int i = 0; i < jsonArrayMateri.length(); i++){
                        JSONObject objectMateri =  jsonArrayMateri.getJSONObject(i);
                        String id = objectMateri.getString(Config.TAG_JSON_MAT_ID);
                        String name = objectMateri.getString(Config.TAG_JSON_MAT_NAMA);

                        listIdMateri.add(id);
                        listNameMateri.add(name);
                    }

                    for (int i = 0; i < jsonArrayInstruktur.length(); i++){
                        JSONObject objectInstruktur =  jsonArrayInstruktur.getJSONObject(i);
                        String id = objectInstruktur.getString(Config.TAG_JSON_INS_ID);
                        String name = objectInstruktur.getString(Config.TAG_JSON_INS_NAMA);

                        listIdInstruktur.add(id);
                        listNameInstruktur.add(name);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                ArrayAdapter<String> adapterMateri = new ArrayAdapter<String>(AddKelasActivity.this, android.R.layout.simple_spinner_dropdown_item, listNameMateri);
                ArrayAdapter<String> adapterInstruktur = new ArrayAdapter<String>(AddKelasActivity.this, android.R.layout.simple_spinner_dropdown_item, listNameInstruktur);
                adapterMateri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterInstruktur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editSpMateri.setAdapter(adapterMateri);
                editSpInstruktur.setAdapter(adapterInstruktur);
                editSpMateri.setOnItemSelectedListener(this);
                editSpInstruktur.setOnItemSelectedListener(this);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(parent.getId() == R.id.editSpMateriName){
                    spIdMateri = String.valueOf(listIdMateri.get(position));
                }else if(parent.getId() == R.id.editSpInstrukturName){
                    spIdInstruktur = String.valueOf(listIdInstruktur.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        String entryDate = dateFormatEntryKelas.getText().toString().trim();
        String endDate = dateFormatEndKelas.getText().toString().trim();
        String materi = spIdMateri;
        String instruktur = spIdInstruktur;

        sendData.add(entryDate);
        sendData.add(endDate);
        sendData.add(materi);
        sendData.add(instruktur);

        String showData = utility.showDataPrompt(sendData);

        CustomDialog customDialog = new CustomDialog(AddKelasActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.ADD_KELAS);
        if(dateFormatEntryKelas.equals(buttons)){
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddKelasActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListenerEntryDate, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
        else if(dateFormatEndKelas.equals(buttons)){
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddKelasActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListenerEndDate, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
        else if(btnSaveData.equals(buttons)){
            if(checkFormData()){
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