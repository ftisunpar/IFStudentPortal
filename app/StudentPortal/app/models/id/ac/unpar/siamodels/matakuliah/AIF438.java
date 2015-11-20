package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.Pilihan;

import java.util.List;

@MataKuliah(kode = "AIF438", nama = "Penambangan Data", sks = 3)
public class AIF438 implements HasPrasyarat, Pilihan {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasLulusKuliah("AIF102") && !mahasiswa.hasLulusKuliah("AIF192")) {
			reasonsContainer.add("Tidak memenuhi prasyarat lulus AIF102 atau AIF192");
			ok = false;
		}
		return ok;
	}

}
