package id.abypraya.personalassignment.api;

public class Config {
    //hardcode
    public static final String URL_SEARCH_PST = "http://192.168.0.101/inixtraining_api/search/search_info_pst.php?id_pst=";
    public static final String URL_SEARCH_KLS = "http://192.168.0.101/inixtraining_api/search/search_info_kls.php?id_kls=";
    public static final String URL_SEARCH_INS = "http://192.168.0.101/inixtraining_api/search/search_info_ins.php?id_ins=";

    //request method
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String GET_DETAIL = "GET_DETAIL";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String SPECIAL_GET_DETAIL = "GET_DETAIL_SUB_KELAS";
    public static final String SPECIAL_GET = "GET_SUB_KELAS";

    //url and file name
    public static final String IP_USER = "http://192.168.0.101/";
    public static final String IP_USER_MOBILE = "http://172.20.10.2/";
    public static final String SUBDIR_PESERTA = "peserta";
    public static final String SUBDIR_KELAS = "kelas";
    public static final String SUBDIR_INSTRUKTUR = "instruktur";
    public static final String SUBDIR_MATERI = "materi";
    public static final String SUBDIR_DETAIL_KELAS = "detail_kelas";
    public static final String SUBDIR_INSTANSI = "instansi";
    public static final String DIR_INIXTRAINING = "inixtraining_api";

    //key & value JSON
    //peserta
    public static final String KEY_PST_ID = "id_pst";
    public static final String KEY_PST_NAMA = "nama_pst";
    public static final String KEY_PST_EMAIL = "email_pst";
    public static final String KEY_PST_HP = "hp_pst";

    //kelas
    public static final String KEY_KLS_ID = "id_kls";
    public static final String KEY_KLS_ENTRY_DATE = "tgl_mulai_kls";
    public static final String KEY_KLS_END_DATE = "tgl_akhir_kls";
    public static final String KEY_KLS_NAME_INSTRUKTUR = "nama_ins";
    public static final String KEY_KLS_NAME_MATERI = "nama_mat";
    public static final String KEY_KLS_TOTAL_PARTICIPANT = "jum_pst";


    //detail kelas
    public static final String KEY_DETAIL_KLS_ID = "id_detail_kls";

    //materi
    public static final String KEY_MAT_ID = "id_mat";
    public static final String KEY_MAT_NAMA = "nama_mat";

    //instruktur
    public static final String KEY_INS_ID = "id_ins";
    public static final String KEY_INS_NAMA = "nama_ins";
    public static final String KEY_INS_EMAIL = "email_ins";
    public static final String KEY_INS_PHONE = "hp_ins";

    //instansi
    public static final String KEY_INSTANSI_ID = "id_instansi";
    public static final String KEY_INSTANSI_NAMA = "nama_instansi";
    public static final String KEY_INSTANSI_ALAMAT = "alamat_instansi";

    //flag JSON
    //general
    public static final String TAG_JSON_ARRAY = "result";

    //peserta
    public static final String TAG_JSON_PST_ID = "id_pst";
    public static final String TAG_JSON_PST_NAMA = "nama_pst";
    public static final String TAG_JSON_PST_EMAIL = "email_pst";
    public static final String TAG_JSON_PST_HP = "hp_pst";

    //kelas
    public static final String TAG_JSON_KLS_ID = "id_kls";
    public static final String TAG_JSON_KLS_ENTRY_DATE = "tgl_mulai_kls";
    public static final String TAG_JSON_KLS_END_DATE = "tgl_akhir_kls";
    public static final String TAG_JSON_KLS_NAME_INSTRUKTUR = "nama_ins";
    public static final String TAG_JSON_KLS_NAME_MATERI = "nama_mat";
    public static final String TAG_JSON_KLS_TOTAL_PARTICIPANT = "jum_pst";

    //materi
    public static final String TAG_JSON_MAT_ID = "id_mat";
    public static final String TAG_JSON_MAT_NAMA = "nama_mat";

    //instruktur
    public static final String TAG_JSON_INS_ID = "id_ins";
    public static final String TAG_JSON_INS_NAMA = "nama_ins";
    public static final String TAG_JSON_INS_EMAIL = "email_ins";
    public static final String TAG_JSON_INS_HP = "hp_ins";

    //instansi
    public static final String TAG_JSON_INSTANSI_ID = "id_instansi";
    public static final String TAG_JSON_INSTANSI_NAMA = "nama_instansi";
    public static final String TAG_JSON_INSTANSI_NAMA_TEMP = "nama_instansi";
    public static final String TAG_JSON_INSTANSI_ALAMAT = "alamat_instansi";

    //detail kelas
    public static final String TAG_JSON_DETAIL_KELAS_ID = "id_detail_kls";

    //variable ID alias peserta
    public static final String PST_ID = "peserta_id";

    //variable ID alias kelas
    public static final String KLS_ID = "kelas_id";

    //variable ID alias instruktur
    public static final String INS_ID = "instruktur_id";

    //variable ID alias detail kelas
    public static final String DETAIL_KLS_ID = "detail_kelas_id";

    //variable ID alias materi
    public static final String MAT_ID = "materi_id";

    //variable ID alias instansi
    public static final String INSTANSI_ID = "instansi_id";

    public static final String KEY_PST_ID_INSTANSI = "id_instansi";
}
