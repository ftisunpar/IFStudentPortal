package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import models.display.JadwalDisplay;
import models.display.PrasyaratDisplay;
import models.display.RingkasanDisplay;
import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.support.JadwalBundle;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	Scraper scrap = new Scraper();
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
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	String email = dynamicForm.get("email");
    	String pass = dynamicForm.get("pass");
    	if(!email.matches("[0-9]{7}+@student.unpar.ac.id")){
    		return ok(views.html.login.render("Email tidak valid"));
    	}
    	if(!(email.charAt(0)=='7'&&email.charAt(1)=='3')){
    		return ok(views.html.login.render("Maaf, Anda bukan mahasiswa Teknik Informatika"));
    	}
    	String npm = "20" + email.substring(2,4) + email.substring(0,2)+ "0" + email.substring(4,7);
    	Mahasiswa login_mhs = this.scrap.login(npm, pass);
    	if(login_mhs!=null){
    		session("npm", npm);
    		mahasiswaList.put(session("npm"), login_mhs);
    		return home();
    	}
	    else{
	    	return ok(views.html.login.render("Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif"));
	    }
    }
    
    public Result home() {
    	if(session("npm")==null){
    		return index();
    	}
    	return ok(views.html.home.render(mahasiswaList.get(session("npm"))));	
    }
    
    public Result prasyarat() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
    	List<PrasyaratDisplay> table = cekPrasyarat();
    	String semester = scrap.getSemester();
    	return ok(views.html.prasyarat.render(table,semester));
    }
    
    public Result jadwalKuliah() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
		JadwalBundle jb = mahasiswaList.get(session("npm")).getJadwalList();
		JadwalDisplay table = new JadwalDisplay(jb);
    	return ok(views.html.jadwalKuliah.render(table));
    }
    
    public Result jadwalUTS() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
    	JadwalBundle jb = mahasiswaList.get(session("npm")).getJadwalList();
		JadwalDisplay table = new JadwalDisplay(jb);
    	return ok(views.html.jadwalUTS.render(table));
    }
    
    public Result jadwalUAS() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
    	JadwalBundle jb = mahasiswaList.get(session("npm")).getJadwalList();
		JadwalDisplay table = new JadwalDisplay(jb);
    	return ok(views.html.jadwalUAS.render(table));
    }
    
    public Result ringkasan() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
    	else{
    		RingkasanDisplay display = new RingkasanDisplay( mahasiswaList.get(session("npm")).calculateIPS(), mahasiswaList.get(session("npm")).calculateIPKLulus(), mahasiswaList.get(session("npm")).calculateSKSLulus());
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
    		return ok(views.html.ringkasan.render(display));
    	}
    }
    
    public Result logout() throws IOException {
    	session().clear();
    	scrap.logout();
    	mahasiswaList.remove(session("npm"));
    	return index();
    }
    
    private List<PrasyaratDisplay> cekPrasyarat() throws IOException{
    	List<PrasyaratDisplay> table = new ArrayList<PrasyaratDisplay>();
    	List<String> mkl = scrap.getMkList();
        String MATAKULIAH_REPOSITORY_PACKAGE = "models.id.ac.unpar.siamodels.matakuliah"; 
    	List<Object> mkKnown = new ArrayList<Object>(); 
    	List<String> mkUnknown = new ArrayList<String>(); 
        for(String kodeMK : mkl){
	        try {
	            Class<?> mkClass = Class.forName(MATAKULIAH_REPOSITORY_PACKAGE + "." + kodeMK);
	            Object matakuliah = mkClass.newInstance();
	            mkKnown.add(matakuliah);
	        } catch (ClassNotFoundException e) {
	        	mkUnknown.add(kodeMK);
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
                    table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),status.split(";")));
                }
                else{
                    if(mahasiswaList.get(session("npm")).hasLulusKuliah(mk.getClass().getSimpleName())){
                    	table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),new String[]{"sudah lulus"}));
                    }
                    else{ 
                    	table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),new String[]{"memenuhi syarat"}));
                    }
                }
            }
        }
        
        for (String mk: mkUnknown) {
        	table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk),new String[]{"data prasyarat tidak tersedia"}));
        }
        
        return table;
    }
}

