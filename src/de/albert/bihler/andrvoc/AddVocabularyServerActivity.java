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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import de.albert.bihler.andrvoc.db.DataSource;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.model.Server;
import de.albert.bihler.andrvoc.model.VocabularyServer;
import de.albert.bihler.andrvoc.orangeiron.R;

public class AddVocabularyServerActivity extends Activity {

    private DataSource db;

    private VocabularyServer vocServer;

    EditText serverUrl;

    LinearLayout serverDetails;
    TextView serverName;
    TextView serverDescription;
    TextView serverLessonCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocabulary_server);

        serverUrl = (EditText) findViewById(R.id.server_url);
        serverDetails = (LinearLayout) findViewById(R.id.server_details);
        serverName = (TextView) findViewById(R.id.server_name);
        serverDescription = (TextView) findViewById(R.id.server_description);
        serverLessonCount = (TextView) findViewById(R.id.server_lessonCount);

        // hide the server details
        serverDetails.setVisibility(View.INVISIBLE);

        // Demo-URL vorbelegen
        serverUrl = (EditText) findViewById(R.id.server_url);
        // serverUrl.setText("https://googledrive.com/host/0B5pL2OLIkCeiN00xdnVyRGszTmM/albert.json");
        serverUrl.setText("https://dl.dropboxusercontent.com/u/64100103/AndrVocJSON/albert.json");
        serverUrl.selectAll();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Open database
        if (db == null) {
            db = new DataSource(getApplicationContext());
        }
        db.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_vocabulary_server, menu);
        return true;
    }

    public void onLoad(View view) {
        Button button = (Button) findViewById(R.id.button_server_load);
        button.setEnabled(false);
        checkServer(serverUrl.getText().toString());
        // TODO: show spinner while loading data
    }

    public void onSave(View view) {
        Button button = (Button) findViewById(R.id.button_server_save);
        button.setEnabled(false);
        long serverId = db.saveServer(new Server(vocServer));
        for (Lesson lesson : vocServer.getLessons()) {
            db.saveLesson(lesson, serverId);
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Close the db connection
        db.close();
    }

    private void checkServer(final String url) {
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
                    server.setServerUrl(url);
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
                    serverDetails.setVisibility(View.VISIBLE);
                    serverName.setText(server.getServerName());
                    serverDescription.setText(server.getServerDescription());
                    serverLessonCount.setText(server.getLessons().size() + "");

                    vocServer = server;

                    AppPreferences appPrefs = new AppPreferences(getApplicationContext());
                    appPrefs.saveVocabularyServer(serverUrl.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_valid_server, Toast.LENGTH_LONG).show();

                }
                Button button = (Button) findViewById(R.id.button_server_load);
                button.setEnabled(true);
            }
        }.execute(url);

    }
}
