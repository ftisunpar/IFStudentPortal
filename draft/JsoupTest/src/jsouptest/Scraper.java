/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsouptest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.Mahasiswa.Nilai;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.Semester;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Herfan Heryandi
 */
public class Scraper {
    private Map<String,String> login_cookies;
    private final String BASE_URL = "https://studentportal.unpar.ac.id/";
    private final String LOGIN_URL = BASE_URL + "home/index.login.submit.php";
    private final String CAS_URL = "https://cas.unpar.ac.id/login";
    private final String ALLJADWAL_URL = BASE_URL + "includes/jadwal.all.php";
    private final String NILAI_URL = BASE_URL + "includes/nilai.sem.php";
    private final String DPS_URL = BASE_URL + "includes/nilai.dps.php";
    private final String LOGOUT_URL = BASE_URL + "home/index.logout.php";
    private final int KODE_FAK = 7;
    private String thn_akd, sem_akd;
    private Mahasiswa logged_mhs; 

    public Mahasiswa getLoggedMahasiswa() {
        return logged_mhs;
    }
    
    public void init() throws IOException{
        Connection baseConn = Jsoup.connect(BASE_URL);
        baseConn.timeout(0);
        baseConn.validateTLSCertificates(false);
        baseConn.method(Connection.Method.GET);
        baseConn.execute(); 
    }
    
    public boolean login(String npm, String pass) throws IOException{
        this.logged_mhs = new Mahasiswa(npm);
        String user = this.logged_mhs.getEmailAddress();
        Connection conn = Jsoup.connect(LOGIN_URL);
        conn.data("Submit", "Login");
        conn.timeout(0);
        conn.validateTLSCertificates(false);
        conn.method(Connection.Method.POST);
        Response resp = conn.execute();
        Document doc = resp.parse();
        String lt = doc.select("input[name=lt]").val();
        String execution = doc.select("input[name=execution]").val();
        /*CAS LOGIN*/
        Connection loginConn = Jsoup.connect(CAS_URL);
        loginConn.cookies(resp.cookies());
        loginConn.data("username",user);
        loginConn.data("password", pass);
        loginConn.data("lt", lt);
        loginConn.data("execution", execution);
        loginConn.data("_eventId","submit");
        loginConn.timeout(0);
        loginConn.validateTLSCertificates(false);
        loginConn.method(Connection.Method.GET);
        resp = loginConn.execute();
        if(resp.body().contains("Data Akademik")){
            this.login_cookies = resp.cookies();
            doc = resp.parse();
            String nama = doc.select("p[class=student-name]").text();
            this.logged_mhs.setNama(nama);
            String curr_sem = doc.select(".main-info-semester a").text();
            String[] sem_set = this.parseSemester(curr_sem);
            this.thn_akd = sem_set[0];
            this.sem_akd = sem_set[1];
            return true;
        }       
        else{
            return false;
        }
    }
    
    public ArrayList<String> requestKuliah() throws IOException{
        Connection kuliahConn = Jsoup.connect(ALLJADWAL_URL);
        kuliahConn.cookies(this.login_cookies);
        kuliahConn.data("kode_fak",KODE_FAK+"");
        kuliahConn.data("thn_akd",this.thn_akd);
        kuliahConn.data("sem_akd",Semester.fromString(this.sem_akd)+"");
        kuliahConn.timeout(0);
        kuliahConn.validateTLSCertificates(false); 
        kuliahConn.method(Connection.Method.GET);
        Response resp = kuliahConn.execute();
        Document doc = resp.parse();
        Elements jadwal = doc.select("tr");
        String prev = "";
        ArrayList<String> mkList = new ArrayList<String>();
        for (int i = 1; i < jadwal.size()-1; i++) {
            Elements row = jadwal.get(i).children();
            if(!row.get(1).text().equals("")){
                String kode = row.get(1).text();
                String nama = row.get(2).text();
                String sks = row.get(3).text();
                if(!kode.equals(prev)){
                    //
                    //System.out.println(kode + " : " + nama + " -> "  + sks + " sks");
                    MataKuliah curr = MataKuliah.createMataKuliah(kode, Integer.parseInt(sks), nama);
                    mkList.add(curr.getKode());
                }
                prev = kode;
            }   
        }    
        return mkList;
    }
    
    public void setNilai() throws IOException{  
        Connection nilai = Jsoup.
                connect(NILAI_URL).
                cookies(this.login_cookies);
        nilai.data("npm",this.logged_mhs.getNpm(),"thn_akd","ALL");
        nilai.timeout(0).validateTLSCertificates(false); 
        nilai.method(Connection.Method.POST);
        
        Document doc = nilai.execute().parse();
        Elements mk = doc.select("table");

        for(Element tb:mk){
            //String curr_sem = tb.select(".table_color3 th").text();
            //System.out.println(curr_sem);
            Elements tr = tb.select("tr");
            String[] sem_set = this.parseSemester(tr.get(0).text());
            String thn = sem_set[0];
            String sem = sem_set[1];
            if(!(thn.equals(this.thn_akd)&&sem.equals(this.sem_akd))){
                for(Element td:tr){           
                    if(td.className().contains("row")){
                      String kode = td.child(1).text();
                      int sks = Integer.parseInt(td.child(3).text());
                      String nama_mk = td.child(2).text();
                      MataKuliah curr_mk = MataKuliah.createMataKuliah(kode, sks, nama_mk);
                      char kelas = td.child(4).text().charAt(0);
                      double ART = 0;
                      double UTS = 0;
                      double UAS = 0;
                      if(kelas!='*'){
                        ART = Double.parseDouble(td.child(5).text());
                        UTS = Double.parseDouble(td.child(6).text());
                        UAS = Double.parseDouble(td.child(7).text());
                      }
                      char NA = td.child(9).text().charAt(0);
                      this.logged_mhs.getRiwayatNilai().add(new Nilai(Integer.parseInt(thn), Semester.fromString(sem), curr_mk, kelas, ART, UTS, UAS, NA));
                    }
                }
            }
        }
        //
        //System.out.println(this.logged_mhs.calculateIPKLulus());
    }
    
    public void logout() throws IOException{
        Connection logoutConn = Jsoup.connect(LOGOUT_URL);
        logoutConn.timeout(0);
        logoutConn.validateTLSCertificates(false);
        logoutConn.method(Connection.Method.GET);
        logoutConn.execute();
    }
    
    private String[] parseSemester(String sem_raw){
         String[] sem_set = sem_raw.split("/")[0].split("-");
         return new String[]{sem_set[1].trim(),sem_set[0].trim()};
    } 
    
    /*public void getNilaiSem() throws IOException{
        Connection session = Jsoup.connect(BASE_URL.concat("includes/session.check.php?npm=npm"))
                .cookies(login_cookies);
        session.timeout(0).validateTLSCertificates(false);
        session.execute();
        Connection nilai = Jsoup.
                connect("https://studentportal.unpar.ac.id/includes/nilai.sem.php").
                cookies(this.login_cookies).data("npm","2012730012","thn_akd","2014","sem_akd","2");
        nilai.validateTLSCertificates(false); 
        Document doc = nilai.get();
    }*/
}
