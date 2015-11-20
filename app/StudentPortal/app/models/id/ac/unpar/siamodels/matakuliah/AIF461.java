package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

@MataKuliah(kode = "AIF461", nama = "Pencarian & Temu Kembali Informasi", sks = 2)
public class AIF461 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		int tahunAngkatan = mahasiswa.getTahunAngkatan();
		// FIXME aturan ini tidak sepanjang masa!
		if (tahunAngkatan >= 2013 && tahunAngkatan <= 2014) {
			reasonsContainer.add("Tidak dianjurkan untuk angkatan 2013 dan 2014");
		}
		return true;
	}

}
