package mtroysi.m2sili.explorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by android on 11/26/16.
 */

public class Display extends Activity {
    private DisplayFragment displayFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_display);
        displayFragment=(DisplayFragment)getFragmentManager().findFragmentById(R.id.display);

        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            if(path != null){
                displayFragment.addFile(path);
            }
        }

    }
}
