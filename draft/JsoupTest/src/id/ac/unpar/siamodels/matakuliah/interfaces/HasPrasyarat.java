package id.ac.unpar.siamodels.matakuliah.interfaces;

import id.ac.unpar.siamodels.Mahasiswa;

import java.util.List;

/**
 * Mendefinisikan kelas-kelas yang memiliki prasyarat, terkustomisasi
 * untuk seorang {@link Mahasiswa}. Jika ada tambahan, jangan lupa untuk
 * mendaftarkannya di {@link #DEFAULT_HASPRASYARAT_CLASSES}. Jika berubah package,
 * jangan lupa mengupdate {@link #DEFAULT_PRASYARAT_PACKAGE}.
 * @author pascal
 *
 */
public interface HasPrasyarat {
	
	/**
	 * Daftar dari nama kelas default seluruh turunan interface ini. Perlu didaftarkan
	 * manual, karena Java reflection tidak dapat mendeteksi otomatis.
	 */
	public String[] DEFAULT_HASPRASYARAT_CLASSES = { "AIF102", "AIF202",
			"AIF204", "AIF206", "AIF208", "AIF302", "AIF304", "AIF306",
			"AIF312", "AIF314", "AIF316", "AIF318", "AIF332", "AIF339",
			"AIF342", "AIF344", "AIF360", "AIF401", "AIF402", "AIF445",
			"AIF458", "AIF461", "APS402" };
	
	/**
	 * Package tempat menyimpan seluruh turunan standar interface ini. Perlu didefinisikan
	 * manual, karena Java reflection tidak dapat mendeteksi otomatis.
	 */
	public String DEFAULT_PRASYARAT_PACKAGE = "id.ac.unpar.siamodels.matakuliah";
	
	/**
	 * Memeriksa prasyarat-prasyarat dari kuliah, spesifik untuk mahasiswa
	 * yang dituju. Jika ada pesan-pesan khusus, akan ditambahkan pada parameter
	 * reasonsContainer.
	 * @param mahasiswa prasyarat kuliah akan diperiksa spesifik pada mahasiswa ini
	 * @param reasonsContainer pesan-pesan terkait prasyarat akan ditambahkan di sini, jika ada. 
	 * @return true jika seluruh prasyarat dipenuhi, false jika tidak. 
	 */
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer);
}
