package com.sqld_board.sqld;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JasyptTest {

    public static void main(String[] args){
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
//
//        // Dev 설정
//        jasypt.setPassword("fKsaouZeRD0WkEFIR9hyRehaDxSUkHOptveAdEc9Y7c="); // DEV 용 마스터키
//        jasypt.setAlgorithm("PBEWithMD5AndDES"); // 기본 알고리즘

        //redis 설정
//        String redisPass = "Knou200634!!@";
//        System.out.println("Encrypted Redis Pass: " + jasypt.encrypt(redisPass));

//        String devDbPass = "992035Sec+_)bok"; // DEV DB PASS
//        String mailPw = "gckrfaaa zkvlhmhh"; // 실제 메일 비번
//
//        System.out.println("devDbPass :" + jasypt.encrypt(devDbPass));
//        System.out.println("mailPw :" + jasypt.encrypt(mailPw));

//         Prod 설정
        jasypt.setPassword("fKsaouZeRD0WkEFIR9hyRehaDxSUkHOptveAdEc9Y7c="); //PROD 용 DB 마스터키
        jasypt.setAlgorithm("PBEWithMD5AndDES"); // 기본 알고리즘

        //redis 설정
        String redisPass = "Knou200634!!@";
        System.out.println("Encrypted Redis Pass: " + jasypt.encrypt(redisPass));
//
        String prodDbPass = "992035Sec+_)"; // PROD PASS
        String mailpW = "gckrfaaa zkvlhmhh"; //실제 메일 비번
        System.out.println("prodDbPass :" + jasypt.encrypt(prodDbPass));
        System.out.println("mailPw :" + jasypt.encrypt(mailpW));

    }
}
