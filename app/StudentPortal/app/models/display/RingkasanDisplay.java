package models.display;

import models.id.ac.unpar.siamodels.MataKuliah;

public class RingkasanDisplay {
	private String[] pilWajib;
	private double IPS;
	private double IPK;
	private int sksLulus;
	
	public RingkasanDisplay(double IPS, double IPK, int sksLulus){
		this.IPS = IPS;
		this.IPK = IPK;
		this.sksLulus = sksLulus;
		pilWajib = new String[]{"AIF311","AIF312","AIF313","AIF314","AIF315","AIF316","AIF317","AIF318"}; 
		MataKuliah.createMataKuliah("AIF311", 2, "Pemrograman Fungsional");
        MataKuliah.createMataKuliah("AIF312", 2, "Keamanan Informasi");
        MataKuliah.createMataKuliah("AIF313", 2, "Grafika Komputer");
        MataKuliah.createMataKuliah("AIF314", 2, "Pemrograman Basis Data");
        MataKuliah.createMataKuliah("AIF315", 2, "Pemrograman Berbasis Web");
        MataKuliah.createMataKuliah("AIF316", 2, "Komputasi Paralel");
        MataKuliah.createMataKuliah("AIF317", 2, "Desain Antarmuka Grafis");
        MataKuliah.createMataKuliah("AIF318", 2, "Pemrograman Aplikasi Bergerak");
	}
	
	public double getIPS(){
		return this.IPS;
	}
	
	public double getIPK(){
		return this.IPK;
	}
	
	public int getSKSLulus(){
		return this.sksLulus;
	}
	
	public int getMinSisaSKS(){
		if(sksLulus>=144){
			return 0;
		}
		else{
			return 144-sksLulus;
		}
	}
}
