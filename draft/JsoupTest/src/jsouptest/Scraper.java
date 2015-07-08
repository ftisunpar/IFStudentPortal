/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsouptest;

import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.Semester;
import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Herfan Heryandi
 */
public class Scraper {
    Map<String,String> login_cookies;
    
     public Connection init() throws IOException{
        Connection conn = Jsoup.connect("https://studentportal.unpar.ac.id/home/index.login.submit.php").data("Submit", "Login");
        conn.validateTLSCertificates(false);
        return conn;
    }
    
    public Connection login(String user, String pass) throws IOException{
        Connection main = init();
        Document doc = main.post();
        String lt = doc.select("input[name=lt]").val();
        String execution = doc.select("input[name=execution]").val();
        Connection conn = Jsoup.connect("https://cas.unpar.ac.id/login").cookies(main.response().cookies())
                .data("username",user).data("password", pass).data("lt", lt).data("execution", execution).data("_eventId","submit");
        conn.validateTLSCertificates(false);
        this.login_cookies = conn.response().cookies();
        return conn;
    }
     //KULIAH//
    public void initKuliah(int thn_akd, String sem_akd) throws IOException{
        Connection kuliah = Jsoup.
                connect("https://studentportal.unpar.ac.id/includes/jadwal.all.php").
                cookies(this.login_cookies).data("kode_fak","7","thn_akd",thn_akd+"","sem_akd",Semester.fromString(sem_akd)+"");
        kuliah.validateTLSCertificates(false); 
        Document doc = kuliah.get();
        Elements jadwal = doc.select("tr");
        String prev = "";
       
        for (int i = 1; i < jadwal.size()-1; i++) {
            Elements row = jadwal.get(i).children();
            if(!row.get(1).text().equals("")){
                String kode = row.get(1).text();
                String nama = row.get(2).text();
                String sks = row.get(3).text();
                if(!kode.equals(prev)){
                    //System.out.println(kode + " : " + nama + " -> "  + sks + " sks");
                    MataKuliah.createMataKuliah(kode, Integer.parseInt(sks), nama);
                }
                prev = kode;
            }   
        }    
    }
    
    public void getNilai() throws IOException{
        Connection nilai = Jsoup.
                connect("https://studentportal.unpar.ac.id/includes/nilai.ip.php").
                cookies(this.login_cookies);
        nilai.validateTLSCertificates(false); 
        Document doc = nilai.get();
        System.out.println(nilai.response().body());
    }
    
    public void getNilaiSem() throws IOException{
        Connection nilai = Jsoup.
                connect("https://studentportal.unpar.ac.id/includes/nilai.sem.php").
                cookies(this.login_cookies).data("npm","2012730012","thn_akd","2014","sem_akd","2");
        nilai.validateTLSCertificates(false); 
        Document doc = nilai.get();
        System.out.println(nilai.response().body());
    }
}
