package mx.com.encargalo.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.com.encargalo.Model.mio_mdlMensajeOrden;
import mx.com.encargalo.R;

import static mx.com.encargalo.Utils.Util.RUTA;

public class mio_adaprvChatAdapter extends RecyclerView.Adapter<mio_adaprvChatAdapter.ChatHolder> {
    List<mio_mdlMensajeOrden>  mio_mdlMensajeOrdenList;
    Context context;

    public mio_adaprvChatAdapter(List<mio_mdlMensajeOrden> mio_mdlMensajeOrdenList, Context context) {
        this.mio_mdlMensajeOrdenList = mio_mdlMensajeOrdenList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mio_dpitemlistachat,parent,false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull mio_adaprvChatAdapter.ChatHolder holder, int position) {
        holder.nomUser.setText(String.valueOf(mio_mdlMensajeOrdenList.get(position).getMio_nomUsuario()));
        holder.contMensaje.setText(String.valueOf(mio_mdlMensajeOrdenList.get(position).getMio_contenidoMensaje()));
        holder.fechaMensaje.setText(String.valueOf(getTime(mio_mdlMensajeOrdenList.get(position).getMio_fechaMensaje())));
        Glide.with(context).load(String.valueOf(RUTA+mio_mdlMensajeOrdenList.get(position).getMio_imgUsuario())).into(holder.imgUser);
        String rol = String.valueOf(mio_mdlMensajeOrdenList.get(position).getMio_rolUsuario());
        holder.rolUser.setText(String.valueOf(rol));
        if (rol.equals("Tendero")){
            holder.lnContendor.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_400));
            holder.imgUser.setVisibility(View.GONE);
            holder.rolUser.setGravity(Gravity.RIGHT);
            holder.lnContenedorChat.setHorizontalGravity(Gravity.RIGHT);
            holder.lnContenedorChat.setGravity(Gravity.RIGHT);
            holder.nomUser.setText("Tu");
            holder.nomUser.setGravity(Gravity.RIGHT);
        }else if (rol.equals("Cliente")){
            holder.lnContendor.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_600));
            holder.nomUser.setText(String.valueOf(mio_mdlMensajeOrdenList.get(position).getMio_nomUsuario()));
            holder.lnContenedorChat.setHorizontalGravity(Gravity.LEFT);
            holder.lnContenedorChat.setGravity(Gravity.LEFT);
            holder.nomUser.setGravity(Gravity.LEFT);
        }else if (rol.equals("Repartidor")){
            holder.lnContendor.setBackgroundColor(ContextCompat.getColor(context, R.color.green_400));
            holder.nomUser.setText(String.valueOf(mio_mdlMensajeOrdenList.get(position).getMio_nomUsuario()));
            holder.lnContenedorChat.setHorizontalGravity(Gravity.LEFT);
            holder.lnContenedorChat.setGravity(Gravity.LEFT);
            holder.nomUser.setGravity(Gravity.LEFT);
        }

    }

    private String getTime(String mio_fechaMensaje) {
        String timeget=":c";

        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "hh:mm aa";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        try {
            date = inputFormat.parse(mio_fechaMensaje);
            timeget = outputFormat.format(date); // <-- I got result here
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeget;
    }

    @Override
    public int getItemCount() {
        return mio_mdlMensajeOrdenList.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        CircleImageView imgUser;
        LinearLayout lnContendor,lnContenedorChat;
        TextView nomUser, contMensaje, fechaMensaje,rolUser;

        public ChatHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.mio_imgv_user);
            nomUser = itemView.findViewById(R.id.mio_tv_item_nombre);
            contMensaje = itemView.findViewById(R.id.mio_tv_item_mensaje);
            fechaMensaje = itemView.findViewById(R.id.mio_tv_item_fecha);
            lnContendor = itemView.findViewById(R.id.mio_lnl_contedor);
            rolUser = itemView.findViewById(R.id.mio_tv_item_rol);
            lnContenedorChat = itemView.findViewById(R.id.mio_content_mensaje);
        }
    }
}
