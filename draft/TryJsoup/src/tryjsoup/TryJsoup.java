/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tryjsoup;

import java.io.IOException;

/**
 *
 * @author Herfan Heryandi
 */
public class TryJsoup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scraper scrap = new Scraper();
        scrap.login("2012730012", "Ttdjq6Be");
        String nama = scrap.getLoggedMahasiswa().getNama();
        scrap.requestKuliah();
        scrap.setNilai();
        
        //System.out.println(scrap.getLoggedMahasiswa().calculateIPKLulus());
        JadwalBundle jb = scrap.requestJadwal();
        System.out.println("Kuliah");
        if(jb.getJadwalKuliah().isEmpty()){
            System.out.println("Jadwal kuliah belum tersedia");
        }
        else{
            for (int i = 0; i < jb.getJadwalKuliah().size(); i++) {
                JadwalKuliah jdw = jb.getJadwalKuliah().get(i);
                System.out.println(jdw.mataKuliah.getNama()+ " / " + jdw.getKelas() + " / " + jdw.getDosen()+ " / " + jdw.getHari() + " / " + jdw.getWaktu()+ " / "+ jdw.getRuang());
            }
        }
        System.out.println("");
        System.out.println("UTS");
        if(jb.getJadwalUTS().isEmpty()){
            System.out.println("Jadwal UTS belum tersedia");
        }
        else{
            for (int i = 0; i < jb.getJadwalUTS().size(); i++) {
                JadwalUjian jdw = jb.getJadwalUTS().get(i);
                System.out.println(jdw.mataKuliah.getNama()+ " / " + jdw.getKelas() + " / " + jdw.getTanggal()+ " / " + jdw.getWaktu()+ " / " + jdw.getRuang() + jdw.getKursi());
            }
        }
        System.out.println("");
        System.out.println("UAS");
        if(jb.getJadwalUAS().isEmpty()){
            System.out.println("Jadwal UAS belum tersedia");
        }
        else{
            for (int i = 0; i < jb.getJadwalUAS().size(); i++) {
                JadwalUjian jdw = jb.getJadwalUAS().get(i);
                System.out.println(jdw.mataKuliah.getNama()+ " / " + jdw.getKelas() + " / " + jdw.getTanggal()+ " / " + jdw.getWaktu()+ " / " + jdw.getRuang() + jdw.getKursi());
            }
        }

    }
    
}
