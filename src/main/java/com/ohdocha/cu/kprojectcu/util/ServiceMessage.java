package com.ohdocha.cu.kprojectcu.util;

public class ServiceMessage extends DataMessage {

    @Override
    public ServiceMessage addData(String key, Object object) {
        super.addData(key, object);
        return this;
    }

//    public ServiceMessage setLoginMember(AdminInfoDto member) {
//        addData("loginMemberSession", member);
//        return this;
//    }

    public ServiceMessage setResult(Object object) {
        put("result", object);
        return this;
    }

    public Object getResult() {
        return get("result");
    }

//    public AdminInfoDto getLoginMember() {
//        return getObject("loginMemberSession", AdminInfoDto.class);
//    }

}
