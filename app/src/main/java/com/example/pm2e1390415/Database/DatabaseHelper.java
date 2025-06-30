package com.example.pm2e1390415.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pm2e1390415.Modelos.Contacto;
import com.example.pm2e1390415.Modelos.Pais;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "examen.db";
    private static final int DB_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("BD", "onCreate ejecutado: creando tablas");

        db.execSQL("CREATE TABLE contactos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pais TEXT," +
                "nombre TEXT," +
                "telefono TEXT," +
                "nota TEXT," +
                "imagen TEXT)");

        db.execSQL("CREATE TABLE paises (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "codigo TEXT," +
                "regex TEXT)");

        configuracion(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contactos");
        db.execSQL("DROP TABLE IF EXISTS paises");
        onCreate(db);
    }

    // --- Contactos ---
    public void insertContacto(Contacto c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pais", c.getPais());
        values.put("nombre", c.getNombre());
        values.put("telefono", c.getTelefono());
        values.put("nota", c.getNota());
        values.put("imagen", c.getImagen());
        db.insert("contactos", null, values);
        db.close();
    }

    public ArrayList<Contacto> getContactos() {
        ArrayList<Contacto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Contacto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public void updateContacto(Contacto c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pais", c.getPais());
        values.put("nombre", c.getNombre());
        values.put("telefono", c.getTelefono());
        values.put("nota", c.getNota());
        values.put("imagen", c.getImagen());
        db.update("contactos", values, "id=?", new String[]{String.valueOf(c.getId())});
        db.close();
    }

    public void deleteContacto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contactos", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // --- Paises ---
    public void insertPais(Pais p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", p.getNombre());
        values.put("codigo", p.getCodigo());
        values.put("regex", p.getRegex());
        db.insert("paises", null, values);
        db.close();
    }

    public ArrayList<Pais> getPaises() {
        ArrayList<Pais> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nombre, codigo, regex FROM paises", null);
        if (c.moveToFirst()) {
            do {
                lista.add(new Pais(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2)
                ));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return lista;
    }

    public void updatePais(int id, Pais p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", p.getNombre());
        values.put("codigo", p.getCodigo());
        values.put("regex", p.getRegex());
        db.update("paises", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletePais(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("paises", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    private void ii(SQLiteDatabase db, Pais p) {
        ContentValues v = new ContentValues();
        v.put("nombre", p.getNombre());
        v.put("codigo", p.getCodigo());
        v.put("regex", p.getRegex());
        db.insert("paises", null, v);
    }

    private void configuracion(SQLiteDatabase db) {
        // América
        ii(db, new Pais("Argentina", "+54", "^\\d{7,12}$"));
        ii(db, new Pais("Bahamas", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Barbados", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Belice", "+501", "^\\d{7,12}$"));
        ii(db, new Pais("Bolivia", "+591", "^\\d{7,12}$"));
        ii(db, new Pais("Brasil", "+55", "^\\d{7,12}$"));
        ii(db, new Pais("Canadá", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Chile", "+56", "^\\d{7,12}$"));
        ii(db, new Pais("Colombia", "+57", "^\\d{7,12}$"));
        ii(db, new Pais("Costa Rica", "+506", "^\\d{7,12}$"));
        ii(db, new Pais("Cuba", "+53", "^\\d{7,12}$"));
        ii(db, new Pais("Dominica", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Ecuador", "+593", "^\\d{7,12}$"));
        ii(db, new Pais("El Salvador", "+503", "^\\d{7,12}$"));
        ii(db, new Pais("Estados Unidos", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Granada", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Guatemala", "+502", "^\\d{7,12}$"));
        ii(db, new Pais("Guyana", "+592", "^\\d{7,12}$"));
        ii(db, new Pais("Haití", "+509", "^\\d{7,12}$"));
        ii(db, new Pais("Honduras", "+504", "^[23789]\\d{7}$"));
        ii(db, new Pais("Jamaica", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("México", "+52", "^\\d{10}$"));
        ii(db, new Pais("Nicaragua", "+505", "^\\d{7,12}$"));
        ii(db, new Pais("Panamá", "+507", "^\\d{7,12}$"));
        ii(db, new Pais("Paraguay", "+595", "^\\d{7,12}$"));
        ii(db, new Pais("Perú", "+51", "^\\d{7,12}$"));
        ii(db, new Pais("República Dominicana", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("San Cristóbal y Nieves", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("San Vicente y las Granadinas", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Santa Lucía", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Surinam", "+597", "^\\d{7,12}$"));
        ii(db, new Pais("Trinidad y Tobago", "+1", "^\\d{7,12}$"));
        ii(db, new Pais("Uruguay", "+598", "^\\d{7,12}$"));
        ii(db, new Pais("Venezuela", "+58", "^\\d{7,12}$"));

        // Asia
        ii(db, new Pais("Afganistán", "+93", "^\\d{7,12}$"));
        ii(db, new Pais("Arabia Saudita", "+966", "^\\d{7,12}$"));
        ii(db, new Pais("Armenia", "+374", "^\\d{7,12}$"));
        ii(db, new Pais("Azerbaiyán", "+994", "^\\d{7,12}$"));
        ii(db, new Pais("Baréin", "+973", "^\\d{7,12}$"));
        ii(db, new Pais("Bangladés", "+880", "^\\d{7,12}$"));
        ii(db, new Pais("Bhután", "+975", "^\\d{7,12}$"));
        ii(db, new Pais("Brunéi", "+673", "^\\d{7,12}$"));
        ii(db, new Pais("Camboya", "+855", "^\\d{7,12}$"));
        ii(db, new Pais("China", "+86", "^\\d{7,12}$"));
        ii(db, new Pais("Chipre", "+357", "^\\d{7,12}$"));
        ii(db, new Pais("Corea del Norte", "+850", "^\\d{7,12}$"));
        ii(db, new Pais("Corea del Sur", "+82", "^\\d{7,12}$"));
        ii(db, new Pais("Emiratos Árabes Unidos", "+971", "^\\d{7,12}$"));
        ii(db, new Pais("Filipinas", "+63", "^\\d{7,12}$"));
        ii(db, new Pais("Georgia", "+995", "^\\d{7,12}$"));
        ii(db, new Pais("India", "+91", "^\\d{7,12}$"));
        ii(db, new Pais("Indonesia", "+62", "^\\d{7,12}$"));
        ii(db, new Pais("Irak", "+964", "^\\d{7,12}$"));
        ii(db, new Pais("Irán", "+98", "^\\d{7,12}$"));
        ii(db, new Pais("Israel", "+972", "^\\d{7,12}$"));
        ii(db, new Pais("Japón", "+81", "^\\d{7,12}$"));
        ii(db, new Pais("Jordania", "+962", "^\\d{7,12}$"));
        ii(db, new Pais("Kazajistán", "+7", "^\\d{7,12}$"));
        ii(db, new Pais("Kirguistán", "+996", "^\\d{7,12}$"));
        ii(db, new Pais("Kuwait", "+965", "^\\d{7,12}$"));
        ii(db, new Pais("Laos", "+856", "^\\d{7,12}$"));
        ii(db, new Pais("Líbano", "+961", "^\\d{7,12}$"));
        ii(db, new Pais("Malasia", "+60", "^\\d{7,12}$"));
        ii(db, new Pais("Maldivas", "+960", "^\\d{7,12}$"));
        ii(db, new Pais("Mongolia", "+976", "^\\d{7,12}$"));
        ii(db, new Pais("Nepal", "+977", "^\\d{7,12}$"));
        ii(db, new Pais("Omán", "+968", "^\\d{7,12}$"));
        ii(db, new Pais("Pakistán", "+92", "^\\d{7,12}$"));
        ii(db, new Pais("Qatar", "+974", "^\\d{7,12}$"));
        ii(db, new Pais("Rusia", "+7", "^\\d{7,12}$"));
        ii(db, new Pais("Singapur", "+65", "^\\d{7,12}$"));
        ii(db, new Pais("Sri Lanka", "+94", "^\\d{7,12}$"));
        ii(db, new Pais("Siria", "+963", "^\\d{7,12}$"));
        ii(db, new Pais("Tailandia", "+66", "^\\d{7,12}$"));
        ii(db, new Pais("Turkmenistán", "+993", "^\\d{7,12}$"));
        ii(db, new Pais("Turquía", "+90", "^\\d{7,12}$"));
        ii(db, new Pais("Uzbekistán", "+998", "^\\d{7,12}$"));
        ii(db, new Pais("Vietnam", "+84", "^\\d{7,12}$"));
        ii(db, new Pais("Yemen", "+967", "^\\d{7,12}$"));

        // Europa
        ii(db, new Pais("Albania", "+355", "^\\d{7,12}$"));
        ii(db, new Pais("Alemania", "+49", "^\\d{7,12}$"));
        ii(db, new Pais("Andorra", "+376", "^\\d{7,12}$"));
        ii(db, new Pais("Austria", "+43", "^\\d{7,12}$"));
        ii(db, new Pais("Bélgica", "+32", "^\\d{7,12}$"));
        ii(db, new Pais("Bosnia y Herzegovina", "+387", "^\\d{7,12}$"));
        ii(db, new Pais("Bulgaria", "+359", "^\\d{7,12}$"));
        ii(db, new Pais("Croacia", "+385", "^\\d{7,12}$"));
        ii(db, new Pais("Dinamarca", "+45", "^\\d{7,12}$"));
        ii(db, new Pais("Eslovenia", "+386", "^\\d{7,12}$"));
        ii(db, new Pais("España", "+34", "^\\d{7,12}$"));
        ii(db, new Pais("Estonia", "+372", "^\\d{7,12}$"));
        ii(db, new Pais("Finlandia", "+358", "^\\d{7,12}$"));
        ii(db, new Pais("Francia", "+33", "^\\d{7,12}$"));
        ii(db, new Pais("Georgia", "+995", "^\\d{7,12}$"));
        ii(db, new Pais("Grecia", "+30", "^\\d{7,12}$"));
        ii(db, new Pais("Hungría", "+36", "^\\d{7,12}$"));
        ii(db, new Pais("Islandia", "+354", "^\\d{7,12}$"));
        ii(db, new Pais("Irlanda", "+353", "^\\d{7,12}$"));
        ii(db, new Pais("Italia", "+39", "^\\d{7,12}$"));
        ii(db, new Pais("Kosovo", "+383", "^\\d{7,12}$"));
        ii(db, new Pais("Letonia", "+371", "^\\d{7,12}$"));
        ii(db, new Pais("Liechtenstein", "+423", "^\\d{7,12}$"));
        ii(db, new Pais("Lituania", "+370", "^\\d{7,12}$"));
        ii(db, new Pais("Luxemburgo", "+352", "^\\d{7,12}$"));
        ii(db, new Pais("Malta", "+356", "^\\d{7,12}$"));
        ii(db, new Pais("Moldavia", "+373", "^\\d{7,12}$"));
        ii(db, new Pais("Mónaco", "+377", "^\\d{7,12}$"));
        ii(db, new Pais("Montenegro", "+382", "^\\d{7,12}$"));
        ii(db, new Pais("Países Bajos", "+31", "^\\d{7,12}$"));
        ii(db, new Pais("Noruega", "+47", "^\\d{7,12}$"));
        ii(db, new Pais("Polonia", "+48", "^\\d{7,12}$"));
        ii(db, new Pais("Portugal", "+351", "^\\d{7,12}$"));
        ii(db, new Pais("Reino Unido", "+44", "^\\d{7,12}$"));
        ii(db, new Pais("República Checa", "+420", "^\\d{7,12}$"));
        ii(db, new Pais("Rumania", "+40", "^\\d{7,12}$"));
        ii(db, new Pais("Rusia", "+7", "^\\d{7,12}$"));
        ii(db, new Pais("Serbia", "+381", "^\\d{7,12}$"));

        // África
        ii(db, new Pais("Argelia", "+213", "^\\d{7,12}$"));
        ii(db, new Pais("Angola", "+244", "^\\d{7,12}$"));
        ii(db, new Pais("Benín", "+229", "^\\d{7,12}$"));
        ii(db, new Pais("Botsuana", "+267", "^\\d{7,12}$"));
        ii(db, new Pais("Burkina Faso", "+226", "^\\d{7,12}$"));
        ii(db, new Pais("Burundi", "+257", "^\\d{7,12}$"));
        ii(db, new Pais("Cabo Verde", "+238", "^\\d{7,12}$"));
        ii(db, new Pais("Camerún", "+237", "^\\d{7,12}$"));
        ii(db, new Pais("Chad", "+235", "^\\d{7,12}$"));
        ii(db, new Pais("Comoras", "+269", "^\\d{7,12}$"));
        ii(db, new Pais("Congo", "+242", "^\\d{7,12}$"));
        ii(db, new Pais("Congo (RDC)", "+243", "^\\d{7,12}$"));
        ii(db, new Pais("Costa de Marfil", "+225", "^\\d{7,12}$"));
        ii(db, new Pais("Egipto", "+20", "^\\d{7,12}$"));
        ii(db, new Pais("Eritrea", "+291", "^\\d{7,12}$"));
        ii(db, new Pais("Esuatini", "+268", "^\\d{7,12}$"));
        ii(db, new Pais("Etiopía", "+251", "^\\d{7,12}$"));
        ii(db, new Pais("Gabón", "+241", "^\\d{7,12}$"));
        ii(db, new Pais("Gambia", "+220", "^\\d{7,12}$"));
        ii(db, new Pais("Ghana", "+233", "^\\d{7,12}$"));
        ii(db, new Pais("Guinea", "+224", "^\\d{7,12}$"));
        ii(db, new Pais("Guinea-Bisáu", "+245", "^\\d{7,12}$"));
        ii(db, new Pais("Guinea Ecuatorial", "+240", "^\\d{7,12}$"));
        ii(db, new Pais("Kenia", "+254", "^\\d{7,12}$"));
        ii(db, new Pais("Lesoto", "+266", "^\\d{7,12}$"));
        ii(db, new Pais("Liberia", "+231", "^\\d{7,12}$"));
        ii(db, new Pais("Libia", "+218", "^\\d{7,12}$"));
        ii(db, new Pais("Madagascar", "+261", "^\\d{7,12}$"));
        ii(db, new Pais("Malawi", "+265", "^\\d{7,12}$"));
        ii(db, new Pais("Malí", "+223", "^\\d{7,12}$"));
        ii(db, new Pais("Marruecos", "+212", "^\\d{7,12}$"));
        ii(db, new Pais("Mauricio", "+230", "^\\d{7,12}$"));
        ii(db, new Pais("Mauritania", "+222", "^\\d{7,12}$"));
        ii(db, new Pais("Mozambique", "+258", "^\\d{7,12}$"));
        ii(db, new Pais("Namibia", "+264", "^\\d{7,12}$"));
        ii(db, new Pais("Níger", "+227", "^\\d{7,12}$"));
        ii(db, new Pais("Nigeria", "+234", "^\\d{7,12}$"));
        ii(db, new Pais("República Centroafricana", "+236", "^\\d{7,12}$"));
        ii(db, new Pais("Ruanda", "+250", "^\\d{7,12}$"));
        ii(db, new Pais("Santo Tomé y Príncipe", "+239", "^\\d{7,12}$"));
        ii(db, new Pais("Senegal", "+221", "^\\d{7,12}$"));
        ii(db, new Pais("Seychelles", "+248", "^\\d{7,12}$"));
        ii(db, new Pais("Sierra Leona", "+232", "^\\d{7,12}$"));
        ii(db, new Pais("Somalia", "+252", "^\\d{7,12}$"));
        ii(db, new Pais("Sudáfrica", "+27", "^\\d{7,12}$"));
        ii(db, new Pais("Sudán", "+249", "^\\d{7,12}$"));
        ii(db, new Pais("Sudán del Sur", "+211", "^\\d{7,12}$"));
        ii(db, new Pais("Tanzania", "+255", "^\\d{7,12}$"));
        ii(db, new Pais("Togo", "+228", "^\\d{7,12}$"));
        ii(db, new Pais("Túnez", "+216", "^\\d{7,12}$"));
        ii(db, new Pais("Uganda", "+256", "^\\d{7,12}$"));
        ii(db, new Pais("Zambia", "+260", "^\\d{7,12}$"));
        ii(db, new Pais("Zimbabue", "+263", "^\\d{7,12}$"));

        // Oceanía
        ii(db, new Pais("Australia", "+61", "^\\d{7,12}$"));
        ii(db, new Pais("Fiyi", "+679", "^\\d{7,12}$"));
        ii(db, new Pais("Islas Marshall", "+692", "^\\d{7,12}$"));
        ii(db, new Pais("Islas Salomón", "+677", "^\\d{7,12}$"));
        ii(db, new Pais("Kiribati", "+686", "^\\d{7,12}$"));
        ii(db, new Pais("Micronesia", "+691", "^\\d{7,12}$"));
        ii(db, new Pais("Nauru", "+674", "^\\d{7,12}$"));
        ii(db, new Pais("Nueva Zelanda", "+64", "^\\d{7,12}$"));
        ii(db, new Pais("Palaos", "+680", "^\\d{7,12}$"));
        ii(db, new Pais("Papúa Nueva Guinea", "+675", "^\\d{7,12}$"));
        ii(db, new Pais("Samoa", "+685", "^\\d{7,12}$"));
        ii(db, new Pais("Tonga", "+676", "^\\d{7,12}$"));
        ii(db, new Pais("Tuvalu", "+688", "^\\d{7,12}$"));
        ii(db, new Pais("Vanuatu", "+678", "^\\d{7,12}$"));
    }
}