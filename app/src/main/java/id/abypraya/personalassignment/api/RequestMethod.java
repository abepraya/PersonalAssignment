package id.abypraya.personalassignment.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import id.abypraya.personalassignment.utility.Utility;

public class RequestMethod {

    //method for define url by request method
    public String setUrlApi(String menu, String subMenu, String httpRequestMethod){
        String completeUrl = null,
                baseUrl = Config.IP_USER,
                endPoint,
                idType = null;
        Utility utility = new Utility();

        switch (httpRequestMethod){
            case Config.POST:
//                baseUrl = Config.IP_USER;
                endPoint = menu + "/" + subMenu + "/tr_add_" + subMenu + ".php";
                completeUrl = baseUrl + endPoint;
                break;
            case Config.GET:
//                baseUrl = Config.IP_USER;
                endPoint = menu + "/" + subMenu + "/tr_datas_" + subMenu + ".php";
                completeUrl = baseUrl + endPoint;
                break;
            case Config.SPECIAL_GET:
//                baseUrl = Config.IP_USER;
                idType = utility.idType(subMenu);
                endPoint = menu + "/" + subMenu + "/tr_detail_detail_" + subMenu +".php" + "?" + idType + "=";
                completeUrl = baseUrl + endPoint;
                break;
            case Config.GET_DETAIL:
//                baseUrl = Config.IP_USER;
                idType = utility.idType(subMenu);
                endPoint = menu + "/" + subMenu + "/tr_detail_" + subMenu +".php" + "?" + idType + "=";
                completeUrl = baseUrl + endPoint;
                break;
            case Config.SPECIAL_GET_DETAIL:
                idType = utility.idType(subMenu);
                endPoint = menu + "/" + subMenu + "/tr_detail_detail_" + subMenu +".php" + "?" + idType + "=";
                completeUrl = baseUrl + endPoint;
                break;
            case Config.DELETE:
//                baseUrl = Config.IP_USER;
                idType = utility.idType(subMenu);
                endPoint = menu + "/" + subMenu + "/tr_delete_" + subMenu +".php" + "?" + idType + "=";
                completeUrl = baseUrl + endPoint;
                break;
            case Config.UPDATE:
//                baseUrl = Config.IP_USER;
                idType = utility.idType(subMenu);
                endPoint = menu + "/" + subMenu + "/tr_update_" + subMenu +".php" + "?" + idType + "=";
                completeUrl = baseUrl + endPoint;
                break;
            default:
                System.out.println("Can't find the API");
        }

        return completeUrl;
    }

    //constructor 1: send POST request
    public String sendPostRequest(String requestUrl, HashMap<String, String> postDataParams){
        //Membuat URL
        URL url;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //mengirim request dari client ke server
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            //check http status code untuk memastikan request diterima oleh server
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //kirim response dari server ke client
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String response;

                while((response = reader.readLine()) != null){
                    sb.append(response);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace(); //error message
        }

        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first){
                first = false;
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }

        return result.toString();
    }

    //Contructor 2: send GET response -> GET_ALL & GET_DETAIL

    //GET_ALL
    public String sendGetResponse(String responseUrl){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(responseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;

            while ((response = reader.readLine()) != null){
                sb.append(response + "\n");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return sb.toString();
    }

    //GET_DETAIL
    public String sendGetDetailResponse(String responseUrl, String id){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(responseUrl + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;

            while ((response = reader.readLine()) != null){
                sb.append(response + "\n");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return sb.toString();
    }
}
