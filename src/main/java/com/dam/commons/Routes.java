package com.dam.commons;

public class Routes {

    public final static String GET_users_admin = "/admin/users";
    public final static String GET_users_by_id = "/users/{id}";
    public final static String PUT_profile = "/profile";

    public final static String GET_profile = "/profile";

    public final static String POST_user_auth_mobile = "/auth/mobile";

    public final static String POST_user_auth_email = "/auth/email";

    public final static String POST_forget_pass_email = "/forgetPass/email";
    public final static String POST_forget_pass_mobile = "/forgetPass/mobile";

    public final static String POST_user_verify_email = "/verify/email";

    public final static String POST_user_verify_mobile = "/verify/mobile";


    public final static String POST_reset_pass_email = "/resetPass/mobile";

    public final static String POST_reset_pass_mobile = "/resetPass/email";

    public final static String POST_login = "/login";
    public final static String POST_admin_login = "/admin/login";
    public final static String POST_admin_users = "/admin/users";

    public final static String Get_damdari_dams = "/damdari/{damdariId}";
    public final static String Get_damdari = "/damdari";
    public final static String Get_dam = "/dam/{damId}";
    public final static String Get_dams = "/dam";

    public final static String Get_dam_status = "/damStatus/{damId}";
    public final static String POST_dam_status = "/damStatus/{damId}";
    public final static String POST_dam_add = "/dam";
    public final static String DELETE_dam_delete = "/dam/{id}";

    public final static String PUT_dam_edit = "/dam/{id}";

}
