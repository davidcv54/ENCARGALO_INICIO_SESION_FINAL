package mx.com.encargalo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class is_cls_ActivitysInicioSesion {
    //Contiene el singleton
    private static is_cls_ActivitysInicioSesion actividades;

    //Contiene el contexto
    private static Activity contexto;

    //Contiene la actividad con la que se va a trabajar
    private static Class actividad;

    //Parámetros para pasar entre actividades
    public static String        PARAM_1 = "PARAM_1";
    final public static int     PARAM_INT_1 = 1;
    public static String        PARAM_2 = "PARAM_2";
    public static String        PARAM_3 = "PARAM_3";
    public static String        PARAM_4 = "PARAM_4";
    public static String        PARAM_5 = "PARAM_5";
    public static String        PARAM_6 = "PARAM_6";
    public static String        PARAM_7 = "PARAM_7";
    public static String        PARAM_8 = "PARAM_8";
    public static String        PARAM_9 = "PARAM_9";
    public static String        PARAM_10 = "PARAM_10";


    //Constructor privado
    private is_cls_ActivitysInicioSesion()
    {

    }

    //Obtiene el singleton
    final public static is_cls_ActivitysInicioSesion getSingleton(final Context contexto, final Class actividad)
    {

        //Si ya hay instancia entonces
        if(is_cls_ActivitysInicioSesion.actividades == null)
            is_cls_ActivitysInicioSesion.actividades = new is_cls_ActivitysInicioSesion();

        //Guarda el contexto con el que debe de trabajar
        is_cls_ActivitysInicioSesion.contexto = (Activity) contexto;

        //Guarda la actividad con la que se va a trabajar
        is_cls_ActivitysInicioSesion.actividad  = actividad;

        //Devuelve la instancia única
        return is_cls_ActivitysInicioSesion.actividades;

    }


    //Muestra la actividad
    final public void muestraActividadRestrasada(final boolean cierraActividadActual, final int milisegundos)
    {
        //Corre el thread separado
        (new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(milisegundos);
                }
                catch(final Exception excepcion)
                {
                }

                //Si tiene que cerrar la actividad actual entonces que así sea
                if(cierraActividadActual)
                    is_cls_ActivitysInicioSesion.contexto.finish();

                //Corre la otra actividad
                is_cls_ActivitysInicioSesion.contexto.startActivity(new Intent(is_cls_ActivitysInicioSesion.contexto,is_cls_ActivitysInicioSesion.actividad));
            }

        }).start();
    }


    //Muestra la actividad
    final public void muestraActividad()
    {
        is_cls_ActivitysInicioSesion.contexto.startActivity(new Intent(is_cls_ActivitysInicioSesion.contexto, is_cls_ActivitysInicioSesion.actividad));
    }


    //Muestra la actividad
    final public void muestraActividadForResult(final int code)
    {
        is_cls_ActivitysInicioSesion.contexto.startActivityForResult(new Intent(is_cls_ActivitysInicioSesion.contexto, is_cls_ActivitysInicioSesion.actividad), code);
    }


    //Muestra la actividad
    final public void muestraActividad(final HashMap<String, String> listaStrings)
    {
        //Crea el intent
        Intent intent = new Intent(is_cls_ActivitysInicioSesion.contexto, is_cls_ActivitysInicioSesion.actividad);

        //Recorre todos los parámetros que tiene que pasar
        Iterator iterator = listaStrings.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            String value = (String) pair.getValue();
            intent.putExtra(key, value);
        }

        //Corre la actividad
        is_cls_ActivitysInicioSesion.contexto.startActivity(intent);
    }


    //Muestra la actividad
    final public void muestraActividad(final HashMap<String, String> listaStrings, final HashMap<String, Serializable> listaSerializable)
    {
        //Crea el intent
        Intent intent = new Intent(is_cls_ActivitysInicioSesion.contexto, is_cls_ActivitysInicioSesion.actividad);

        //Recorre todos los parámetros que tiene que pasar de strings
        Iterator iterator = listaStrings.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            String value = (String) pair.getValue();
            intent.putExtra(key, value);
        }

        //Recorre todos los parámetros que tiene que pasar de serializables
        iterator = listaSerializable.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            Serializable value = (Serializable) pair.getValue();
            intent.putExtra(key, value);
        }

        //Corre la actividad
        is_cls_ActivitysInicioSesion.contexto.startActivity(intent);
    }


    //Muestra la actividad
    final public void muestraActividadSerializable(final HashMap<String, Serializable> listaSerializable)
    {
        //Crea el intent
        Intent intent = new Intent(is_cls_ActivitysInicioSesion.contexto, is_cls_ActivitysInicioSesion.actividad);

        //Recorre todos los parámetros que tiene que pasar de serializables
        Iterator iterator = listaSerializable.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            Serializable value = (Serializable) pair.getValue();
            intent.putExtra(key, value);
        }

        //Corre la actividad
        is_cls_ActivitysInicioSesion.contexto.startActivity(intent);
    }
}
