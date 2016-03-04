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

/**
 * 
 * Kelas untuk mengetes permasalahan jadwal kuliah, jika jadwal kuliah pengguna sudah tersedia
 * akan ditampilkan jadwal kuliah dalam bentuk kalendar yang sudah diurutkan berdasarkan hari
 * 
 * @author FTIS\i13013
 *
 */
public class TestDataAkademik extends WithBrowser {
  //basic info
  private WebDriver driver;
  private int na;
 
  private static int PORT = 9000;
  private String baseURL = String.format("http://localhost:%d", PORT);

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
//  public void testUserAndPassValid() {
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text("7313013@student.unpar.ac.id");
//			  browser.find(".form-control", withId("pw-input")).get(0).text("");
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  browser.goTo("/ringkasan");
//			  assertEquals("RINGKASAN DATA AKADEMIK",
//					  browser.find(".row").get(0).find("h2").get(0).getText());
//			  
//			  String [] temp = new String[4];
//			  temp = browser.find(".row").get(1).find(".col-lg-8 ringkasan-panel").get(0).find(".ringkasan-body").get(0).getText().split("</br>");
//			  assertEquals("IPS",temp[0]);
//			  assertEquals("IPK(lulus)",temp[1]);
//			  assertEquals("SKS lulus",temp[2]);
//			  assertEquals("Sisa SKS untuk kelulusan",temp[3]);
//			  
//			  assertEquals("PILIHAN WAJIB",
//					  browser.find(".row").get(2).find("col-lg-8 ringkasan-panel").get(1).find("page-header").get(0).find("text-center").get(0).getText());
//          }
//      });
//  }
}