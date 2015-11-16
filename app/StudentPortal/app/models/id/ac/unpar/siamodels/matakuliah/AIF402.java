package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

@MataKuliah(kode = "AIF402", nama = "Skripsi 2", sks = 6)
public class AIF402 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (mahasiswa.hasLulusKuliah("AIF401")) {
			return true;
		} else if (mahasiswa.calculateSKSLulus() >= 124) {
			reasonsContainer.add("CATATAN: Mahasiswa harus mengambil juga AIF401 (tempuh bersama)");
			return true;
		} else {
			reasonsContainer.add("Harus sudah mengambil AIF401 atau lulus 124 SKS");
			return false;
		}
	}
}
