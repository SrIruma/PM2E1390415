package com.example.pm2e1390415;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pm2e1390415.Database.DatabaseHelper;
import com.example.pm2e1390415.Modelos.Contacto;
import com.example.pm2e1390415.Modelos.Pais;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PICK_IMAGE = 101;
    private static final int REQUEST_PICK_CONTACT = 102;
    private static final int REQUEST_CONTACT_PERMISSION = 103;

    private DatabaseHelper database;
    private Button btnGuardar, btnVer;
    private EditText etNombres, etTelefono, etNota;
    private Spinner spPais;
    private ImageView ivPerfil, ivPickFoto, ivImportar;
    private TextView region;
    private ArrayList<Pais> paises;
    private Uri imagenSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        btnGuardar  = findViewById(R.id.btnGuardar);
        btnVer      = findViewById(R.id.btnVer);
        etNombres   = findViewById(R.id.etNombres);
        etTelefono  = findViewById(R.id.etTelefono);
        etNota      = findViewById(R.id.etNota);
        spPais      = findViewById(R.id.spPais);
        ivPerfil    = findViewById(R.id.ivPerfil);
        ivPickFoto  = findViewById(R.id.ivpf);
        ivImportar  = findViewById(R.id.ivic);
        region      = findViewById(R.id.tvreg);
        database = new DatabaseHelper(this);

        paises = database.getPaises();
        Collections.sort(paises, (c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        ArrayAdapter<Pais> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapter);
        spPais.setSelection(76);

        ivPickFoto.setOnClickListener(v -> seleccionarFotoPerfil());
        ivImportar.setOnClickListener(v -> confirmarImportacion());
        btnGuardar.setOnClickListener(v -> guardarContacto());
        btnVer.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ActivityContactos.class)));

        spPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Pais seleccionado = (Pais) parent.getItemAtPosition(position);
                region.setText(seleccionado.getCodigo());
                validarTelefono();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etTelefono.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEditing) return;
                String textoOriginal = s.toString();
                String textoSinGuiones = textoOriginal.replace("-", "");
                if (!textoOriginal.equals(textoSinGuiones)) {
                    isEditing = true;
                    etTelefono.setText(textoSinGuiones);
                    etTelefono.setSelection(textoSinGuiones.length());
                    isEditing = false;
                }
                validarTelefono();
            }
        });
    }

    private void validarTelefono() {
        Pais p = (Pais) spPais.getSelectedItem();
        if (p == null) return;
        String numero = etTelefono.getText().toString().trim();
        etTelefono.setError(null);
        if (!numero.isEmpty() && !numero.matches(p.getRegex())) {
            etTelefono.setError("Número inválido para " + p.getNombre());
        }
    }

    private void seleccionarFotoPerfil() {
        String permiso = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED) {
            abrirGaleria();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permiso}, 1001);
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private Uri copiarImagenEnAlmacenamientoInterno(Uri uriOriginal) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream input = resolver.openInputStream(uriOriginal);

            String nombreArchivo = "img_" + System.currentTimeMillis() + ".jpg";
            File directorio = new File(getFilesDir(), "imagenes");
            if (!directorio.exists()) directorio.mkdirs();

            File archivoDestino = new File(directorio, nombreArchivo);
            OutputStream output = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            input.close();
            output.close();

            return Uri.fromFile(archivoDestino);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void confirmarImportacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mostrarDialogoImportacion();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CONTACT_PERMISSION);
        }
    }

    private void mostrarDialogoImportacion() {
        new AlertDialog.Builder(this)
                .setTitle("Importar contacto")
                .setMessage("¿Deseas importar un contacto del dispositivo?")
                .setPositiveButton("Sí", (dialog, which) -> abrirSelectorDeContactos())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void abrirSelectorDeContactos() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mostrarDialogoImportacion();
            } else {
                Toast.makeText(this, "Permiso denegado para leer contactos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_PICK_IMAGE) {
                Uri uri = data.getData();
                Uri copiaUri = copiarImagenEnAlmacenamientoInterno(uri);
                if (copiaUri != null) {
                    imagenSeleccionada = copiaUri;
                    ivPerfil.setImageURI(copiaUri);
                } else {
                    Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_PICK_CONTACT) {
                Uri contactoUri = data.getData();
                if (contactoUri != null) {
                    importarDesdeUri(contactoUri);
                }
            }
        }
    }

    private void importarDesdeUri(Uri contactoUri) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(contactoUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            String telefono = "";
            if (cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                Cursor telefonos = resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );
                if (telefonos != null && telefonos.moveToFirst()) {
                    telefono = telefonos.getString(telefonos.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    telefonos.close();
                }
            }

            telefono = telefono.replaceAll("[^0-9+]+", "");

            String telefonoSinCodigo = telefono;
            boolean codigoDetectado = false;
            if (telefono.startsWith("+")) {
                for (int i = 0; i < paises.size(); i++) {
                    Pais pais = paises.get(i);
                    String codigo = pais.getCodigo().replace("+", "");
                    if (telefono.startsWith("+" + codigo)) {
                        spPais.setSelection(i);
                        telefonoSinCodigo = telefono.substring(codigo.length() + 1); // +1 por el '+'
                        codigoDetectado = true;
                        break;
                    }
                }
            }

            etNombres.setText(nombre);
            etTelefono.setText(codigoDetectado ? telefonoSinCodigo : telefono);
            cursor.close();
        }
    }

    private void guardarContacto() {
        String nombre = etNombres.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String nota = etNota.getText().toString().trim();
        Pais paisSeleccionado = (Pais) spPais.getSelectedItem();

        if (nombre.trim().isEmpty()) {
            mostrarAlerta("Por favor, ingresa un nombre para el contacto.");
            return;
        }

        if (telefono.trim().isEmpty()) {
            mostrarAlerta("El campo de teléfono no puede estar vacío. Ingresa un número válido.");
            return;
        }

        if (nota.trim().isEmpty()) {
            mostrarAlerta("Agrega una nota para este contacto. Puedes escribir algo importante o personal.");
            return;
        }

        if (paisSeleccionado == null || !telefono.matches(paisSeleccionado.getRegex())) {
            mostrarAlerta("Número inválido para " + (paisSeleccionado != null ? paisSeleccionado.getNombre() : "el país seleccionado"));
            return;
        }

        String telcom = paisSeleccionado.getCodigo() + telefono;
        String uriString = imagenSeleccionada != null ? imagenSeleccionada.toString() : "";

        Contacto contacto = new Contacto(paisSeleccionado.toString(), nombre, telcom, nota, uriString);
        database.insertContacto(contacto);

        Toast.makeText(this, "Contacto guardado exitosamente", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        ivPerfil.setImageResource(R.drawable.pf);
    }

    private void mostrarAlerta(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage(mensaje)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void limpiarCampos() {
        etNombres.setText("");
        etTelefono.setText("");
        etNota.setText("");
        imagenSeleccionada = null;
        etNombres.requestFocus();
    }
}
