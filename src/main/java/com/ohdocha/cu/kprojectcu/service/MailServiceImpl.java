package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.MailDto;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/*
 * 사용법
 * Mail mail = new Mail();
   mail.setMailFrom("woosung.lee@docha.com");
   mail.setMailTo("woosung.lee@docha.com");
   mail.setMailSubject("이메일인증안내 - 카썸모빌리티");
   
   Map < String, Object > model = new HashMap < String, Object > ();
   model.put("userName", "홍길동");
   model.put("userId", "woosung.lee@docha.com");
   model.put("authNumber", "1234");
   mail.setModel(model);
   
   AbstractApplicationContext context = new AnnotationConfigApplicationContext(EMailConfig.class);
   
   mailService.sendEmail(mail);
   context.close();
  
   todo. 이미지는 톰캣에 설정 후에 편집해주어야한다.
   
 * */

@Service("mailService")
public class MailServiceImpl implements MailService {

    JavaMailSender mailSender;

    VelocityEngine velocityEngine;

    public void sendEmail(MailDto mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(mail.getMailFrom());
            mimeMessageHelper.setTo(mail.getMailTo());
            mail.setMailContent(geContentFromTemplate(mail.getModel()));
            mimeMessageHelper.setText(mail.getMailContent(), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String geContentFromTemplate(Map < String, Object > model) {
        StringBuffer content = new StringBuffer();
        try {
//            content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/email-template.vm", model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }



}