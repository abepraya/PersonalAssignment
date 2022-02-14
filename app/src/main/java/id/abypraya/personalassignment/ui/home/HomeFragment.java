package id.abypraya.personalassignment.ui.home;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.databinding.FragmentHomeBinding;
import id.abypraya.personalassignment.utility.ContentMenu;

public class HomeFragment extends Fragment implements View.OnClickListener {

    String JSON_STRING;
    private FragmentHomeBinding binding;
//    FloatingActionButton btnCheck;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSearch.setOnClickListener(this);

//        btnCheck = binding.btnCreateHome;
//        btnCheck.setOnClickListener(this);
//
//        binding.btnCreateHome.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View buttons) {
        if(binding.btnSearch.equals(buttons)){
            String spCategory = binding.spinnerSearch.getSelectedItem().toString();

            char valSearch = spCategory.charAt(0);
            String spValue = Character.toString(valSearch);
            String getValue = binding.editSearch.getText().toString().trim();

            switch (spValue){
                case "1":
                    getDataPeserta(getValue);
                    break;
                case "2":
                    getDataInstruktur(getValue);
                    break;
                case "3":
                    getDataKelas(getValue);
                    break;
                default:
                    Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
            }

        }
//        if(btnCheck.equals(buttons)){
//            RequestMethod requestMethod = new RequestMethod();
//            String completeUrl = requestMethod.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.POST);
//            Toast.makeText(getActivity(), "Test: " + completeUrl, Toast.LENGTH_SHORT).show();
//
//        }else{
//            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//        }
    }

    private void getDataKelas(String getValue) {
    }

    private void getDataInstruktur(String getValue) {
    }

    private void getDataPeserta(String val) {
        class GetJsonData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestMethod handler = new RequestMethod();
                String result = handler.sendGetDetailResponse(Config.URL_SEARCH_PST,val);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);

                loading.dismiss();
                JSON_STRING = message;
                displaySearchPeserta(JSON_STRING);
//                displaySearchResult();
            }
        }
        GetJsonData getJsonData = new GetJsonData();
        getJsonData.execute();
    }

    private void displaySearchPeserta(String json_string) {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String id_pst = object.getString("p.id_pst");
                String nama_pst = object.getString("p.nama_pst");
                String id_detail_kls = object.getString("dk.id_detail_kls");
                String id_kls = object.getString("dk.id_kls");
                String nama_mat = object.getString("m.nama_mat");

                HashMap<String, String> res = new HashMap<>();
                res.put("id_pst", id_pst);
                res.put("nama_pst", nama_pst);
                res.put("id_detail_kls", id_detail_kls);
                res.put("id_kls", id_kls);
                res.put("nama_mat", nama_mat);


                list.add(res);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // adapter untuk meletakkan array list kedalam list view
        ListAdapter adapter = new SimpleAdapter(
                getContext(), list, R.layout.list_view_items_data,
                new String[]{
                        "nama_pst",
                        "nama_mat"},
                new int[]{R.id.txt_name,
                        R.id.txt_sub_info,
}

        );
        binding.listView.setAdapter(adapter);
    }
}