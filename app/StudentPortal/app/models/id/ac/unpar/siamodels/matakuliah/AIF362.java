package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.Pilihan;

import java.util.List;

@MataKuliah(kode = "AIF362", nama = "Sistem & Aplikasi Telematika", sks = 3)
public class AIF362 implements HasPrasyarat, Pilihan {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF305")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF305");			
			return false;
		}
		return true;
	}

}
