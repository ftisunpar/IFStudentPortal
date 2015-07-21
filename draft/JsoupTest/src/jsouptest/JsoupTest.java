/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsouptest;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author ADMIN7
 */
public class JsoupTest {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Scraper scrap = new Scraper();
        
        Mahasiswa herfan = new Mahasiswa("2012730012");
        String user = herfan.getEmailAddress();
        String pass = "";
        
        Connection login_conn = scrap.login(user,pass);        
        Document login_doc =  login_conn.post();
        String nama = login_doc.select("p[class=student-name]").text();
        herfan.setNama(nama);
     
//        scrap.initKuliah(2015, "GANJIL");
        scrap.getNilai();
        //int sks = herfan.calculateSKSLulus();

    }
 
}
