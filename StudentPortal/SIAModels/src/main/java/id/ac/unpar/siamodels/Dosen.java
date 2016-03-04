package id.ac.unpar.siamodels;

public class Dosen {
	private String nik;
	private String nama;

	public Dosen(String nik, String nama) throws IllegalArgumentException {
		super();
		if (nik == null && nama == null) {
			throw new IllegalArgumentException("Salah satu dari NIK atau nama harus diisi!");
		}
		this.nik = nik;
		this.nama = nama;
	}
	
	public String getNik() {
		return nik;
	}
	
	public void setNik(String nik) {
		this.nik = nik;
	}
	
	public String getNama() {
		return nama;
	}
	
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * Equality check for dosen. First check NIK if both available.
	 * Otherwise, check for name.
	 * @return true if equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dosen other = (Dosen) obj;
		if (nik != null && other.nik != null) {
			return nik.equals(other.nik);
		}
		if (nama != null) {
			return nama.equals(other.nama);
		}
		return false;
	}
	
	
}
