package mx.com.encargalo.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import mx.com.encargalo.Model.mio_mdlProductosOrden;
import mx.com.encargalo.R;

import static mx.com.encargalo.Utils.Util.RUTA;

public class mio_adaplvProductosOrdenAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<mio_mdlProductosOrden> mio_mdlProductosOrdenArrayList;
    onClick onClick;
    private SparseBooleanArray mSelectedItems;

    public mio_adaplvProductosOrdenAdapter(Context context, ArrayList<mio_mdlProductosOrden> mio_mdlProductosOrdenArrayList, onClick onClick) {
        this.context = context;
        this.mio_mdlProductosOrdenArrayList = mio_mdlProductosOrdenArrayList;
        this.onClick = onClick;
        mSelectedItems = new SparseBooleanArray();

    }

    @Override
    public int getCount() {
        return this.mio_mdlProductosOrdenArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mio_mdlProductosOrdenArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.mio_dpitemlistaproducto,null);
        mio_mdlProductosOrden productosOrden = mio_mdlProductosOrdenArrayList.get(i);
        TextView txtNombreProducto = v.findViewById(R.id.mio_txtProductoItem);
        TextView txtPrecioProducto = v.findViewById(R.id.mio_txtProductoPrecioItem);
        TextView txtCantProducto = v.findViewById(R.id.mio_txtProductoCantidadItem);
        ImageView imgvwProducto  = v.findViewById(R.id.mio_imgvwProductoItem);
        CheckBox chbxProductoSelect = v.findViewById(R.id.is_chkbProductoCompletadoItem);

        txtNombreProducto.setText(String.valueOf(productosOrden.getMio_locdescProducto()));
        txtPrecioProducto.setText(String.valueOf("S/" +productosOrden.getMio_locprecioProducto() + " x " + productosOrden.getMio_locunidadMedidaProducto()));
        txtCantProducto.setText(String.valueOf(productosOrden.getMio_loccantProducto()));
        Glide.with(context).load(String.valueOf(RUTA+productosOrden.getMio_locimagenProducto())).into(imgvwProducto);
        chbxProductoSelect.setChecked(mSelectedItems.get(i));

        /*chbxProductoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCheckBox(i, !getBooleanSelectedItem(i));
            }
        });*/

        chbxProductoSelect.setOnClickListener(view -> {
            onClick.onClick(i);
        });

        return v;
    }

    public interface onClick{
        void onClick(int id);
    }

    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItems.put(position, true);
        else
            mSelectedItems.delete(position);

        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelectedItems = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public boolean getBooleanSelectedItem(int i){
        return mSelectedItems.get(i);
    }

    public int getCountItems(){
        return  mSelectedItems.size();
    }


}
