package epf.tica.lamesimone.projet_mb;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by paullame on 07/06/2017.
 */

public class NewsFeedAdapter extends ArrayAdapter<NewsFeedElement> {


    public NewsFeedAdapter(@NonNull Context context, @NonNull List<NewsFeedElement> objects) {
        super(context, 0, objects);
    }

    class NFViewHolder{
        public ImageView thumbnail;
        public TextView data;
        public TextView gps;
        public TextView fileName;
        public TextView fileSize;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //Android nous fournit un convertView null lorsqu'il nous demande de la créer
        //dans le cas contraire, cela veux dire qu'il nous fournit une vue recyclée
        if(convertView == null){
            //Nous récupérons notre row_tweet via un LayoutInflater,
            //qui va charger un layout xml dans un objet View
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_element,parent, false);
        }

        NFViewHolder viewHolder = (NFViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new NFViewHolder();
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.data = (TextView) convertView.findViewById(R.id.data);
            viewHolder.gps = (TextView) convertView.findViewById(R.id.gps);
            viewHolder.fileName = (TextView) convertView.findViewById(R.id.fileName);
            viewHolder.fileSize = (TextView) convertView.findViewById(R.id.fileSize);
            convertView.setTag(viewHolder);
        }

        NewsFeedElement newsFeedElement = getItem(position);

        Glide.with(getContext()).load(newsFeedElement.getThumbnail()).into(viewHolder.thumbnail);
        viewHolder.data.setText(newsFeedElement.getData());
        viewHolder.gps.setText(newsFeedElement.getGps());
        viewHolder.fileName.setText(newsFeedElement.getFileName());
        viewHolder.fileSize.setText(newsFeedElement.getFileSize());

        //nous renvoyons notre vue à l'adapter, afin qu'il l'affiche
        //et qu'il puisse la mettre à recycler lorsqu'elle sera sortie de l'écran
        return convertView;
    }
}
