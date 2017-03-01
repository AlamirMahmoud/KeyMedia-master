package ameircom.keymedia.internal_db;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;


@Table(name = "room")
public class Room_tabel extends  Model
{


    // This is the unique id given by the server
    @Column(name = "room_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String room_id;
    @Column(name = "name")
    public String name ;

    @Column(name = "count")
    public int count ;






    // Make sure to have a default constructor for every ActiveAndroid model
    public Room_tabel(){
        super();
    }


    // Used to return items from another table based on the foreign key
    public List<Room_tabel> data() {
          return new Select()
                .from(Room_tabel.class)
                .execute();
    }
    public int unseenCount(String room_id) {
        List<Room_tabel> data =  new Select()
                .from(Room_tabel.class)
                .where("room_id = ?" , room_id)
                .execute();
        if (data.size() != 0  && data.get(0).count > 0 )
            return data.get(0).count;
        return  0;
    }
    public void clear(String room_id) {
        new Update(Room_tabel.class).set("count = ? " , 0).where("room_id = ?" , room_id).execute(); ;

    }
    public void update_count(String room_id) {
        new Update(Room_tabel.class).set("count = ?" , (unseenCount(room_id)+1) ).where("room_id = ?" , room_id).execute(); ;

    }

    public boolean check(String room_id) {
       List list =   new Select()
                .from(Room_tabel.class)
                .where("room_id = ?", room_id)
                .execute();
            if (list.size() >0){
                return true;
            }else return false ;

    }

//    public  void  deletefav(String ID ){
//        new Delete().from(Mess_tabel.class).where("_id = ?", ID).execute();
//    }

}