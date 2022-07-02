package com.example.trabalho3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;
    private FirebaseAuth mAuth;
    TextView textEntrar;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textEntrar = (TextView) findViewById(R.id.btnEntrar);
        email = (EditText) findViewById(R.id.edtEmail);
        password = (EditText) findViewById(R.id.edtSenha);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getUserFirebase();
        }



        textEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView = findViewById(R.id.listViewMusica);
                runtimePermission();
            }
        });
    }

    public void getUserFirebase(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mostrarMusicas();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> acharMusica (File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(acharMusica(singleFile.getAbsoluteFile()));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        arrayList.add(singleFile);
                    }
                }
            }

        } return arrayList;
    }

    void mostrarMusicas() {
        final ArrayList<File> minhasMusicas = acharMusica(Environment.getExternalStorageDirectory());

        items = new String[minhasMusicas.size()];
        for (int i = 0; i < minhasMusicas.size(); i++) {
            items[i] = minhasMusicas.get(i).getName().replace(".mp3","").replace(".wav", "");
        }
        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String nomeMusica = (String) listView.getItemAtPosition(i);
            startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                    .putExtra("musicas", minhasMusicas)
                    .putExtra("nomedamusica", nomeMusica)
                    .putExtra("pos", i));
        });
    }

    class customAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View minhaView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textMusica = minhaView.findViewById(R.id.txtNomeMusica);
            textMusica.setSelected(true);
            textMusica.setText(items[i]);

            return minhaView;
        }
    }
}