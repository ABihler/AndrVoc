package de.robertmathes.android.orangeiron;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import de.robertmathes.android.orangeiron.adapter.ServerListViewAdapter;
import de.robertmathes.android.orangeiron.db.DataSource;
import de.robertmathes.android.orangeiron.model.Lesson;
import de.robertmathes.android.orangeiron.model.Server;
import de.robertmathes.android.orangeiron.model.VocabularyServer;

public class VocabularyServerConfigActivity extends Activity {

    private DataSource db;

    private List<Server> servers;
    private ListView listView;
    private ServerListViewAdapter serverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_server_config);

        this.listView = (ListView) findViewById(R.id.serverList);
        // this.listView.setOnItemClickListener(this);

        // set the view when the list is empty
        TextView emtpyView = (TextView) findViewById(R.id.empty_server_list);
        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Light.ttf");
        emtpyView.setTypeface(roboto_light);
        this.listView.setEmptyView(emtpyView);

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
    protected void onResume() {
        super.onResume();

        // get servers, if any present
        servers = db.getAllServers();

        // set the adapter to the list view
        this.serverAdapter = new ServerListViewAdapter(getApplicationContext(), servers);
        this.listView.setAdapter(serverAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vocabulary_server_config, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Close the db connection
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addNewServer:
                Intent intent = new Intent(this, AddVocabularyServerActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_checkForUpdates:
                checkForUpdates();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkForUpdates() {
        for (Server server : servers) {
            checkServer(server);
        }
    }

    private void checkServer(final Server server) {
        new AsyncTask<Server, Void, Server>() {

            @Override
            protected Server doInBackground(Server... servers) {
                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet getRequest = new HttpGet(servers[0].getUrl());

                try {

                    HttpResponse getResponse = client.execute(getRequest);
                    final int statusCode = getResponse.getStatusLine().getStatusCode();

                    if (statusCode != HttpStatus.SC_OK) {
                        Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + servers[0].getUrl());
                        return null;
                    }

                    HttpEntity getResponseEntity = getResponse.getEntity();
                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(getResponseEntity.getContent());
                    VocabularyServer vocServer = gson.fromJson(reader, VocabularyServer.class);
                    vocServer.setServerUrl(servers[0].getUrl());
                    Server server = new Server(vocServer);
                    return server;
                } catch (IOException e) {
                    getRequest.abort();
                    Log.w(getClass().getSimpleName(), "Error for URL " + servers[0].getUrl(), e);
                }

                return null;

            }

            @Override
            protected void onPostExecute(Server remoteServer) {
                if (server != null) {
                    // is the server really the one we know?
                    if (remoteServer.getUuid().equals(server.getUuid())) {
                        if (remoteServer.getDataVersion() > server.getDataVersion()) {
                            // TODO: Replace toast with code and some other kind of hint
                            Toast.makeText(getApplicationContext(),
                                    "Update gefunden! Alte Version: " + server.getDataVersion() + ", neue Version: " + remoteServer.getDataVersion(),
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Create a hashmap from local lessons for better access
                            List<Lesson> localLessons = db.getLessonsByServerId(server.getId());
                            Map<String, Lesson> localLessonsMap = new HashMap<String, Lesson>();
                            for (Lesson lesson : localLessons) {
                                localLessonsMap.put(lesson.getUuid(), lesson);
                            }

                            // Iterate through remote lessons and check if they are new or updated
                            for (Lesson remoteLesson : remoteServer.getLessons()) {
                                Lesson localLesson = localLessonsMap.get(remoteLesson.getUuid());
                                if (localLesson != null) {
                                    // existing lesson -> check if remote version is higher
                                    if (remoteLesson.getVersion() > localLesson.getVersion()) {
                                        // update lesson locally
                                        db.updateLesson(remoteLesson);
                                    }
                                } else {
                                    // new lesson -> add locally
                                    db.saveLesson(remoteLesson, server.getId());
                                }
                            }
                        } else {
                            // TODO: Replace toast with code and some other kind of hint
                            Toast.makeText(getApplicationContext(),
                                    "Kein Update gefunden", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        // wrong or no server
                        // TODO: Replace toast with code and some other kind of hint
                        // TODO: Remove server from database?
                        Toast.makeText(getApplicationContext(), "Unbekannter Server!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_valid_server, Toast.LENGTH_LONG).show();

                }
            }
        }.execute(server);
    }
}
