package controllers;

import java.io.IOException;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import models.display.JadwalDisplay;
import models.display.PrasyaratDisplay;
import models.display.RingkasanDisplay;
import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.Mahasiswa.Nilai;
import id.ac.unpar.siamodels.MataKuliahFactory;
import id.ac.unpar.siamodels.Semester;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.support.Scraper;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import play.Logger;

public class Application extends Controller {
	Scraper scrap = new Scraper();
	public String email;
	public String remote;
	
	Map<String,Mahasiswa> mahasiswaList = new HashMap<String,Mahasiswa>();
	
    public Result index() {
    	if(session("npm")==null){
    		return ok(views.html.login.render(""));
    	}
    	else{
    		return home();
    	}
    }
    
    public Result login() {
    	if(session("npm")==null){
    		return index();
    	}
    	else{
    		return home();
    	}
    }
    
    public Result submitLogin() throws IOException{
    	this.remote = request().remoteAddress();
    	
    	String errorHtml = 
    	"<div class='alert alert-danger' role='alert'>" +
    	  "<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"+
    	  "<span class='sr-only'>Error:</span>";
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	
    	this.email = dynamicForm.get("email");
    	String pass = dynamicForm.get("pass");
    	
    	if(!email.matches("[0-9]{7}+@student.unpar.ac.id")){
    		Logger.info("User: " + email+" gagal login dari IP " + remote + " karena salah input E-Mail");
    		return ok(views.html.login.render(errorHtml+ "Email tidak valid" + "</div>"));
    	}
    	if(!(email.charAt(0)=='7'&&email.charAt(1)=='3')){
    		Logger.info("User: " + email+" gagal login dari IP " + remote +  " karena bukan mahasiswa teknik informatika");
    		return ok(views.html.login.render(errorHtml+"Maaf, Anda bukan mahasiswa teknik informatika"+ "</div>"));
    	}
    	String npm = "20" + email.substring(2,4) + email.substring(0,2)+ "0" + email.substring(4,7);
    	Mahasiswa login_mhs = this.scrap.login(npm, pass);
    	if(login_mhs!=null){
    		Logger.info("User "+ email+ " berhasil login dari IP: "+ remote );
    		session("npm", npm);
    		mahasiswaList.put(session("npm"), login_mhs);
    		return home();
    	}
	    else{
	    	Logger.info("User: " + email+" gagal login dari IP " + remote + " karena input password salah atau anda bukan mahasiswa aktif");
	    	return ok(views.html.login.render(errorHtml+"Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif"+ "</div>"));
	    }
    }
    
    public Result home() {
    	
    	if(session("npm") == null || !mahasiswaList.containsKey(session("npm"))) {
    		session().clear();
    		return index();
    	}
    	else{
    		Logger.info("User " + email +" mengakses halaman home dari IP "+ remote);
    		return ok(views.html.home.render(mahasiswaList.get(session("npm"))));	
    	}
    }
    
    public Result prasyarat() throws IOException{
    	if(session("npm") == null || !mahasiswaList.containsKey(session("npm"))) {
    		session().clear();
    		return index();
    	}
    	else if(mahasiswaList.get(session("npm")).getRiwayatNilai().size()==0){
    		List<PrasyaratDisplay> table = null;
	    	String semester = scrap.getSemester();
	    	Logger.info("User " + email +" mengakses halaman prasyarat dari IP "+ remote);
         	return ok(views.html.prasyarat.render(table,semester));
    	}
    	else{
	    	List<PrasyaratDisplay> table = checkPrasyarat();
	    	String semester = scrap.getSemester();
	    	Logger.info("User " + email +" mengakses halaman prasyarat dari IP "+ remote);
   	    	return ok(views.html.prasyarat.render(table,semester));
    	}
    }
    
    public Result jadwalKuliah() throws IOException{
    	if(session("npm") == null || !mahasiswaList.containsKey(session("npm"))) {
    		session().clear();
    		return index();
    	}
    	else{
			JadwalDisplay table = new JadwalDisplay(mahasiswaList.get(session("npm")).getJadwalKuliahList());
			String semester = scrap.getSemester();
			Logger.info("User " + email +" mengakses halaman jadwal kuliah dari IP "+ remote);
   	    	return ok(views.html.jadwalKuliah.render(table,semester));
    	}
    }

    public Result ringkasan() throws IOException{
    	if(session("npm") == null || !mahasiswaList.containsKey(session("npm"))) {
    		session().clear();
    		return index();
    	}
    	else if(mahasiswaList.get(session("npm")).getRiwayatNilai().size()==0){
    		RingkasanDisplay display  = null;;
    		Logger.info("User " + email +" mengakses halaman Data akademik dari IP "+ remote);
  	    	return ok(views.html.ringkasan.render(display));
    	}
    	else{
    		Mahasiswa currMahasiswa = mahasiswaList.get(session("npm"));
    		RingkasanDisplay display = new RingkasanDisplay(
				String.format("%.2f", currMahasiswa.calculateIPS()), 
				String.format("%.2f", currMahasiswa.calculateIPKLulus()), 
				currMahasiswa.calculateSKSLulus()
			);
	    	List<Nilai> riwayatNilai = currMahasiswa.getRiwayatNilai();	
	    	int lastIndex = riwayatNilai.size() - 1;
			Semester semester = riwayatNilai.get(lastIndex).getSemester();
			int tahunAjaran = riwayatNilai.get(lastIndex).getTahunAjaran();
			int totalSKS = 0;
			for (int i = lastIndex; i >= 0; i--) {
				Nilai nilai = riwayatNilai.get(i);
				if (nilai.getSemester() == semester && nilai.getTahunAjaran() == tahunAjaran) {
					if (nilai.getAngkaAkhir() != null) {
						totalSKS += nilai.getMataKuliah().sks();
					}
				} else {
					break;
				}
			}

			String semTerakhir = semester +" "+tahunAjaran+"/"+(tahunAjaran+1);
	    	display.setDataSemTerakhir(semTerakhir, totalSKS);
    		String pilWajibLulus = new String();
	    	String pilWajibBelumLulus = new String();
    		for(int i=0; i<display.getPilWajib().length; i++){
	    		if( mahasiswaList.get(session("npm")).hasLulusKuliah(display.getPilWajib()[i])){
	    			pilWajibLulus += display.getPilWajib()[i]+";";
	    		}
	    		else{
	    			pilWajibBelumLulus += display.getPilWajib()[i]+";";
	    		}
	    	}
    		if(!pilWajibLulus.isEmpty()){
	    		display.setPilWajibLulus(pilWajibLulus.split(";"));
    		}
    		else{
    			display.setPilWajibLulus(new String[]{});
    		}
    		if(!pilWajibBelumLulus.isEmpty()){
	    		display.setPilWajibBelumLulus(pilWajibBelumLulus.split(";"));
    		}
    		else{
    			display.setPilWajibBelumLulus(new String[]{});
    		}
    		Logger.info("User " + email +" mengakses halaman Data akademik dari IP "+ remote);
    		return ok(views.html.ringkasan.render(display));
    	}
    }
    
    public Result tentang() {
    	if(session("npm") == null || !mahasiswaList.containsKey(session("npm"))) {
    		session().clear();
    		return index();
    	}
    	else{
    		Logger.info("User " + email +" mengakses halaman info dari IP "+ remote);
    		return ok(views.html.tentang.render());	
    	}
    }
    
    public Result logout() throws IOException {
    	session().clear();
    	mahasiswaList.remove(session("npm"));
    	Logger.info("User " + email +" telah logout dari IP "+ remote);
    	return index();
    }
    
    private List<PrasyaratDisplay> checkPrasyarat() throws IOException{
    	List<PrasyaratDisplay> table = new ArrayList<PrasyaratDisplay>();
    	List<MataKuliah> mkList = scrap.getMkList();
        String MATAKULIAH_REPOSITORY_PACKAGE = "id.ac.unpar.siamodels.matakuliah"; 
    	List<Object> mkKnown = new ArrayList<Object>(); 
    	List<MataKuliah> mkUnknown = new ArrayList<MataKuliah>(); 
        for(MataKuliah mk : mkList){
	        try {
	            Class<?> mkClass = Class.forName(MATAKULIAH_REPOSITORY_PACKAGE + "." + mk.kode());
	            Object matakuliah = mkClass.newInstance();
	            mkKnown.add(matakuliah);
	        } catch (ClassNotFoundException e) {
	        	mkUnknown.add(mk);
	        } catch (InstantiationException e) {
	                e.printStackTrace();
	        } catch (IllegalAccessException e) {
	                e.printStackTrace();
	        }           
        }
        
        for (Object mk: mkKnown) {
            if (mk instanceof HasPrasyarat) {
                List<String> reasons = new ArrayList<String>();
                ((HasPrasyarat)mk).checkPrasyarat(mahasiswaList.get(session("npm")), reasons);
                if (!reasons.isEmpty()) {
                    String status = new String();
            		for (String reason: reasons) {
            			status+=reason + ";";
                    }
                    table.add(new PrasyaratDisplay(MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),MataKuliahFactory.UNKNOWN_SKS,MataKuliahFactory.UNKNOWN_NAMA),status.split(";")));
                }
                else{
                    if(mahasiswaList.get(session("npm")).hasLulusKuliah(mk.getClass().getSimpleName())){
                    	table.add(new PrasyaratDisplay(MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),MataKuliahFactory.UNKNOWN_SKS,MataKuliahFactory.UNKNOWN_NAMA),new String[]{"sudah lulus"}));
                    }
                    else{ 
                    	table.add(new PrasyaratDisplay(MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),MataKuliahFactory.UNKNOWN_SKS,MataKuliahFactory.UNKNOWN_NAMA),new String[]{"memenuhi syarat"}));
                    }
                }
            }
            else{ 
            	if(mahasiswaList.get(session("npm")).hasLulusKuliah(mk.getClass().getSimpleName())){
                	table.add(new PrasyaratDisplay(MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),MataKuliahFactory.UNKNOWN_SKS,MataKuliahFactory.UNKNOWN_NAMA),new String[]{"sudah lulus"}));
                }
                else{ 
                	table.add(new PrasyaratDisplay(MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),MataKuliahFactory.UNKNOWN_SKS,MataKuliahFactory.UNKNOWN_NAMA),new String[]{"tidak memiliki prasyarat"}));
                }
            }
        }
        
        for (MataKuliah mk: mkUnknown) {
        	table.add(new PrasyaratDisplay(mk,new String[]{"data prasyarat tidak tersedia"}));
        }
        
        return table;
    }
    
}

