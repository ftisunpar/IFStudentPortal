import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk melakukan pengujian data akademik mahasiswa.
 * Jika pengujian berhasil jika menampilkan IPS semester terakhir,
 * IPK, SKS lulus, sisa SKS kelulusan, dan ringkasan data mengenai mata kuliah pilihan wajib
 * 
 * @author FTIS\i13013
 *
 */
public class TestDataAkademikLainnya extends FunctionalTest {
  
  /**
   * Jika pengguna sudah memiliki riwayat nilai, akan ditampilkan ringkasan data akademik mahasiswa 
   * berupa IPS semester terakhir, IPK, SKS lulus, sisa SKS kelulusan, dan ringkasan
   */
  @Test
  public void testDataAkademik() {
      running(server, new Runnable() {
    	  @Override
          public void run() {
        	  browser.goTo("/");
			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			  browser.find(".form-control", withName("submit")).get(0).click();
			  browser.goTo("/ringkasan");

			  //cek berhasil masuk ke halaman Ringkasan Data Akademik
			  FluentList<FluentWebElement> e1 = browser.find("h2",withClass("text-center"));
			  assertEquals("RINGKASAN DATA AKADEMIK", e1.get(0).getText());
			  
			  //testing
			  String [] temp = new String[4];
			  temp = browser.find(".ringkasan-body").get(0).getText().split("\n");
			  assertEquals("IPS",temp[0].substring(0, 3));
			  assertEquals("IPK (Lulus)",temp[1].substring(0, 11));
			  assertEquals("SKS lulus",temp[2].substring(0, 9));
			  assertEquals("Sisa SKS untuk kelulusan",temp[3].substring(0,24));
			  
			  assertEquals("PILIHAN WAJIB", browser.find("h5", withClass("text-center")).get(1).getText());
          }
      });
  }
}