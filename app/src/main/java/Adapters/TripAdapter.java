package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bicycleApp.R;

import java.util.List;

import Model.Trip;

public class TripAdapter extends BaseAdapter {

    private Context context;
    private List<Trip> tripList;
    private LayoutInflater layoutInflater;
    private int [] isComming;

    public TripAdapter(Context context, List<Trip> tripList, int[] isComming) {
        this.context = context;
        this.tripList = tripList;
        this.isComming = isComming;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tripList.size();
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

        View view = layoutInflater.inflate(R.layout.trip_adapter, viewGroup, false);

        TextView index = view.findViewById(R.id.trip_adapter_text_number);
        TextView date = view.findViewById(R.id.trip_adapter_text_date);
        TextView title = view.findViewById(R.id.trip_adapter_text_title);
        ImageView imageView = view.findViewById(R.id.trip_adapter_image);

        index.setText(""+(position+1));
        date.setText(""+tripList.get(position).getDateWithoutSeconds());
        title.setText(""+tripList.get(position).getTitle());
        if(isComming[position] == 1)
            imageView.setImageResource(R.mipmap.timer_icon);

        return view;
    }

}