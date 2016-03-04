package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Projek;

import java.util.List;

@MataKuliah(kode = "AIF304", nama = "Proyek Sistem Informasi 1", sks = 3)
public class AIF304 implements HasPrasyarat, Projek {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF303")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF303");			
			return false;
		}
		return true;
	}

}
