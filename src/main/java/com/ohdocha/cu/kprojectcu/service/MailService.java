package com.ohdocha.cu.kprojectcu.service;


import com.ohdocha.cu.kprojectcu.domain.MailDto;

public interface MailService {
    public void sendEmail(MailDto mail);
}