package com.grafflersys.alejo.clientes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClientesListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Cliente> clientesList;

    public ClientesListAdapter(Context context, int layout, ArrayList<Cliente> clientesList) {
        this.context = context;
        this.layout = layout;
        this.clientesList = clientesList;
    }


    @Override
    public int getCount() {
        return clientesList.size();
    }

    @Override
    public Object getItem(int i) {
        return clientesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtNombre, txtApellido, txtDni, txtDomicilio, txtTelefono;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtNombre = row.findViewById(R.id.txtNombre);
            holder.txtApellido = row.findViewById(R.id.txtApellido);
            holder.txtDni = row.findViewById(R.id.txtDNI);
            holder.txtDomicilio = row.findViewById(R.id.txtDomicilio);
            holder.txtTelefono = row.findViewById(R.id.txtTelefono);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }
        Cliente cliente = clientesList.get(i);
        holder.txtNombre.setText(cliente.getNombre());
        holder.txtApellido.setText(cliente.getApellido());
        holder.txtDni.setText(cliente.getDni());
        holder.txtDomicilio.setText(cliente.getDomicilioCobro());
        holder.txtTelefono.setText(cliente.getTelefono());

        byte[] clienteImage = cliente.getImagen();
        Bitmap bitmap = BitmapFactory.decodeByteArray(clienteImage, 0, clienteImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}
