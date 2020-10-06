package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.util.ServiceMessage;
import com.ohdocha.cu.kprojectcu.util.TextUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ControllerExtension {

    public ControllerExtension() {
    }

    public ServiceMessage createServiceMessage(HttpServletRequest request) {
        ServiceMessage serviceMessage = new ServiceMessage();
//        serviceMessage.setLoginMember(getLoginMember(request));

        String realIp = request.getHeader("X-Real-IP");
        if (TextUtils.isEmpty(realIp))
            realIp = request.getRemoteAddr();

        serviceMessage
                .addData("ipAddress", realIp)
                .addData("userAgent", request.getHeader("user-agent"));

        return serviceMessage;
    }

//    public AdminInfoDto getLoginMember(HttpServletRequest request) {
//        Object memberObject = request.getSession().getAttribute("LOGIN_SESSION");
//        if (memberObject instanceof AdminInfoDto) return (AdminInfoDto) memberObject;
//        else return null;
//    }

    String getRequestParam(HttpServletRequest request, String key) {
        return TextUtils.clear(request.getParameter(key));
    }

    String[] getRequestListParam(HttpServletRequest request, String key) {
        String[] stringList = request.getParameterValues(key);
        try {
            return stringList;
        } catch (Exception e) {
            return stringList = null;
        }
    }

    int getRequestIntParam(HttpServletRequest request, String key) {
        return getRequestIntParam(request, key, 0);
    }

    int getRequestIntParam(HttpServletRequest request, String key, int defaultValue) {
        String temp = getRequestParam(request, key);
        try {
            return Integer.parseInt(temp);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    long getRequestLongParam(HttpServletRequest request, String key) {
        String temp = getRequestParam(request, key);
        try {
            return Long.parseLong(temp);
        } catch (Exception e) {
            return 0L;
        }
    }

    double getRequestDoubleParam(HttpServletRequest request, String key) {
        return getRequestDoubleParam(request, key, 0.0);
    }

    double getRequestDoubleParam(HttpServletRequest request, String key, double defaultValue) {
        String temp = getRequestParam(request, key);
        try {
            return Double.parseDouble(temp);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    void sendRedirect(HttpServletResponse response, String redirectUrl) {
        try {
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("Redirect Error: ", e);
        }
    }
}
