package maq.shopmeats.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import maq.shopmeats.R;


/**
 * Created by RedixbitUser on 3/22/2018.
 */

public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    public final TextView mDate;
    public final TextView mMessage;
    public final TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        mDate = itemView.findViewById(R.id.text_timeline_date) ;
        mMessage = itemView.findViewById( R.id.text_timeline_title) ;
        mTimelineView=itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}