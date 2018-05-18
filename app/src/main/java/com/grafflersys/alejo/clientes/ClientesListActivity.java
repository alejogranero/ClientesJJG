package com.grafflersys.alejo.clientes;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class ClientesListActivity extends AppCompatActivity {

    ArrayList<Cliente> nList;
    ClientesListAdapter nAdapter = null;
    ListView mListView;
    ImageView imageViewIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_list);

        mListView = findViewById(R.id.listView);

        nList = new ArrayList<>();
        nAdapter = new ClientesListAdapter(this, R.layout.row, nList);
        mListView.setAdapter(nAdapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Listado de clientes");

        //obtiene todos los datos de sqlite

        Cursor cursor = MainActivity.nSQLiteHelper.getData("SELECT * FROM CLIENTES");

        nList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String apellido = cursor.getString(2);
            String dni = cursor.getString(3);
            String direccion = cursor.getString(4);
            String telefono = cursor.getString(5);
            byte[] imagen = cursor.getBlob(6);

            nList.add(new Cliente(id, nombre, apellido, dni, direccion, telefono, imagen));
        }

        nAdapter.notifyDataSetChanged();
        if (nList.size() == 0) {
            Toast.makeText(this, "No se encontraron clientes", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                CharSequence[] items = {"Actualizar", "Borrar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClientesListActivity.this);
                dialog.setTitle("Elija una opción");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Cursor c = MainActivity.nSQLiteHelper.getData("SELECT id FROM CLIENTES");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();

                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(ClientesListActivity.this, arrID.get(position));
                        }
                        if (i == 1) {
                            //Borrar
                            Cursor c = MainActivity.nSQLiteHelper.getData("SELECT id FROM CLIENTES");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();

                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }


                });
                dialog.show();
                return true;
            }
        });


    }

    private void showDialogDelete(final Integer idCliente) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ClientesListActivity.this);
        dialogDelete.setTitle("Atención");
        dialogDelete.setMessage("¿Está seguro de eliminar al cliente?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            MainActivity.nSQLiteHelper.deleteData(idCliente);
                            Toast.makeText(ClientesListActivity.this, "Borrado exitoso", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                        }
                        updateClientList();
                    }

                }
        );
        dialogDelete.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Actualizar datos");

        imageViewIcon = dialog.findViewById(R.id.imageViewCliente);
        final EditText edtName = dialog.findViewById(R.id.txtNombre);
        final EditText edtApellido = dialog.findViewById(R.id.txtApellido);
        final EditText edtDni = dialog.findViewById(R.id.txtDNI);
        final EditText edtDomicilio = dialog.findViewById(R.id.txtDomicilio);
        final EditText edtTelefono = dialog.findViewById(R.id.txtTelefono);

        Button btnUpdate = dialog.findViewById(R.id.btnSave);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        //cambiar foto

        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        ClientesListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.nSQLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtApellido.getText().toString().trim(),
                            edtDni.getText().toString().trim(),
                            edtDomicilio.getText().toString().trim(),
                            edtTelefono.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Actualización de datos exitosa", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Update error", e.getMessage());
                }
            }
        });

    }

    private void updateClientList() {
        Cursor c = MainActivity.nSQLiteHelper.getData("SELECT * FROM CLIENTES");
        nList.clear();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String apellido = c.getString(2);
            String dni = c.getString(3);
            String domicilio = c.getString(4);
            String telefono = c.getString(5);
            byte[] imagen = c.getBlob(6);

            nList.add(new Cliente(id, name, apellido, dni, domicilio, telefono, imagen));
        }

        nAdapter.notifyDataSetChanged();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
            } else {
                Toast.makeText(this, "No hay permisos para acceder al archivo", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //habilita el corte de la imagen
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult resultado = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = resultado.getUri();
                //setea la imagen seleccionada de la galería en el imageView
                imageViewIcon.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultado.getError();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
