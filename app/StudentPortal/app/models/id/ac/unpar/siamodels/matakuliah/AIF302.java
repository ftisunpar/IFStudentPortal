package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

@MataKuliah(kode = "AIF302", nama = "Penulisan Ilmiah", sks = 2)
public class AIF302 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		int sksLulus = mahasiswa.calculateSKSLulus();
		if (sksLulus < 84) {
			reasonsContainer.add("SKS Lulus " + sksLulus + ", belum mencapai syarat minimal 84");			
			return false;
		}
		return true;
	}

}
