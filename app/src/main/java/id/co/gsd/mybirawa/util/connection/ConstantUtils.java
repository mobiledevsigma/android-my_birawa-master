package id.co.gsd.mybirawa.util.connection;

/**
 * Created by ramli on 23/04/2017.
 */

public interface ConstantUtils {

    interface URL {
        //String SERVER = "http://180.250.242.69:8069/";
        //String SERVER = "http://27.111.36.194/";
        String SERVER = "http://mybirawa.gsd.co.id/";

        String VERSION = SERVER + "api_version/checkVersion";
        String LOGIN = SERVER + "api_login/auth/";
        String LOGOUT = SERVER + "api_login/logout/";
        //DASHBOARD
        String DASH_HARIAN = SERVER + "api_dashboard/getDashboardHarian/";
        String DASH_MINGGUAN = SERVER + "api_dashboard/getDashboardMingguan/";
        String DASH_2MINGGUAN = SERVER + "api_dashboard/getDashboard2Mingguan/";
        String DASH_BULANAN = SERVER + "api_dashboard/getDashboardBulanan/";
        String DASH_3BULANAN = SERVER + "api_dashboard/getDashboard3Bulanan/";
        String DASH_6BULANAN = SERVER + "api_dashboard/getDashboard6Bulanan/";
        String DASH_TAHUN = SERVER + "api_dashboard/getDashboardTahunan/";
        String DASH_PUNCH = SERVER + "api_dashboard/getDashboardPunchlist/";
        String DASH_PUNCH_SPV = SERVER + "api_dashboard/getDashboardPunchlistSpv/";
        //NEWS
        String NEWS = SERVER + "api_data/news/";
        String BUILDING = SERVER + "api_data/getBuilding/";
        //CHECKLIST
        String DEVICE_TYPE = SERVER + "api_data/deviceType/";
        String DEVICE_DETAIL = SERVER + "api_data/deviceDetail/";
        String CHECKLIST = SERVER + "api_data/getChecklist/";
        String SUBMIT_CHECKLIST = SERVER + "api_data/submitChecklistInput/";
        //HK
        String DEVICE_DETAIL_HK = SERVER + "api_data/deviceDetailHK/";
        String CHECK_INPUT_HK = SERVER + "api_data/checkInputHK/";
        //PUNCHLIST ENGINEER
        String PUNCHLIST = SERVER + "api_punchlist/getPunchlist/";
        String SUBMIT_PUNCHLIST = SERVER + "api_punchlist/submitPunchlist/";
        String ADD_PUNCHLIST = SERVER + "api_punchlist/addPunchlist/";
        //PUNCHLIST SPV
        String GET_PUNCH_SPV = SERVER + "api_punchlist/getPunchlistSpv/";
        String GET_PUNCH_SPV_CLAUSE = SERVER + "api_punchlist/getPunchlistSpvClause/";
        String GET_NUMBER = SERVER + "api_punchlist/getNumber/";
        String GET_ROLE = SERVER + "api_punchlist/getRole/";
        String GET_GEDUNG = SERVER + "api_punchlist/getGedung/";
        String GET_LANTAI = SERVER + "api_punchlist/getLantai/";
        String GET_PERANGKAT = SERVER + "api_punchlist/getPerangkat/";
        String GET_HISTORY = SERVER + "api_punchlist/getPunchlistHistory/";
    }

    interface VERSION {
        String TAG_TITLE = "version";
        String TAG_ID = "version_id";
        String TAG_NUMBER = "version_number";
        String TAG_URL = "version_url";
        String TAG_NOTE = "version_note";
        String TAG_DATE = "version_date";
    }

    interface LOGIN {
        String TAG_USERNAME = "username";
        String TAG_PASSWORD = "password";
        String TAG_VERSION = "version_apk";
        String TAG_TOKEN = "token_number";
        String TAG_IMEI = "imei_device";
    }

    interface USER_DATA {
        String TAG_USER_ID = "user_id";
        String TAG_USERNAME = "user_name";
        String TAG_NAME = "nama_user";
        String TAG_JABATAN = "jabatan";
        String TAG_ROLE_ID = "role_id";
        String TAG_ROLE_NAME = "role_name";
        String TAG_UNIT_ID = "unit_id";
        String TAG_UNIT_NAME = "nama_unit";
        String TAG_AREA_NAME = "area_name";
        String TAG_STATUS = "status";
        String TAG_IMEI = "imei";
    }

    interface DASHBOARD {
        String TAG_TITLE = "dashboard";
        String TAG_SUDAH = "sudah";
        String TAG_BELUM = "belum";
        String TAG_TOTAL = "total";
        String TAG_TITLE2 = "list_type";
        String TAG_PJ_ID = "perangkat_jenis_id";
        String TAG_PJ_NAME = "perangkat_jenis_name";
        String TAG_PJ_BELUM = "perangkat_jenis_belum";
    }

    interface NEWS {
        String TAG_TITLE = "news";
        String TAG_ID = "id";
        String TAG_JUDUL = "judul";
        String TAG_ISI = "isi";
        String TAG_TGL = "tgl";
        String TAG_STATUS = "status";
        String TAG_USERID = "user_id";
        String TAG_USERNAME = "user_name";
    }

    interface BUILDING {
        String TAG_TITLE = "building";
        String TAG_BUILDING_ID = "gedung_id";
        String TAG_NAME = "gedung_name";
        String TAG_ADDRESS = "gedung_address";
        String TAG_LAT = "gedung_lat";
        String TAG_LONG = "gedung_long";
        String TAG_TITLE2 = "list_floor";
        String TAG_FLOOR_ID = "lantai_id";
        String TAG_FLOOR_NAME = "lantai_name";
    }

    interface PERIOD {
        String TAG_TITLE = "period";
        String TAG_ID = "period_id";
        String TAG_NAME = "period_name";
        String TAG_DESC = "period_desc";
        String TAG_DURATION = "period_duration";
        String TAG_TITLE2 = "list_device_type";
        String TAG_DT_ID = "id";
        String TAG_DT_NAME = "name";
    }

    interface DEVICE {
        String TAG_TITLE = "perangkat_jenis";
        String TAG_PJ_ID = "perangkat_jenis_id";
        String TAG_PJ_NAME = "perangkat_jenis_name";
        String TAG_PJ_BLM = "perangkat_jenis_belum";
        String TAG_PJ_ICON = "logo_jp";
        String TAG_TITLE2 = "list_perangkat";
        String TAG_PERANGKAT_ID = "perangkat_id";
        String TAG_PERANGKAT_CODE = "perangkat_code";
        String TAG_PERANGKAT_NAME = "perangkat_name";
        String TAG_PERANGKAT_MERK = "perangkat_merk";
        String TAG_PERANGKAT_CAP = "perangkat_capacity";
        String TAG_PERANGKAT_YEAR = "perangkat_year";
        String TAG_PERANGKAT_RMS = "perangkat_rms";
    }

    interface CHECKLIST {
        String TAG_TITLE = "data_checklist";
        String TAG_ID_DESC = "id_deskripsi";
        String TAG_NAME_DESC = "nama_deskripsi";
        String TAG_TITLE2 = "checklist_input";
        String TAG_ID = "id";
        String TAG_CHECKLIST = "checklist";
        String TAG_STD = "standard";
        String TAG_TYPE = "tipe";
        String TAG_SEPARATORY = "isSeparatorY";
        String TAG_SEPARATORN = "isSeparatorN";
    }

    interface PUNCHLIST {
        String TAG_TITLE = "punchlist";
        String TAG_ID_PUNCHLIST = "id_punchlist";
        String TAG_NO_PUNCHLIST = "no_punchlist";
        String TAG_ORDER_DATE = "order_date";
        String TAG_KELUHAN = "keluhan";
        String TAG_NAMA_GEDUNG = "nama_gedung";
        String TAG_NAMA_LANTAI = "nama_lantai";
        String TAG_ID_PERANGKAT = "id_perangkat";
        String TAG_NAMA_PERANGKAT = "nama_perangkat";
        String TAG_FOTO_1 = "image_before";
        String TAG_FOTO_2 = "image_after";
        String TAG_DESC = "description";
        String TAG_STATUS = "status";
    }

    interface ROLE {
        String TAG_TITLE = "role";
        String TAG_ID = "role_id";
        String TAG_NAME = "role_name";
    }

    interface GEDUNG {
        String TAG_TITLE = "gedung";
        String TAG_ID = "gedung_id";
        String TAG_NAME = "gedung_name";
    }

    interface LANTAI {
        String TAG_TITLE = "lantai";
        String TAG_ID = "lantai_id";
        String TAG_NAME = "lantai_name";
    }

    interface PERANGKAT {
        String TAG_TITLE = "perangkat";
        String TAG_ID = "id";
        String TAG_CODE = "id_perangkat";
        String TAG_NAME = "nama_perangkat";
    }

    interface ADD_PUNCH {
        String TAG_NO = "no_punchlist";
        String TAG_ID = "id_perangkat";
        String TAG_KELUHAN = "keluhan";
        String TAG_UNIT = "unit_id";
        String TAG_ROLE = "role_id";
    }

    interface PUNCH_SPV {
        String TAG_TITLE = "punchlist";
        String TAG_ID_PUNCHLIST = "id_punchlist";
        String TAG_NO_PUNCHLIST = "no_punchlist";
        String TAG_ROLE = "nama_role";
        String TAG_NAMA_GEDUNG = "nama_gedung";
        String TAG_NAMA_LANTAI = "nama_lantai";
        String TAG_NAMA_PERANGKAT = "nama_perangkat";
        String TAG_KELUHAN = "keluhan";
        String TAG_STATUS = "status";
        String TAG_IMAGE_1 = "image_before";
        String TAG_IMAGE_2 = "image_after";
        String TAG_DESC = "description";
        String TAG_ORDER = "order_date";
        String TAG_SUBMIT = "submit_date";
        String TAG_CREATED = "created_date";
    }
}


