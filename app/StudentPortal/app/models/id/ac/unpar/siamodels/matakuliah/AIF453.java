package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.Pilihan;
import java.util.List;

@MataKuliah(kode = "AIF453", nama = "Kecerdasan Bisnis", sks = 3)
public class AIF453 implements HasPrasyarat, Pilihan {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = false;
		if (mahasiswa.hasTempuhKuliah("AIF204") || mahasiswa.hasTempuhKuliah("AIF294")) {
			ok = true;
		}
		if ((mahasiswa.hasTempuhKuliah("AIF102") || !mahasiswa.hasTempuhKuliah("AIF192")) && mahasiswa.calculateIPKLulus() > 2.75) {
			ok = true;
		}
		if (!ok) {
			reasonsContainer.add("Tidak memenuhi prasyarat ((tempuh AIF204/294) atau (tempuh AIF102/AIF192 & IPK Lulus > 2.75))");
		}
		return ok;
	}

}
