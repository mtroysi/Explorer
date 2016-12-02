package mtroysi.m2sili.explorer;

import android.app.Activity;
import android.os.Bundle;

public class Explorer extends Activity {

    private MyFragment mainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
        mainFragment=(MyFragment)getFragmentManager().findFragmentById(R.id.fragment);
    }


    @Override
    public void onBackPressed() {
        if(mainFragment.isRoot()){
            System.exit(0);
        }
        else {
            mainFragment.onBackPressed();
        }
    }

}
