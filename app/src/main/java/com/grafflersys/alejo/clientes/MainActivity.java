package com.grafflersys.alejo.clientes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtDni, edtDomicilio, edtTelefono;
    Button btAdd, btList;
    ImageView mImageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper nSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNombre = findViewById(R.id.editTextNombre);
        edtApellido = findViewById(R.id.editTextApellido);
        edtDni = findViewById(R.id.DNI);
        edtDomicilio = findViewById(R.id.DomicilioCobro);
        edtTelefono = findViewById(R.id.telefono);

        mImageView = findViewById(R.id.imageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY
                );
            }
        });

        //Creando BD
        nSQLiteHelper = new SQLiteHelper(this, "CLIENTESBD.sqlite", null, 1);


        //Creando Tabla
        nSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS CLIENTES(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre VARCHAR, apellido VARCHAR, dni VARCHAR, domicilioCobro VARCHAR, telefono VARCHAR, image BLOB)");

        btAdd = findViewById(R.id.btnAdd);
        btList = findViewById(R.id.btnList);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nSQLiteHelper.insertData(
                            edtNombre.getText().toString().trim(),
                            edtApellido.getText().toString().trim(),
                            edtDni.getText().toString().trim(),
                            edtDomicilio.getText().toString().trim(),
                            edtTelefono.getText().toString().trim(),
                            imageViewToByte(mImageView)
                    );
                    Toast.makeText(MainActivity.this, "Agregado correctamente", Toast.LENGTH_SHORT).show();

                    //Resetea vista
                    edtNombre.setText("");
                    edtApellido.setText("");
                    edtDni.setText("");
                    edtDomicilio.setText("");
                    edtTelefono.setText("");
                    mImageView.setImageResource(R.drawable.addphoto);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ClientesListActivity.class));
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "No hay permisos para acceder al archivo", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
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
                mImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultado.getError();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
