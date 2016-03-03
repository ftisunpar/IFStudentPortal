package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Projek;

import java.util.List;

@MataKuliah(kode = "AIF306", nama = "Proyek Informatika", sks = 6)
public class AIF306 implements HasPrasyarat, Projek {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF208")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF208");			
			return false;
		}
		return true;
	}

}
