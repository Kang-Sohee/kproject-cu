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

    int IMAGE_NOT_MULTIPART_FILE = 1010;
    String IMAGE_NOT_MULTIPART_FILE_MSG = "Multipart File 이 아닙니다.";

    int IMAGE_IS_EMPTY = 1011;
    String IMAGE_IS_EMPTY_MSG = "이미지 파일이 없습니다.";

    int IMAGE_PARSING_ERROR = 1012;
    String IMAGE_PARSING_ERROR_MSG = "이미지 파일 분석 중 오류가 발생했습니다. ";

    int IMAGE_PERMISSION_ERROR = 1013;
    String IMAGE_PERMISSION_ERROR_MSG = "이미지를 읽기 위한 권한이 존재하지 않습니다.";
    
}
