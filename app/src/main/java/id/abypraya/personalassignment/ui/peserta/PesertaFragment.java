package id.abypraya.personalassignment.ui.peserta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import id.abypraya.personalassignment.databinding.FragmentPesertaBinding;
import id.abypraya.personalassignment.ui.instruktur.integration.DetailInstrukturActivity;
import id.abypraya.personalassignment.ui.peserta.integration.AddPesertaActivity;
import id.abypraya.personalassignment.ui.peserta.integration.DetailPesertaActivity;
import id.abypraya.personalassignment.utility.ContentMenu;

public class PesertaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FragmentPesertaBinding binding;
    private FloatingActionButton btnAddPeserta;
    private ImageView ivFrontScreen;
    private TextView txtFrontScreen;
    private ListView listViewPeserta;
    
    private String JSON_STRING;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PesertaModel pesertaModel =
                new ViewModelProvider(this).get(PesertaModel.class);

        binding = FragmentPesertaBinding.inflate(inflater, container, false);
        btnAddPeserta = binding.btnCreatePeserta;

        txtFrontScreen = binding.listViewData.txtFrontScreen;
        listViewPeserta = binding.listViewData.listViewData;

        ivFrontScreen = binding.listViewData.ivFrontScreen;
        ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_participant_large));

        txtFrontScreen.setText(ContentMenu.TITLE_LIST_PESERTA);

        listViewPeserta.setOnItemClickListener(this);

        btnAddPeserta.setOnClickListener(this);
        getJSON();

        View root = binding.getRoot();

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
                String getApiPeserta = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.GET);
                String result = handler.sendGetResponse(getApiPeserta);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;
//                Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
//                Log.d("DATA_JSON: ", JSON_STRING);

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
            Log.d("DATA_JSON: ", JSON_STRING);

            for(int i = 0; i < result.length();i++){
                JSONObject object = result.getJSONObject(i);
                String id = object.getString(Config.TAG_JSON_PST_ID);
                String name = object.getString(Config.TAG_JSON_PST_NAMA);
                String phoneNumber = object.getString(Config.TAG_JSON_PST_HP);
                String email = object.getString(Config.TAG_JSON_PST_EMAIL);
                HashMap<String, String> peserta = new HashMap<>();
                peserta.put(Config.TAG_JSON_PST_ID, id);
                peserta.put(Config.TAG_JSON_PST_NAMA, name);
                peserta.put(Config.TAG_JSON_PST_HP, phoneNumber);
                peserta.put(Config.TAG_JSON_PST_EMAIL, email);

                //ubah format JSON menjadi ArrayList
                list.add(peserta);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //adapter untuk meletakan array list ke dalam list view
        ListAdapter adapter = new SimpleAdapter(
                getActivity(),
                list,
                R.layout.list_view_items_data,
                new String[]{Config.TAG_JSON_PST_NAMA, Config.TAG_JSON_PST_EMAIL},
                new int[]{R.id.txt_name, R.id.txt_sub_info}
        );
        listViewPeserta.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View buttons) {
        if(btnAddPeserta.equals(buttons)){
            Intent moveAddPeserta = new Intent(getActivity(), AddPesertaActivity.class);
            startActivity(moveAddPeserta);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveDetailPeserta = new Intent(getActivity(), DetailPesertaActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_PST_ID).toString();
        moveDetailPeserta.putExtra(Config.PST_ID, id);
        startActivity(moveDetailPeserta);
    }
}