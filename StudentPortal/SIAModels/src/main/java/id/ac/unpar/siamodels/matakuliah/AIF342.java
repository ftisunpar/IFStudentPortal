package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

@MataKuliah(kode = "AIF342", nama = "Administrasi Jaringan Komputer 2", sks = 3)
public class AIF342 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasLulusKuliah("AIF341")) {
			reasonsContainer.add("Tidak memenuhi prasyarat lulus AIF341");			
			return false;
		}
		return true;
	}

}
