package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Pilihan;

import java.util.List;

@MataKuliah(kode = "AIF469", nama = "Layanan Berbasis Web", sks = 3)
public class AIF469 implements HasPrasyarat, Pilihan {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF305")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF305");
			ok = false;
		}
		if (!mahasiswa.hasTempuhKuliah("AIF315")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF315");			
			ok = false;
		}
		return ok;
	}

}
