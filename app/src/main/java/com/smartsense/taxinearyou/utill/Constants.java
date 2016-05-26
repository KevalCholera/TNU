package com.smartsense.taxinearyou.utill;

public class Constants {
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_FAIL = 500;
    public static final int STATUS_CHECK = 201;
    public static final String DB_PATH = "/data/data/com.smartsense.taxiNearU/databases/";
//    public static final String DB_PATH = "/data/data/com.smartsense.gifkar/databases/";


    //    public static final String BASE_URL = "http://13.76.228.139:8080";
    public static final String BASE_URL = "http://fmcgfinal.cloudapp.net:8080";
    public static final String BASE_URL_POSTFIX = "/tnu/m?__eventid=";
    public static final String BASE_URL_PHOTO = BASE_URL + "/tnu";
    public static final String BASE_URL_PHOTO_THUMB = BASE_URL + "/images/products/";
    public static final String BASE_IMAGE_URL = "http://104.43.15.1:8025";
    public static final String PRO_TITLE = "Please Wait";
    public static final String PRO_LIST = "Syncing Process Running...";
    public static final String FROM_ADDRESS = "from_add";
    public static final String TO_ADDRESS = "to_add";
    public static final String VIA_ADDRESS = "via_add";
    public static final String VIA2_ADDRESS = "via2_add";

    public class PrefKeys {

        public static final String PREF_CITY_ID = "city_id";
        public static final String PREF_CITY_NAME = "city_name";
        public static final String PREF_AREA_ID = "area_id";
        public static final String PREF_AREA_NAME = "area_name";
        public static final String PREF_CATEGORY_ID = "category_id";
        public static final String PREF_NOTIFICATION_COUNT = "count";
        public static final String PREF_ACCESS_TOKEN = "accessToken";
        public static final String PREF_RIDE_ID = "rideId";
        public static final String PREF_REFER_CODE = "refer_code";
        public static final String PREF_USER_ID = "user_id";
        public static final String PREF_REFER_MSG = "refer_msg";
        public static final String PREF_USER_FULLNAME = "user_fullname";
        public static final String PREF_USER_FIRST = "user_first_name";
        public static final String PREF_USER_LAST = "user_last_name";
        public static final String PREF_USER_MNO = "user_mno";
        public static final String PREF_USER_EMAIL = "user_email";
        public static final String PREF_USER_PROIMG = "user_proimg";
        public static final String PREF_USER_STATUS = "user_status";
        public static final String MAIN_DATA_JSON = "main_data_json";
        public static final String PREF_USER_PASS = "user_pass";
        public static final String PREF_PROD_LIST = "prod_list";
        public static final String PREF_ADDRESS = "address";
        public static final String PREF_ADDRESS_ID = "address_id";
        public static final String PREF_ISWAITING_SMS = "iswaiting_sms";
        public static final String PREF_ISOTP_VERIFIED = "isotp_verified";
        public static final String PREF_COUNTRY_LIST = "country_list";
        public static final String PREF_AREA_PIN_CODE = "area_pin_code";
        public static final String PREF_USER_INFO = "user_info";
        public static final String SHOP_NAME = "shop_name";
        public static final String SHOP_ID = "shop_id";
        public static final String OCCASIONS = "occasions";
        public static final String SHOP_FILTER = "shop_filter";
        public static final String FILTER_SHOP_NAME = "filter_shop_name";
        public static final String FILTER_SHOP_MIN = "filter_shop_min";
        public static final String FILTER_SHOP_RATTING = "filter_shop_rat";
        public static final String FILTER_PROD_NAME = "filter_prod_name";
        public static final String FILTER_PROD_PRICE = "filter_prod_price";
        public static final String PROD_FILTER = "prod_filter";
        public static final String SHOP_IMAGE = "shop_image";
        public static final String TIMESLOT = "time_slot";
        public static final String PREF_IS_SOCIAL = "social_flag";
        public static final String MIN_ORDER = "min_order";
        public static final String DELIVERY_CHARGES = "del_charges";
        public static final String PREF_LUGGAGE = "luggageArray";
        public static final String PREF_PASSENGER = "passengerArray";
        public static final String PREF_TAXI_TYPE = "taxiTypeArray";
        public static final String MAIN_DATA = "mainData";
        public static final String PREF_DISTANCE_MATRIX = "distanceMatrix";
        public static final String PREF_DISTANCE_LIST = "distanceList";
        public static final String PREF_PARTNER_ARRAY = "partnerArray";
        public static final String PREF_FILTER_REQUEST = "filterRequest";
    }

    public class NavigationItems {
        public static final int NAV_LOGIN = 0;
        public static final int NAV_HOME = 1;
        public static final int NAV_MY_CART = 3;
        public static final int NAV_MY_ORDERS = 4;
        public static final int NAV_MY_ADDRESSES = 5;
        public static final int NAV_MY_REMINDERS = 6;
        public static final int NAV_NOTIFICATIONS = 8;
        public static final int NAV_REFER_FRIEND = 9;
        public static final int NAV_ABOUT_US = 11;
        public static final int NAV_FEED_US = 10;
        public static final int NAV_SETTING = 13;
        public static final int NAV_LOGOUT = 12;
    }

    public class Events {
        public static final int EVENT_LOGIN = 4002;
        public static final int LOST_ITEM = 1116;
        public static final int CONTACT_US = 1103;
        public static final int FEED_BACK = 1122;
        public static final int ADD_LOST_ITEM = 1115;
        public static final int CANCEL_RIDE = 4009;
        public static final int PERSONAL_INFO = 1102;
        public static final int EVENT_FORGOT_PASS = 4013;
        public static final int EVENT_FORGOT_EMAIL = 1129;
        public static final int EVENT_FORGOT_EMAIL_WITHOUT = 1130;
        public static final int UPDATE_GENERAL_INFO = 1105;
        public static final int UPDATE_EMAIL = 1118;
        public static final int GET_USER_DETAILS = 5005;
        public static final int EVENT_SIGNUP = 4003;
        public static final int EVENT_PARTNER_LIST = 5001;
        public static final int CHECK_MOBILE_AVAILABILITY = 5009;
        public static final int EVENT_BOOKING = 1101;
        public static final int CHECK_EMAIL_AVAILABILITY = 5007;
        public static final int RESEND_INVOICE = 1124;
        public static final int EVENT_MY_TRIP = 5003;
        public static final int EVENT_LOGOUT = 5004;
        public static final int EVENT_GET_SECURITY_QES = 5008;
    }

    public class ErrorCode {
        public static final int INVALID_CREDENTIALS = 501;
        public static final int PARAM_MISSING = 1001;
        public static final int UNAUTHORIZED = 1002;
        public static final int SERVER_ERROR = 1004;
    }

    public class ScreenCode {
        public static final int SCREEN_OTP = 1;
        public static final int SCREEN_FORGOT = 2;
        public static final int SCREEN_MYADDRESS = 3;
        public static final int SCREEN_MYREMINDER = 4;
        public static final int SCREEN_LOGIN = 5;
    }
}