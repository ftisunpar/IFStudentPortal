package models.display;

import id.ac.unpar.siamodels.MataKuliahFactory;


public class RingkasanDisplay {
	private String[] pilWajib;
	private String[] pilWajibLulus;
	private String[] pilWajibBelumLulus;
	private String IPS;
	private String IPK;
	private int sksLulusTotal;
	private int sksLulusSemTerakhir;
	private String semesterTerakhir;
	private final int MIN_LULUS_PIL_WAJIB = 4;
	private int nilaiTOEFL;
	
	public RingkasanDisplay(String IPS, String IPK, int sksLulusTotal){
		this.IPS = IPS;
		this.IPK = IPK;
		this.sksLulusTotal = sksLulusTotal;
		/*create mata kuliah pilihan wajib*/
		pilWajib = new String[]{"AIF311","AIF312","AIF313","AIF314","AIF315","AIF316","AIF317","AIF318"}; 
	}
	
	public int getMinLulusPilWajib(){
		return this.MIN_LULUS_PIL_WAJIB;
	}
	
	public String getNamaPilWajib(String kode){
		return MataKuliahFactory.getInstance().createMataKuliah(kode, MataKuliahFactory.UNKNOWN_SKS, MataKuliahFactory.UNKNOWN_NAMA).nama()+"";
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
	
	public int getNilaiTOEFL(){
		return this.nilaiTOEFL;
	}
	
	public void setNilaiTOEFL(int apa){
		this.nilaiTOEFL = apa;
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
