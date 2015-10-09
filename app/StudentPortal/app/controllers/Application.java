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
    		return ok(views.html.login.render());
    	}
    	else{
    		return home();
    	}
    }
    
    public Result login() {
    	if(session("npm")==null){
    		return ok(views.html.login.render());
    	}
    	else{
    		return home();
    	}
    }
    
    public Result submitLogin() throws IOException{
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	String npm = dynamicForm.get("npm");
    	String pass = dynamicForm.get("pass");
    	if(this.scrap.login(npm, pass)){
    		session("npm", npm);
    		return home();
    	}
	    else{
	    	return login();
	    }
    }
    
    public Result home() {
    	String nama = scrap.getLoggedMahasiswa().getNama();
    	return ok(views.html.home.render(nama));
    }
    
    public Result prasyarat() throws IOException{
    	ArrayList<PrasyaratTable> table = cekPrasyarat();
    	int sz = table.size();
    	return ok(views.html.prasyarat.render(table, sz));
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

