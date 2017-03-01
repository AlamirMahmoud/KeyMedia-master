package ameircom.keymedia.internal_db;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "mess")
public class Mess_tabel extends  Model
{





    // This is the unique id given by the server
    @Column(name = "mess_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String mess_id;

    @Column(name = "user_id")
    public String user_id ;

    @Column(name = "user_name")
    public String user_name ;

    @Column(name = "message")
    public String message ;

    @Column(name = "type")
    public int type ;

    @Column(name = "timeStamp")
    public String timeStamp ;

    @Column(name = "dir")
    public int dir = 2 ;






    // Make sure to have a default constructor for every ActiveAndroid model
    public Mess_tabel(){
        super();
    }


    // Used to return items from another table based on the foreign key
    public List<Mess_tabel> data() {
          return new Select()
                .from(Mess_tabel.class)
                .execute();
    }

    public Mess_tabel room_data(String room_id) {
       List<Mess_tabel> data =   new Select()
                .from(Mess_tabel.class)
                .where("user_id = ?", room_id)
                .execute();
        if (data.size() > 1 )
        return  data.get(0) ;

        return  new Mess_tabel() ;
    }
    public List<Mess_tabel> list_of_room_data(String room_id) {
        List<Mess_tabel> data =   new Select()
                .from(Mess_tabel.class)
                .where("user_id = ?", room_id)
                .execute() ;

        return  data ;
    }
//    public int unseenCount(String room_id) {
//        List<Mess_tabel> data =  new Select()
//                .from(Mess_tabel.class)
//                .where("user_id =? false")
//                .execute();
//        return  data.size();
//    }

    public boolean check(String ID) {
       List list =   new Select()
                .from(Mess_tabel.class)
                .where("mess_id = ?", ID)
                .execute();
            if (list.size() >0){
                return true;
            }else return false ;

    }

//    public  void  deletefav(String ID ){
//        new Delete().from(Mess_tabel.class).where("mess_id = ?", ID).execute();
//    }

}