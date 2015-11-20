package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.PilihanWajib;

import java.util.List;

@MataKuliah(kode = "AIF315", nama = "Pemrograman Berbasis Web", sks = 2)
public class AIF315 implements HasPrasyarat, PilihanWajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF204") && !mahasiswa.hasTempuhKuliah("AIF294")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF204 atau AIF294");
			return false;
		}
		return true;
	}

}
