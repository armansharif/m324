package com.pa.commons;

public class Routes {

    public final static String POST_user_verify_mobile = "/verify/mobile";
    public final static String POST_user_auth_mobile = "/auth/mobile";

    public final static String GET_location_city = "/location/city/{stateId}";
    public final static String GET_location_district = "/location/district/{cityId}";
    public final static String GET_location_state = "/location/state";

    public final static String GET_profile = "/profile";

    public final static String GET_news_post = "/news/post";
    public final static String POST_news_post_like = "/news/post/like/{postId}";
    public final static String POST_news_post_dislike = "/news/post/dislike/{postId}";
    public final static String POST_news_post_double_like = "/news/post/doubleLike/{postId}";
    public final static String POST_news_post_double_dislike = "/news/post/doubleDislike/{postId}";

    public final static String GET_news_category = "/news/category";


    public final static String GET_committee= "/committee";
    public final static String POST_committee_request= "/committee/request";
    public final static String GET_committee_requests= "committee/requests";

    public final static String POST_committee_request_accept= "/committee/request/accept";
    public final static String POST_committee_request_reject= "/committee/request/reject";
    public final static String POST_committee_request_remove= "/committee/request/remove";
    public final static String GET_notifications= "/notifications";
    public final static String POST_notification_read= "/notification/read/{id}";

    public final static String PUT_profile = "/profile";

    public final static String GET_admin_tickets  = "/admin/tickets";
    public final static String GET_tickets= "/tickets";
    public final static String GET_tickets_add= "/tickets/add";
    public final static String GET_tickets_close= "/tickets/close/{id}";
    public final static String GET_tickets_response= "/tickets/response/{id}";


    //--------------------------------------------
    public final static String GET_users_admin = "/admin/users";
    public final static String GET_users_by_id = "/users/{id}";

    public final static String POST_user_auth_email = "/auth/email";

    public final static String POST_forget_pass_email = "/forgetPass/email";
    public final static String POST_forget_pass_mobile = "/forgetPass/mobile";

    public final static String POST_user_verify_email = "/verify/email";

    public final static String POST_reset_pass_email = "/resetPass/mobile";

    public final static String POST_reset_pass_mobile = "/resetPass/email";

    public final static String POST_login = "/login";
    public final static String POST_admin_login = "/admin/login";
    public final static String POST_admin_users = "/admin/users";

    public final static String Get_damdari_dams = "/damdari/{damdariId}";
    public final static String Get_damdari = "/damdari";
    public final static String Get_dam = "/dam/{damId}";
    public final static String Get_dams = "/dam";
    public final static String Get_dams_hasProblem_damdariId = "/damHasProblem/{damdariId}";
    public final static String Get_dams_hasProblem = "/damHasProblem";
    public final static String Get_dam_status = "/damStatus/{damId}";
    public final static String POST_dam_status = "/damStatus/{damId}";
    public final static String POST_dam_add = "/dam";
    public final static String DELETE_dam_delete = "/dam/{id}";

    public final static String PUT_dam_edit = "/dam/{id}";

    public final static String Get_dashboard= "/dashboard";
    public final static String Get_dashboard_damdariId= "/dashboard/{damdariId}";
}
