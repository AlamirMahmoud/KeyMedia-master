package ameircom.keymedia.AppManger;

/**
 * Created by Lincoln on 05/01/16.
 */
public class Config {


    public static final String BASE_URL = "http://keymediaagency.co";
    public static final String UPDATE_USER_CHAT_ICON = "UPDATE_USER_CHAT_ICON";
    public static final String REFRESH_MESSAGES ="refresh_messages" ;
    public static String OPERATION = BASE_URL + "/opp.php";
    public static String img_url=BASE_URL+"/uploads/";



    public static final String media = "0" ;
    public static final String adv = "1" ;
    public static final String mob = "2" ;
    public static final String arc = "3" ;
    public static final String pa360 = "4" ;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000 ;

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";


    public static final int LOGIN = 0 ;
    public static final int SIGNUP = 1 ;
    public static final int UPDATE_USER = 2;
    public static final int SEND_MESS = 3;
    public static final String GET_USERS =  "4";
    public static final String UPLOAD_IMG = "5";
    public static final int ADD_PROD = 6;
    public static final String GET_PROD = "7";


    public static int TEXT_MESSAGE_TYPE = 1 ;
    public static int MesaageDetailActivity = 8;
}
