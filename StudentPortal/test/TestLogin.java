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
 * Kelas untuk mengetes permasalahan login.
 * 
 * @author FTIS\i13015
 *
 */
public class TestLogin extends WithBrowser {
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
   * Method untuk mengetes saat field username dan password kosong.
   */
//  @Test
//  public void testUserAndPassBlank() {	  
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withName("submit")).click();
//			  assertEquals("Error:Email tidak valid", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email yang dimasukkan bukan email student UNPAR, 
   * akan ditampilkan pesan “Email tidak valid”
   */
//  @Test
//  public void testUserInvalid() {	  
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailInvalid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:Email tidak valid", browser.find(".alert-danger").getText());
//          }
//      });
//  }

  /**
   * Jika email yang dimasukkan bukan email mahasiswa teknik informatika, 
   * akan ditampilkan pesan “Maaf, Anda bukan mahasiswa teknik informatika”
   */
//  @Test
//  public void testUserInvalidUnpar() {	  
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailNotStudentUnpar());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:Maaf, Anda bukan mahasiswa teknik informatika", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email dan password tidak sesuai atau mahasiswa bukan mahasiswa aktif, 
   * akan ditampilkan pesan “Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif”
   */
//  @Test
//  public void testUserValidPassInvalid() {	  
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassInvalid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email dan password tidak sesuai atau mahasiswa bukan mahasiswa aktif, 
   * akan ditampilkan pesan “Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif”
   */
//  @Test
//  public void testUserValidPassValidInactive() {	  
//      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
//          public void invoke(TestBrowser browser) {
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text("7313015@student.unpar.ac.id");
//			  browser.find(".form-control", withId("pw-input")).get(0).text("pass valid");
//			  //WARN : BELUM DICEK KARENA GA ADA ID YANG TIDAK AKTIF
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email dan password sesuai, pengguna akan diarahkan ke halaman utama.
   */
  @Test
  public void testUserAndPassValid() {
      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
        	  browser.goTo("/");
        	  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			  browser.find(".form-control", withName("submit")).get(0).click();
			  assertEquals("Selamat datang di Informatika Student Portal!", 
					  browser.find(".row").get(0).find("h2").get(0).getText());
          }
      });
  }
  
}
