package com.spychcio.awesome;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<String, String, String>{
public AsyncResponse delegate;

public RequestTask(AsyncResponse delegate){
    this.delegate=delegate;
}
@Override
protected String doInBackground(String... url) {
    // constants
    int timeoutSocket = 5000;
    int timeoutConnection = 5000;

    HttpParams httpParameters = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    HttpClient client = new DefaultHttpClient(httpParameters);

    HttpGet httpget = new HttpGet(url[0]);

    try {
        HttpResponse getResponse = client.execute(httpget);
        final int statusCode = getResponse.getStatusLine().getStatusCode();

        if(statusCode != HttpStatus.SC_OK) {
            Log.i("MyApp", "Download Error: " + statusCode + "| for URL: " + url);
            return null;
        }

        String line = "";
        StringBuilder total = new StringBuilder();

        HttpEntity getResponseEntity = getResponse.getEntity();

        BufferedReader reader = new BufferedReader(new InputStreamReader(getResponseEntity.getContent()));  

        while((line = reader.readLine()) != null) {
            total.append(line);
        }

        line = total.toString();
        return line;
    } catch (Exception e) {
        Log.i("MyApp", "Download Exception : " + e.toString());
    }
    return null;
}

@Override
protected void onPostExecute(String result) {
	
	delegate.processFinish(result);
	Log.i("xddd", result);
	

}
}