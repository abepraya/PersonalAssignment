package id.abypraya.personalassignment.ui.materi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.R;
import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;
import id.abypraya.personalassignment.databinding.FragmentMateriBinding;
import id.abypraya.personalassignment.ui.materi.integration.AddMateriActivity;
import id.abypraya.personalassignment.ui.materi.integration.DetailMateriActivity;
import id.abypraya.personalassignment.utility.ContentMenu;

public class MateriFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FragmentMateriBinding binding;
    private String JSON_STRING;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MateriViewModel materiViewModel = new ViewModelProvider(this).get(MateriViewModel.class);

        binding = FragmentMateriBinding.inflate(inflater, container, false);

        binding.listViewData.ivFrontScreen.setImageDrawable(getResources().getDrawable(R.drawable.img_subject_large));

        binding.listViewData.txtFrontScreen.setText(ContentMenu.TITLE_LIST_MATERI);

        binding.btnCreateMateri.setOnClickListener(this);
        binding.listViewData.listViewData.setOnItemClickListener(this);


        View root = binding.getRoot();

        getJSON();

//        final TextView textView = binding.textSlideshow;
//        materiModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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
                String getApiMateri = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.GET);
                String result = handler.sendGetResponse(getApiMateri);
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
                String id = object.getString(Config.TAG_JSON_MAT_ID);
                String name = object.getString(Config.TAG_JSON_MAT_NAMA);

                HashMap<String, String> materi = new HashMap<>();
                materi.put(Config.TAG_JSON_MAT_ID, id);
                materi.put(Config.TAG_JSON_MAT_NAMA, name);

                //ubah format JSON menjadi ArrayList
                list.add(materi);
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
                new String[]{Config.TAG_JSON_MAT_NAMA},
                new int[]{R.id.txt_name}
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
        if(binding.btnCreateMateri.equals(buttons)){
            Intent moveAddMateri = new Intent(getActivity(), AddMateriActivity.class);
            startActivity(moveAddMateri);
        }else{
            Toast.makeText(getActivity(), "NOT GOOD", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Intent moveDetailMateri = new Intent(getActivity(), DetailMateriActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String id = map.get(Config.TAG_JSON_MAT_ID).toString();
        moveDetailMateri.putExtra(Config.MAT_ID, id);
        startActivity(moveDetailMateri);
    }
}