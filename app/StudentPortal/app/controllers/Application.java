package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import models.display.PrasyaratDisplay;
import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	Scraper scrap = new Scraper();
	
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
    	if(this.scrap.login(npm, pass)){
    		session("npm", npm);
    		return home();
    	}
	    else{
	    	return ok(views.html.login.render("Login gagal, password yang Anda masukkan salah atau Anda bukan mahasiswa aktif"));
	    }
    }
    
    public Result home() {
    	if(session("npm")==null){
    		return index();
    	}
    	else{
	    	String nama = scrap.getLoggedMahasiswa().getNama();
	    	String photoPath = scrap.getPhotoPath();
	    	return ok(views.html.home.render(nama,photoPath));
    	}
    }
    
    public Result prasyarat() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
    	else{
	    	List<PrasyaratDisplay> table = cekPrasyarat();
	    	return ok(views.html.prasyarat.render(table));
    	}
    }
    
    public Result logout() throws IOException {
    	session().clear();
    	scrap.logout();
    	return index();
    }
    
    private List<PrasyaratDisplay> cekPrasyarat() throws IOException{
    	List<PrasyaratDisplay> table = new ArrayList<PrasyaratDisplay>();
    	List<String> mkl = scrap.requestKuliah();
        scrap.setNilai();
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
                ((HasPrasyarat)mk).checkPrasyarat(scrap.getLoggedMahasiswa(), reasons);
                if (!reasons.isEmpty()) {
                    String status = new String();
            		for (String reason: reasons) {
            			status+=reason + ", ";
                    }
                    table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),status));
                }
                else{
                    if(scrap.getLoggedMahasiswa().hasLulusKuliah(mk.getClass().getSimpleName())){
                    	table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),"sudah lulus"));
                    }
                    else{ 
                    	table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),"memenuhi syarat"));
                    }
                }
            }
        }
        
        for (String mk: mkUnknown) {
        	table.add(new PrasyaratDisplay(MataKuliah.getMataKuliah(mk),"data tidak tersedia"));
        }
        
        return table;
    }
}

