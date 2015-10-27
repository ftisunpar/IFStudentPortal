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
public class Jadwal {
    protected MataKuliah mataKuliah;
    protected char kelas;
    protected String waktu;
    protected String ruang;

    public Jadwal(MataKuliah mataKuliah, char kelas, String waktu, String ruang) {
        this.mataKuliah = mataKuliah;
        this.kelas = kelas;
        this.waktu = waktu;
        this.ruang = ruang;
    }
    
    public Jadwal() {
        
    }
    
    public MataKuliah getMataKuliah() {
        return mataKuliah;
    }

    public char getKelas() {
        return kelas;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getRuang() {
        return ruang;
    }
    
}
