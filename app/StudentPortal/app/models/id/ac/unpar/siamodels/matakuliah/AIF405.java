package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

@MataKuliah(kode = "AIF405", nama = "Proyek Sistem Informasi 2", sks = 3)
public class AIF405 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF304")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF304");			
			ok = false;
		}
		return ok;
	}

}
