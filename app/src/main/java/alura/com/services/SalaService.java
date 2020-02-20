package alura.com.services;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SalaService extends AsyncTask<String, Void, String>
{

    @Override
    protected String doInBackground(String... strings)
    {

        String urlWS = "http://172.30.248.32:8080/ReservaDeSala/rest/sala/salas";

        StringBuilder result = new StringBuilder();
        try
        {
            URL url = new URL(urlWS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", "secret");
            conn.setRequestProperty("id_organizacao", strings[0]);
            conn.setConnectTimeout(3000);

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)
            {
                result.append(line);
            }
            rd.close();
            return result.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }
}
