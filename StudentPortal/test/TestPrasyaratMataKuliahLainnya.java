import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes permasalahan prasyarat matakuliah, jika 
 * pengguna sudah memiliki riwayat nilai akan ditampilkan tabel prasyarat mata
 * kuliah beserta status pengambilannya
 * 
 * @author FTIS\i13006
 *
 */
public class TestPrasyaratMataKuliahLainnya extends FunctionalTest {

  /**
   * Jika pengguna menuju navigasi drawer dan melalukan click terhadap prasyarat matakuliah
   * akan ditampilkan halaman prasyarat matakuliah beserta tabel prasyarat mata
   * kuliah beserta status pengambilannya
   */
//  @Test
//  public void testPrasyaratMataKuliah() {
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  browser.goTo("/prasyarat");
//			  
//			  assertEquals("PEMERIKSAAN PRASYARAT MATA KULIAH", 
//					  browser.find(".row").get(0).find("h2").get(0).getText());
//			  assertEquals("Keterangan", 
//					  browser.find(".table-bordered").get(0).find("th").get(2).getText());
//          }
//      });
//  }
  
}
