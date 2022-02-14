package id.abypraya.personalassignment.ui.kelas.integration.SubDetailKelas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import id.abypraya.personalassignment.ui.peserta.integration.DetailPesertaActivity;
import id.abypraya.personalassignment.utility.ContentMenu;
import id.abypraya.personalassignment.utility.Utility;

public class InfoSubDetailKelasActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner editSpSubPeserta, editSpSubKelas;
    private MaterialButton btnUpdateDataSubKelas, btnDeleteDataSubKelas;

    private String getId, spListPeserta, spListKelas, valIdPeserta, valIdKelas, tempIdKelas, tempIdPeserta;
    ArrayList<String> arrayListIdKelas = new ArrayList<>();
    ArrayList<String> arrayListKelas = new ArrayList<>();
    ArrayList<String> arrayListIdPeserta = new ArrayList<>();
    ArrayList<String> arrayListPeserta = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_sub_detail_kelas);

        Intent receiveIntentParams = getIntent();
        getId = receiveIntentParams.getStringExtra(Config.DETAIL_KLS_ID);

        getSupportActionBar().setTitle(ContentMenu.ADD_DETAIL_KELAS + " " + getId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editSpSubPeserta = findViewById(R.id.editSpSubPeserta);
        editSpSubKelas = findViewById(R.id.editSpSubKelas);

        btnUpdateDataSubKelas = findViewById(R.id.btnUpdateDataSubKelas);
        btnDeleteDataSubKelas = findViewById(R.id.btnDeleteDataSubKelas);

        btnUpdateDataSubKelas.setOnClickListener(this);
        btnDeleteDataSubKelas.setOnClickListener(this);

        displayData();
    }

    private void displayData() {
        class DisplayData extends AsyncTask<Void, Void, String> implements AdapterView.OnItemSelectedListener {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InfoSubDetailKelasActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();

                String getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_DETAIL_KELAS, Config.SPECIAL_GET_DETAIL);

//                String getApiSubDetailKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.GET);

                String getApiKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.GET);
                String getApiPeserta = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.GET);

                String getListPeserta = handler.sendGetResponse(getApiPeserta);
                String getListKelas = handler.sendGetResponse(getApiKelas);

                spListKelas = getListKelas;
                spListPeserta = getListPeserta;

                String result = handler.sendGetDetailResponse(getApi, getId);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();

                JSONObject jsonObjectKelas = null;
                JSONObject jsonObjectPeserta = null;

                JSONArray jsonArrKelas = null;
                JSONArray jsonArrPeserta = null;

                try {
                    jsonObjectKelas = new JSONObject(spListKelas);
                    jsonObjectPeserta = new JSONObject(spListPeserta);

                    jsonArrKelas = jsonObjectKelas.getJSONArray(Config.TAG_JSON_ARRAY);
                    jsonArrPeserta = jsonObjectPeserta.getJSONArray(Config.TAG_JSON_ARRAY);

                    for (int i = 0; i < jsonArrKelas.length(); ++i){
                        JSONObject object = jsonArrKelas.getJSONObject(i);
                        String kelasName = object.getString(Config.TAG_JSON_KLS_NAME_MATERI);
                        String IdKelas = object.getString(Config.TAG_JSON_KLS_ID);

                        arrayListKelas.add(kelasName);
                        arrayListIdKelas.add(IdKelas);
                    }

                    for(int i = 0; i < jsonArrPeserta.length(); ++i){
                        JSONObject object = jsonArrPeserta.getJSONObject(i);
                        String pesertaName = object.getString(Config.TAG_JSON_PST_NAMA);
                        String idPeserta = object.getString(Config.TAG_JSON_PST_ID);

                        arrayListPeserta.add(pesertaName);
                        arrayListIdPeserta.add(idPeserta);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                ArrayAdapter<String> adapterKelas = new ArrayAdapter<String>(InfoSubDetailKelasActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListKelas);
                ArrayAdapter<String> adapterPeserta = new ArrayAdapter<String>(InfoSubDetailKelasActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListPeserta);

                adapterKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterPeserta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                editSpSubKelas.setAdapter(adapterKelas);
                editSpSubPeserta.setAdapter(adapterPeserta);

                try {
                    JSONObject jsonObject = new JSONObject(message);
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONObject object = result.getJSONObject(0);
                    int idxKelas = Integer.parseInt(object.getString(Config.TAG_JSON_KLS_ID));
                    int idxPeserta = Integer.parseInt(object.getString(Config.TAG_JSON_PST_ID ));
                    int positionKelas = adapterKelas.getPosition(arrayListKelas.get(idxKelas - 1));
                    int positionPeserta = adapterPeserta.getPosition(arrayListPeserta.get(idxPeserta - 1));

                    tempIdPeserta = object.getString(Config.TAG_JSON_PST_ID);
                    tempIdKelas = object.getString(Config.TAG_JSON_KLS_ID);

                    editSpSubKelas.setSelection(positionKelas);
                    editSpSubPeserta.setSelection(positionPeserta);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                editSpSubKelas.setOnItemSelectedListener(this);
                editSpSubPeserta.setOnItemSelectedListener(this);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(parent.equals(editSpSubKelas)){
                    valIdKelas = String.valueOf(arrayListIdKelas.get(position));
                }
                else if(parent.equals(editSpSubPeserta)){
                    valIdPeserta = String.valueOf(arrayListIdPeserta.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        DisplayData displayData = new DisplayData();
        displayData.execute();
    }

    @Override
    public void onClick(View buttons) {
        Utility utility = new Utility();
        Drawable drawableImgPrompt;
        ArrayList<String> sendData = new ArrayList<>();
        ArrayList<String> sendId = new ArrayList<>();

        String idKls = valIdKelas;
        String idPeserta = valIdPeserta;

        sendData.add(getId);
        sendData.add(idKls);
        sendData.add(idPeserta);

        sendId.add(getId);

        String showData = utility.showDataPrompt(sendData);

        drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);

        if(btnUpdateDataSubKelas.equals(buttons)){
            if(checkFormData()){
                CustomDialog customDialog = new CustomDialog(InfoSubDetailKelasActivity.this, ContentMenu.PROMPT_CHECK_UPDATE_TITLE, ContentMenu.PROMPT_CHECK_UPDATE_DETAIL + showData,ContentMenu.PROMPT_CHECK_CHECKBOXLIST, drawableImgPrompt, sendData, ContentMenu.UPDATE_DETAIL_KELAS);
                customDialog.show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    customDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                }
                customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 550);
                customDialog.setCancelable(false);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }
        else if(btnDeleteDataSubKelas.equals(buttons)){
            drawableImgPrompt = getResources().getDrawable(R.drawable.img_confirmation);
            CustomDialog customDialog = new CustomDialog(InfoSubDetailKelasActivity.this, ContentMenu.PROMPT_CHECK_DELETE_TITLE, ContentMenu.PROMPT_CHECK_DELETE_DETAIL,ContentMenu.PROMPT_CHECK_DELETE_CHECKBOXLIST, drawableImgPrompt, sendId, ContentMenu.DELETE_DETAIL_KELAS);
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
        TextView errTextSubKelas = (TextView) editSpSubKelas.getSelectedView();
        TextView errTextSubPeserta = (TextView) editSpSubPeserta.getSelectedView();

        String errKelas = String.valueOf(R.string.invalid_kelas);
        String errPeserta = String.valueOf(R.string.invalid_peserta);

        if(editSpSubKelas.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_KELAS_NAME)){
            errTextSubKelas.setError(errKelas);
            errTextSubKelas.setText(ContentMenu.ERROR_KELAS);
            errTextSubKelas.setTextColor(Color.RED);
            result = false;
        }
        else if(editSpSubPeserta.getSelectedItem().toString().trim().equals(ContentMenu.INITIALIZE_DROPDOWN_PESERTA_NAME)){
            errTextSubPeserta.setError(errPeserta);
            errTextSubPeserta.setText(ContentMenu.ERROR_PESERTA);
            errTextSubPeserta.setTextColor(Color.RED);
            result = false;
        }
        else{
            result = true;
        }
        return result;
    }
}