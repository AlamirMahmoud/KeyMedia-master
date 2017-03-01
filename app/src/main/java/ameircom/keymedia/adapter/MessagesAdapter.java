package ameircom.keymedia.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.R;
import ameircom.keymedia.internal_db.Mess_tabel;

/**
 * Created by sotra on 9/25/2016.
 */
public class MessagesAdapter extends  RecyclerView.Adapter<MessagesAdapter.CoursesViewHolder> {
    private Activity context ;
    private List<Mess_tabel> data ;
    private LayoutInflater layoutInflater ;

    public MessagesAdapter(Activity context, List<Mess_tabel> data  )  {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
        Log.e("newAdapter","size" + data.size());

    }
    public Mess_tabel getItem(int postion){
        return  data.get(postion);
    }


    @Override
    public MessagesAdapter.CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.message_item,parent,false);
        CoursesViewHolder coursesViewHolder = new CoursesViewHolder(view);
        return coursesViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.CoursesViewHolder holder, final int position) {
        final Mess_tabel currentElement =  data.get((data.size() - position -1 ));
        if (currentElement.dir == 1){
            holder.message_container.setGravity(Gravity.LEFT);

        }else{
            holder.message_container.setGravity(Gravity.RIGHT);

        }

            Log.e("message dir ","   "+currentElement.dir  );


        holder.from.setText(currentElement.user_name);
        holder.timestamp.setText(currentElement.timeStamp);
        holder.message.setText(currentElement.message);
        Log.e("course member adapter","onb"  );
    }

  

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void insertData(Mess_tabel data){
        this.data.add(data);
        notifyDataSetChanged();
    }
    public void refresh(List<Mess_tabel> data) {
        this.data = data ;
        notifyDataSetChanged();
    }

    public class  CoursesViewHolder extends RecyclerView.ViewHolder {
        TextView  message , timestamp , from;
        LinearLayout message_container  ;
        public CoursesViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            from = (TextView) itemView.findViewById(R.id.mess_from);
            message_container =(LinearLayout)itemView.findViewById(R.id.message_container);
        }
    }

}
