package id.abypraya.personalassignment.ui.kelas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.databinding.FragmentKelasBinding;
import id.abypraya.personalassignment.ui.instruktur.integration.DetailInstrukturActivity;
import id.abypraya.personalassignment.ui.kelas.integration.AddKelasActivity;
import id.abypraya.personalassignment.ui.kelas.integration.DetailKelasActivity;
import id.abypraya.personalassignment.ui.kelas.integration.SubDetailKelas.SubDetailKelasActivity;
import id.abypraya.personalassignment.utility.ContentMenu;

public class KelasFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FragmentKelasBinding binding;

    private ListView listViewKelas;
    private FloatingActionButton btnCreateKelas, btnSubDetailKelas;
    private TextView txtFrontScreen;
    private ImageView ivFrontScreen;

    private String JSON_STRING;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        KelasViewModel kelasViewModel =
                new ViewModelProvider(this).get(KelasViewModel.class);

        binding = FragmentKelasBinding.inflate(inflater, container, false);

        listViewKelas = binding.listViewData.listViewData;
        listViewKelas.setOnItemClickListener(this);

        btnCreateKelas = binding.btnCreateKelas;
        btnCreateKelas.setOnClickListener(this);

        btnSubDetailKelas = binding.btnSubDetailKelas;
        btnSubDetailKelas.setOnClickListener(this);

        txtFrontScreen = binding.listViewData.txtFrontScreen;
        txtFrontScreen.setText(ContentMenu.TITLE_LIST_KELAS);

        ivFrontScreen = binding.listViewData.ivFrontScreen;
        ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_class_large));

        View root = binding.getRoot();

        getJSON();

        return root;
    }

    private void getJSON() {
        //bantuan dari class AsyncTask
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING,false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String getApiKelas = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.GET);
                String result = handler.sendGetResponse(getApiKelas);
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
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
//            Log.d("DATA_JSON: ", JSON_STRING);

            for(int i = 0; i < result.length();i++){
                JSONObject object = result.getJSONObject(i);
                String id = object.getString(Config.TAG_JSON_KLS_ID);
                String nameIns = object.getString(Config.TAG_JSON_KLS_NAME_INSTRUKTUR);
                String nameMat = object.getString(Config.TAG_JSON_KLS_NAME_MATERI);
//                String totalParticipant = object.getString(Config.TAG_JSON_KLS_TOTAL_PARTICIPANT);
                String entryDate = object.getString(Config.TAG_JSON_KLS_ENTRY_DATE);
                String endDate = object.getString(Config.TAG_JSON_KLS_END_DATE);

                HashMap<String, String> kelas = new HashMap<>();
                kelas.put(Config.TAG_JSON_KLS_ID, id);
                kelas.put(Config.TAG_JSON_KLS_ENTRY_DATE, entryDate);
                kelas.put(Config.TAG_JSON_KLS_END_DATE, endDate);
                kelas.put(Config.TAG_JSON_KLS_NAME_INSTRUKTUR, nameIns);
                kelas.put(Config.TAG_JSON_KLS_NAME_MATERI, nameMat);
//                kelas.put(Config.TAG_JSON_KLS_TOTAL_PARTICIPANT, totalParticipant);

                //ubah format JSON menjadi ArrayList
                list.add(kelas);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //adapter untuk meletakan array list ke dalam list view
        ListAdapter adapter = new SimpleAdapter(
                getActivity(),
                list,
                R.layout.list_view_items_group,
                new String[]{Config.TAG_JSON_KLS_NAME_MATERI, Config.TAG_JSON_KLS_NAME_INSTRUKTUR, Config.TAG_JSON_KLS_ENTRY_DATE, Config.TAG_JSON_KLS_END_DATE},
                new int[]{R.id.txt_mat, R.id.txt_name, R.id.txt_start, R.id.txt_end}
        );
        listViewKelas.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View buttons) {
        if(btnCreateKelas.equals(buttons)){
            Intent moveAddKelas = new Intent(getActivity(), AddKelasActivity.class);
            startActivity(moveAddKelas);
        }
        else if(btnSubDetailKelas.equals(buttons)){
            Intent moveSubDetailKelas = new Intent(getActivity(), SubDetailKelasActivity.class);
            startActivity(moveSubDetailKelas);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveDetailKelas = new Intent(getActivity(), DetailKelasActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_KLS_ID).toString();
        moveDetailKelas.putExtra(Config.KLS_ID, id);
        startActivity(moveDetailKelas);
    }
}