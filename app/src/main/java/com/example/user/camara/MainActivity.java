package com.example.user.camara;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageView imagen ;
    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ+"misfotos";
final int tomar_foto=10;
final int seleccionar_foto =20;
String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagen = (ImageView)findViewById(R.id.imgCamara);

    }

    public void onclick(View view) {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setTitle("Selecione un opcion");
        alerta.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    TomarFoto();
            }else if (opciones[i].equals("Cargar Imagen")){
                    Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intento.setType("image/");
                    startActivityForResult(intento.createChooser(intento,"Seleccionar la aplicacion"),seleccionar_foto);
            }else if (opciones[i].equals("Cancelar")){
                    dialog.dismiss();
                }
        }});
                alerta.show();



    }

    private void TomarFoto() {
        File file = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean creada = file.exists();
        String nombre="";
        if (creada==false){
            creada = file.mkdirs();

        }else if (creada ==true){
            nombre = (System.currentTimeMillis()/1000)+".jpg";

        }
         path = Environment.getExternalStorageDirectory()+file.separator+RUTA_IMAGEN+file.separator+nombre;
        File imagen = new File(path);
        Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intento.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imagen));
        startActivityForResult(intento,tomar_foto);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case seleccionar_foto:
                    Uri path1 =data.getData();
                    imagen.setImageURI(path1);
                    break;
                case tomar_foto:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                            Log.i("Ruta Almacenamniento","path:"+path);
                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);

                    break;

            }

        }
    }
}
