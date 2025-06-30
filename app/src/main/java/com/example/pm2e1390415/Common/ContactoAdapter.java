package com.example.pm2e1390415.Common;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2e1390415.Modelos.Contacto;
import com.example.pm2e1390415.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private Context context;
    private List<Contacto> listaContactos;
    private List<Contacto> listaContactosOriginal;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCompartirClick(Contacto contacto);
        void onBorrarClick(Contacto contacto, int posicion);
        void onVerImagenClick(Contacto contacto);
        void onVerNotaClick(Contacto contacto);
        void onLlamarClick(Contacto contacto);
    }

    public ContactoAdapter(Context context, List<Contacto> listaContactos, OnItemClickListener listener) {
        this.context = context;
        this.listaContactos = new ArrayList<>(listaContactos);
        this.listaContactosOriginal = new ArrayList<>(listaContactos);
        this.listener = listener;
    }

    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder holder, int position) {
        Contacto contacto = listaContactos.get(position);

        if (contacto.getImagen() != null && !contacto.getImagen().isEmpty()) {
            File imagenFile = new File(Uri.parse(contacto.getImagen()).getPath());
            if (imagenFile.exists()) {
                Uri uriImagen = Uri.fromFile(imagenFile);
                holder.btnVerImagen.setImageURI(uriImagen);
            } else {
                holder.btnVerImagen.setImageResource(R.drawable.pf);
            }
        } else {
            holder.btnVerImagen.setImageResource(R.drawable.pf);
        }

        holder.txtNombre.setText(contacto.getNombre());
        holder.txtTelefono.setText(contacto.getTelefono());

        holder.btnCompartir.setOnClickListener(v -> listener.onCompartirClick(contacto));
        holder.btnBorrar.setOnClickListener(v -> listener.onBorrarClick(contacto, position));
        holder.btnVerImagen.setOnClickListener(v -> listener.onVerImagenClick(contacto));
        holder.btnLlamar.setOnClickListener(v -> listener.onLlamarClick(contacto));
        holder.btnVerNota.setOnClickListener(v -> listener.onVerNotaClick(contacto));
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public void eliminarContacto(int posicion) {
        listaContactos.remove(posicion);
        listaContactosOriginal.remove(posicion);
        notifyItemRemoved(posicion);
    }

    public void filtrar(String texto) {
        listaContactos.clear();
        if (texto.isEmpty()) {
            listaContactos.addAll(listaContactosOriginal);
        } else {
            String query = texto.toLowerCase();
            for (Contacto c : listaContactosOriginal) {
                if (c.getNombre().toLowerCase().startsWith(query)) {
                    listaContactos.add(c);
                }
            }
        }
        Collections.sort(listaContactos, (c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        notifyDataSetChanged();
    }


    class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtTelefono;
        ImageButton btnCompartir, btnBorrar, btnVerImagen, btnLlamar, btnVerNota;

        public ContactoViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtTelefono = itemView.findViewById(R.id.txt_telefono);
            btnCompartir = itemView.findViewById(R.id.btn_compartir);
            btnBorrar = itemView.findViewById(R.id.btn_borrar);
            btnVerImagen = itemView.findViewById(R.id.btn_ver_imagen);
            btnVerNota = itemView.findViewById(R.id.btn_ver_nota);
            btnLlamar = itemView.findViewById(R.id.btn_llamar);
        }
    }
}
