package mx.com.encargalo.tendero.Inicio_sesion.ui.Mis_ordenes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mx.com.encargalo.R;

public class mio_frgmensajeconfirmacion extends Fragment {
    Button mio_mcbtnvolverinicio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mio_frgmensajeconfirmacion, container, false);

        mio_mcbtnvolverinicio=vista.findViewById(R.id.mio_mcbtnvolverinicio);
        mio_mcbtnvolverinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_misordenesdetallepedido);
            }
        });
        return vista;
    }
}