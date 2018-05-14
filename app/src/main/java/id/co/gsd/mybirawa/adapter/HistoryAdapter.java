package id.co.gsd.mybirawa.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.gsd.mybirawa.R;
import id.co.gsd.mybirawa.model.ModelHistory;
import id.co.gsd.mybirawa.util.TimeHelper;
import id.co.gsd.mybirawa.util.VectorDrawableUtils;

/**
 * Created by Biting on 1/16/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.historyHolder> {
    private Context mContext;
    private ModelHistory model;
    private List<ModelHistory> listModel;
    private TextView tvDesc, tvTgl;
    private TimeHelper th;
    private LayoutInflater inflater;

    public HistoryAdapter(Context mContext, List<ModelHistory> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        th = new TimeHelper();
        inflater = LayoutInflater.from(mContext);

    }

    @Override
    public historyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_history, null);
        historyHolder holder = new historyHolder(view);
        view.getWidth();
        view.getHeight();
        return holder;
    }

    @Override
    public void onBindViewHolder(historyHolder holder, int position) {
        model = listModel.get(position);

        ModelHistory history = listModel.get(0);

        if (model.getTglSubmit().equals(history.getTglSubmit())) {
            holder.timelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.timelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

        SimpleDateFormat fServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateServer = fServer.parse(model.getTglSubmit());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd-MMM-yyyy");
            String date = sdf.format(dateServer);
            holder.tvTgl.setText(date + " ~ " + model.getNamaUser());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvDesc.setText(model.getDesc());
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }

    static class historyHolder extends RecyclerView.ViewHolder {
        TextView tvDesc;
        TextView tvTgl;
        TimelineView timelineView;

        historyHolder(View view) {
            super(view);
            timelineView = view.findViewById(R.id.time_marker);
            tvDesc = view.findViewById(R.id.text_timeline_desc);
            tvTgl = view.findViewById(R.id.text_timeline_date);
        }
    }
}
