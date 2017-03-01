package ameircom.keymedia;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ameircom.keymedia.AppManger.AppController;

public class MenuSheetFragment extends Fragment {
   View res_layout , logout_view  ;


    public MenuSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        if (res_layout != null) {
            ViewGroup parent = (ViewGroup) res_layout.getParent();
            if (parent != null) {
                parent.removeView(res_layout);
            }
        }
        try {
            res_layout = inflater.inflate(R.layout.fragment_menu_sheet, container, false);
        } catch (InflateException e) {

        }
        logout_view = res_layout.findViewById(R.id.logout_view);
        logout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        return res_layout  ;
    }



    public void logout(View view) {
        AppController.getInstance().getPrefManager().clear();

    }


}
