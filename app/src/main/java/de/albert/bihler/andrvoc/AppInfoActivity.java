package de.albert.bihler.andrvoc;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import de.albert.bihler.andrvoc.db.TrainingLogDataSource;
import de.albert.bihler.andrvoc.model.Vokabel;
import de.albert.bihler.andrvoc.orangeiron.R;

public class AppInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /** Called when the user clicks the OK button */
    public void buttonOK(View view) {
        this.finish();
    }

}
