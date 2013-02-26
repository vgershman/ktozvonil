package com.vgershman.whocalling.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.dao.Call;
import com.vgershman.whocalling.dao.Comment;

import java.util.ArrayList;
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
        } else {

        }

        return view;
    }

    static class ContentHolder {
        TextView itemPhoneNumber;
        TextView itemCallerName;
        TextView itemCallTime;
        ImageView itemCallType;
        ImageView itemCallerIcon;
        ImageView itemTextDivider;
        FrameLayout itemBackground;
        int oldPosition;
    }
}
