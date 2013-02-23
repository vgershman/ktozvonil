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

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vkosarev
 * Date: 22.02.13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class LastCallsAdapter extends BaseAdapter {

    private Context context;
    private List<Call> calls = new ArrayList<Call>();

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    public LastCallsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return calls.size();
    }

    @Override
    public Object getItem(int i) {
        return calls.get(i);
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
            view = setupLayout(viewGroup, position);
            holder = new ContentHolder();

            holder.itemCallType = (ImageView) view.findViewById(R.id.itemIcCallState);
            holder.itemPhoneNumber = (TextView) view.findViewById(R.id.itemNumber);
            holder.itemCallerIcon = (ImageView) view.findViewById(R.id.itemIcPerson);
            holder.itemCallerName = (TextView) view.findViewById(R.id.itemName);
            holder.itemTextDivider = (ImageView) view.findViewById(R.id.itemIcDivider);
            holder.itemCallTime = (TextView) view.findViewById(R.id.itemTime);
            holder.itemBackground = (FrameLayout) view.findViewById(R.id.itemBackground);

            view.setTag(holder);
        } else {
            holder = (ContentHolder) view.getTag();
            if (needReinflate(holder.oldPosition, position)) {
                view = setupLayout(viewGroup, position);
                holder = new ContentHolder();

                holder.itemCallType = (ImageView) view.findViewById(R.id.itemIcCallState);
                holder.itemPhoneNumber = (TextView) view.findViewById(R.id.itemNumber);
                holder.itemCallerIcon = (ImageView) view.findViewById(R.id.itemIcPerson);
                holder.itemCallerName = (TextView) view.findViewById(R.id.itemName);
                holder.itemTextDivider = (ImageView) view.findViewById(R.id.itemIcDivider);
                holder.itemCallTime = (TextView) view.findViewById(R.id.itemTime);
                holder.itemBackground = (FrameLayout) view.findViewById(R.id.itemBackground);

                view.setTag(holder);
            }
        }

        holder.oldPosition = position;
        holder.itemBackground.postInvalidate();

        if (calls.get(position).isOut()) {
            holder.itemCallType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_out));
        } else {
            holder.itemCallType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_in));
        }

        holder.itemPhoneNumber.setText(calls.get(position).getNumber());
        if (calls.get(position).hasName()) {
            holder.itemCallerIcon.setVisibility(View.VISIBLE);
            holder.itemCallerName.setVisibility(View.VISIBLE);
            holder.itemTextDivider.setVisibility(View.VISIBLE);
            holder.itemCallerName.setText(calls.get(position).getName());
        } else {
            holder.itemCallerIcon.setVisibility(View.GONE);
            holder.itemCallerName.setVisibility(View.GONE);
            holder.itemTextDivider.setVisibility(View.GONE);
        }

        holder.itemCallTime.setText(calls.get(position).getTimeString());

        return view;
    }

    private View setupLayout(ViewGroup viewGroup, int position) {
        if (calls.size() == 1) {
            return ((Activity) context).getLayoutInflater().inflate(R.layout.last_call_header, viewGroup, false);
        } else if (calls.size() == 2) {
            if (position == 0) {
                return ((Activity) context).getLayoutInflater().inflate(R.layout.last_call_header, viewGroup, false);
            } else if (position == 1) {
                return ((Activity) context).getLayoutInflater().inflate(R.layout.last_call_footer, viewGroup, false);
            }
        } else if (calls.size() > 2) {
            if (position == 0) {
                return ((Activity) context).getLayoutInflater().inflate(R.layout.last_call_header, viewGroup, false);
            } else if (position == calls.size() - 1) {
                return ((Activity) context).getLayoutInflater().inflate(R.layout.last_call_footer, viewGroup, false);
            } else {
                return ((Activity) context).getLayoutInflater().inflate(R.layout.last_call_item, viewGroup, false);
            }
        }
        return null;
    }

    private boolean needReinflate(int oldPosition, int position) {
        if (position == 0) {
            if (oldPosition == 0) {
                return false;
            } else {
                return true;
            }
        }

        if (position == calls.size() - 1) {
            if (oldPosition == calls.size() - 1) {
                return false;
            } else {
                return true;
            }
        }

        if (position > 0 && position < calls.size() - 1) {
            if (oldPosition > 0 && oldPosition < calls.size() - 1) {
                return false;
            } else {
                return true;
            }
        }

        return true;
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
