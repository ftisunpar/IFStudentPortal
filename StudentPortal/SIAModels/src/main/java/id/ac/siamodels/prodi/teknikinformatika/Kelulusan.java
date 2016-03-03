package id.ac.siamodels.prodi.teknikinformatika;

import java.util.ArrayList;
import java.util.List;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

public class Kelulusan implements HasPrasyarat{

	public static final String[] PILIHAN_WAJIB={"AIF311","AIF312","AIF313","AIF314","AIF315","AIF316","AIF317"};
	public static final String[][] WAJIB=
	{
		{"AIF101","AIF103","AIF105","MKU001","MKU008","MKU010"},
		{"AIF102","AIF104","AIF106","AMS100","MKU009","MKU011"},
		{"AIF210","AIF203","AIF205","AMS200","MKU012"},
		{"AIF202","AIF204","AIF206","AIF208","AIF210"},
		{"AIF301","AIF303","AIF305","MKU002"},
		{"AIF302","AIF304","AIF306"},
		{"AIF401","AIF403","AIF405"},
		{"AIF402","APS402"}
	};
	public static final String[] AGAMA={"MKU003","MKU004"};
	
	/**pil wajib				wajib													agama
	 *  AIF315 pbw				sem 1: aif101,aif103,aif105,mku001,mku008,mku010		mku003
	 *  AIF311 pemfung			sem 2: aif102,aif104,aif106,ams100,mku009,mku011		mku004
	 *  AIF312 kamin			sem 3: aif201,aif203,aif205,ams200,mku012
	 *  AIF314 grafkom			sem 4: aif202,aif204,aif206,aif208,aif210
	 *  AIF316 pbd				sem 5: aif301,aif303,aif305,mku002
	 *  AIF316 kompar			sem 6: aif302,aif304,aif306
	 *  AIF317 dag				sem 7: aif401,aif403,aif405
	 *  AIF318 pam				sem 8: aif402,aps402
	 */
	List<String> data=new ArrayList<String>();
	boolean check=false;
	
	private void checkBoolean(boolean test)
	{
		if(test==true && this.check==false)
		{
			check=false;
		}
		else if(test==false && this.check==true)
		{
			check=false;
		}
		else if(test==false && this.check==false)
		{
			check=false;
		}
		else
		{
			check=true;
		}
	}
	
	public List<String> getData()
	{
		return this.data;
	}
	
	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {		
		//cek sks
		int sks=mahasiswa.calculateSKSLulus();
		if(sks<144)
		{
			reasonsContainer.add("Masih Kurang "+(144-sks)+" SKS");
		}
		else
		{
			check=true;
		}
		//cek agama
		boolean katolik=false;
		boolean fenom=false;
		if(mahasiswa.hasLulusKuliah("MKU003"))
		{
			katolik=true;
		}
		if(mahasiswa.hasLulusKuliah("MKU004"))
		{
			fenom=true;
		}
		if(katolik==true||fenom==true)
		{
			checkBoolean(true);
		}
		else
		{
			reasonsContainer.add("Belum mengambil MKU003 atau MKU004");
		}
		//cek kuliah wajib
		for(int i=0;i<WAJIB.length;i++)
		{
			for(int j=0;j<WAJIB[i].length;j++)
			{
				if(mahasiswa.hasLulusKuliah(WAJIB[i][j])==false)
				{
					reasonsContainer.add("Belum Lulus Kuliah Wajib "+ WAJIB[i][j]);
				}
				else
				{
					checkBoolean(true);
				}
			}
		}
		//cek pilihan wajib
		int pil_count=0;
		for(int i=0;i<PILIHAN_WAJIB.length;i++)
		{
			if(mahasiswa.hasLulusKuliah(PILIHAN_WAJIB[i]))
			{
				pil_count++;
			}
		}
		if(pil_count<4)
		{
			reasonsContainer.add("Belum Mencukupi 4 Pilihan Wajib, Baru Lulus "+pil_count+" Mata Kuliah");
		}
		else
		{
			checkBoolean(true);
		}
		//cek projek
		boolean proif=false;
		boolean prosi1=false;
		boolean prosi2=false;
		if(mahasiswa.hasLulusKuliah("AIF306"))
		{
			proif=true;
		}
		if(mahasiswa.hasLulusKuliah("AIF304"))
		{
			prosi1=true;
		}
		if(mahasiswa.hasLulusKuliah("AIF406"))
		{
			prosi2=true;
		}
		if(proif==false&&prosi1==false)
		{
			reasonsContainer.add("Belum mengambil salah satu dari AIF306 atau AIF304+405");
		}
		else if(prosi1==true && prosi2==false)
		{
			reasonsContainer.add("Baru lulus Projek Sistem Informasi 1");
		}
		else if (proif==true)
		{
			checkBoolean(true);
		}
		else if(prosi1==true && prosi2==true)
		{
			checkBoolean(true);
		}
		this.data=reasonsContainer;
		return check;
	}
}
