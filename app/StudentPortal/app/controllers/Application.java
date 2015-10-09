package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import models.display.PrasyaratTable;
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
	    	return ok(views.html.home.render(nama));
    	}
    }
    
    public Result prasyarat() throws IOException{
    	if(session("npm")==null){
    		return index();
    	}
    	else{
	    	ArrayList<PrasyaratTable> table = cekPrasyarat();
	    	int sz = table.size();
	    	return ok(views.html.prasyarat.render(table, sz));
    	}
    }
    
    public Result logout() throws IOException {
    	session().clear();
    	scrap.logout();
    	return index();
    }
    
    private ArrayList<PrasyaratTable> cekPrasyarat() throws IOException{
    	ArrayList<PrasyaratTable> table = new ArrayList<PrasyaratTable>();
    	ArrayList<String> mkl = scrap.requestKuliah();
        scrap.setNilai();
        String MATAKULIAH_REPOSITORY_PACKAGE = "models.id.ac.unpar.siamodels.matakuliah"; 
    	List<Object> mkKnown = new ArrayList<Object>(); 
        for(String kodeMK : mkl){
            try {
                Class<?> mkClass = Class.forName(MATAKULIAH_REPOSITORY_PACKAGE + "." + kodeMK);
                Object matakuliah = mkClass.newInstance();
                mkKnown.add(matakuliah);
                //System.out.println(MataKuliah.getMataKuliah(kodeMK)+" added");
            } catch (ClassNotFoundException e) {
            		e.printStackTrace();
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
                            //System.out.println(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()) + ":");
                            String status = new String();
                    		for (String reason: reasons) {
                                    status+="        " + reason + "\n";
                            }
                            table.add(new PrasyaratTable(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),status));
                    }
                    else{
                        if(scrap.getLoggedMahasiswa().hasLulusKuliah(mk.getClass().getSimpleName())){
                            //System.out.println(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()) + " sudah lulus");
                        	table.add(new PrasyaratTable(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),"Sudah lulus"));
                        }
                        else{
                            //System.out.println(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()) + " memenuhi syarat");
                        	table.add(new PrasyaratTable(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()),"memenuhi syarat"));
                        }
                    }
            }
        }
        return table;
    }
}

