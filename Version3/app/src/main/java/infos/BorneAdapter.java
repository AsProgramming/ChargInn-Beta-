package infos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.asprogramming.charginn.R;
import java.util.ArrayList;
import java.util.Collections;

public class BorneAdapter extends ArrayAdapter<Borne> {

    private Context mContext;
    private ArrayList<Borne> lst;
    private static final int[] merkersImg = {R.drawable.grey_marker,
            R.drawable.green_marker, R.drawable.orang_marker};

    public BorneAdapter(Context context, ArrayList<Borne> borne) {
        super(context, R.layout.listview_borne, borne);
        mContext = context;
        lst = borne;
    }

    private class BorneHolder{
        ImageView mLvl;
        TextView mNom;
        TextView mTel;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        BorneHolder holder = null;
        Borne b = (Borne) getItem(position);

            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView == null){
                convertView = mInflater.inflate(R.layout.listview_borne, null);
                holder = new BorneHolder();
                creerHolder(convertView, holder);
            }else
                holder = (BorneHolder) convertView.getTag();

            mettreHolder(holder, b);


        return convertView;
    }

    private void creerHolder(View convertView, BorneHolder holder) {
        holder.mLvl = (ImageView) convertView.
                findViewById(R.id.activity_listerborne_imageView_niv);
        holder.mNom = (TextView) convertView.findViewById(R.id.
                activity_listerborne_textView_nom);
        holder.mTel = (TextView) convertView.findViewById(R.id.
                activity_listerborne_textView_tel);
        convertView.setTag(holder);
    }

    private void mettreHolder(BorneHolder holder, Borne b) {
            holder.mLvl.setImageResource(merkersImg[b.getNiveau() - 1]);
            holder.mNom.setText(b.getNom());
            holder.mTel.setText(b.getTelephone());
    }

    @Override
    public Borne getItem(int position) {
        return lst.get(position);
    }

    public static int getImages(int indice){
        return merkersImg[indice];
    }

}
