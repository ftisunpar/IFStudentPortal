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
        System.out.println(scrap.getLoggedMahasiswa().calculateIPKLulus());
        
    }
    
}
