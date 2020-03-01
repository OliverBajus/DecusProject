package com.spse.decusproject.cosmetic_database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CosmeticDatabase {

    private String name;
    private String function;
    private JSONArray fields;

    public String getName() {
        return name;
    }

    public String getFunction() {
        return function;
    }

    public CosmeticDatabase(String name){
        this.name = name;

        this.name.replaceAll(" ","+");

    }

    public String getDataFromDatabase() throws IOException, JSONException {

        String url = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=cosmetic-ingredient-database-ingredients-and-fragrance-inventory&q="+name+"&rows=1&facet=update_date&facet=restriction&facet=function";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject (response.toString());

        JSONArray jsonRecords = json.getJSONArray("records");
        int length = jsonRecords.length();
        for(int i=0; i<length; i++)
        {
            JSONObject jObj = jsonRecords.getJSONObject(i);
            function = jObj.optString("fields");
        }



        System.out.println("funkcia" + function);

        return function;
    }



}
