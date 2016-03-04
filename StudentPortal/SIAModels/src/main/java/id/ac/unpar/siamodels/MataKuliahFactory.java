package id.ac.unpar.siamodels;

import java.lang.annotation.Annotation;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Kelas yang bertugas membuat kelas mata kuliah, dan menyimpannya untuk bisa
 * digunakan kemudian.
 * 
 * @author pascal
 *
 */
public class MataKuliahFactory {

	/**
	 * Lokasi package untuk daftar mata kuliah
	 */
	public static String DEFAULT_MATAKULIAH_PACKAGE = "id.ac.unpar.siamodels.matakuliah";

	/**
	 * Menandakan jumlah SKS tidak diketahui.
	 */
	public static int UNKNOWN_SKS = Integer.MIN_VALUE;

	/**
	 * Menandakan nama mata kuliah tidak diketahui.
	 */
	public static String UNKNOWN_NAMA = null;

	/**
	 * Singleton instance to factory.
	 */
	private static MataKuliahFactory instance = null;

	/**
	 * Singleton instances untuk mata kuliah.
	 */
	private final SortedMap<String, MataKuliah> mataKuliahCache;

	public static MataKuliahFactory getInstance() {
		if (instance == null) {
			instance = new MataKuliahFactory();
		}
		return instance;
	}

	protected MataKuliahFactory() {
		this.mataKuliahCache = new TreeMap<>();
	}

	/**
	 * Membuat atau mendapatkan mata kuliah yang baru. Akan mencoba mengambil
	 * dari:
	 * <ol>
	 * <li>Daftar objek internal cache mata kuliah yang sudah dibuat sebelumnya,
	 * jika tidak ada</li>
	 * <li>Daftar kelas statik di package {@link #DEFAULT_MATAKULIAH_PACKAGE},
	 * jika tidak ada
	 * <li>
	 * <li>Membuat baru</li>
	 * </ol>
	 * Jika diambil dari cache/kelas statik, sks akan diperiksa kesamaannya
	 * (kecuali jika tidak tahu).
	 * 
	 * @param kode
	 *            kode mata kuliah
	 * @param sks
	 *            jumlah SKS atau isi dengan {@link #UNKNOWN_SKS} jika tidak
	 *            tahu.
	 * @param nama
	 *            nama mata kuliah atau isi dengan {@link #UNKNOWN_NAMA} jika
	 *            tidak tahu.
	 * @return objek mata kuliah
	 * @throws IllegalStateException
	 *             jika sks dan tidak sesuai dengan yang ada di kode
	 */
	public MataKuliah createMataKuliah(final String kode, final int sks, final String nama)
			throws IllegalStateException {
		MataKuliah mataKuliah = this.mataKuliahCache.get(kode);
		// Coba dapatkan mata kuliah dari cache
		if (mataKuliah != null) {
			addOrUpdateMataKuliahData(kode, sks, nama);
			return mataKuliah;
		}

		// Coba dapatkan dari kelas statik
		Class<?> mkClass;
		try {
			mkClass = Class.forName(DEFAULT_MATAKULIAH_PACKAGE + "." + kode);
			if (mkClass.isAnnotationPresent(MataKuliah.class)) {
				MataKuliah staticMatakuliah = (MataKuliah) mkClass.getAnnotation(MataKuliah.class);
				if (sks != UNKNOWN_SKS && staticMatakuliah.sks() != sks) {
					throw new IllegalStateException(String.format(
							"SKS yang diberikan %d tidak sama dengan yang tercatat %d, dan bukan UNKNOWN_SKS untuk mata kuliah %s-%d %s.", sks,
							staticMatakuliah.sks(), kode, sks, nama));
				}
				mataKuliahCache.put(kode, staticMatakuliah);
				return staticMatakuliah;
			} else {
				Logger.getGlobal().warning("Class is listed but not annotated: " + String.format("%s-%d %s", kode, sks, nama));
			}
		} catch (ClassNotFoundException e) {
			Logger.getGlobal().warning("Class is not listed: " + String.format("%s-%d %s", kode, sks, nama));
		}
		
		// Tidak ditemukan, buat baru
		return addOrUpdateMataKuliahData(kode, sks, nama);
	}

	/**
	 * Menambahkan atau memperbarahui data mata kuliah ke cache. Beberapa aturan
	 * yang digunakan:
	 * <ul>
	 * <li>Jika belum ada di cache, langsung ditambahkan
	 * <li>Jika sudah ada di cache, dan data baru lebih lengkap, akan
	 * diperbaharui.
	 * <li>Selain itu, mengambil dari cache.
	 * </ul>
	 * 
	 * @param kode
	 *            kode mata kuliah
	 * @param sks
	 *            jumlah SKS
	 * @param nama
	 *            nama mata kuliah.
	 * @return objek mata kuliah
	 */
	private MataKuliah addOrUpdateMataKuliahData(final String kode, final int sks, final String nama) {
		MataKuliah mataKuliah = this.mataKuliahCache.get(kode);
		if ((mataKuliah == null || mataKuliah.sks() == UNKNOWN_SKS && sks != UNKNOWN_SKS)
				|| (mataKuliah.nama() == UNKNOWN_NAMA && nama != UNKNOWN_NAMA)) {
			mataKuliah = new MataKuliah() {

				@Override
				public Class<? extends Annotation> annotationType() {
					return MataKuliah.class;
				}

				@Override
				public int sks() {
					return sks;
				}

				@Override
				public String nama() {
					return nama;
				}

				@Override
				public String kode() {
					return kode;
				}
			};
			mataKuliahCache.put(kode, mataKuliah);
		}
		return mataKuliah;
	}
}
