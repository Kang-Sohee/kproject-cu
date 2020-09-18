package com.ohdocha.cu.kprojectcu.config;

public interface ErrorCode {

    int PAGE_NOT_FOUND = 0;
    String PAGE_NOT_FOUND_MSG = "페이지를 찾을 수 없습니다.";

    int ADMIN_ID_IS_NOT_EXIST = 1000;
    String ADMIN_ID_IS_NOT_EXIST_MSG = "존재하지 않는 아이디 입니다.";

    int ADMIN_PASSWORD_MISMATCH = 1010;
    String ADMIN_PASSWORD_MISMATCH_MSG = "패스워드가 일치하지 않습니다.";

    int UNKNOWN_EXCEPTION = 9998;
    String UNKNOWN_EXCEPTION_MSG = "내부 서버 오류 입니다.";

}
