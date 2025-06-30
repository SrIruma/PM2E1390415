package com.example.pm2e1390415;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2e1390415.Common.ContactoAdapter;
import com.example.pm2e1390415.Database.DatabaseHelper;
import com.example.pm2e1390415.Modelos.Contacto;

import java.util.Collections;
import java.util.List;

public class ActivityContactos extends AppCompatActivity {

    ImageButton back, favs;
    EditText busqueda;
    RecyclerView contactos;
    ContactoAdapter ca;
    DatabaseHelper database;
    List<Contacto> listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_contacto);

        back = (ImageButton) findViewById(R.id.btn_back);
        favs = (ImageButton) findViewById(R.id.btn_favs);
        contactos = (RecyclerView) findViewById(R.id.rvContactos);
        busqueda = (EditText) findViewById(R.id.busqueda);

        back.setOnClickListener(v -> finish());
        favs.setOnClickListener(v -> Toast.makeText(this, "No hay contactos agregados a favoritos!", Toast.LENGTH_SHORT).show());
        contactos.setLayoutManager(new LinearLayoutManager(this));

        database = new DatabaseHelper(this);
        listaContactos = database.getContactos();

        Collections.sort(listaContactos, (c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        ca = new ContactoAdapter(this, listaContactos, new ContactoAdapter.OnItemClickListener() {
            @Override
            public void onCompartirClick(Contacto contacto) {
                new AlertDialog.Builder(ActivityContactos.this)
                        .setTitle("Compartir Contacto")
                        .setMessage("¿Deseas compartir este contacto?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, "Contacto: " + contacto.getNombre() + "\nTeléfono: " + contacto.getTelefono());
                            startActivity(Intent.createChooser(intent, "Compartir contacto"));
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override
            public void onBorrarClick(Contacto contacto, int posicion) {
                new AlertDialog.Builder(ActivityContactos.this)
                        .setTitle("Eliminar Contacto")
                        .setMessage("¿Estás seguro de que deseas eliminar a " + contacto.getNombre() + " de tus contactos?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            ca.eliminarContacto(posicion);
                            database.deleteContacto(contacto.getId());
                            Toast.makeText(ActivityContactos.this, "Contacto eliminado: " + contacto.getNombre(), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override
            public void onVerImagenClick(Contacto contacto) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContactos.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_image, null);
                ImageView imageView = view.findViewById(R.id.imageViewDialog);

                try {
                    Uri uri = Uri.parse(contacto.getImagen());
                    imageView.setImageURI(uri);
                } catch (Exception e) {
                    imageView.setImageResource(R.drawable.pf);
                }

                builder.setView(view)
                        .setTitle("Imagen de " + contacto.getNombre())
                        .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }

            @Override
            public void onVerNotaClick(Contacto contacto) {
                new AlertDialog.Builder(ActivityContactos.this)
                        .setTitle("Nota de " + contacto.getNombre())
                        .setMessage(contacto.getNota())
                        .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                        .show();
            }

            @Override
            public void onLlamarClick(Contacto contacto) {
                new AlertDialog.Builder(ActivityContactos.this)
                        .setTitle("Llamar a " + contacto.getNombre())
                        .setMessage("¿Deseas llamar al número: " + contacto.getTelefono() + "?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            if (ContextCompat.checkSelfPermission(ActivityContactos.this,
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(ActivityContactos.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        1);

                            } else {
                                realizarLlamada(contacto.getTelefono());
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ca.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        contactos.setAdapter(ca);
    }

    private void realizarLlamada(String telefono) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + telefono));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido. Intenta llamar de nuevo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede realizar la llamada.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
