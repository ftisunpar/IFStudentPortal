/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tryjsoup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.lang.reflect.*;
import models.id.ac.unpar.siamodels.Mahasiswa.Nilai;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.Semester;


/**
 *
 * @author Herfan Heryandi
 */
public class TryJsoup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scraper scrap = new Scraper();
        scrap.login("2012730012", "Ttdjq6Be");
        String nama = scrap.getLoggedMahasiswa().getNama();
        scrap.requestKuliah();
        scrap.setNilai();
        int last = scrap.getLoggedMahasiswa().getRiwayatNilai().size()-1;
        Nilai latest = scrap.getLoggedMahasiswa().getRiwayatNilai().get(last);
        System.out.println(nama);
        if(latest.getTahunAjaran()==Integer.parseInt(scrap.thn_akd) && latest.getSemester() == Semester.fromString(scrap.sem_akd)){
            System.out.println("IPS: "+scrap.getLoggedMahasiswa().calculateIPS());
        }
        else{
            System.out.println("IPS: belum tersedia");
        }
        System.out.println("IPK: "+scrap.getLoggedMahasiswa().calculateIPKLulus());
        System.out.println("Anda telah lulus: "+scrap.getLoggedMahasiswa().calculateSKSLulus()+" sks");
        int sisa = 144-scrap.getLoggedMahasiswa().calculateSKSLulus();
        if(sisa<0){
            sisa = 0;
        }
        System.out.println("Sisa sks yang perlu Anda tempuh minimal " + sisa + " sks");
        String[] pilWajib = new String[]{"AIF311","AIF312","AIF313","AIF314","AIF315","AIF316","AIF317","AIF318"}; 
        MataKuliah.createMataKuliah("AIF311", 2, "Pemrograman Fungsional");
        MataKuliah.createMataKuliah("AIF312", 2, "Keamanan Informasi");
        MataKuliah.createMataKuliah("AIF313", 2, "Grafika Komputer");
        MataKuliah.createMataKuliah("AIF314", 2, "Pemrograman Basis Data");
        MataKuliah.createMataKuliah("AIF315", 2, "Pemrograman Berbasis Web");
        MataKuliah.createMataKuliah("AIF316", 2, "Komputasi Paralel");
        MataKuliah.createMataKuliah("AIF317", 2, "Desain Antarmuka Grafis");
        MataKuliah.createMataKuliah("AIF318", 2, "Pemrograman Aplikasi Bergerak");
       
        String pilWajibLulus = new String();
        String pilWajibBelumLulus = new String();
        for (int i = 0; i < pilWajib.length; i++) {
            if(scrap.getLoggedMahasiswa().hasLulusKuliah(pilWajib[i])){
               pilWajibLulus += pilWajib[i] + ";";
            }
            else{
               pilWajibBelumLulus += pilWajib[i] + ";";  
            }
        }
        System.out.println("Pilihan Wajib");
        System.out.println("Lulus: " + pilWajibLulus);
        System.out.println("Belum lulus: " + pilWajibBelumLulus);
        System.out.println("Pilihan wajib minimal lulus 4 dari 8");
        if(pilWajibLulus.split(";").length>=4){
            System.out.println("Syarat kelulusan pilihan wajib terpenuhi");
        }
        else{
            System.out.println("Syarat kelulusan pilihan wajib belum terpenuhi");
            System.out.println("Anda hanya lulus " + pilWajibLulus.split(";").length + " mata kuliah pilihan wajib");
            System.out.println("Anda perlu lulus setidaknya " + (4-pilWajibLulus.split(";").length) + " mata kuliah pilihan wajib lagi");
        }
    }
    
}
