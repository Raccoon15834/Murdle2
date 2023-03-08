package com.example.murdle2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragRecyclerViewAdapter extends RecyclerView.Adapter<FragRecyclerViewAdapter.ViewHolder> {

    public List<GuessFrag> guessData;
    private LayoutInflater mInflater;
    Context ctx;

    // data is passed into the constructor
    FragRecyclerViewAdapter(Context context, List<GuessFrag> data) {
        this.mInflater = LayoutInflater.from(context);
        this.guessData = data;
        ctx=context;
    }

    // inflating layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.guessfrag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragRecyclerViewAdapter.ViewHolder holder, int position) {
        GuessFrag curr = guessData.get(position);
        holder.mtxtView.setText(curr.nameDisplay);
        holder.colorcode.setBackgroundColor(curr.num);
        //holder.textEntrance(ctx);
    }

    @Override
    public int getItemCount() {
        return guessData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mtxtView;
        public Button colorcode;

        ViewHolder(View view) {
            super(view);
            mtxtView = view.findViewById(R.id.guessName);
            colorcode = view.findViewById(R.id.colorcode);

        }



//        public void textEntrance(Context c) {
//            Animation inL = AnimationUtils.loadAnimation(c, R.anim.inl);
//            mtxtView.startAnimation(inL);
//            colorcode.startAnimation(inL);
//        }

    }
    public void clear() {
        int size = guessData.size();
        guessData.clear();
        notifyItemRangeRemoved(0, size);
    }
}
