/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.Semester;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Herfan Heryandi
 */
public class Scraper {
    private Map<String,String> login_cookies;
    private final String BASE_URL = "https://studentportal.unpar.ac.id/";
    private final String INIT_URL = BASE_URL + "home/index.login.submit.php";
    private final String LOGIN_URL = "https://cas.unpar.ac.id/login";
    private final String ALLJADWAL_URL = BASE_URL + "includes/jadwal.all.php";
    private final String DPS_URL = BASE_URL + "includes/nilai.dps.php";
    
     public Connection init() throws IOException{
        Connection conn = Jsoup.connect(INIT_URL).data("Submit", "Login");
        conn.timeout(0).validateTLSCertificates(false);
        return conn;
    }
    
    public Connection login(String user, String pass) throws IOException{
        Connection main = init();
        Document doc = main.post();
        String lt = doc.select("input[name=lt]").val();
        String execution = doc.select("input[name=execution]").val();
        Connection conn = Jsoup.connect(LOGIN_URL).cookies(main.response().cookies())
                .data("username",user).data("password", pass).data("lt", lt).data("execution", execution).data("_eventId","submit");
        conn.timeout(0).validateTLSCertificates(false);
        this.login_cookies = conn.response().cookies();
        return conn;
    }
    
    public ArrayList<MataKuliah> requestKuliah(int thn_akd, String sem_akd) throws IOException{
        Connection kuliah = Jsoup.
                connect(ALLJADWAL_URL).
                cookies(this.login_cookies).data("kode_fak","7","thn_akd",thn_akd+"","sem_akd",Semester.fromString(sem_akd)+"");
        kuliah.timeout(0).validateTLSCertificates(false); 
        Document doc = kuliah.get();
        Elements jadwal = doc.select("tr");
        String prev = "";
        ArrayList<MataKuliah> mkList = new ArrayList<MataKuliah>();
        for (int i = 1; i < jadwal.size()-1; i++) {
            Elements row = jadwal.get(i).children();
            if(!row.get(1).text().equals("")){
                String kode = row.get(1).text();
                String nama = row.get(2).text();
                String sks = row.get(3).text();
                if(!kode.equals(prev)){
                    //System.out.println(kode + " : " + nama + " -> "  + sks + " sks");
                    MataKuliah curr = MataKuliah.createMataKuliah(kode, Integer.parseInt(sks), nama);
                	mkList.add(curr);
                }
                prev = kode;
            }   
        }    
        return mkList;
    }
    
    public void getNilai() throws IOException{  
        Connection session = Jsoup.connect(BASE_URL.concat("includes/session.check.php?npm=npm"))
                .cookies(login_cookies);
        session.timeout(0).validateTLSCertificates(false);
        session.execute();
        System.out.println(session.response().body());
        Connection nilai = Jsoup.
                connect(DPS_URL).
                cookies(this.login_cookies);
        nilai.timeout(0).validateTLSCertificates(false); 
        Document doc = nilai.get();
        //System.out.println(nilai.response().body());
    }
    
    public void getNilaiSem() throws IOException{
        Connection session = Jsoup.connect(BASE_URL.concat("includes/session.check.php?npm=npm"))
                .cookies(login_cookies);
        session.timeout(0).validateTLSCertificates(false);
        session.execute();
        System.out.println(session.response().body());
        Connection nilai = Jsoup.
                connect("https://studentportal.unpar.ac.id/includes/nilai.sem.php").
                cookies(this.login_cookies).data("npm","2012730012","thn_akd","2014","sem_akd","2");
        nilai.validateTLSCertificates(false); 
        Document doc = nilai.get();
        //System.out.println(nilai.response().body());
    }
}
