package de.albert.bihler.andrvoc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import de.albert.bihler.andrvoc.model.VocabularyServer;

public class VocabularyServerConfig extends Activity {

    EditText serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_server_config);

        // Demo-URL vorbelegen
        serverUrl = (EditText) findViewById(R.id.server_popup_url);
        serverUrl.setText("https://googledrive.com/host/0B5pL2OLIkCeiN00xdnVyRGszTmM/index.json");
        serverUrl.selectAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vocabulary_server_config, menu);
        return true;
    }

    public void onSave(View view) {
        checkServer(serverUrl.getText().toString());
    }

    private void checkServer(String url) {
        new AsyncTask<String, Void, VocabularyServer>() {

            @Override
            protected VocabularyServer doInBackground(String... params) {
                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet getRequest = new HttpGet(params[0]);

                try {

                    HttpResponse getResponse = client.execute(getRequest);
                    final int statusCode = getResponse.getStatusLine().getStatusCode();

                    if (statusCode != HttpStatus.SC_OK) {
                        Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + params[0]);
                        return null;
                    }

                    HttpEntity getResponseEntity = getResponse.getEntity();
                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(getResponseEntity.getContent());
                    VocabularyServer server = gson.fromJson(reader, VocabularyServer.class);
                    return server;
                } catch (IOException e) {
                    getRequest.abort();
                    Log.w(getClass().getSimpleName(), "Error for URL " + params[0], e);
                }

                return null;

            }

            @Override
            protected void onPostExecute(VocabularyServer server) {
                if (server != null) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.found_valid_server, server.getServerName(), server.getLessons().size()), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_valid_server, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(url);

    }
}
