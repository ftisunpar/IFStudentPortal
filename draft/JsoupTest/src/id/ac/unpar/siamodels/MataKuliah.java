package id.ac.unpar.siamodels;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Merepresentasikan sebuah mata kuliah.
 * TODO: baiknya kelas-kelas id.ac.unpar.siamodels.matakuliah terkait ke sini.
 * @author pascal
 *
 */
public class MataKuliah {
	protected final String kode;
	protected final int sks;
	protected final String nama;

	public String getKode() {
		return kode;
	}

	public int getSKS() {
		return sks;
	}

	public String getNama() {
		return nama;
	}

	protected MataKuliah(String kode, int sks, String nama) {
		super();
		this.kode = kode;
		this.sks = sks;
		this.nama = nama;
	}

	protected static SortedMap<String, MataKuliah> daftarMataKuliah = null;

	/**
	 * Mendapatkan atau membuat mata kuliah baru
	 * @param kode kode MK
	 * @param sks bobot SKS
	 * @param nama Nama mata kuliah
	 * @return objek mata kuliah singleton
	 */
	public static MataKuliah createMataKuliah(String kode, int sks, String nama) {
		if (daftarMataKuliah == null) {
			daftarMataKuliah = new TreeMap<String, MataKuliah>();
		}
		MataKuliah mataKuliah = daftarMataKuliah.get(kode);
		if (mataKuliah == null) {
			mataKuliah = new MataKuliah(kode, sks, nama);
			daftarMataKuliah.put(kode, mataKuliah);
		} else {
			assert mataKuliah.sks == sks;
			assert mataKuliah.nama.equals(nama);
		}
		return mataKuliah;
	}
	
	public static MataKuliah getMataKuliah(String kode) {
		if (daftarMataKuliah == null) {
			daftarMataKuliah = new TreeMap<String, MataKuliah>();
		}
		return daftarMataKuliah.get(kode);
	}

	@Override
	public String toString() {
		return "MataKuliah [kode=" + kode + ", sks=" + sks + ", nama=" + nama
				+ "]";
	}
}
