package mtroysi.m2sili.explorer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by android on 11/22/16.
 */

public class FileAdapter extends ArrayAdapter<File> {

    private final Context context;

    public FileAdapter(Context context) {
        super(context, R.layout.ligne);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        File item = getItem(position);
        String nom = item.getName();
        AssetManager asset = context.getAssets();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.ligne, parent, false);

        TextView text = (TextView)v.findViewById(R.id.label);
        text.setText(nom);

        ImageView imageView = (ImageView)v.findViewById(R.id.icon);
        String imgPath;
        if (item.isDirectory()){
            imgPath = "directory_icon.png";
        }
        else{
            imgPath="blank_file_icon.png";
        }
        try {
            InputStream ims = asset.open(imgPath);
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return v;
    }
}
