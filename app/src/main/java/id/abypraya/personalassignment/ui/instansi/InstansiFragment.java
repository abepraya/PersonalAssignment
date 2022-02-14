package id.abypraya.personalassignment.ui.instansi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.databinding.FragmentInstansiBinding;
import id.abypraya.personalassignment.ui.instansi.integration.AddInstansiActivity;
import id.abypraya.personalassignment.ui.instansi.integration.DetailInstansiActivity;
import id.abypraya.personalassignment.utility.ContentMenu;

public class InstansiFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private InstansiViewModel mViewModel;
    private FragmentInstansiBinding binding;
    private String JSON_STRING;

    public static InstansiFragment newInstance() {
        return new InstansiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InstansiViewModel.class);
        binding = FragmentInstansiBinding.inflate(inflater, container, false);

        binding.listViewData.ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_instansi_large));

        binding.listViewData.txtFrontScreen.setText(ContentMenu.TITLE_LIST_INSTANSI);
        binding.btnCreateInstansi.setOnClickListener(this);
        binding.listViewData.listViewData.setOnItemClickListener(this);
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
                String getApiInstansi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.GET);
                String result = handler.sendGetResponse(getApiInstansi);
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
                String id = object.getString(Config.TAG_JSON_INSTANSI_ID);
                String name = object.getString(Config.TAG_JSON_INSTANSI_NAMA);
                String address = object.getString(Config.TAG_JSON_INSTANSI_ALAMAT);

                HashMap<String, String> instansi = new HashMap<>();
                instansi.put(Config.TAG_JSON_INSTANSI_ID, id);
                instansi.put(Config.TAG_JSON_INSTANSI_NAMA, name);
                instansi.put(Config.TAG_JSON_INSTANSI_ALAMAT, address);

                //ubah format JSON menjadi ArrayList
                list.add(instansi);
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
                new String[]{Config.TAG_JSON_INSTANSI_NAMA, Config.TAG_JSON_INSTANSI_ALAMAT},
                new int[]{R.id.txt_name, R.id.txt_sub_info}
        );
        binding.listViewData.listViewData.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View buttons) {
        if(binding.btnCreateInstansi.equals(buttons)){
            Intent moveAddInstansi = new Intent(getActivity(), AddInstansiActivity.class);
            startActivity(moveAddInstansi);
        }else{
            Toast.makeText(getActivity(), "Can't Find Page", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveDetailInstansi = new Intent(getActivity(), DetailInstansiActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_INSTANSI_ID).toString();
        moveDetailInstansi.putExtra(Config.INSTANSI_ID, id);
        startActivity(moveDetailInstansi);
    }
}