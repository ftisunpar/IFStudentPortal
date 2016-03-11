import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes saat pengguna logout saat sudah berhasil masuk ke aplikasi.
 * 
 * @author FTIS\i13015
 *
 */
public class TestLogout extends FunctionalTest {
  /**
   * Pengguna logout, pengguna akan diarahkan kembali ke halaman login
   */
  @Test
  public void testLogout() {
      running(server, new Runnable() {
    	  @Override
          public void run() {  
			  browser.goTo("/");
			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			  browser.find(".form-control", withName("submit")).get(0).click();
			  browser.goTo("/logout");
			  assertEquals("Login", browser.find(".form-control", withName("submit")).get(0).getText());
		  }
	  });
  }
  
}
