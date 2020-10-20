package maq.shopmeats.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import maq.shopmeats.Getset.DeliveryGetSet;
import maq.shopmeats.R;

import static maq.shopmeats.activity.MainActivity.tf_opensense_regular;

public class DeliveryAdapter extends BaseAdapter {
    private final ArrayList<DeliveryGetSet> dat;
    private final Context context;
    private LayoutInflater inflater = null;



    public DeliveryAdapter(ArrayList<DeliveryGetSet> dat, Context context) {
        this.dat = dat;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return dat.size();
    }

    @Override
    public Object getItem(int position) {
        return dat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View vi = convertView;
        if(convertView==null)
        {
            vi = inflater.inflate(R.layout.celldelivery, parent,false);
        }
        ImageView img_user = vi.findViewById( R.id.img_user);


        if (dat.get(position).getComplete().equals(context.getString(R.string.order_processing))) {
            img_user.setImageDrawable(context.getDrawable(R.drawable.img_orderprocess));
        }
        else if (dat.get(position).getComplete().equals(context.getString(R.string.order_ready_to_delivery)))
        {
            img_user.setImageDrawable(context.getDrawable(R.drawable.img_orderprocess));

        }
        else if(dat.get(position).getComplete().equals(context.getString(R.string.order_delivered)))
        {
            img_user.setImageDrawable(context.getDrawable(R.drawable.img_ordercomplete));

        }
        TextView txt_orderNo = vi.findViewById(R.id.txt_orderNo);
        txt_orderNo.setTypeface(tf_opensense_regular);
        String orderNo=context.getString(R.string.order_no)+dat.get(position).getOrderNo();
        txt_orderNo.setText(orderNo);


         TextView txt_orderAmount = vi.findViewById(R.id.txt_orderAmount);
        txt_orderAmount.setTypeface(tf_opensense_regular);

        String orderAmount = context.getString(R.string.order_amount)+context.getString(R.string.currency)+dat.get(position).getOrderAmount();
        txt_orderAmount.setText(orderAmount);


        TextView txt_orderQuantity = vi.findViewById(R.id.txt_orderQuantity);
        txt_orderQuantity.setTypeface(tf_opensense_regular);
        String itemNum = dat.get(position).getOrderQuantity()+ " " +context.getString(R.string.items);
        txt_orderQuantity.setText(itemNum);


        TextView txt_orderDateTime = vi.findViewById(R.id.txt_orderDateTime);
        txt_orderDateTime.setTypeface(tf_opensense_regular);
        txt_orderDateTime.setText(dat.get(position).getOrderTimeDate());

        return vi;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flipImage(Boolean ifTrue, ImageView imageView){
        if(ifTrue)
        {
            imageView.setImageDrawable(context.getDrawable(R.drawable.img_ordercomplete));
        }
        else  imageView.setImageDrawable(context.getDrawable(R.drawable.img_orderprocess));

    }
}
