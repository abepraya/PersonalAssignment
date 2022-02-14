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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.utility.ContentMenu;

public class SubDetailKelasActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listViewSubDetailKelas;
    private FloatingActionButton btnCreateSubDetailKelas;
    private String JSON_STRING;

    private ImageView ivFrontScreen;
    private TextView txtFrontScreen;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_detail_kelas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ContentMenu.TITLE_SUB_DETAIL_KELAS);

        listViewSubDetailKelas = findViewById(R.id.listViewData);
        btnCreateSubDetailKelas = findViewById(R.id.btnCreateSubKelas);

        txtFrontScreen = findViewById(R.id.txt_frontScreen);
        txtFrontScreen.setText(ContentMenu.TITLE_LIST_SUB_DETAIL_KELAS);

        ivFrontScreen = findViewById(R.id.iv_frontScreen);
        ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_detail_kelas_large));

        btnCreateSubDetailKelas.setOnClickListener(this);

        listViewSubDetailKelas.setOnItemClickListener(this);

        showData();
    }

    private void showData() {
        class ShowData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SubDetailKelasActivity.this, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiSubKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_DETAIL_KELAS, Config.GET);
                String result = handler.sendGetResponse(getApiSubKelas);
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
                String id = object.getString(Config.TAG_JSON_DETAIL_KELAS_ID);
                String totalParticipant = object.getString(Config.TAG_JSON_KLS_TOTAL_PARTICIPANT);

                HashMap<String, String> subKelas = new HashMap<>();
                subKelas.put(Config.TAG_JSON_KLS_ID, idKls);
                subKelas.put(Config.TAG_JSON_DETAIL_KELAS_ID, id);
                subKelas.put(Config.TAG_JSON_KLS_TOTAL_PARTICIPANT, totalParticipant);

                //ubah format JSON menjadi ArrayList
                list.add(subKelas);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                SubDetailKelasActivity.this,
                list,
                R.layout.list_view_items_data_alternative,
                new String[]{Config.TAG_JSON_KLS_ID, Config.TAG_JSON_KLS_TOTAL_PARTICIPANT},
                new int[]{R.id.txt_id_class, R.id.txt_participant}
        );
        listViewSubDetailKelas.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveSubDetailKelas = new Intent(SubDetailKelasActivity.this, SubSubDetailKelasActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_DETAIL_KELAS_ID).toString();
        String idKls = map.get(Config.TAG_JSON_KLS_ID).toString();
        moveSubDetailKelas.putExtra(Config.DETAIL_KLS_ID, id);
        moveSubDetailKelas.putExtra(Config.KLS_ID, idKls);
        startActivity(moveSubDetailKelas);
    }

    @Override
    public void onClick(View buttons) {
        if(btnCreateSubDetailKelas.equals(buttons)){
            Intent moveAddDetailKelas = new Intent(SubDetailKelasActivity.this, AddSubDetailKelasActivity.class);
            startActivity(moveAddDetailKelas);
        }
    }
}