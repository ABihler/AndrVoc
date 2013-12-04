package de.robertmathes.android.orangeiron;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import de.robertmathes.android.orangeiron.adapter.ServerListViewAdapter;
import de.robertmathes.android.orangeiron.db.DataSource;
import de.robertmathes.android.orangeiron.model.Server;

public class VocabularyServerConfigActivity extends Activity {

    private DataSource db;

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
        List<Server> servers = db.getAllServers();

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
