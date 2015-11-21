package models.id.ac.unpar.siamodels;

import java.lang.annotation.Annotation;
import java.util.SortedMap;
import java.util.logging.Logger;

public class MataKuliahFactory {

	/**
	 * Lokasi package untuk daftar mata kuliah
	 */
	private static String DEFAULT_MATAKULIAH_PACKAGE = "models.id.ac.unpar.siamodels.matakuliah";
	
	/**
	 * Menandakan jumlah SKS tidak diketahui.
	 */
	public static int UNKNOWN_SKS = Integer.MIN_VALUE;
	
	/**
	 * Menandakan nama mata kuliah tidak diketahui.
	 */
	public static String UNKNOWN_NAMA = null;
	
	/**
	 * Membuat objek mata kuliah baru. Jika memungkinkan mengambil dari kelas
	 * yang sudah ada.
	 * 
	 * @param kode
	 *            kode mata kuliah
	 * @param sks
	 *            jumlah SKS atau isi dengan {@link #UNKNOWN_SKS} jika tidak tahu.
	 * @param nama
	 *            nama mata kuliah atau isi dengan {@link #UNKNOWN_NAMA} jika tidak tahu.
	 * @return objek mata kuliah
	 * @throws IllegalStateException
	 *             jika sks dan tidak sesuai dengan yang ada di kode
	 */
	public static MataKuliah createMataKuliah(final String kode, final int sks, final String nama)
			throws IllegalStateException {
		Class<?> mkClass;
		try {
			mkClass = Class.forName(DEFAULT_MATAKULIAH_PACKAGE + "." + kode);
			if (mkClass.isAnnotationPresent(MataKuliah.class)) {
				MataKuliah matakuliah = (MataKuliah)mkClass.getAnnotation(MataKuliah.class);
				if (sks != UNKNOWN_SKS && matakuliah.sks() != sks) {
					throw new IllegalStateException(String.format(kode+ ": SKS yang diberikan %d tidak sama dengan yang tercatat %d, dan bukan UNKNOWN_SKS.", sks, matakuliah.sks()));
				}
				return matakuliah;
			} else {
				Logger.getGlobal().warning("Class is listed but not annotated: " + kode);
			}
		} catch (ClassNotFoundException e) {
			Logger.getGlobal().warning("Class is not listed: " + kode);
		}

		// Do default return
		return new MataKuliah() {

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
	}
}