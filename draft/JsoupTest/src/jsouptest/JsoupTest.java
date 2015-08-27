/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsouptest;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.Mahasiswa.Nilai;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author ADMIN7
 */
public class JsoupTest {
    /**
     * @param args the command line arguments
     */
    
    private static final String MATAKULIAH_REPOSITORY_PACKAGE = "id.ac.unpar.siamodels.matakuliah"; 
    
    public static void main(String[] args) throws IOException {
        Scraper scrap = new Scraper();
        String pass = "Ttdjq6Be";
        scrap.init();
        scrap.login("2012730012",pass);  
        ArrayList<String> mkl = scrap.requestKuliah();
        scrap.setNilai();
        Mahasiswa mhs = scrap.getLoggedMahasiswa();
        for(Nilai n: mhs.getRiwayatNilai()){
            System.out.println(n.toString());
        }
        
        /*List<Object> mkKnown = new ArrayList<Object>(); 
        List<String> mkUnknown = new ArrayList<String>();
        for(String kodeMK : mkl){
            try {
                Class<?> mkClass = Class.forName(MATAKULIAH_REPOSITORY_PACKAGE + "." + kodeMK);
                Object matakuliah = mkClass.newInstance();
                mkKnown.add(matakuliah);
                //System.out.println(MataKuliah.getMataKuliah(kodeMK)+" added");
            } catch (ClassNotFoundException e) {
                    mkUnknown.add(kodeMK);
            } catch (InstantiationException e) {
                    e.printStackTrace();
            } catch (IllegalAccessException e) {
                    e.printStackTrace();
            }           
        }
        if (!mkUnknown.isEmpty()) {
            System.out.println(" [WARNING] Mata kuliah tidak dikenal: " + mkUnknown);
        }
        
        for (Object mk: mkKnown) {
            if (mk instanceof HasPrasyarat) {
                    List<String> reasons = new ArrayList<String>();
                    ((HasPrasyarat)mk).checkPrasyarat(scrap.getLoggedMahasiswa(), reasons);
                    if (!reasons.isEmpty()) {
                            System.out.println(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()) + ":");
                            for (String reason: reasons) {
                                    System.out.println("    " + reason);
                            }
                    }
                    else{
                        if(scrap.getLoggedMahasiswa().hasLulusKuliah(mk.getClass().getSimpleName())){
                            //System.out.println(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()) + " sudah lulus");
                        }
                        else{
                            System.out.println(MataKuliah.getMataKuliah(mk.getClass().getSimpleName()) + " memenuhi syarat");
                        }
                    }
            }
        }*/
        
        scrap.logout();

    }
 
}
