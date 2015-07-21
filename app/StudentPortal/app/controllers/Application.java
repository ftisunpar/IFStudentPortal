package controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	Scraper scrap = new Scraper();
	Mahasiswa logged_mhs;
	
    public Result index() {
        return ok(index.render("Student Portal"));
    }
    
    public Result login() {
    	return ok(views.html.login.render());
    }
    
    public Result submitLogin() throws IOException{
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	String npm = dynamicForm.get("npm");
    	String pass = dynamicForm.get("pass");
    	this.logged_mhs = new Mahasiswa(npm);
    	Connection login_conn = this.scrap.login(this.logged_mhs.getEmailAddress(), pass);
    	Document login_doc =  login_conn.post();
        String nama = login_doc.select("p[class=student-name]").text();
        this.logged_mhs.setNama(nama);
    	return ok(home.render(this.logged_mhs.getNama()));
    }
    
    public Result home(String nama) {
    	return ok(views.html.home.render(nama));
    }
    
    public Result submitKuliah() throws IOException{
    	ArrayList<MataKuliah> mkList = scrap.requestKuliah(2015, "GANJIL");
    	return ok(views.html.kuliah.render(mkList));
    }
    
    public Result kuliah(ArrayList<MataKuliah> mkList){
    	return ok(views.html.kuliah.render(mkList));
    }
}

