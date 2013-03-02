package com.vgershman.whocall.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.vgershman.whocall.R;
import com.vgershman.whocall.dto.Comment;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vkosarev
 * Date: 22.02.13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class CommentsAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> comments = new ArrayList<Comment>();

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public CommentsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View reView, ViewGroup viewGroup) {
        ContentHolder holder;
        View view = reView;

        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(R.layout.comment_item, viewGroup, false);
            holder = new ContentHolder();
            holder.itemName = (TextView)view.findViewById(R.id.userName);
            holder.itemText = (TextView)view.findViewById(R.id.commentText);
            holder.itemTime = (TextView)view.findViewById(R.id.commentTime);
            holder.itemLiked = (ImageView)view.findViewById(R.id.liked);
            view.setTag(holder);

        } else {
            holder = (ContentHolder) view.getTag();
        }
        long millis = comments.get(position).getTime() * 1000L;
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        Date date = cal.getTime();

        holder.itemTime.setText(date.toLocaleString());
        holder.itemText.setText(comments.get(position).getText());
        holder.itemName.setText(comments.get(position).getName());
        holder.itemLiked.setSelected(1==comments.get(position).getLiked());


        return view;
    }

    static class ContentHolder {
      TextView itemText;
      TextView itemTime;
      TextView itemName;
      ImageView itemLiked;


    }
}
