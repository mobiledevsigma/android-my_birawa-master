package id.co.gsd.mybirawa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import id.co.gsd.mybirawa.R;

/**
 * Created by Biting on 2/27/2018.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {

    private List<String> listTime;
    private Context context;

    public TimeAdapter(Context context, List<String> listTime) {
        this.listTime = listTime;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_time, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String time = sdf.format(cal.getTime());
        int currentTime = Integer.parseInt(time);

        String times1 = listTime.get(position).toString().substring(0, 2);
        int timer = Integer.parseInt(times1);

        if (currentTime >= timer) {
            if ((position + 1) < listTime.size()) {
                String times2 = listTime.get(position + 1).toString().substring(0, 2);
                int timer2 = Integer.parseInt(times2);
                if (currentTime < timer2) {
                    holder.titleTextView.setText(listTime.get(position).toString());
                    holder.titleTextView.setTextSize(20.f);
                    holder.titleTextView.setTextColor(Color.parseColor("#FF4543"));
                } else {
                    holder.titleTextView.setText(listTime.get(position).toString());
                    holder.titleTextView.setTextSize(20.f);
                    holder.titleTextView.setTextColor(Color.parseColor("#666666"));
                }
            } else {
                if (currentTime < timer + 4) {
                    holder.titleTextView.setText(listTime.get(position).toString());
                    holder.titleTextView.setTextSize(20.f);
                    holder.titleTextView.setTextColor(Color.parseColor("#FF4543"));
                } else {
                    holder.titleTextView.setText(listTime.get(position).toString());
                    holder.titleTextView.setTextSize(20.f);
                    holder.titleTextView.setTextColor(Color.parseColor("#666666"));
                }
            }
        } else {
            holder.titleTextView.setText(listTime.get(position).toString());
            holder.titleTextView.setTextSize(20.f);
            holder.titleTextView.setTextColor(Color.parseColor("#666666"));
        }
    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        LinearLayout layout;

        MyViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.tv_time);
            layout = v.findViewById(R.id.lay_back_time);
        }
    }

    public interface TimeInterface {
        void set_batas(String bawah, String atas);
    }
}