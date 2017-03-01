package ameircom.keymedia.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ameircom.keymedia.R;
import ameircom.keymedia.internal_db.Mess_tabel;
import ameircom.keymedia.internal_db.Room_tabel;


/**
 * Created by sotra on 9/25/2016.
 */
public class MessageRoomAdapter extends  RecyclerView.Adapter<MessageRoomAdapter.CoursesViewHolder> {
    private Activity context ;
    private List<Room_tabel> data ;
    private LayoutInflater layoutInflater ;
    int bg_color ;

    public MessageRoomAdapter(Activity context, List<Room_tabel> data  )  {
        this.context = context;
        this.data = data;
        this.bg_color = bg_color ;
        layoutInflater = LayoutInflater.from(context);
        Log.e("newAdapter","size" + data.size());

    }
    public Room_tabel getItem(int postion){
        return  data.get(postion);
    }


    @Override
    public MessageRoomAdapter.CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.room_item,parent,false);
        CoursesViewHolder coursesViewHolder = new CoursesViewHolder(view);
        return coursesViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageRoomAdapter.CoursesViewHolder holder, final int position) {
        final Room_tabel currentElement =  data.get(position);
         Mess_tabel last_mess =  new Mess_tabel().room_data(currentElement.room_id);

        holder.name.setText(currentElement.name);
        holder.timestamp.setText(last_mess.timeStamp);
        holder.message.setText(last_mess.message);

        Log.e("course member adapter","onb"  );
    }

  

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void insertData(Room_tabel data){
        this.data.add(data);
        notifyDataSetChanged();
    }
    public void refresh(List<Room_tabel> data) {
        this.data = data ;
        notifyDataSetChanged();
    }

    public class  CoursesViewHolder extends RecyclerView.ViewHolder {
        TextView name , message , timestamp ;
        ImageView img ;
        public CoursesViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.it_text);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            img = (ImageView) itemView.findViewById(R.id.it_img);
        }
    }




}
