package mtroysi.m2sili.explorer;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by android on 11/26/16.
 */

public class DisplayFragment extends Fragment {
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.display, container, false);
        textView = (TextView) rootView.findViewById(R.id.content);
        textView.setMovementMethod(new ScrollingMovementMethod());
        return rootView;
    }

    public void addFile(String path){
        File file=new File(path);
        String content="";
        BufferedReader br = null;
        try {

            StringBuffer output = new StringBuffer();
            String fpath = file.getAbsolutePath();

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"n");
            }
            content = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.setText(content);
    }
}
