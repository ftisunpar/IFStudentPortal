import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import play.test.TestBrowser;
import play.test.WithBrowser;
import play.libs.F.Callback;
import static play.test.Helpers.HTMLUNIT; 
import static play.test.Helpers.running; 
import static play.test.Helpers.testServer;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Kelas untuk mengetes permasalahan prasyarat matakuliah, jika 
 * pengguna sudah memiliki riwayat nilai akan ditampilkan tabel prasyarat mata
 * kuliah beserta status pengambilannya
 * 
 * @author FTIS\i13006
 *
 */
public class TestPrasyaratMataKuliahLainnya extends WithBrowser {
  //basic info
  private WebDriver driver;
  private static int PORT = 9000;
  private String baseURL = String.format("http://localhost:%d", PORT);
  private FileConfReader objFileConfReader = FileConfReader.getObjFileConfReader();
  
  @Before
  public void setUp() {	
	driver = new FirefoxDriver();
	browser = new TestBrowser(driver, baseURL);
  }
  
  @After
  public void tearDown() {
	  browser.quit();
  }

  /**
   * Jika pengguna menuju navigasi drawer dan melalukan click terhadap prasyarat matakuliah
   * akan ditampilkan halaman prasyarat matakuliah beserta tabel prasyarat mata
   * kuliah beserta status pengambilannya
   */
//  @Test
//  public void testPrasyaratMataKuliah() {
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  browser.goTo("/prasyarat");
//			  assertEquals("PEMERIKSAAN PRASYARAT MATA KULIAH", 
//					  browser.find(".row").get(0).find("h2").get(0).getText());
//			  assertEquals("Keterangan", 
//					  browser.find(".table-bordered").get(0).find("th").get(2).getText());
//          }
//      });
//  }
  
}
