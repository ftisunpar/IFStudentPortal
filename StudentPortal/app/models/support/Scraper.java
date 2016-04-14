package models.support;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.ac.unpar.siamodels.Dosen;
import id.ac.unpar.siamodels.JadwalKuliah;
import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.Mahasiswa.Nilai;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.MataKuliahFactory;
import id.ac.unpar.siamodels.Semester;
import id.ac.unpar.siamodels.TahunSemester;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
    private final String BASE_URL = "https://studentportal.unpar.ac.id/";
    private final String LOGIN_URL = BASE_URL + "home/index.login.submit.php";
    private final String CAS_URL = "https://cas.unpar.ac.id/login";
    private final String ALLJADWAL_URL = BASE_URL + "includes/jadwal.all.php";
    private final String JADWAL_URL = BASE_URL + "includes/jadwal.aktif.php";
    private final String NILAI_URL = BASE_URL + "includes/nilai.sem.php";
    private final String LOGOUT_URL = BASE_URL + "home/index.logout.php";
    private TahunSemester currTahunSemester;
    private List<MataKuliah> mkList;
    private Map<String,String> login_cookies;
    private Document doc;
    private Mahasiswa logged_mhs;
    
    public List<MataKuliah> getMkList(){
    	return this.mkList;
    }
    
    public void init() throws IOException{
        Connection baseConn = Jsoup.connect(BASE_URL);
        baseConn.timeout(0);
        baseConn.validateTLSCertificates(false);
        baseConn.method(Connection.Method.GET);
        baseConn.execute(); 
    }
    
    public Map<String,String> login(String npm, String pass) throws IOException{
        init();
    	logged_mhs = new Mahasiswa(npm);	
        String user = logged_mhs.getEmailAddress();
        Connection conn = Jsoup.connect(LOGIN_URL);
        conn.data("Submit", "Login");
        conn.timeout(0);
        conn.validateTLSCertificates(false);
        conn.method(Connection.Method.POST);
        Response resp = conn.execute();
        doc = resp.parse();
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
        	login_cookies = resp.cookies();
            doc = resp.parse();
            return login_cookies;
        }       
        else{
            return null;
        }
    }
    
    public Document getDoc(){
    	return doc;
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
        mkList = new ArrayList<MataKuliah>();
        for (int i = 1; i < jadwal.size()-1; i++) {
            Elements row = jadwal.get(i).children();
            if(!row.get(1).text().equals("")){
                String kode = row.get(1).text();
                String nama = row.get(2).text();
                String sks = row.get(3).text();
                if(!kode.equals(prev)){
                    MataKuliah curr = MataKuliahFactory.getInstance().createMataKuliah(kode, Integer.parseInt(sks), nama);
                    mkList.add(curr);
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
                    MataKuliah currMk = MataKuliahFactory.getInstance().createMataKuliah(kode, Integer.parseInt(elem.child(3).text()), nama);
                    try {
	                    String kelasString = elem.child(4).text();
	                    String hariString = elem.child(7).text(); 
	                    String waktuString = elem.child(8).text();
	                    if (hariString != null & hariString.length() != 0 && waktuString != null & waktuString.length() != 0) {
	                        jadwalList.add(new JadwalKuliah(currMk,kelasString.length() == 0 ? null : kelasString.charAt(0),new Dosen(null, elem.child(5).text()), hariString, waktuString,elem.child(9).text()));                    	
	                    }
                    } catch (IndexOutOfBoundsException e) {
                    	// void. do not add jadwal.
                    }
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
            
            for(Element td:tr){           
                if(td.className().contains("row")){
                  String kode = td.child(1).text();
                  int sks = Integer.parseInt(td.child(3).text());
                  String nama_mk = td.child(2).text();
                  MataKuliah curr_mk = MataKuliahFactory.getInstance().createMataKuliah(kode, sks, nama_mk);
                  Character kelas, NA;
                  Double ART, UTS, UAS;
                  try {
                	  kelas = td.child(4).text().charAt(0);
                  } catch (IndexOutOfBoundsException e) {
                	  kelas = null;
                  }
                  try {
                	  ART = Double.valueOf(td.child(5).text());
                  } catch (NumberFormatException e) {
                	  ART = null;
                  }
                  try {
                	  UTS = Double.valueOf(td.child(6).text());
                  } catch (NumberFormatException e) {
                	  UTS = null;
                  }
                  try {
                	  UAS = Double.valueOf(td.child(7).text());
                  } catch (NumberFormatException e) {
                	  UAS = null;
                  }
                  try {
                	  NA = td.child(9).text().charAt(0);
                  } catch (IndexOutOfBoundsException e) {
                	  NA = null;
                  }

                  if(NA != null) {
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
    
    public String[] parseSemester(String sem_raw){
         String[] sem_set = sem_raw.split("/")[0].split("-");
         return new String[]{sem_set[1].trim(),sem_set[0].trim()};
    }    
}
