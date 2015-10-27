/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models.support;

import models.id.ac.unpar.siamodels.MataKuliah;

/**
 *
 * @author ADMIN7
 */
public class JadwalKuliah extends Jadwal{
    private String dosen;
    private String hari;

    public JadwalKuliah(MataKuliah mataKuliah, char kelas, String dosen, String hari, String waktu, String ruang) {
        super(mataKuliah, kelas, waktu, ruang);
        this.dosen = dosen;
        this.hari = hari;
    }
    
    public JadwalKuliah(){
        super();
    }
        
    public String getDosen() {
        return dosen;
    }

    public String getHari() {
        return hari;
    }

}
