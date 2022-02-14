package id.abypraya.personalassignment.ui.kelas.integration.SubDetailKelas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.ui.Component.CustomDialog.CustomDialog;
import id.abypraya.personalassignment.ui.kelas.integration.AddKelasActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class AddSubDetailKelasActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner editSpKelas, editSpPeserta;
    private MaterialButton btnSaveData;

    ArrayList<String> listIdKelas = new ArrayList<>();
    ArrayList<String> listIdPeserta = new ArrayList<>();

    private String tempDataJsonPeserta, JSON_STRING_PESERTA, JSON_STRING_KELAS, spIdKelas, spIdPeserta;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_detail_kelas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.ADD_DETAIL_KELAS);

        editSpKelas = findViewById(R.id.editSpKelas);
        editSpPeserta = findViewById(R.id.editSpPeserta);

        btnSaveData = findViewById(R.id.btnSaveDataSubKelas);
        btnSaveData.setOnClickListener(this);

        showDropDownData();
    }

    private void showDropDownData() {
        class ShowDropDownData extends AsyncTask<Void, Void, String> implements AdapterView.OnItemSelectedListener {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddSubDetailKelasActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiPeserta = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.GET);
                String getApiKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.GET);
                String resultKelas = handler.sendGetResponse(getApiKelas);
                String resultPeserta = handler.sendGetResponse(getApiPeserta);
                tempDataJsonPeserta = resultPeserta;
                return resultKelas;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();

                JSON_STRING_KELAS = message;
                JSON_STRING_PESERTA = tempDataJsonPeserta;

                JSONObject jsonObjectKelas = null;
                JSONObject jsonObjectPeserta = null;

                ArrayList<String> listNameKelas = new ArrayList<>();
                ArrayList<String> listNamePeserta = new ArrayList<>();

                try {
                    jsonObjectKelas = new JSONObject(JSON_STRING_KELAS);
                    jsonObjectPeserta = new JSONObject(JSON_STRING_PESERTA);
                    JSONArray jsonArrayKelas = jsonObjectKelas.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArrayPeserta = jsonObjectPeserta.getJSONArray(Config.TAG_JSON_ARRAY);

                    listNameKelas.add(ContentMenu.INITIALIZE_DROPDOWN_KELAS_NAME);
                    listIdKelas.add("0");
                    listNamePeserta.add(ContentMenu.INITIALIZE_DROPDOWN_PESERTA_NAME);
                    listIdPeserta.add("0");
                    for (int i = 0; i < jsonArrayKelas.length(); i++){
                        JSONObject objectMateri =  jsonArrayKelas.getJSONObject(i);
                        String id = objectMateri.getString(Config.TAG_JSON_KLS_ID);
                        String name = objectMateri.getString(Config.TAG_JSON_KLS_NAME_MATERI);

                        listIdKelas.add(id);
                        listNameKelas.add(name);
                    }

                    for (int i = 0; i < jsonArrayPeserta.length(); i++){
                        JSONObject objectInstruktur =  jsonArrayPeserta.getJSONObject(i);
                        String id = objectInstruktur.getString(Config.TAG_JSON_PST_ID);
                        String name = objectInstruktur.getString(Config.TAG_JSON_PST_NAMA);

                        listIdPeserta.add(id);
                        listNamePeserta.add(name);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                ArrayAdapter<String> adapterKelas = new ArrayAdapter<String>(AddSubDetailKelasActivity.this, android.R.layout.simple_spinner_dropdown_item, listNameKelas);
                ArrayAdapter<String> adapterPeserta = new ArrayAdapter<String>(AddSubDetailKelasActivity.this, android.R.layout.simple_spinner_dropdown_item, listNamePeserta);
                adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterPeserta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                editSpKelas.setAdapter(adapterKelas);
                editSpPeserta.setAdapter(adapterPeserta);
                editSpKelas.setOnItemSelectedListener(this);
                editSpPeserta.setOnItemSelectedListener(this);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(parent.getId() == R.id.editSpKelas){
                    spIdKelas = String.valueOf(listIdKelas.get(position));
                }else if(parent.getId() == R.id.editSpPeserta){
                    spIdPeserta = String.valueOf(listIdPeserta.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        ShowDropDownData showDropDownData = new ShowDropDownData();
        showDropDownData.execute();
    }

    private boolean checkFormData() {
        boolean result = false;
        TextView errTextKelas = (TextView) editSpKelas.getSelectedView();
        TextView errTextPeserta = (TextView) editSpPeserta.getSelectedView();
        String errKelas = String.valueOf(R.string.invalid_kelas);
        String errPeserta = String.valueOf(R.string.invalid_peserta);

        if(editSpKelas.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_KELAS_NAME)){
            errTextKelas.setError(errKelas);
            errTextKelas.setText(ContentMenu.ERROR_KELAS);
            errTextKelas.setTextColor(Color.RED);
            result = false;
        }
        else if(editSpPeserta.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_PESERTA_NAME)){
            errTextPeserta.setError(errPeserta);
            errTextPeserta.setText(ContentMenu.ERROR_PESERTA);
            errTextPeserta.setTextColor(Color.RED);
            result = false;
        }
        else{
            result = true;
        }
        return result;
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
        ArrayList<String> sendData = new ArrayList<>();

        String idKelas = spIdKelas;
        String idPeserta = spIdPeserta;

        sendData.add(idKelas);
        sendData.add(idPeserta);

        String showData = utility.showDataPrompt(sendData);

        CustomDialog customDialog = new CustomDialog(AddSubDetailKelasActivity.this, ContentMenu.PROMPT_CHECK_TITLE, ContentMenu.PROMPT_CHECK_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.ADD_DETAIL_KELAS);

        if(btnSaveData.equals(buttons)){
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
}