package com.ohdocha.cu.kprojectcu.service;




import com.ohdocha.cu.kprojectcu.config.ErrorCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ServiceExtension implements ErrorCode {

    protected String createIdx() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String node = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return sdf.format(new Date(System.currentTimeMillis())) + "-" + node;
    }

}