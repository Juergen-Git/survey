package edu.vt.ward.survey;
import java.util.*;
import java.io.*;

public class DemoModeDeleteTimerTask extends TimerTask {
  public DemoModeDeleteTimerTask (){
    //any number of arguments could be used in the contrustuctor
    super();
  }

  // this is the method that will get called when the Timer executes this task
  public void run(){
    // This is where you'd put your code that you want done when the Timer executes this event.

    // find & delete all users who's directory is older than X minutes
    File userDataDirFile = new File ( Config.appDataDir + "users" + Config.FILE_SEPARATOR );
    java.io.File[] userFileList = userDataDirFile.listFiles( );
    for ( int i = 0; i < userFileList.length; i++ ) {
      if ( !userFileList[i].getName().equals("admin") ) { // don't delete "admin" dir
        if ( userFileList[i].isDirectory() ) {
          if ( userFileList[i].getName().length() > Config.demoModeUser.length()+1 &&
               userFileList[i].getName().substring(0,Config.demoModeUser.length()+1).equals(Config.demoModeUser+".")
             ) {
            java.util.Date date = new java.util.Date ();
            if ( date.getTime() - userFileList[i].lastModified() > Config.demoModeLifetimeMinutes*60*1000 ) {
              User user = new User ( Config.appDataDir, userFileList[i].getName() );
              if ( user.exists() ) {
                // delete all surveys where this user is an administrator of
                SurveyMetaDataList surveyMetaDataList = new SurveyMetaDataList ( Config.appDataDir, user.getSurveyIdList () );
                Iterator surveys = surveyMetaDataList.getSortedByStatus ();
                while (surveys.hasNext () ) {
                  SurveyMetaData s = (SurveyMetaData) surveys.next ();
                  s.delete();
                } // end: while (surveys.hasNext () )

                // delete user
                user.remove();
              } // end: if ( user.exists() )
            }
          }
        } // end: if ( userFileList[i].isDirectory() )
      }
    }
  }

}