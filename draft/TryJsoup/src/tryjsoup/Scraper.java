/*
alc
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tryjsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.Mahasiswa.Nilai;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.Semester;

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
    private final String LOGOUT_URL = BASE_URL + "home/index.logout.php";
    private final String KODE_FAK_FTIS = "7";
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
        init();
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
        //System.out.println(resp.cookies().toString());
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
        kuliahConn.data("kode_fak",KODE_FAK_FTIS);
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
    
    /*
        null jika jadwal belum keluar
    */
    public JadwalBundle requestJadwal() throws IOException{
        Connection jadwalConn = Jsoup.connect(BASE_URL+"includes/jadwal.aktif.php");
        jadwalConn.cookies(this.login_cookies);
        jadwalConn.timeout(0);
        jadwalConn.validateTLSCertificates(false); 
        jadwalConn.method(Connection.Method.GET);
        Response resp = jadwalConn.execute();
        Document doc = resp.parse();
        Elements jadwalTable = doc.select(".portal-full-table"); 
        JadwalBundle jadwalList = new JadwalBundle();
        
        /*Kuliah*/
        if(jadwalTable.size()>0){
           Elements tableKuliah = jadwalTable.get(0).select("tbody tr");
           String kode = new String(); 
           String nama = new String();
           for(Element elem : tableKuliah){
               if(elem.className().contains("row")){       
                    if(!(elem.child(1).text().isEmpty() && elem.child(2).text().isEmpty())){
                        kode = elem.child(1).text();
                        nama = elem.child(2).text();  
                    }  
                    MataKuliah currMk = MataKuliah.createMataKuliah(kode, Integer.parseInt(elem.child(3).text()), nama);
                    jadwalList.getJadwalKuliah().add(new JadwalKuliah(currMk,elem.child(4).text().charAt(0),elem.child(5).text(),elem.child(7).text(),elem.child(8).text(),elem.child(9).text()));
                    /*
                    System.out.print(kode + "/" + nama + "/" + elem.child(3).text()
                        +"/"+ elem.child(4).text() + "/" + elem.child(5).text()+"/"+ elem.child(7).text() + "/" + elem.child(8).text() + "/" + elem.child(9).text());
                    System.out.println("");
                     */
               }
            }
        }
        
        /*UTS*/
        if(jadwalTable.size()>1){
            Elements tableUTS = jadwalTable.get(1).select("tbody tr");
            for(Element elem : tableUTS){
               if(elem.className().contains("row")){     
                   MataKuliah currMk = MataKuliah.createMataKuliah(elem.child(1).text(), Integer.parseInt(elem.child(3).text()), elem.child(2).text());
                   jadwalList.getJadwalUTS().add(new JadwalUjian(currMk, elem.child(4).text().charAt(0), elem.child(5).text(), elem.child(6).text(), elem.child(7).text(), elem.child(8).text()));
                    /*System.out.print(kode + "/" + nama + "/" + elem.child(3).text()
                        +"/"+ elem.child(4).text() + "/" + elem.child(5).text()+"/"+ elem.child(6).text() + "/" + elem.child(7).text() + "/" + elem.child(8).text());
                    System.out.println("");*/
               }
            }
        }

        /*UAS*/
        if(jadwalTable.size()>2){
            Elements tableUAS = jadwalTable.get(2).select("tbody tr");
            for(Element elem : tableUAS){
               if(elem.className().contains("row")){     
                   MataKuliah currMk = MataKuliah.createMataKuliah(elem.child(1).text(), Integer.parseInt(elem.child(3).text()), elem.child(2).text());
                   jadwalList.getJadwalUAS().add(new JadwalUjian(currMk, elem.child(4).text().charAt(0), elem.child(5).text(), elem.child(6).text(), elem.child(7).text(), elem.child(8).text()));
                    /*System.out.print(kode + "/" + nama + "/" + elem.child(3).text()
                        +"/"+ elem.child(4).text() + "/" + elem.child(5).text()+"/"+ elem.child(6).text() + "/" + elem.child(7).text() + "/" + elem.child(8).text());
                    System.out.println("");*/
               }
            }
        }
        
        return jadwalList;
    }
    
    public void setNilai() throws IOException{  
        Connection nilaiConn = Jsoup.connect(NILAI_URL);
        nilaiConn.cookies(this.login_cookies);
        nilaiConn.data("npm",this.logged_mhs.getNpm());
        nilaiConn.data("thn_akd","ALL");
        nilaiConn.timeout(0);
        nilaiConn.validateTLSCertificates(false); 
        nilaiConn.method(Connection.Method.POST);
        Response resp = nilaiConn.execute();
        Document doc = resp.parse();
        Elements mk = doc.select("table");

        for(Element tb:mk){
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
    
}
