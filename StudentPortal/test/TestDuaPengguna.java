import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import play.test.TestBrowser;
import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;


/**
 * Kelas untuk melakukan pengujian dua pengguna menjalankan aplikasi secara bersamaan.
 * 
 * @author FTIS\i13013
 *
 */
public class TestDuaPengguna extends FunctionalTest {
	
  private WebDriver driver2  = new FirefoxDriver();
  private TestBrowser browser2 = new TestBrowser(driver2, baseURL);
    
  @After
  public void tearDown() {
	  browser.quit();
	  this.browser2.quit();
  }
 
  @Test
  public void testDuaPengguna() {	  
      running(server, new Runnable() {
		@Override
		public void run() {
			browser.goTo("/");
			browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			browser.find(".form-control", withName("submit")).get(0).click();
			browser.goTo("/prasyarat");
			  assertEquals("PEMERIKSAAN PRASYARAT MATA KULIAH", 
					 browser.find(".row").get(0).find("h2").get(0).getText());
			browser2.goTo("/");
			browser2.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
		    browser2.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
		    browser2.find(".form-control", withName("submit")).get(0).click();
			browser2.goTo("/jadwalkuliah");
			  assertEquals("JADWAL KULIAH", 
					  browser2.find(".row").get(0).find("h2").get(0).getText());
		}   
      });
  }
}
