import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running; 
import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes permasalahan login.
 * 
 * @author FTIS\i13015
 *
 */
public class TestLogin extends FunctionalTest {
  /**
   * Method untuk mengetes saat field username dan password kosong.
   */
//  @Test
//  public void testUserAndPassBlank() {	  
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//			  browser.find(".form-control", withName("submit")).click();
//			  assertEquals("Error:\nEmail tidak valid", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email yang dimasukkan bukan email student UNPAR, 
   * akan ditampilkan pesan “Email tidak valid”
   */
//  @Test
//  public void testUserInvalid() {	  
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailInvalid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:\nEmail tidak valid", browser.find(".alert-danger").getText());
//          }
//      });
//  }

  /**
   * Jika email yang dimasukkan bukan email mahasiswa teknik informatika, 
   * akan ditampilkan pesan “Maaf, Anda bukan mahasiswa teknik informatika”
   */
//  @Test
//  public void testUserInvalidITUnpar() {	  
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailNotStudentUnpar());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:\nMaaf, Anda bukan mahasiswa teknik informatika", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email dan password tidak sesuai
   * akan ditampilkan pesan “Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif”
   */
//  @Test
//  public void testUserValidPassInvalid() {	  
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassInvalid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:\nPassword yang Anda masukkan salah atau Anda bukan mahasiswa aktif", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email dan password sesuai tetapi bukan mahasiswa aktif, 
   * akan ditampilkan pesan “Password yang Anda masukkan salah atau Anda bukan mahasiswa aktif”
   */
//  @Test
//  public void testUserValidPassValidInactive() {	  
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailNotActive());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPasswordNotActive());
//			  //WARN : BELUM DICEK KARENA TIDAK ADA ID YANG TIDAK AKTIF
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Error:\nPassword yang Anda masukkan salah atau Anda bukan mahasiswa aktif", browser.find(".alert-danger").getText());
//          }
//      });
//  }
  
  /**
   * Jika email dan password sesuai, pengguna akan diarahkan ke halaman utama.
   */
//  @Test
//  public void testUserAndPassValid() {
//      running(server, new Runnable() {
//    	  @Override
//          public void run() {  
//        	  browser.goTo("/");
//        	  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
//			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
//			  browser.find(".form-control", withName("submit")).get(0).click();
//			  assertEquals("Selamat datang di Informatika Student Portal!", 
//					  browser.find(".row").get(0).find("h2").get(0).getText());
//          }
//      });
//  }
  
}
