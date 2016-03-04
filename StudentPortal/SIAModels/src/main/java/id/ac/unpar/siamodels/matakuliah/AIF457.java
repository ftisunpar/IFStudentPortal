package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Pilihan;

import java.util.List;

@MataKuliah(kode = "AIF457", nama = "Kewirausahaan Berbasis Teknologi", sks = 3)
public class AIF457 implements HasPrasyarat, Pilihan {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		int sksLulus = mahasiswa.calculateSKSLulus();		
		if (sksLulus < 70) {
			reasonsContainer.add("SKS Lulus " + sksLulus + ", belum mencapai syarat minimal 70");			
			return false;
		}
		return ok;
	}

}
