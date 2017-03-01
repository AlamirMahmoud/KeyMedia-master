
package ameircom.keymedia.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import ameircom.keymedia.R;
import ameircom.keymedia.adapter.CustomListAdapterTwo;

public class Advertising extends AppCompatActivity {


    ListView list;

    String[] itemname = {
            "خدماتنا",
            "حتما سنعود",
            "من نحن",
            "Make today Amazing",
            "Key Media"
    };

   Integer[] imgid = {
            R.drawable.onee,
            R.drawable.twoo,
            R.drawable.thrie,
            R.drawable.fourrr,
            R.drawable.fiveee,

    };

    ImageView i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertising);




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
