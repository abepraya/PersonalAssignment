package id.abypraya.personalassignment.ui.instruktur;

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
import id.abypraya.personalassignment.databinding.FragmentInstrukturBinding;
import id.abypraya.personalassignment.ui.instruktur.integration.AddInstrukturActivity;
import id.abypraya.personalassignment.ui.instruktur.integration.DetailInstrukturActivity;
import id.abypraya.personalassignment.ui.peserta.InstrukturModel;
import id.abypraya.personalassignment.utility.ContentMenu;

public class InstrukturFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FragmentInstrukturBinding binding;

    private FloatingActionButton btnCreateInstruktur;
    private TextView txtFrontScreen;
    private ImageView ivFrontScreen;
    private ListView listViewInstruktur;

    private String JSON_STRING;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InstrukturModel instrukturModel =
                new ViewModelProvider(this).get(InstrukturModel.class);

        binding = FragmentInstrukturBinding.inflate(inflater, container, false);

        listViewInstruktur = binding.listViewData.listViewData;
        listViewInstruktur.setOnItemClickListener(this);

        btnCreateInstruktur = binding.btnCreateInstruktur;
        btnCreateInstruktur.setOnClickListener(this);

        txtFrontScreen = binding.listViewData.txtFrontScreen;
        ivFrontScreen = binding.listViewData.ivFrontScreen;

        txtFrontScreen.setText(ContentMenu.TITLE_LIST_INSTRUKTUR);
        ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_instructor_large));

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
                String getApiInstruktur = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTRUKTUR, Config.GET);
                String result = handler.sendGetResponse(getApiInstruktur);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;
                //Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
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
//            Log.d("DATA_JSON: ", JSON_STRING);

            for(int i = 0; i < result.length();i++){
                JSONObject object = result.getJSONObject(i);
                String id = object.getString(Config.TAG_JSON_INS_ID);
                String name = object.getString(Config.TAG_JSON_INS_NAMA);
                String email = object.getString(Config.TAG_JSON_INS_EMAIL);
                String hp = object.getString(Config.TAG_JSON_INS_HP);

                HashMap<String, String> instruktur = new HashMap<>();
                instruktur.put(Config.TAG_JSON_INS_ID, id);
                instruktur.put(Config.TAG_JSON_INS_NAMA, name);
                instruktur.put(Config.TAG_JSON_INS_EMAIL, email);
                instruktur.put(Config.TAG_JSON_INS_HP, hp);

                //ubah format JSON menjadi ArrayList
                list.add(instruktur);
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
                new String[]{Config.TAG_JSON_INS_NAMA, Config.TAG_JSON_INS_EMAIL},
                new int[]{R.id.txt_name, R.id.txt_sub_info}
        );
        listViewInstruktur.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View buttons) {
        if(btnCreateInstruktur.equals(buttons)){
            Intent moveAddInstruktur = new Intent(getActivity(), AddInstrukturActivity.class);
            startActivity(moveAddInstruktur);
        }else{
            Toast.makeText(getActivity(), "CAN'T FIND", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveDetailInstruktur = new Intent(getActivity(), DetailInstrukturActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_INS_ID).toString();
        moveDetailInstruktur.putExtra(Config.INS_ID, id);
        startActivity(moveDetailInstruktur);
    }
}