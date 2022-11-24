package mx.com.encargalo.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class is_cls_Session_Inicio_Sesion {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public static final String SETTING_Quiz_PREF = "setting_quiz_pref";
    private static final String SOUND_ONOFF = "sound_enable_disable";
    private static final String SHOW_MUSIC_ONOFF = "showmusic_enable_disable";
    private static final String VIBRATION = "vibrate_status";
    public static final String TOTAL_SCORE = "total_score";
    public static final String POINT = "no_of_point";

    public static final String IS_LAST_LEVEL_COMPLETED = "is_last_level_completed";
    public static final String LAST_LEVEL_SCORE = "last_level_score";
    public static final String COUNT_QUESTION_COMPLETED = "count_question_completed";
    public static final String COUNT_RIGHT_ANSWARE_QUESTIONS = "count_right_answare_questions";


    public static final String LANG_MODE = "lang_mode";
    public static final String N_COUNT = "n_count";
    public static final String E_MODE = "e_mode";
    public static final String LOGIN = "login";
    public static final String USER_ID = "userId";
    public static final String OPEN_ADS="openAds";
    public static final String INAPP_MODE="inappmode";
    public static final String ADDTYPE="addtype";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String PROFILE = "profile";
    public static final String F_CODE = "f_code";
    public static final String IS_FIRST_TIME = "isfirsttime";
    public static final String REFER_CODE = "refer_code";
    public static final String TYPE = "type";
    public static final String LANGUAGE = "language";
    public static final String GETDAILY = "getdaily";
    public static final String GETCONTEST = "getcontest";
    public static final String QUICK_ANS = "quick_ans";
    public static final String FCM = "fcm";
    public static final String STORE = "store";
    public static final String APPLANGUAGE = "applanguage";


    public is_cls_Session_Inicio_Sesion(Context context) {
        this._context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(_context);
        editor = pref.edit();
    }

    public static void clearUserSession(Context mContext) {
        if (mContext != null) {
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (mSharedPreferences != null) {
                mSharedPreferences.edit().remove(USER_ID).apply();
                mSharedPreferences.edit().remove(NAME).apply();
                mSharedPreferences.edit().remove(EMAIL).apply();
                mSharedPreferences.edit().remove(MOBILE).apply();
                mSharedPreferences.edit().remove(LOGIN).apply();
                mSharedPreferences.edit().remove(PROFILE).apply();
                mSharedPreferences.edit().remove(LANGUAGE).apply();
                mSharedPreferences.edit().remove(IS_FIRST_TIME).apply();

            }
        }
    }


    public static void setUserData(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

}
