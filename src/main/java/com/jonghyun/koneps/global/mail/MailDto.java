package com.jonghyun.koneps.global.mail;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailDto {
    String from;
    String to;
    String subject;
    String text;
}
