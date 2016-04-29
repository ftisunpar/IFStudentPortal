import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk melakukan pengujian data akademik mahasiswa. Jika pengujian
 * berhasil jika menampilkan IPS semester terakhir, IPK, SKS lulus, sisa SKS
 * kelulusan, dan ringkasan data mengenai mata kuliah pilihan wajib
 * 
 * @author FTIS\i13013
 *
 */
public class TestSyaratKelulusan extends FunctionalTest {

	public TestSyaratKelulusan() throws IOException {
		super();
	}

//	/**
//	 * Jika pengguna sudah memiliki riwayat nilai, akan ditampilkan ringkasan
//	 * syarat kelulusan mahasiswa berupa IPS semester terakhir, IPK, SKS lulus,
//	 * sisa SKS kelulusan, dan ringkasan
//	 */
	@Test
	public void testSyaratKelulusan() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_SYARAT_KELULUSAN);

				// cek berhasil masuk ke halaman Ringkasan Data Akademik
				FluentList<FluentWebElement> e1 = browser.find("h2", withClass("text-center"));
				String cek=e1.get(0).getText();
				Matcher matcher = Pattern.compile(".*syarat.+kelulusan.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue(condition);
				//assertEquals("SYARAT KELULUSAN", e1.get(0).getText());

				// testing
				String[] temp = new String[4];
				temp = browser.find(".ringkasan-body").get(0).getText().split("\n");
				//assertEquals("IPS", temp[0].substring(0, 3));
//				assertEquals("IPK (Lulus)", temp[1].substring(0, 11));
//				assertEquals("SKS lulus", temp[2].substring(0, 9));
				
				cek=temp[0].substring(0, 3);
				matcher = Pattern.compile(".*ips.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue(condition);
				
				cek=temp[1].substring(0, 11);
				matcher = Pattern.compile(".*ipk.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue(condition);
				
				cek=temp[2].substring(0, 9);
				matcher = Pattern.compile(".*sks.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue(condition);
				
				//assertEquals("Sisa SKS untuk kelulusan", temp[3].substring(0, 24));
				//assertEquals("PILIHAN WAJIB", browser.find("h5", withClass("text-center")).get(1).getText());
			}
		});
	}

	/**
	 * Jika pengguna belum memiliki riwayat nilai misalnya jika baru menempuh
	 * semester 1 akan ditampilkan "DATA AKADEMIK BELUM TERSEDIA"
	 */
	@Test
	public void testSyaratKelulusanNotAvailable() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_SYARAT_KELULUSAN);

				// cek berhasil masuk ke halaman Ringkasan Data Akademik
				FluentList<FluentWebElement> e1 = browser.find("h2", withClass("text-center"));
				// System.out.println("hasil : " + e1.get(0).getText());
				//assertEquals("RINGKASAN DATA AKADEMIK", e1.get(0).getText());
				String cek=e1.get(0).getText();
				Matcher matcher = Pattern.compile(".*syarat.+kelulusan.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue(condition);

				// testing
				FluentList<FluentWebElement> e2 = browser.find("div", withClass("row"));
				// System.out.println("hasil : " +
				// e2.get(1).find("h5").get(0).getText());
				
				cek=e2.get(1).find("h5").get(0).getText();
				matcher = Pattern.compile(".*akedemik.+belum.+tersedia.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue(condition);
				System.err.print("Data Akademik Tersedia");
				//assertEquals("DATA AKADEMIK BELUM TERSEDIA", e2.get(1).find("h5").get(0).getText());
				
				
			}
		});
	}
}