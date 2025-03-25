package edu.vt.ward.survey;
import java.util.*;
import java.io.*;
import org.apache.log4j.*;

public class SendEmailTimerTask extends TimerTask {
  static Category log = Category.getInstance(SendEmailTimerTask.class.getName());

  public SendEmailTimerTask (){
    //any number of arguments could be used in the contrustuctor
    super();
  }

  // this is the method that will get called when the Timer executes this task
  public void run(){
    // This is where you'd put your code that you want done when the Timer executes this event.
    log.debug("Attempting to send all queued e-mail messages.");

    try {
      // scan e-mail directory for new mail that is to be sent
      File emailsDirFile = new File ( Config.appDataDir + "emails" + Config.FILE_SEPARATOR );
      if ( !emailsDirFile.isDirectory() ) { emailsDirFile.mkdir(); }
      java.io.File[] emailsFileList = emailsDirFile.listFiles( new FilenameFilterEmailFile () );
  
      for ( int i = 0; i < emailsFileList.length; i++ ) {
        SurveyEmail surveyEmail = new SurveyEmail ().restore ( emailsFileList [i].getName() );
        if ( surveyEmail.send () ) {
          surveyEmail.delete ();
        }
        else {
          log.error("Could not send e-mail. Check your mail server settings.");
        }
      }
    }
    catch (Exception e) { 
      log.error("Error while trying to send queued e-mail messages.");
    }
  }

}