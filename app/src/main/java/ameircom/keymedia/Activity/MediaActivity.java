
package ameircom.keymedia.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import ameircom.keymedia.adapter.CustomListAdapterTwo;
import ameircom.keymedia.R;

public class MediaActivity extends AppCompatActivity {


    ListView list;

    String[] itemname = {
            "Key Media 1",
            "Key Media 2",
            "Key Media 3",
            "Key Media 4",
            "Key Media 5",
            "Key Media 6",
            "Key Media 7"
    };

    Integer[] imgid = {
            R.drawable.mediaone,
            R.drawable.mediatwo,
            R.drawable.mediathree,
            R.drawable.mediafour,
            R.drawable.mediafive,
            R.drawable.mediasix,
            R.drawable.mediaseven


    };

    ImageView i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);




        CustomListAdapterTwo adapter = new CustomListAdapterTwo(this, itemname, imgid);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[position] ;
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();




            }
        });


    }


}
