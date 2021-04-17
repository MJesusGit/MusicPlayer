package com.example.musicplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer.Constantes.Constantes;
import com.example.musicplayer.Dominio.Usuario;
import com.example.musicplayer.Persistencia.ConexionSQLiteHelper;
import com.example.musicplayer.Persistencia.ImagenDAO;
import com.example.musicplayer.Persistencia.UsuarioDAO;

public class MainActivity extends AppCompatActivity {

    private EditText txtNombreUsuarioLogin;
    private EditText txtPasswordLogin;

    private UsuarioDAO gestor_usuario_login = new UsuarioDAO();
    private ImagenDAO gestor_imagen = new ImagenDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos las referencias a los elementos graficos de la GUI

        txtNombreUsuarioLogin = findViewById(R.id.txtNombreUsuarioLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);

        //gestor_usuario_login.crearTablaUsuario(MainActivity.this);
        //gestor_imagen.crearTablaImagen(MainActivity.this);

        //gestor_imagen.insertarDatosTablaImagen(MainActivity.this, "Imagen1",null);



    }

    /**
     *
     * Descripcion: Oyente asociado al boton inicio sesion. Permite acceder al sistema si los datos son correctos
     *
     * @param view
     */
    public void oyente_btnIniciarAplicacion(View view){

        if(txtNombreUsuarioLogin.getText().toString().equals("") || txtPasswordLogin.getText().toString().equals("")){

            dialogoAviso("Datos Imcompletos.",MainActivity.this);
            comprobarDatosFormalarioLogin(view);

        }
        else{

            int comprobar_password = comprobacionPasswordRegistrado();
            int usuario_disponible = comprobacionUsuarioRegistrado();

            if(comprobar_password == 0 && usuario_disponible == 0){

                Intent i = new Intent(this, activity_menu_principal.class );
                i.putExtra("nombre_usuario_registrado", txtNombreUsuarioLogin.getText().toString());
                startActivity(i);

            }
            else{
                dialogoAviso("Este usuario no existe en el sistema",MainActivity.this);
                txtNombreUsuarioLogin.setBackgroundColor(Color.rgb(200,0,0));
                txtPasswordLogin.setBackgroundColor(Color.rgb(200,0,0));
            }

        }

    }

    /**
     *
     * Descripcion: Metodo que comprueba los datos que faltan por completar
     *
     * @param view
     */
    private void comprobarDatosFormalarioLogin(View view){

        //Datos Nombre Usuario

        int usuario_disponible = comprobacionUsuarioRegistrado();

        if(txtNombreUsuarioLogin.getText().toString().equals("")) {
            txtNombreUsuarioLogin.setBackgroundColor(Color.rgb(200,0,0));
        }
        else{
            if(usuario_disponible == 1){
                txtNombreUsuarioLogin.setBackgroundColor(Color.rgb(200,0,0));
                dialogoAviso("Nombre de Usuario no existe.",MainActivity.this);
            }
            else{
                txtNombreUsuarioLogin.setBackgroundColor(Color.rgb(0,255,0));
            }
        }

        //Datos Password Usuario

        int comprobar_password = comprobacionPasswordRegistrado();

        if(txtPasswordLogin.getText().toString().equals("")) {
            txtPasswordLogin.setBackgroundColor(Color.rgb(200,0,0));
        }
        else{
            if(usuario_disponible == 1){
                txtPasswordLogin.setBackgroundColor(Color.rgb(200,0,0));
            }
            else{
                txtPasswordLogin.setBackgroundColor(Color.rgb(0,255,0));
            }
        }

    }

    /**
     *
     * Descripcion: Metodo que comprueba si el nombre de usuaruo introducido se corresponde con la contrasena
     *
     * @return entero que permite conocer si la operacion se ha realizado correctamente
     */
    private int comprobacionPasswordRegistrado(){

        int comprobacion = -1;
        String cadena_comprobacion = null;

        if(txtPasswordLogin.getText().toString() != null){

            cadena_comprobacion = gestor_usuario_login.buscarDatosUsuarioRegistrado(MainActivity.this, txtNombreUsuarioLogin.getText().toString(), "Password");

            if(cadena_comprobacion != null){
                if(txtPasswordLogin.getText().toString().equals(cadena_comprobacion.toString())){
                    comprobacion = 0; //Usuario existe sistema
                }
            }
            else{
                comprobacion = 1; //Usuario no existe sistema
            }
        }
        return  comprobacion;
    }

    /**
     *
     * Descripcion: Metodo que comprueba si el nombre de usuario existe en el sistema
     *
     * @return entero que permite conocer si la operacion se ha realizado correctamente
     */
    private int comprobacionUsuarioRegistrado(){

        int comprobacion = -1;
        String cadena_comprobacion = null;

        if(txtNombreUsuarioLogin.getText().toString() != null){

            cadena_comprobacion = gestor_usuario_login.buscarDatosUsuarioRegistrado(MainActivity.this, txtNombreUsuarioLogin.getText().toString(), "NombreUsuario");

            if(cadena_comprobacion != null){
                comprobacion = 0; //Usuario existe
            }
            else{
                comprobacion = 1; //Usuario no existe
            }

        }
        return  comprobacion;
    }

    /**
     *
     * Descripcion: Metodo que muestra un aviso al usuario dependiendo de las acciones que este realice
     *
     * @param aviso Mensaje personalizado dependiendo del mensaje del aviso
     * @param context contexto en este caso es ventanaRegistro.this
     */
    private void dialogoAviso(String aviso, Context context){

        AlertDialog.Builder dialogo_builder = new AlertDialog.Builder(context);
        dialogo_builder .setMessage(aviso);
        dialogo_builder.setCancelable(true);

        dialogo_builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog dialogo_alert = dialogo_builder.create();
        dialogo_alert.show();

    }

    /**
     *
     * Descripcion: Oyente asociado al boton Registro que te permite ir a la actividad para Registrarse
     *
     * @param view
     */
    public void oyente_btnRegristroAplicacion(View view){

        Intent i = new Intent(this, ventanaRegistro.class );
        startActivity(i);

    }
}