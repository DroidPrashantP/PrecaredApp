package com.app.precared.interfaces;

/**
 * Created by prashant on 13/7/16.
 */
public interface Constants {

    int MY_SOCKET_TIMEOUT_MS = 10000;
    String PHOTO_UPOLOAD_KEY ="image";
    int MAX_RETRIES = 0;
    String SegmentKey = "dnYvfpExNQTYDmUVkOlLRBgR9nj8YPiP";
    // flag to identify whether to show single line
    // or multi line test push notification tray
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    //String GCM_SENDER_ID = "120391810404";
    String GCM_SENDER_ID = "393882663760"; // staging

    //String GCM_SERVER_KEY = "AIzaSyCeVubiX4o5TkSxPOBN3GHMxxF0w6E16aI";
    String GCM_SERVER_KEY = "AIzaSyDraPdVi9gQ6df_24xjKHgTOc6R1oz9-5M";
    int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    interface MyChatKeys {
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
        String STATUS = "status";
        String TYPE = "type";

        String NOTES_CREATED_AT = "created_at";
        String NOTES_CREATED_BY = "created_by";
        //Show Tickets
        String MAIN_REPLY = "main_reply";
        String TICKET = "ticket";
        String SUBJECT = "subject";
        String BODY = "body";
        String REPLIED_BY = "replied_by";
        String NAME = "name";
        String ATTACHMENTS = "attachments";
        String ATTACHMENT_URL = "attachment_url";
        String FILE_NAME = "content_file_name";
        String FILE_SIZE = "content_file_size";
        String USER = "User";

        String SENDER_ID = "sender_id";
        String RECEIVER_ID = "recevier_id";
        String MESSAGES = "message";
        String SENDER_NAME = "sender_name";
        String RECEIVER_NAME = "recevier_name";
        String ID = "id";
        String IMAGE_URL = "image_url";
        String CHAT_TIME = "chat_time";

        String API_SEND_MSG = "send_message";
        String API_CHAT_LIST = "chat_list";

    }

    interface Preferences {
        String PREFERENCE = "precaredPreference";
        String ACCESS_TOKEN = "access_token";
        String REFRESH_TOKEN = "refresh_token";
        String USER_ID = "id";
        String NAME = "name";
        String EMAIL = "email";
        String IS_LOGGED_IN = "isLogin";
        String GCM_REGISTRATION_TOKEN = "gcm_reg_id";
        String DEVICE_NUMBER = "device_number";
        String DEVICE_TYPE = "device_type";
        String REFERRAL_CODE = "referral_code";
        String REF_URL = "ref_url";
        String REFERRAL_MESSAGE = "referral_message";
        String AMOUNT_EARNED = "amount_earned";
        String AMOUNT_PENDING = "amount_pending";
        String TOTOL_AMOUNT_EARNED = "total_amount_earned";
        String ADDRESS = "address";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PHONE = "phone";
    }

    interface LoginKeys {

        String MODE_EMAIL = "email";
        String GCM_REGISTRATION_TOKEN = "device_token";

        //Social Login
        String UID = "id";
        String EMAIL = "email";
        String NAME = "name";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PASSWORD = "password";
        String ACCESS_TOKEN = "access_token";
        String NUMBER = "phone";
        String CONFIRM_PASSWORD = "password_confirmation";
        String REFERRAL_CODE = "referral_code";
        String REF_URL = "ref_url";
        String REFERRAL_MESSAGE = "referral_message";
        String AMOUNT_EARNED = "amount_earned";
        String AMOUNT_PENDING = "amount_pending";
        String TOTOL_AMOUNT_EARNED = "total_amount_earned";
        String ADDRESS = "address";
    }

    interface VolleyRequestTags {
        String EMAIL_LOGIN = "emailLogin";
        String SELLER_REQUEST = "seller request";
        String SUBMIT_SELLER_REQUEST = "Submit request";
        String CHAT_LIST_REQUEST = "chat list";
        String SEND_MEG_REQUEST = "Send message";
        String SELLER_COUNTS_REQUEST = "seller counts";
        String DEVICE_REGISTRAION = "device registration";
        String GCM_REGISTRAION_AFTER_LOGIN = "gcm regostraion after login";
        String EMAIL_SIGNUP = "email signup";
        String UPDATE_PROFILE = "update";
        String ADD_ADDRESS = "add address";
        String UPDATE_SELLER_PRODUCT = "update seller product";
    }

    interface URL {
        //String BASE_URL = "http://staging.precared.com/";
        String BASE_URL = "http://www.precared.com/";
        String API_LOGIN = BASE_URL+"api/v1/auth/login.json";
        String API_SIGNUP = BASE_URL+"api/v1/auth/sign_up.json";
        String API_Seller = BASE_URL+"api/v1/seller_requests/";
        String API_SELLER_REQUEST = BASE_URL +"api/v1/seller_requests.json";
        String API_FETCH_CHAT_DATA = BASE_URL +"api/v1/conversations.json";
        String API_SEND_MESSAGE = BASE_URL +"api/v1/conversations.json";
        String API_DEVICE_GCM_REGISTRAION = BASE_URL +"api/v1/devices/register_user_device.json";
        String API_SELLER_COUNTS = BASE_URL+"api/v1/users.json";
        String API_DEVICE_REGISTRATION = BASE_URL+"api/v1/devices.json";
        String API_UPDATE_PROFILE = BASE_URL+"api/v1/users.json";
        String API_GET_ADDRESSES = BASE_URL+"api/v1/addresses.json";

        String UPDATE_SELLER_PRODUCT = BASE_URL + "api/v1/seller_requests/";;
    }

    interface BundleKeys{
        String ProductId = "productId";
    }
    interface SellerKeys {

        String DATA = "data";
        String ID = "id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String SELLER_PRICE = "seller_price";
        String SELLING_PRICE = "selling_price";
        String CATEGORY_NAME = "category_name";
        String DISPLAY_STATE = "display_state";
        String IMAGE_URL = "image_url";
        String VIEW_COUNT = "view_count";
        String API_SELLER_LISTING = "seller listing";
        String API_SELLER_COUNTS = "seller count";
        String SELLER_NAME = "name";
        String SELLER_EMAIL = "email";
        String MY_PRICE = "my_price";
        String PRECARED_PRICE = "precared_price";
        String REFURBISH_PRICE = "refurbish_cost";
        String SERVICE_TAX = "service_tax";
        String CAN_PUBLISH = "can_publish";
        String CAN_HOLD = "can_hold";
        String PRODUCT_URL = "product_url";

    }

    interface SellerAddRequestKey {
            String PRODUCT_NAME = "name";
            String PRODUCT_DESCRIPTION = "description";
            String PRODUCT_Efects = "defects";
            String PRODUCT_Category= "category_id";
            String PRODUCT_ADDRESS_ID = "address_id";
            String PHOTO_IMAGE = "image";

    }
    interface AttachmentsKeys {
        int REQUEST_CODE = 2000;

        int FETCH_STARTED = 2001;
        int FETCH_COMPLETED = 2002;
        int PERMISSION_GRANTED = 2003;
        int PERMISSION_DENIED = 2004;

        /**
         * Request code for permission has to be < (1 << 8)
         * Otherwise throws java.lang.IllegalArgumentException: Can only use lower 8 bits for requestCode
         */
        int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 23;

        String INTENT_EXTRA_ALBUM = "album";
        String INTENT_EXTRA_IMAGES = "images";
        String INTENT_EXTRA_LIMIT = "limit";
        int DEFAULT_LIMIT = 1;

        //Maximum number of images that can be selected at a time
        int limit = 1;
    }

    interface GoogleAnalyticKey {
        String ADD_SELLER_PRODUCT = "add seller product";
        String CHAT_LISTING = "chat listing";
        String HOME_ACTIVITY = "Home Page";
        String LOGIN_ACTIVITY = "Login page";
        String LOGIN_FRAGMENT = "Login";
        String SIGN_UP_FRAGMENT = "Sign up";
        String SELLER_ACTIVITY = "Seller page";
    }
}
