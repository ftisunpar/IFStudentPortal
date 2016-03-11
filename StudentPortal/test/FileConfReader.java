import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Kelas untuk membaca conf\functionaltest.conf
 * File tersebut berisi id dan password yang digunakan untuk test-test yang akan dilakukan
 * Kelas ini bersifat singleton
 * @author FTIS\i13006
 *
 */
public class FileConfReader {
	  private Properties prop = new Properties();
	  private InputStream input = null;
	  private String email_invalid;
	  private String email_notStudentUnpar;
	  private String pass_notStudentUnpar;
	  private String pass_invalid;
	  private String email_valid;
	  private String pass_wrong;
	  private String pass_valid;
	  
	  private String email_smt1;
	  private String pass_smt1;
	  private String email_notactive;
	  private String pass_notactive;
	  private static FileConfReader objFileConfReader;
	  
	  FileConfReader(){
		  try {
  			input = new FileInputStream("conf/functionaltest.conf");
  			prop.load(input);
  			email_invalid=prop.getProperty("email.invalid");
  			email_notStudentUnpar=prop.getProperty("email.notStudentUnpar");
  			pass_notStudentUnpar=prop.getProperty("pass.notStudentUnpar");
  			pass_invalid=prop.getProperty("password.invalid");
  			email_valid=prop.getProperty("email.valid");
  			pass_wrong=prop.getProperty("password.wrong");
  			pass_valid=prop.getProperty("password.valid");
  			email_smt1=prop.getProperty("email.smt1");
  			pass_smt1=prop.getProperty("password.smt1");
  			email_notactive=prop.getProperty("email.notActive");
  			pass_notactive=prop.getProperty("password.notActive");
  		} catch (IOException ex) {
  			ex.printStackTrace();
  		} finally {
  			if (input != null) {
  				try {
  					input.close();
  				} catch (IOException e) {
  					e.printStackTrace();
  				}
  			}
  		}
	  }
	  
	  public String getEmailInvalid(){
		  return this.email_invalid;
	  }
	  
	  public String getEmailNotStudentUnpar(){
		  return this.email_notStudentUnpar;
	  }
	  public String getPassNotStudentUnpar(){
		  return this.pass_notStudentUnpar;
	  }
	  
	  
	  public String getPassInvalid(){
		  return this.pass_invalid;
	  }
	  
	  public String getEmailValid(){
		  return this.email_valid;
	  }

	  public String getPassWrong(){
		  return this.pass_wrong;
	  }
	  
	  public String getPassValid(){
		  return this.pass_valid;
	  }
	  public String getEmailSmt1(){
		  return this.email_smt1;
	  }
	  public String getPassSmt1(){
		  return this.pass_smt1;
	  }
	  
	  public String getEmailNotActive() {
		  return this.email_notactive;
	  }
	  
	  public String getPasswordNotActive() {
		  return this.pass_notactive;
	  }
	  
	  /**
	   * Method untuk mendapatkan objek FileConfReader
	   * @return obj FileConfReader
	   */
	  public static FileConfReader getObjFileConfReader(){
		  if(objFileConfReader==null){
			  objFileConfReader= new FileConfReader();
			  return objFileConfReader;
		  }
		  return objFileConfReader;
	  }

}
