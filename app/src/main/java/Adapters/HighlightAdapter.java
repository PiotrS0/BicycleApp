package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bicycleApp.R;

import java.util.List;

import Model.Highlight;

public class HighlightAdapter extends BaseAdapter {

    private Context context;
    private List<Highlight> highlightList;
    private LayoutInflater layoutInflater;

    public HighlightAdapter(Context context, List<Highlight> highlightList) {
        this.context = context;
        this.highlightList = highlightList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return highlightList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.highlight_adapter,viewGroup,false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.textViewHA);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageViewHA);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.date.setText(""+ highlightList.get(position).getDate());
        byte[] imgByte = highlightList.get(position).getImage();
        Bitmap img = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        viewHolder.image.setImageBitmap(img);

        return convertView;
    }

    class ViewHolder{
        TextView date;
        ImageView image;
    }
}