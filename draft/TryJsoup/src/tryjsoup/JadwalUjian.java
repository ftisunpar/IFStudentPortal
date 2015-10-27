/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tryjsoup;

import models.id.ac.unpar.siamodels.MataKuliah;

/**
 *
 * @author ADMIN7
 */
public class JadwalUjian extends Jadwal{
    private String tanggal;
    private String kursi;
    
    public JadwalUjian(MataKuliah mataKuliah, char kelas, String tanggal, String waktu, String ruang, String kursi) {
        super(mataKuliah, kelas, waktu, ruang);
        this.tanggal = tanggal;
        this.kursi = kursi;
    }
    
    public JadwalUjian(){
        super();
    }
    
    public String getTanggal() {
        return tanggal;
    }

    public String getKursi() {
        return kursi;
    }

}
