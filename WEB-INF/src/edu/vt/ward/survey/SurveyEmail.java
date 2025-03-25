package edu.vt.ward.survey;
import java.io.*;
import org.apache.log4j.*;

public class SurveyEmail implements Serializable {
  static Category log = Category.getInstance(SurveyEmail.class.getName());
  
  private String recipient;
  private String subject;
  private String message;
  private String fileName;

  public String getRecipient () { return this.recipient; }
  public String getSubject () { return this.subject; }
  public String getMessage () { return this.message; }

  public SurveyEmail () {
  }

  public SurveyEmail( String recipient, String subject, String message ) {
    this.recipient = recipient;
    this.subject = subject;
    this.message = message;
  }

  public void save () {
    try {
      // create "email" dir if necessary
      File emailsDirFile = new File ( Config.appDataDir + "emails" + Config.FILE_SEPARATOR );
      if ( !emailsDirFile.isDirectory() ) { emailsDirFile.mkdir(); }

      // loop to determine an unused file name
      File emailFile = null;
      do {
        String emailId = String.valueOf ( ( new java.util.Date () ).getTime() );
        this.fileName = "email." + emailId;
        emailFile = new File ( emailsDirFile, this.fileName );
      } while ( emailFile.exists () );

      FileOutputStream os = new FileOutputStream( emailFile );
      ObjectOutputStream o = new ObjectOutputStream(os);
      o.writeObject(this);
      o.flush();
      o.close();
      os.close();
    }
    catch (Exception e) { 
      log.error("Error saving e-mail file to queueing directory.");
    }
  }

  public SurveyEmail restore ( String fileName ) { // throws IOException, ClassNotFoundException {
    SurveyEmail restoredSurveyEmail = null;
log.debug("restoredSurveyEmail is null");
    try {
      File emailFile = new File ( Config.appDataDir + "emails" + Config.FILE_SEPARATOR + fileName );
log.debug("new email file created");
log.debug("Config.appDataDir="+Config.appDataDir);
log.debug("Config.FILE_SEPARATOR="+Config.FILE_SEPARATOR);
log.debug("fileName="+fileName);
      FileInputStream is = new FileInputStream( emailFile );
log.debug("new file input stream created");

      if ( emailFile.exists() ) {
log.debug("email file found");
        ObjectInputStream o = new ObjectInputStream(is);
log.debug("object input stream created");
        restoredSurveyEmail = (SurveyEmail)o.readObject();
log.debug("read objects");
        this.fileName = fileName;
log.debug ("filename created");
      }
      is.close();
    }
    catch (Exception e) { 
      log.error("Error reading e-mail from queueing directory.");
    }

    return restoredSurveyEmail;
  }

/*
  public SurveyEmail restore ( String fileName ) { // throws IOException, ClassNotFoundException {
    SurveyEmail restoredSurveyEmail = null;
    try {
      File emailFile = new File ( Config.appDataDir + "emails" + Config.FILE_SEPARATOR + fileName );
      FileInputStream is = new FileInputStream( emailFile );

      if ( emailFile.exists() ) {
        ObjectInputStream o = new ObjectInputStream(is);
        restoredSurveyEmail = (SurveyEmail)o.readObject();
        this.fileName = fileName;
      }
      is.close();
    }
    catch (Exception e) { 
      log.error("Error reading e-mail from queueing directory.");
    }

    return restoredSurveyEmail;
  }
*/
  public boolean send () {
    return EMail.send ( this.recipient, this.subject, this.message );
  }

  public boolean delete () {
    boolean fileDeleted = false;
    try {
      if ( this.fileName != null ) {
        File emailFile = new File ( Config.appDataDir + "emails" + Config.FILE_SEPARATOR + this.fileName );
        if ( emailFile.exists() ) {
          if ( emailFile.delete() ) {
            this.fileName = null;
            fileDeleted = true;
          }
        }
      }
    }
    catch (Exception e) { 
      log.error("Error deleting e-mail file from queueing directory.");
    }
    return fileDeleted;
  }

}