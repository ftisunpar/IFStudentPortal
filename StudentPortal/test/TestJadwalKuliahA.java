import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes permasalahan jadwal kuliah, jika Jika pengguna belum melakukan FRS, 
 * cuti studi, atau jadwal kuliah pengguna belum tersedia, akan ditampilkan pesan 
 * “JADWAL KULIAH BELUM TERSEDIA”
 * 
 * @author FTIS\i13054
 *
 */
public class TestJadwalKuliahA extends FunctionalTest {
  /**
   * Jika pengguna menuju navigasi drawer dan melalukan click terhadap jadwal kuliah
   * namun belum FRS, cuti studi, atau jadwal kuliah pengguna belum tersedia,
   * Maka ditampilkan "JADWAL KULIAH BELUM TERSEDIA".
   */
//  @Test
//  public void testJadwalKuliahBelumFRS() {
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//        	  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  browser.goTo("/jadwalkuliah");
//			  FluentList<FluentWebElement> e1 = browser.find(".row").get(1).find("h5");
//			  if(e1.size()>0){
//				  assertEquals("JADWAL KULIAH BELUM TERSEDIA", e1.get(0).getText());
//			  } else {
//				  assertEquals("JADWAL KULIAH BELUM TERSEDIA", "testGagal");
//			  } 
//          }
//      });
//  }
}
