package models.display;

import models.id.ac.unpar.siamodels.MataKuliahFactory;

public class RingkasanDisplay {
	private String[] pilWajib;
	private String[] pilWajibLulus;
	private String[] pilWajibBelumLulus;
	private String IPS;
	private String IPK;
	private int sksLulusTotal;
	private int sksLulusSemTerakhir;
	private String semesterTerakhir;
	
	public RingkasanDisplay(String IPS, String IPK, int sksLulusTotal){
		this.IPS = IPS;
		this.IPK = IPK;
		this.sksLulusTotal = sksLulusTotal;
		pilWajib = new String[]{"AIF311","AIF312","AIF313","AIF314","AIF315","AIF316","AIF317","AIF318"}; 
		MataKuliahFactory.createMataKuliah("AIF311", 2, "Pemrograman Fungsional");
		MataKuliahFactory.createMataKuliah("AIF312", 2, "Keamanan Informasi");
		MataKuliahFactory.createMataKuliah("AIF313", 2, "Grafika Komputer");
		MataKuliahFactory.createMataKuliah("AIF314", 2, "Pemrograman Basis Data");
		MataKuliahFactory.createMataKuliah("AIF315", 2, "Pemrograman Berbasis Web");
		MataKuliahFactory.createMataKuliah("AIF316", 2, "Komputasi Paralel");
		MataKuliahFactory.createMataKuliah("AIF317", 2, "Desain Antarmuka Grafis");
		MataKuliahFactory.createMataKuliah("AIF318", 2, "Pemrograman Aplikasi Bergerak");
	}
	
	
	
	public String[] getPilWajibLulus() {
		return pilWajibLulus;
	}

	public void setPilWajibLulus(String[] pilWajibLulus) {
		this.pilWajibLulus = pilWajibLulus;
	}

	public String[] getPilWajibBelumLulus() {
		return pilWajibBelumLulus;
	}


	public void setPilWajibBelumLulus(String[] pilWajibBelumLulus) {
		this.pilWajibBelumLulus = pilWajibBelumLulus;
	}

	public String[] getPilWajib(){
		return this.pilWajib;
	}
	
	public String getIPS(){
		return this.IPS;
	}
	
	public String getIPK(){
		return this.IPK;
	}
	
	public void setDataSemTerakhir(String semTerakhir, int sksLulusSemTerakhir) {
		this.semesterTerakhir = semTerakhir;
		this.sksLulusSemTerakhir = sksLulusSemTerakhir;
	}
	
	public String getSemesterTerakhir(){
		return semesterTerakhir;
	}
	public int getSKSLulusTotal(){
		return this.sksLulusTotal;
	}
	
	public int getSKSLulusSemTerakhir(){
		return this.sksLulusSemTerakhir;
	}
	
	public int getMinSisaSKS(){
		if(sksLulusTotal>=144){
			return 0;
		}
		else{
			return 144-sksLulusTotal;
		}
	}
}
