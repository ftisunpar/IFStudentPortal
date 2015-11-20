/*
alc
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.Mahasiswa.Nilai;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.MataKuliahFactory;
import models.id.ac.unpar.siamodels.Semester;
import models.id.ac.unpar.siamodels.TahunSemester;
import models.support.*;

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
    private final String BASE_URL = "https://studentportal.unpar.ac.id/";
    private final String LOGIN_URL = BASE_URL + "home/index.login.submit.php";
    private final String CAS_URL = "https://cas.unpar.ac.id/login";
    private final String ALLJADWAL_URL = BASE_URL + "includes/jadwal.all.php";
    private final String JADWAL_URL = BASE_URL + "includes/jadwal.aktif.php";
    private final String NILAI_URL = BASE_URL + "includes/nilai.sem.php";
    private final String LOGOUT_URL = BASE_URL + "home/index.logout.php";
    private TahunSemester currTahunSemester;
    private List<String> mkList;
    
    public List<String> getMkList(){
    	return this.mkList;
    }
    
    public String getSemester() {
        return currTahunSemester.getSemester() +" "+currTahunSemester.getTahun()+"/"+(currTahunSemester.getTahun()+1);
    }
    
    public void init() throws IOException{
        Connection baseConn = Jsoup.connect(BASE_URL);
        baseConn.timeout(0);
        baseConn.validateTLSCertificates(false);
        baseConn.method(Connection.Method.GET);
        baseConn.execute(); 
    }
    
    public CustomMahasiswa login(String npm, String pass) throws IOException{
        init();
    	CustomMahasiswa logged_mhs = new CustomMahasiswa(npm);	
        String user = logged_mhs.getEmailAddress();
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
        	Map<String,String> login_cookies = resp.cookies();
            doc = resp.parse();
            String nama = doc.select("p[class=student-name]").text();
            logged_mhs.setNama(nama);
            String curr_sem = doc.select(".main-info-semester a").text();
            String[] sem_set = this.parseSemester(curr_sem);
            Element photo = doc.select(".student-photo img").first();
            String photoPath = photo.absUrl("src"); 
            logged_mhs.setPhotoPath(photoPath);
            currTahunSemester = new TahunSemester(Integer.parseInt(sem_set[0]),Semester.fromString(sem_set[1]));
            this.requestKuliah(login_cookies);
            List<JadwalKuliah> jadwalList = this.requestJadwal(login_cookies);
            logged_mhs.setJadwalList(jadwalList);
            /*Periksa maba atau bukan*/
            if(!(logged_mhs.getTahunAngkatan()==currTahunSemester.getTahun()&&currTahunSemester.getSemester()==Semester.GANJIL)){
            	this.setNilai(login_cookies, logged_mhs);
            }  
            return logged_mhs;
        }       
        else{
            return null;
        }
    }
    
    public void requestKuliah(Map<String,String> login_cookies) throws IOException{
        Connection kuliahConn = Jsoup.connect(ALLJADWAL_URL);
        kuliahConn.cookies(login_cookies);
        kuliahConn.timeout(0);
        kuliahConn.validateTLSCertificates(false); 
        kuliahConn.method(Connection.Method.GET);
        Response resp = kuliahConn.execute();
        Document doc = resp.parse();
        Elements jadwal = doc.select("tr");
        String prev = "";
        mkList = new ArrayList<String>();
        for (int i = 1; i < jadwal.size()-1; i++) {
            Elements row = jadwal.get(i).children();
            if(!row.get(1).text().equals("")){
                String kode = row.get(1).text();
                String nama = row.get(2).text();
                String sks = row.get(3).text();
                if(!kode.equals(prev)){
                    MataKuliah curr = MataKuliahFactory.createMataKuliah(kode, Integer.parseInt(sks), nama);
                    mkList.add(curr.kode());
                }
                prev = kode;
            }   
        }    
    }
    
    public List<JadwalKuliah> requestJadwal(Map<String,String> login_cookies) throws IOException{
        Connection jadwalConn = Jsoup.connect(JADWAL_URL);
        jadwalConn.cookies(login_cookies);
        jadwalConn.timeout(0);
        jadwalConn.validateTLSCertificates(false); 
        jadwalConn.method(Connection.Method.GET);
        Response resp = jadwalConn.execute();
        Document doc = resp.parse();
        Elements jadwalTable = doc.select(".portal-full-table"); 
        List<JadwalKuliah> jadwalList = new ArrayList<JadwalKuliah>();
        
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
                    MataKuliah currMk = MataKuliahFactory.createMataKuliah(kode, Integer.parseInt(elem.child(3).text()), nama);
                    jadwalList.add(new JadwalKuliah(currMk,elem.child(4).text().charAt(0),elem.child(5).text(),elem.child(7).text(),elem.child(8).text(),elem.child(9).text()));
               }
            }
        }      
        return jadwalList;
    }
    
    public void setNilai(Map<String,String> login_cookies, Mahasiswa logged_mhs) throws IOException{  
        Connection nilaiConn = Jsoup.connect(NILAI_URL);
        nilaiConn.cookies(login_cookies);
        nilaiConn.data("npm",logged_mhs.getNpm());
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
            if(!((Integer.parseInt(thn)==currTahunSemester.getTahun())&&sem.equals(currTahunSemester.getSemester().toString()))){
                for(Element td:tr){           
                    if(td.className().contains("row")){
                      String kode = td.child(1).text();
                      int sks = Integer.parseInt(td.child(3).text());
                      String nama_mk = td.child(2).text();
                      MataKuliah curr_mk = MataKuliahFactory.createMataKuliah(kode, sks, nama_mk);
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
                      TahunSemester tahunSemesterNilai = new TahunSemester(Integer.parseInt(thn),Semester.fromString(sem));
                      logged_mhs.getRiwayatNilai().add(new Nilai(tahunSemesterNilai, curr_mk, kelas, ART, UTS, UAS, NA));
                    }
                }
            }
        }
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
