import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
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

import java.util.Arrays;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes data akademik mahasiswa, akan menampilkan IPS semester terakhir,
 * IPK, SKS lulus, sisa SKS kelulusan, dan ringkasan data mengenai mata kuliah pilihan wajib
 * 
 * @author FTIS\i13013
 *
 */
public class TestDataAkademikLainnya extends WithBrowser {
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
   * Jika pengguna sudah memiliki riwayat nilai, akan ditampilkan ringkasan data akademik mahasiswa 
   * berupa IPS semester terakhir, IPK, SKS lulus, sisa SKS kelulusan, dan ringkasan
   */
//  @Test
//  public void testDataAkademik() {
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  browser.goTo("/ringkasan");
//			  
//			  
//			  
//			  FluentList<FluentWebElement> e1 = browser.find("div",withClass("row"));
//
//			  FluentList<FluentWebElement> e2 = browser.find("h2",withClass("text-center"));
//			  
//			  assertEquals("RINGKASAN DATA AKADEMIK",
//					e2.getText());
//			  
//			  String [] temp = new String[4];
//			  temp = browser.find(".ringkasan-body").get(0).getText().split("\n");
//			  assertEquals("IPS",temp[0].substring(0, 3));
//			  assertEquals("IPK (Lulus)",temp[1].substring(0, 11));
//			  assertEquals("SKS lulus",temp[2].substring(0, 9));
//			  assertEquals("Sisa SKS untuk kelulusan",temp[3].substring(0,24));
//			  
//			  assertEquals("PILIHAN WAJIB",
//					  browser.find("h5", withClass("text-center")).get(1).getText());
//          }
//      });
//  }
}