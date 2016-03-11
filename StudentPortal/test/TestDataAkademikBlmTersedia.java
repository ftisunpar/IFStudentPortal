import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk melakukan pengujian pengguna belum menempuh semester 1.
 * jika pengguna masih menempuh semester 1 akan ditampilkan pesan "DATA AKADEMIK BELUM TERSEDIA"
 * 
 * @author FTIS\i13013
 *
 */
public class TestDataAkademikBlmTersedia extends FunctionalTest {
  
  /**
   * Jika pengguna belum memiliki riwayat nilai misalnya jika baru menempuh semester 1
   * akan ditampilkan "DATA AKADEMIK BELUM TERSEDIA"
   */
  @Test
  public void testDataAkademikNotAvailable() {
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
			  //System.out.println("hasil : " + e1.get(0).getText());
			  assertEquals("RINGKASAN DATA AKADEMIK", e1.get(0).getText());
			  
			  //testing
			  FluentList<FluentWebElement> e2 = browser.find("div",withClass("row"));
			  //System.out.println("hasil : " + e2.get(1).find("h5").get(0).getText());
			  assertEquals("DATA AKADEMIK BELUM TERSEDIA",
					  e2.get(1).find("h5").get(0).getText());
          }
      });
  }
}