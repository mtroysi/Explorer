package mtroysi.m2sili.explorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by android on 11/25/16.
 */

public class MyFragment extends ListFragment implements BackPressListener {

    private FileAdapter adapter;
    private TextView textView;
    private String path;
    private Explorer mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (Explorer) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.liste_details, container, false);
        if(savedInstanceState != null) {
            path = savedInstanceState.getString("path");
        }
        else {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        textView = (TextView) rootView.findViewById(R.id.path);

        setHasOptionsMenu(true);

        adapter = new FileAdapter(inflater.getContext());
        updatePath();
        setListAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    public void updatePath() {
        File current = new File(path);
        if (current.exists() && current.canRead()) {
            if (current.isDirectory()) {
                textView.setText(path);
                adapter.clear();
                adapter.addAll(Arrays.asList(current.listFiles()));
            } else {
//                path = current.getParent();

                DisplayFragment displayFragment=(DisplayFragment)getFragmentManager().findFragmentById(R.id.display);
                if(getResources().getBoolean(R.bool.land_value) && displayFragment != null){
//                    DisplayFragment displayFragment=(DisplayFragment)getFragmentManager().findFragmentById(R.id.display);
                    displayFragment.addFile(path);
                }
                else {
                    Intent intent = new Intent(mainActivity, Display.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                }
            }
        } else {
            if (current.getParentFile().exists()
                    && current.getParentFile().canRead()) {
                path = current.getParent();
            } else {
                path = Environment.getExternalStorageDirectory().getPath();
                updatePath();
            }
        }
    }

    @Override
    public void onListItemClick(ListView x, View v, int pos, long id) {
        File current = new File(path);
        if(!current.isFile())
        {
            path += "/" + adapter.getItem(pos).getName();
        }
        else {
            path = current.getParent() + "/" + adapter.getItem(pos).getName();
        }
        updatePath();
    }

    @Override
    public void onBackPressed() {
        File current = new File(path);
        if (!path.equals("/")) {
            path = current.getParent();
            updatePath();
        } else {
//            super.onBackPressed();
        }
    }

    public boolean isRoot() {
        return path.equals("/");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", path);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean test = askName(id);
        adapter.notifyDataSetChanged();
        return test;
//         return super.onOptionsItemSelected(item);
    }

        public boolean askName(final int id) {
        boolean reussi = true;
        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        final View alertDialogView = inflater.inflate(R.layout.alertdialog,
                null);
        AlertDialog.Builder adb = new AlertDialog.Builder(mainActivity);
        adb.setView(alertDialogView);

        TextView txtAlert = (TextView) alertDialogView
                .findViewById(R.id.viewAlert);

        switch (id) {
            case R.id.create_rep: {
                txtAlert.setText("Entrez le nom du dossier");
                adb.setTitle("Creation du dossier");
            }
            break;
            case R.id.create_file: {
                txtAlert.setText("Entrez le nom du fichier");
                adb.setTitle("Creation du fichier");
            }
            break;
        }

        // comment vérifier que le fichier a bien été crée pour renvoyer un
        // booléen
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText nom = (EditText) alertDialogView
                        .findViewById(R.id.champSaisie);
                File file = new File(path, nom.getText().toString());
                if (file.getParentFile().canWrite()) {
                    try {
                        switch (id) {
                            case R.id.create_rep: {
                                file.mkdirs();
                                updatePath();
                            }
                            break;
                            case R.id.create_file: {
                                try {
                                    file.createNewFile();
                                    updatePath();
                                } catch (IOException e) {
                                    throw e;
                                }
                            }
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updatePath();
                }
            }
        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
        return reussi;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        mainActivity.getMenuInflater().inflate(R.menu.context_menu, menu);
    }

        public boolean supprFile(File toDelete) {
        if (toDelete.canWrite()) {
            toDelete.delete();
            return true;
        } else {
            Toast t = Toast.makeText(mainActivity,
                    "Impossible de supprimer", Toast.LENGTH_LONG);
            t.show();
            return false;
        }
    }

    public boolean supprAll(File file) {
        if (file.isDirectory()) {
            final File contenu[] = file.listFiles();
            if (file.canWrite()) {
                boolean reussi = true;
                for (int i = 0; i < contenu.length || !reussi; ++i) {
                    reussi = supprAll(contenu[i]);
                }
                if (reussi)
                    reussi = file.delete();
                return reussi;
            } else {
                Toast t = Toast.makeText(mainActivity,
                        "Impossible de supprimer", Toast.LENGTH_LONG);
                t.show();
                return false;
            }
        } else {
            return 	supprFile(file);
        }
    }

    public void supprimeDossier(final File dir) {
        final File contenu[] = dir.listFiles();
        if (contenu.length > 0) {
            new AlertDialog.Builder(mainActivity)
                    .setTitle("Attention, dossier non vide !")
                    .setMessage("Voulez-vous supprimer les sous fichiers ?")
                    .setCancelable(false)
                    .setPositiveButton("Oui",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    supprAll(dir);
                                    updatePath();
                                }
                            })
                    .setNegativeButton("Non",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            }).show();
        }
        else {
            supprFile(dir);
            updatePath();
            adapter.notifyDataSetChanged();;
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.supprimer: {
                AdapterView.AdapterContextMenuInfo it = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                File toDelete = adapter.getItem(it.position);
                if (toDelete.isDirectory()) {
                    supprimeDossier(toDelete);
                    updatePath();
                } else {
                    supprFile(toDelete);
                    updatePath();
                }
                adapter.notifyDataSetChanged();
                // return true;
            }
            default: {
                // return false;
            }
        }

        super.onContextItemSelected(item);
        return true;
    }
}
