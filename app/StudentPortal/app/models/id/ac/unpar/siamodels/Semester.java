package models.id.ac.unpar.siamodels;

/**
 * Menyimpan konstanta untuk semester-semester di UNPAR. Nilai konstanta
 * harus sesuai urutan kronologis dalam satu tahun ajaran.
 * @author pascal
 *
 */
public final class Semester {
	public static final int PENDEK = 0;
	public static final int GANJIL = 1;
	public static final int GENAP = 2;

	public static final int fromString(String text) {
		switch (text.toUpperCase()) {
		case "PENDEK":
			return Semester.PENDEK;
		case "GANJIL":
			return Semester.GANJIL;
		case "GENAP":
			return Semester.GENAP;
		}
		return -1;
	}
}
