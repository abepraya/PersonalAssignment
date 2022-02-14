package id.abypraya.personalassignment.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import id.abypraya.personalassignment.api.Config;
import id.abypraya.personalassignment.api.RequestMethod;

public class Utility {
    public int selectSpinnerValue(Spinner spinner, String value)
    {
        int index = 0;
        int result = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(value)){
//                spinner.setSelection(i);
                result = i;
                break;
            }
        }
        return  result;
    }

    public String idType(String type){
        String result = null;
        switch (type){
            case Config.SUBDIR_PESERTA:
                result = Config.KEY_PST_ID;
                break;
            case Config.SUBDIR_DETAIL_KELAS:
                result = Config.KEY_DETAIL_KLS_ID;
                break;
            case Config.SUBDIR_KELAS:
                result = Config.KEY_KLS_ID;
                break;
            case Config.SUBDIR_INSTRUKTUR:
                result = Config.KEY_INS_ID;
                break;
            case Config.SUBDIR_MATERI:
                result = Config.KEY_MAT_ID;
                break;
            case Config.SUBDIR_INSTANSI:
                result = Config.KEY_INSTANSI_ID;
                break;
            default:
                System.out.println("Can't find the id type");
        }

        return result;
    }

    public String showDataPrompt(ArrayList<String> listData){
        String result = null;
        if(listData.size() == 4){
            result = listData.get(0) + ", " + listData.get(1) + ", " + listData.get(2) + ", " + listData.get(3);
        }else if(listData.size() == 3){
            result = listData.get(0) + ", " + listData.get(1) + ", " + listData.get(2);
        }else if(listData.size() == 2){
            result = listData.get(0) + ", " + listData.get(1);
        }else if(listData.size() == 5){
            result = listData.get(0) + ", " + listData.get(1) + ", " + listData.get(2) + ", " + listData.get(3) + ", " + listData.get(4);
        }
        else{
            result = listData.get(0);
        }

        return result;
    }


    public void saveDataPeserta(Activity activity, ArrayList<String> data){
        String name = data.get(0);
        String email = data.get(1);
        String phone = data.get(2);
        String idIns = data.get(3);

        class SaveDataPeserta extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_PST_EMAIL, email);
                params.put(Config.KEY_PST_NAMA, name);
                params.put(Config.KEY_PST_HP, phone);
                params.put(Config.KEY_PST_ID_INSTANSI, idIns);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.POST);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        SaveDataPeserta saveDataPeserta = new SaveDataPeserta();
        saveDataPeserta.execute();
    }

    public void saveDataMateri(Activity activity, ArrayList<String> data){
        String materiName = data.get(0);

        class SaveDataMateri extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_MAT_NAMA, materiName);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.POST);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        SaveDataMateri saveDataMateri = new SaveDataMateri();
        saveDataMateri.execute();
    }

    public void saveDataInstansi(Activity activity, ArrayList<String> data){
        String instansiName = data.get(0);
        String instansiAddress = data.get(1);

        class SaveDataInstansi extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_INSTANSI_NAMA, instansiName);
                params.put(Config.KEY_INSTANSI_ALAMAT, instansiAddress);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.POST);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        SaveDataInstansi saveDataInstansi = new SaveDataInstansi();
        saveDataInstansi.execute();
    }

    public void saveDataInstruktur(Activity activity, ArrayList<String> data){
        String name = data.get(0);
        String email = data.get(1);
        String phone = data.get(2);

        class SaveDataInstruktur extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_INS_EMAIL, email);
                params.put(Config.KEY_INS_NAMA, name);
                params.put(Config.KEY_INS_PHONE, phone);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTRUKTUR, Config.POST);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        SaveDataInstruktur saveDataInstruktur = new SaveDataInstruktur();
        saveDataInstruktur.execute();
    }

    public void saveDataKelas(Activity activity, ArrayList<String> data) {
        String entryDate = data.get(0);
        String endDate = data.get(1);
        String materi = data.get(2);
        String instruktur = data.get(3);

        class SaveDataKelas extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_KLS_ENTRY_DATE, entryDate);
                params.put(Config.KEY_KLS_END_DATE, endDate);
                params.put(Config.KEY_MAT_ID, materi);
                params.put(Config.KEY_INS_ID, instruktur);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.POST);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        SaveDataKelas saveDataKelas = new SaveDataKelas();
        saveDataKelas.execute();
    }

    public void updateDataInstansi(Activity activity, ArrayList<String> data) {
        String id = data.get(0);
        String name = data.get(1);
        String address = data.get(2);

        class UpdateDataInstansi extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_INSTANSI_NAMA, name);
                params.put(Config.KEY_INSTANSI_ALAMAT, address);
                params.put(Config.KEY_INSTANSI_ID, id);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.UPDATE);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        UpdateDataInstansi updateDataInstansi = new UpdateDataInstansi();
        updateDataInstansi.execute();
    }

    public void deleteDataInstansi(Activity activity, ArrayList<String> data) {
        String id = data.get(0);

        class DeleteDataInstansi extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTANSI, Config.DELETE);
                    result = handler.sendGetDetailResponse(getApi, id);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        DeleteDataInstansi deleteDataInstansi = new DeleteDataInstansi();
        deleteDataInstansi.execute();
    }

    public void updateDataMateri(Activity activity, ArrayList<String> data) {
        String id = data.get(0);
        String name = data.get(1);

        class UpdateDataMateri extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_MAT_NAMA, name);
                params.put(Config.KEY_MAT_ID, id);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.UPDATE);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        UpdateDataMateri updateDataMateri = new UpdateDataMateri();
        updateDataMateri.execute();
    }

    public void deleteDataMateri(Activity activity, ArrayList<String> data) {
        String id = data.get(0);

        class DeleteDataMateri extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_MATERI, Config.DELETE);
                    result = handler.sendGetDetailResponse(getApi, id);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        DeleteDataMateri deleteDataMateri = new DeleteDataMateri();
        deleteDataMateri.execute();
    }

    public void updateDataInstruktur(Activity activity, ArrayList<String> data) {
        String id = data.get(0);
        String name = data.get(1);
        String email = data.get(2);
        String phone = data.get(3);

        class UpdateDataInstruktur extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_INS_NAMA, name);
                params.put(Config.KEY_INS_ID, id);
                params.put(Config.KEY_INS_EMAIL, email);
                params.put(Config.KEY_INS_PHONE, phone);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTRUKTUR, Config.UPDATE);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        UpdateDataInstruktur updateDataInstruktur = new UpdateDataInstruktur();
        updateDataInstruktur.execute();

    }

    public void deleteDataInstruktur(Activity activity, ArrayList<String> data) {
        String id = data.get(0);

        class DeleteDataInstruktur extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_INSTRUKTUR, Config.DELETE);
                    result = handler.sendGetDetailResponse(getApi, id);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        DeleteDataInstruktur deleteDataInstruktur = new DeleteDataInstruktur();
        deleteDataInstruktur.execute();
    }

    public void updateDataPeserta(Activity activity, ArrayList<String> data) {
        String id = data.get(0);
        String name = data.get(1);
        String email = data.get(2);
        String phone = data.get(3);
        String idInstansi = data.get(4);

        class UpdateDataPeserta extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_PST_NAMA, name);
                params.put(Config.KEY_PST_ID, id);
                params.put(Config.KEY_PST_EMAIL, email);
                params.put(Config.KEY_PST_HP, phone);
                params.put(Config.KEY_PST_ID_INSTANSI, idInstansi);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.UPDATE);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        UpdateDataPeserta updateDataPeserta = new UpdateDataPeserta();
        updateDataPeserta.execute();
    }

    public void deleteDataPeserta(Activity activity, ArrayList<String> data) {
        String id = data.get(0);

        class DeleteDataPeserta extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_PESERTA, Config.DELETE);
                    result = handler.sendGetDetailResponse(getApi, id);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        DeleteDataPeserta deleteDataPeserta = new DeleteDataPeserta();
        deleteDataPeserta.execute();
    }

    public void updateDataKelas(Activity activity, ArrayList<String> data) {
        String id = data.get(0);
        String entryDate = data.get(1);
        String endDate = data.get(2);
        String materi = data.get(3);
        String instruktur = data.get(4);

        class UpdateDataKelas extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_MAT_ID, materi);
                params.put(Config.KEY_KLS_ID, id);
                params.put(Config.KEY_KLS_ENTRY_DATE, entryDate);
                params.put(Config.KEY_KLS_END_DATE, endDate);
                params.put(Config.KEY_INS_ID, instruktur);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.UPDATE);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        UpdateDataKelas updateDataKelas = new UpdateDataKelas();
        updateDataKelas.execute();
    }

    public void deleteDataKelas(Activity activity, ArrayList<String> data) {
        String id = data.get(0);

        class DeleteDataKelas extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_KELAS, Config.DELETE);
                    result = handler.sendGetDetailResponse(getApi, id);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        DeleteDataKelas deleteDataKelas = new DeleteDataKelas();
        deleteDataKelas.execute();
    }

    public void saveDataDetailKelas(Activity activity, ArrayList<String> data) {
        String idKelas = data.get(0);
        String idPeserta = data.get(1);

        class SaveDataDetailKelas extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();
                params.put(Config.KEY_KLS_ID, idKelas);
                params.put(Config.KEY_PST_ID, idPeserta);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_DETAIL_KELAS, Config.POST);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        SaveDataDetailKelas saveDataDetailKelas = new SaveDataDetailKelas();
        saveDataDetailKelas.execute();
    }

    public void deleteDetailKelas(Activity activity, ArrayList<String> data) {
        String id = data.get(0);

        class DeleteDetailKelas extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_DETAIL_KELAS, Config.DELETE);
                    result = handler.sendGetDetailResponse(getApi, id);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        DeleteDetailKelas deleteDetailKelas = new DeleteDetailKelas();
        deleteDetailKelas.execute();
    }

    public void updateDetailKelas(Activity activity, ArrayList<String> data) {
        String id = data.get(0);
        String idKls = data.get(1);
        String idPst = data.get(2);

        class UpdateDetailKelas extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, ContentMenu.TITLE_LOADING, ContentMenu.MESSAGE_LOADING, false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                String result = null;
                String getApi = null;
                RequestMethod handler = new RequestMethod();

                params.put(Config.KEY_DETAIL_KLS_ID, id);
                params.put(Config.KEY_KLS_ID, idKls);
                params.put(Config.KEY_PST_ID, idPst);

                try {
                    getApi = handler.setUrlApi(Config.DIR_INIXTRAINING, Config.SUBDIR_DETAIL_KELAS, Config.UPDATE);
                    result = handler.sendPostRequest(getApi, params);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                loading.dismiss();
                System.exit(1); //go back to previous screen
            }
        }
        UpdateDetailKelas updateDetailKelas = new UpdateDetailKelas();
        updateDetailKelas.execute();
    }
}
