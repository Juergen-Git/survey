package edu.vt.ward.survey;
import java.util.*;
import java.io.*;
import org.apache.log4j.*;

public class UpdateStatsTimerTask extends TimerTask {
  static Category log = Category.getInstance(UpdateStatsTimerTask.class.getName());
  
  public UpdateStatsTimerTask (){
    //any number of arguments could be used in the contrustuctor
    super();
  }

  // this is the method that will get called when the Timer executes this task
  public void run(){
    // This is where you'd put your code that you want done when the Timer executes this event.
    log.debug("Updating usage statistics.");

    if ( !Config.statsUpdateRunning ) { // avoid that it's called more than once concurrently
      Config.statsUpdateRunning = true;
      Config.statsRegisteredUsers = 0;
      Config.statsUsersWithRespectableSurvey = 0;
      Config.statsNewUsers7days = 0;
      Config.statsNewUsers24hours = 0;
      Config.statsSurveys = 0;
      Config.statsRespectableSurveys = 0;
      Config.statsOpenSurveys = 0;
      Config.statsNewEntries7days = 0;
      Config.statsNewEntries24hours = 0;

      java.util.Date date = new java.util.Date ();
      long currentTime = date.getTime();
      Hashtable surveyCounted = new Hashtable();

      // find & delete all users who's directory is older than X minutes
      File userDataDirFile = new File ( Config.appDataDir + "users" + Config.FILE_SEPARATOR );
      java.io.File[] userFileList = userDataDirFile.listFiles( );
      for ( int i = 0; i < userFileList.length; i++ ) {
        if ( !userFileList[i].getName().equals("admin") ) { // don't delete "admin" dir
          if ( userFileList[i].isDirectory() ) {
            User user = new User ( Config.appDataDir, userFileList[i].getName() );
            if ( user.exists () ) {
              Config.statsRegisteredUsers++;

              if ( currentTime - userFileList[i].lastModified() <= 7*24*60*60*1000 ) {
                Config.statsNewUsers7days++;
              }
              if ( currentTime - userFileList[i].lastModified() <= 24*60*60*1000 ) {
                Config.statsNewUsers24hours++;
              }

              boolean hasRespectableSurvey = false;
              // parse all surveys of this user
              SurveyMetaDataList surveyMetaDataList = new SurveyMetaDataList ( Config.appDataDir, user.getSurveyIdList () );
              Iterator surveys = surveyMetaDataList.getSortedByStatus ();
              while (surveys.hasNext () ) {
                SurveyMetaData s = (SurveyMetaData) surveys.next ();
                if ( s.exists() ) {
                  String sid = s.getId();
                  if ( s.getNumEntries() >= 3 ) { hasRespectableSurvey = true; }
                  if ( !surveyCounted.containsKey(sid) ) {
                    surveyCounted.put(sid,"1");
                    Config.statsSurveys++;
                    Config.statsNewEntries7days += s.getNumEntriesYoungerThan7days();
                    Config.statsNewEntries24hours += s.getNumEntriesYoungerThan24hours();
                    if ( s.getNumEntries() >= 3 ) { Config.statsRespectableSurveys++; }
                    if ( s.isAcceptingDataEntry () ) { Config.statsOpenSurveys++; }
                  }
                } // end: if ( s.exists() )
              } // end: while (surveys.hasNext () ) {

              if ( hasRespectableSurvey ) {
                Config.statsUsersWithRespectableSurvey++;
              }

            } // end: if ( user.exists () )

          } // end: if ( userFileList[i].isDirectory() )
        }
      }
      Config.statsLastUpdated = (new Date()).getTime();
      Config.statsUpdateRunning = false;
    } // end: if ( !Config.statsUpdateRunning )
    else {
      log.error("Not starting usage statistics update because it is already running.");
    }
    log.debug("Done with updating usage statistics.");
  }

}