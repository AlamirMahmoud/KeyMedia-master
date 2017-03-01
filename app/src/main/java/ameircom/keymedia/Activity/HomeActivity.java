package ameircom.keymedia.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import ameircom.keymedia.adapter.CustomListAdapter;
import ameircom.keymedia.R;


public class HomeActivity extends AppCompatActivity{

    ListView list;
    String[] itemname ={

            "Our Departmenmts",
            "Cashing/Order",
            "About us",
            "Comminucate with us"
    };

    Integer[] imgid={

            R.drawable.imgone,
            R.drawable.imgthree,
            R.drawable.imgfour,
            R.drawable.imgfive
    };

    BottomSheetBehavior bottomSheetBehavior ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.farg_menu_sheet));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                if ( Slecteditem.equals("About us"))
                {
                    Intent i = new Intent(HomeActivity.this , AboutUS.class);startActivity(i);
                    overridePendingTransition(R.anim.products_enter, R.anim.products_exit);
                }
                else  if ( Slecteditem.equals("Our Departmenmts"))
                {
                    Intent i = new Intent(HomeActivity.this , DepartmentsActivity.class);startActivity(i);
                    overridePendingTransition(R.anim.products_enter, R.anim.products_exit);
                }
                else if ( Slecteditem.equals("Comminucate with us"))
                {
                    Intent i = new Intent(HomeActivity.this , Communicate.class);startActivity(i);
                    overridePendingTransition(R.anim.products_enter, R.anim.products_exit);
                }
                else Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
        findViewById(R.id.toolbar_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }
}