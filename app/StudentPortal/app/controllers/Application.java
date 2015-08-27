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
    	this.scrap.login(npm, pass);
    	return home();
    }
    
    public Result home() {
    	String nama = scrap.getLoggedMahasiswa().getNama();
    	return ok(views.html.home.render(nama));
    }
    
    
}

