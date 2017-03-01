package ameircom.keymedia.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.FeedModel;
import ameircom.keymedia.R;

/**
 * Created by sotra on 9/25/2016.
 */
public class NewsFeedAdapter extends  RecyclerView.Adapter<NewsFeedAdapter.CoursesViewHolder> {
    private Activity context ;
    private ArrayList<FeedModel> data ;
    private LayoutInflater layoutInflater ;
    int bg_color ;

    public NewsFeedAdapter(Activity context, ArrayList<FeedModel> data  )  {
        this.context = context;
        this.data = data;
        this.bg_color = bg_color ;
        layoutInflater = LayoutInflater.from(context);
        Log.e("newAdapter","size" + data.size());

    }
    public FeedModel getItem(int postion){
        return  data.get(postion);
    }


    @Override
    public NewsFeedAdapter.CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.news_feed_item,parent,false);
        CoursesViewHolder coursesViewHolder = new CoursesViewHolder(view);
        return coursesViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsFeedAdapter.CoursesViewHolder holder, final int position) {
        final FeedModel currentElement =  data.get(position);
        holder.desc.setText(currentElement.getText());
        Glide.with(context)
                .load(Config.img_url + currentElement.getImg())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.cover) ;


        Log.e("course member adapter","onb"  );
    }

  

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void insertData(FeedModel data){
        this.data.add(data);
        notifyDataSetChanged();
    }
    public void refresh(ArrayList<FeedModel> data) {
        this.data = data ;
        notifyDataSetChanged();
    }

    public class  CoursesViewHolder extends RecyclerView.ViewHolder {
        TextView desc  ;
        ImageView cover  ;
        public CoursesViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.news_desc);
            cover = (ImageView) itemView.findViewById(R.id.news_cover_img);

        }
    }

}
