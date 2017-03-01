package ameircom.keymedia.adapter;

        import android.app.Activity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import ameircom.keymedia.Activity.Communicate;
        import ameircom.keymedia.AppManger.AppController;
        import ameircom.keymedia.R;
        import ameircom.keymedia.internal_db.Room_tabel;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid ) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
         TextView chat_notifty = (TextView) rowView.findViewById(R.id.chat_notify);
        if (itemname[position].equals("KeyMedia Chat !")){
            int unseen = new Room_tabel().unseenCount(AppController.getInstance().getPrefManager().getUser().getId()) ;
            if (unseen > 0){
                chat_notifty.setText(unseen+"");
                chat_notifty.setVisibility(View.VISIBLE);
            }

        }


        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        extratxt.setText("Click here to Open !"+itemname[position]);
        return rowView;

    };
}