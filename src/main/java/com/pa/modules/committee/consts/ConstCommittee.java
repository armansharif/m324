package com.pa.modules.committee.consts;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ConstCommittee {

    public static final String PERFIX_SETAD_REF = "REF_";  //پیش نویس
    public static final int COMMITTEE_STATUS_DRAFT = 0;  //پیش نویس
    public static final int COMMITTEE_STATUS_ACCEPT = 1; //تایید شده

    public static final int COMMITTEE_STATUS_REJECT = 2; //رد شده

    public static final int USER_EDUCATION_PHD =4;
    public static final int USER_EDUCATION_MASTER =3;
    public static final int USER_EDUCATION_BACHELOR =2;
    public static final int USER_EDUCATION_OTHER =1;

    public static final Map<Integer, Integer> SCORE_USER_EDUCATION = ImmutableMap.of(
            4, 20,
            3, 10,
            2, 5,
            1, 0
    );


    public static final int USER_UNIVERSITY_DOLATI =1;
    public static final int USER_UNIVERSITY_AZAD =2;
    public static final int USER_UNIVERSITY_GHE =3;
    public static final int USER_UNIVERSITY_OTHER =4;

    public static final Map<Integer, Integer> SCORE_USER_UNIVERSITY = ImmutableMap.of(
            1, 10,
            2, 7,
            3, 5,
            4, 2
    );


    public static final int USER_REASON_INTEREST =1;
    public static final int USER_REASON_ACADEMIC =2;
    public static final int USER_REASON_EXPERIENCE =3;
    public static final int USER_REASON_ALL =4;

    public static final Map<Integer, Integer> SCORE_USER_REASON = ImmutableMap.of(
            1, 7,
            2, 10,
            3, 8,
            4, 25
    );

    public static final int USER_GPA_LOWER_15 =3;
    public static final int USER_GPA_BETWEEN_15_17 =2;
    public static final int USER_GPA_UPPER_17 =1;


    public static final Map<Integer, Integer> SCORE_USER_GPA = ImmutableMap.of(
            1, 10,
            2, 7,
            3, 4
    );

    public static final int SCORE_USER_FACILITY_MEMBERSHIP = 20;
    public static final int SCORE_USER_ELITE_MEMBERSHIP = 20;
    public static final int SCORE_USER_PER_AUTHORED_BOOK = 5;
    public static final int SCORE_USER_PER_TRANSLATED_BOOK = 3;
    public static final int SCORE_USER_PER_ARTICLE = 6;
    public static final int SCORE_USER_PER_WORK_EXPERIENCE_YEAR= 3;
}
