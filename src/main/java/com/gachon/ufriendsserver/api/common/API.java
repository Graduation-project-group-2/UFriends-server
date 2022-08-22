package com.gachon.ufriendsserver.api.common;

public class API {
    public static class CODE {
        public static final int SUCCESS = 200;

        public static final int PARAM_ERROR = 301;
        public static final int DB_ERROR = 302;
        public static final int NOT_FOUND_DATA = 303;
        public static final int DUPLICATE_DATA = 304;

        public static final int TOKEN_ERROR = 401;

        public static final int LOGIN_ERROR = 501;

        public static final int NAVER_LOGIN_ERROR = 601;

        public static final int UNKNOWN_ERROR = 1000;
    }

}
