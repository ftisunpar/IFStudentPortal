/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tryjsoup;

import java.io.IOException;
import models.id.ac.unpar.siamodels.MataKuliah;

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
        MataKuliah mkx = MataKuliah.createMataKuliah("XYZ000",1 ,"dummy");
        jb.getJadwalKuliah().add(new JadwalKuliah( mkx,'Z' ,"Thugsgino" , "Sabtu", "07.30-09.30", "9999"));
        JadwalKuliah[][] kuliahCalendar = new JadwalKuliah[6][22];
        if(kuliahCalendar[0][0]==null){
            System.out.println("xxx");
        }
        System.out.println("Kuliah");
        if(jb.getJadwalKuliah().isEmpty()){
            System.out.println("Jadwal kuliah belum tersedia");
        }
        else{
            for (int i = 0; i < kuliahCalendar.length; i++) {
                for (int j = 0; j < kuliahCalendar[i].length; j++) {
                    kuliahCalendar[i][j] = new JadwalKuliah();
                }
            }
            for (int i = 0; i < jb.getJadwalKuliah().size(); i++) {
                JadwalKuliah jdw = jb.getJadwalKuliah().get(i);
                int day = dayTranslate(jdw.getHari());
                //System.out.println(day);
                //kuliahCalendar[day][0] = jdw;
                if(day!=-1){
                    System.out.println("masuk " + jdw.getMataKuliah().getNama() + " " + jdw.getWaktu());
                    String[] timePair = jdw.getWaktu().split("-");
                    String start = timePair[0];
                    String end = timePair[1];
                    int range = (Integer.parseInt(end.substring(0, 2))- Integer.parseInt(start.substring(0, 2)))*2;
                    System.out.println("range: " +range);
                    int beginIndex = 0;
                    if(start.charAt(3)=='0'){
                        beginIndex = (Integer.parseInt(start.substring(0, 2))-7)*2;
                        System.out.println("BI: " + beginIndex);
                    }
                    else if(start.charAt(3)=='3'){
                        beginIndex =((Integer.parseInt(start.substring(0, 2))-7)*2)+1;
                       // System.out.println("BI: " + beginIndex);
                    }
                    for (int j = beginIndex; j < beginIndex+range; j++) {
                        kuliahCalendar[day][j] = jdw;  
                    }
                }
                //System.out.println(jdw.mataKuliah.getNama()+ " / " + jdw.getKelas() + " / " + jdw.getDosen()+ " / " + jdw.getHari() + " / " + jdw.getWaktu()+ " / "+ jdw.getRuang());
            }
        }

        for (int i = 0; i < kuliahCalendar.length; i++) {
            System.out.println("Hari " + i);
            for (int j = 0; j < kuliahCalendar[i].length; j++) {
                if(kuliahCalendar[i][j].getMataKuliah()!=null){
                    System.out.println(j+" "+kuliahCalendar[i][j].getMataKuliah().getNama());
                }
                else{
                    System.out.println(j+ " -");
                }
            }
        }
        /*
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
         */
    }
    
    public static int dayTranslate(String hari){
        int day = -1;
        switch(hari){
            case "Senin": 
                day = 0;
                break;
            case "Selasa": 
                day = 1;
                break;
            case "Rabu": 
                day = 2;
                break;
            case "Kamis": 
                day = 3;
                break;
            case "Jumat": 
                day = 4;
                break;
            case "Sabtu": 
                day = 5;
                break;
            default:
                day = -1;
                break;
        }
        return day;
    }
}
