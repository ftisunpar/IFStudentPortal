package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.PilihanWajib;

import java.util.List;

@MataKuliah(kode = "AIF318", nama = "Pemrograman Aplikasi Bergerak", sks = 2)
public class AIF318 implements HasPrasyarat, PilihanWajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF102") && !mahasiswa.hasTempuhKuliah("AIF192")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF102 atau AIF192");
			ok = false;
		}
		if (!mahasiswa.hasTempuhKuliah("AIF201")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF201");			
			ok = false;
		}
		return ok;
	}

}
