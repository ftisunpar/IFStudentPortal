package id.ac.unpar.siamodels;

public enum Semester {
	UNKNOWN5(5), TRANSFER(6), PENDEK(10), GANJIL(20), GENAP(30);

	public static Semester fromString(String text) {
		return Semester.valueOf(text.toUpperCase());
	}
	
	private int order;
	
	private Semester(int order) {
		this.order = order;
	}
	
	int getOrder() {
		return order;
	}
	
}
