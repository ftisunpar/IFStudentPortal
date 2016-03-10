
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
 * Kelas untuk mengetes permasalahan prasyarat matakuliah, Jika pengguna 
 * belum memiliki riwayat nilai(masih menempuh semester 1), akan 
 * ditampilkan pesan “PRASYARAT BELUM TERSEDIA”
 * 
 * @author FTIS\i13054
 *
 */
public class TestPrasyaratMKSmt1 extends WithBrowser {
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
   * akan ditampilkan  pesan “PRASYARAT BELUM TERSEDIA”
   */
//  @Test
//  public void testUserAndPassSmt1() {
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailSmt1());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassSmt1());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  browser.goTo("/prasyarat");
//			  assertEquals("PRASYARAT BELUM TERSEDIA", 
//					  browser.find(".row").get(0).find("h2").get(0).getText());
//
//          }
//      });
//  }
  
}
