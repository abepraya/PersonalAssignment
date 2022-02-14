package id.abypraya.personalassignment.ui.kelas.integration.SubDetailKelas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.utility.ContentMenu;

public class SubSubDetailKelasActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listViewSpecificDetailKelas;
    private MaterialButton btnCreateSpecificDetailKelas;
    private ImageView ivFrontScreen;
    private TextView txtFrontScreen;

    private String getId, getIdKelas, JSON_STRING;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sub_detail_kelas);

        Intent receiveIntentParams = getIntent();
        getId = receiveIntentParams.getStringExtra(Config.DETAIL_KLS_ID);
        getIdKelas = receiveIntentParams.getStringExtra(Config.KLS_ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.TITLE_SUB_DETAIL_KELAS + " " + getIdKelas);

        listViewSpecificDetailKelas = findViewById(R.id.listViewData);

        ivFrontScreen = findViewById(R.id.iv_frontScreen);
        txtFrontScreen = findViewById(R.id.txt_frontScreen);

        txtFrontScreen.setText(ContentMenu.TITLE_LIST_PESERTA + " " + ContentMenu.TITLE_SUB_DETAIL_KELAS);
        ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_personal_large));

        listViewSpecificDetailKelas.setOnItemClickListener(this);

        showData();
    }

    private void showData() {
        class ShowData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SubSubDetailKelasActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiSubKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_DETAIL_KELAS, Config.SPECIAL_GET_DETAIL);
                String result = handler.sendGetDetailResponse(getApiSubKelas, getId);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;

                //menampilkan data dalam bentuk list view
                displayAllData();
            }
        }
        ShowData showData = new ShowData();
        showData.execute();
    }

    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i < result.length();i++){
                JSONObject object = result.getJSONObject(i);
                String idKls = object.getString(Config.TAG_JSON_KLS_ID);
                String idPst = object.getString(Config.TAG_JSON_DETAIL_KELAS_ID);
                String id = object.getString(Config.TAG_JSON_DETAIL_KELAS_ID);

                HashMap<String, String> subKelas = new HashMap<>();

                subKelas.put(Config.TAG_JSON_DETAIL_KELAS_ID, id);
                subKelas.put(Config.TAG_JSON_KLS_ID, idKls);
                subKelas.put(Config.TAG_JSON_PST_ID, idPst);

                //ubah format JSON menjadi ArrayList
                list.add(subKelas);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                SubSubDetailKelasActivity.this,
                list,
                R.layout.list_view_items_data,
                new String[]{Config.TAG_JSON_KLS_ID, Config.TAG_JSON_PST_ID},
                new int[]{R.id.txt_name, R.id.txt_sub_info}
        );
        listViewSpecificDetailKelas.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveInfoSubDetailKelas = new Intent(SubSubDetailKelasActivity.this, InfoSubDetailKelasActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_DETAIL_KELAS_ID).toString();
        moveInfoSubDetailKelas.putExtra(Config.DETAIL_KLS_ID, id);
        startActivity(moveInfoSubDetailKelas);
    }
}