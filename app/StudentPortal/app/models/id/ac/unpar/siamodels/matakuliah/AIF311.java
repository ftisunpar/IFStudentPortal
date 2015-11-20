package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.PilihanWajib;

import java.util.List;

@MataKuliah(kode = "AIF311", nama = "Pemrograman Fungsional", sks = 2)
public class AIF311 implements HasPrasyarat, PilihanWajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF103")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF103");			
			ok = false;
		}
		return ok;
	}

}
