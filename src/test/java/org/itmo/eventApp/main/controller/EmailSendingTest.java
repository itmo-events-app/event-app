package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.mail.MailSenderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailSendingTest extends AbstractTestContainers{
    @Autowired
    private MailSenderServiceImpl mailSenderServiceImpl;

//    private GreenMailExtension

}
