package com.BuzzKora;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {



    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final int MATCH_LIST_TIMER = 60;
    public static final int MATCH_TIMER = 30;

    public static final String SHARED_PREF = "ah_firebase";

    public static final  String MATCH_BY_DATE_URL = "http://90kora.com/api/match-by-day.php?date=";
    public static final String MATCH_DETAILS_URL = "http://90kora.com/api/match-details.php?ref=";
    public static final String LEAGUE_DETAILS_URL = "http://90kora.com/api/league-details.php?nid=";
    public static final String TEAM_DETAILS_URL = "http://90kora.com/api/team-details.php?nid=";
    public static final String LEAGUE_LISTING = "http://90kora.com/api/list-leagues.php?";
    public static final String COUNTRY_LISTING = "http://90kora.com/api/list-countries.php?";
    public static final String SUB_MENU_URL = "http://90kora.com/api/list-submenu.php?key=";
    public static final String SUPER_URL = "http://90kora.com/api/list-super-matches.json";
    public static final String USER_DATA_UPDATE = "http://90kora.com/firebase/buzzkora/update-user-data.php";
    public static final String FOLLOW_SUGGESTION = "http://90kora.com/api/follow-suggestion.json?t=";
    public static final String NEWS_SOURCE = "http://90kora.com/api/list-news.json?t=";
    public static final String VIDEO_SOURCE = "http://90kora.com/api/list-videos.json?t=";


}
