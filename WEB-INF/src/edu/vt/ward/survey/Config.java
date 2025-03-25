package edu.vt.ward.survey;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import javax.servlet.*;
import org.apache.log4j.*;

public class Config {
  static Category log = Category.getInstance(Config.class.getName());
  
  public static String FILE_SEPARATOR = System.getProperty( "file.separator" ); // mostly "/"
  public static String validUsersDTD = "validUsers.dtd";
  public static String validUsersXML = "validUsers.xml";

  public static String hostName = "";
  public static String bgColor = "";
  public static String feedbackURL = "";
  public static String rootURL = "";
  public static String appDataDir = "";
  public static String pageHeaderFile = "";
  public static String pageFooterFile = "";
  public static String languageFileName = "";
  public static int minLenSurveyName = 0;
  public static int maxLenSurveyName = 0;
  public static int maxLenResultsPassword = 0;
  public static String regexValidResultsPassword = "";
  public static String regexValidSurveyNameLetter = "";
  public static String regexValidSurveyName = "";
  public static String regexValidEntryPassword = "";
  public static String regexValidEntryPasswordLetter = "";
  public static String regexValidPID = "";
  public static int maxLenPID = 8;
  public static int maxLenPassword = 12;
  public static int minLenEntryPassword;
  public static int maxLenEntryPassword;
  public static String smtpHost = "";
  public static String emailFrom = "";
  public static String adminEmail = "";
  public static String cryptSalt = "";
  public static String ldapAuthHost = "";
  public static String ldapAuthPort = "";
  public static String ldapAuthBaseDN = "";
  public static String ldapAuthServiceUser = "";
  public static String ldapAuthServiceCredential = "";
  public static String ldapAuthUidAttribute = "";
  public static String ldapAuthAuthType = "";
  public static boolean ldapAuthConstructDn = false;
  public static boolean ldapAuthTls = false;
  public static String ldapAuthSslSocketFactory = "";
  public static String ldapNameHost = "";
  public static String ldapNamePort = "";
  public static String ldapNameBaseDN = "";
  public static String ldapNameServiceUser = "";
  public static String ldapNameServiceCredential = "";
  public static String ldapNameUidAttribute = "";
  public static String ldapNameFullNameAttribute = "";
  public static String ldapNameAuthType = "";
  public static String ldapADdomain ="";
  public static String loginLogFileName = "";
  public static String loginLogFilePath = "";
  public static String accountCreationLogFileName = "";
  public static String accountCreationLogFilePath = "";
  public static boolean enableSelfSignup = false;
  public static boolean authenticationMethodStandalone = true;
  public static String regexValidUserPassword = "";
  public static boolean demoModeEnabled = false;
  public static String demoModeUser = "";
  public static String demoModePassword = "";
  public static int demoModeLifetimeMinutes = 60;
  public static boolean enableEmailAdminOnNewUser = false;
  public static boolean enableEmailAdminOnOpenSurvey = false;
  public static boolean enableAutoUpdateStats = false;
  public static String serviceName = "";
  public static String serviceProvider = "";
  public static String acceptableUsePolicyURL = "";
  public static String xmlEncoding = "";
  public static String urlScheme = "";

  public static boolean statsUpdateRunning = false;
  public static long statsLastUpdated = 0;
  public static long statsRegisteredUsers = 0;
  public static long statsUsersWithRespectableSurvey = 0;
  public static long statsNewUsers7days = 0;
  public static long statsNewUsers24hours = 0;
  public static long statsSurveys = 0;
  public static long statsRespectableSurveys = 0;
  public static long statsOpenSurveys = 0;
  public static long statsNewEntries7days = 0;
  public static long statsNewEntries24hours = 0;

  public static int minLenEmail = 6;
  public static int maxLenEmail = 100;
  public static String regexValidEmail = "^[\\w\\.=-]+@[\\w\\.-]+\\.[a-z]{2,3}$";
  public static String tab = String.valueOf ( '\u0009' );
  public static String regexValidSurveyId = "^[0-9]{13,13}$";
  public static Hashtable authorizationCache = new Hashtable ();
  public static Hashtable resultSummaryForms = new Hashtable ();
  public static Hashtable surveyEntryIdUsed = new Hashtable (); // Hashtable of surveyIds that points to individual Hashtables holding used entryIDs
  public static Timer timer;

  public static String [] fonts = {
    "'Courier New', Courier, monospace",
    "'Times New Roman', Times, serif",
    "'MS Sans Serif', Geneva, sans-serif",
    "Verdana, Geneva, Arial, Helvetica, sans-serif",
    "Arial, Helvetica, sans-serif"
  };

  public static String [] fontsNames = {
    "Courier",
    "Times New Roman",
    "Sans Serif",
    "Verdana",
    "Arial"
  };
  public static Hashtable messages = new Hashtable();


  public static void setInitParams ( ServletContext application ) {
    appDataDir = (String) application.getInitParameter ( "appDataDir" );
    hostName = (String) application.getInitParameter ( "hostName" );
    bgColor = (String) application.getInitParameter ( "bgColor" );
    feedbackURL = (String) application.getInitParameter ( "feedbackURL" );
    rootURL = (String) application.getInitParameter ( "rootURL" );
    pageHeaderFile = (String) application.getInitParameter ( "pageHeaderFile" );
    pageFooterFile = (String) application.getInitParameter ( "pageFooterFile" );
    languageFileName = (String) application.getInitParameter ( "languageFileName" );
    ldapAuthHost = (String) application.getInitParameter ( "ldapAuthHost" );
    ldapAuthPort = (String) application.getInitParameter ( "ldapAuthPort" );
    ldapAuthBaseDN = (String) application.getInitParameter ( "ldapAuthBaseDN" );
    ldapAuthServiceUser = (String) application.getInitParameter ( "ldapAuthServiceUser" );
    ldapAuthServiceCredential = (String) application.getInitParameter ( "ldapAuthServiceCredential" );
    ldapAuthUidAttribute = (String) application.getInitParameter ( "ldapAuthUidAttribute" );
    ldapAuthAuthType = (String) application.getInitParameter ( "ldapAuthAuthType" );
    String ldapAuthConstructDnStr = (String) application.getInitParameter ( "ldapAuthConstructDn" );
    ldapAuthConstructDn = ( ldapAuthConstructDnStr != null & ldapAuthConstructDnStr.equals("1") );
    String ldapAuthTlsStr = (String) application.getInitParameter ( "ldapAuthTls" );
    ldapAuthTls = ( ldapAuthTlsStr != null & ldapAuthTlsStr.equals("1") );
    ldapAuthSslSocketFactory = (String) application.getInitParameter ( "ldapAuthSslSocketFactory" );
    
    ldapNameHost = (String) application.getInitParameter ( "ldapNameHost" );
    ldapNamePort = (String) application.getInitParameter ( "ldapNamePort" );
    ldapNameBaseDN = (String) application.getInitParameter ( "ldapNameBaseDN" );
    ldapNameServiceUser = (String) application.getInitParameter ( "ldapNameServiceUser" );
    ldapNameServiceCredential = (String) application.getInitParameter ( "ldapNameServiceCredential" );
    ldapNameUidAttribute = (String) application.getInitParameter ( "ldapNameUidAttribute" );
    ldapNameFullNameAttribute = (String) application.getInitParameter ( "ldapNameFullNameAttribute" );
    ldapNameAuthType = (String) application.getInitParameter ( "ldapNameAuthType" );
    ldapADdomain = (String) application.getInitParameter ( "ldapADdomain" );
    minLenSurveyName = Integer.parseInt ( application.getInitParameter ( "minLenSurveyName" ) );
    maxLenSurveyName = Integer.parseInt ( application.getInitParameter ( "maxLenSurveyName" ) );
    maxLenResultsPassword = Integer.parseInt ( application.getInitParameter ( "maxLenResultsPassword" ) );
    regexValidResultsPassword = (String) application.getInitParameter ( "regexValidResultsPassword" );
    regexValidSurveyNameLetter = (String) application.getInitParameter ( "regexValidSurveyNameLetter" );
    regexValidSurveyName = "^" + Config.regexValidSurveyNameLetter + "{" + Config.minLenSurveyName + "," + Config.maxLenSurveyName + "}$";
    smtpHost = (String) application.getInitParameter ( "smtpHost" );
    emailFrom = (String) application.getInitParameter ( "emailFrom" );
    adminEmail = (String) application.getInitParameter ( "adminEmail" );
    cryptSalt = (String) application.getInitParameter ( "cryptSalt" );
    loginLogFileName = (String) application.getInitParameter ( "loginLogFileName" );
    loginLogFilePath = appDataDir + loginLogFileName;
    accountCreationLogFileName = (String) application.getInitParameter ( "accountCreationLogFileName" );
    accountCreationLogFilePath = appDataDir + accountCreationLogFileName;
    minLenEntryPassword = Integer.parseInt ( application.getInitParameter ( "minLenEntryPassword" ) );
    maxLenEntryPassword = Integer.parseInt ( application.getInitParameter ( "maxLenEntryPassword" ) );
    regexValidEntryPasswordLetter = (String) application.getInitParameter ( "regexValidEntryPasswordLetter" );
    regexValidEntryPassword = "^" + regexValidEntryPasswordLetter + "{" + Integer.toString ( minLenEntryPassword ) + "," + Integer.toString ( maxLenEntryPassword ) + "}$";
    regexValidPID = (String) application.getInitParameter ( "regexValidPID" );
    maxLenPID = Integer.parseInt ( application.getInitParameter ( "maxLenPID" ) );
    maxLenPassword = Integer.parseInt ( application.getInitParameter ( "maxLenPassword" ) );
    String enableSelfSignupStr = (String) application.getInitParameter ( "enableSelfSignup" );
    enableSelfSignup = ( enableSelfSignupStr != null & enableSelfSignupStr.equals("1") );
    String authenticationMethod = (String) application.getInitParameter ( "authenticationMethod" );
    authenticationMethodStandalone = ( authenticationMethod != null && authenticationMethod.equals("standalone") );
    regexValidUserPassword = (String) application.getInitParameter ( "regexValidUserPassword" );
    String demoModeEnabledStr = (String) application.getInitParameter ( "demoModeEnabled" );
    demoModeEnabled = ( demoModeEnabledStr != null & demoModeEnabledStr.equals("1") );
    demoModeUser = (String) application.getInitParameter ( "demoModeUser" );
    demoModePassword = (String) application.getInitParameter ( "demoModePassword" );
    demoModeLifetimeMinutes = Integer.parseInt ( application.getInitParameter ( "demoModeLifetimeMinutes" ) );
    String enableEmailAdminOnNewUserStr = (String) application.getInitParameter ( "enableEmailAdminOnNewUser" );
    enableEmailAdminOnNewUser = ( enableEmailAdminOnNewUserStr != null & enableEmailAdminOnNewUserStr.equals("1") );
    String enableEmailAdminOnOpenSurveyStr = (String) application.getInitParameter ( "enableEmailAdminOnOpenSurvey" );
    enableEmailAdminOnOpenSurvey = ( enableEmailAdminOnOpenSurveyStr != null & enableEmailAdminOnOpenSurveyStr.equals("1") );
    String enableAutoUpdateStatsStr = (String) application.getInitParameter ( "enableAutoUpdateStats" );
    enableAutoUpdateStats = ( enableAutoUpdateStatsStr != null & enableAutoUpdateStatsStr.equals("1") );
    serviceName = (String) application.getInitParameter ( "serviceName" );
    serviceProvider = (String) application.getInitParameter ( "serviceProvider" );
    acceptableUsePolicyURL = (String) application.getInitParameter ( "acceptableUsePolicyURL" );
    xmlEncoding = (String) application.getInitParameter ( "xmlEncoding" );
    urlScheme = (String) application.getInitParameter ( "urlScheme" );

    updateMessages();

    //Passing true to the timer means it will exit when the program exit
    //Passing false means the timer will keep the program from exitting until all
    //it's events are done.  This could cause problems if re-occuring events are
    //scheduled.
    timer = new Timer(true);

    //The task you want to execute
    Date now = new Date();
    log.debug("Schedule e-mail timer task which sends e-mails once a minute.");
    SendEmailTimerTask emailTask = new SendEmailTimerTask();
    timer.scheduleAtFixedRate(emailTask, now, 1*60*1000); // every one minute

    if ( Config.demoModeEnabled && Config.demoModeLifetimeMinutes > 0 ) {
      DemoModeDeleteTimerTask demoModeDeleteTask = new DemoModeDeleteTimerTask();
      timer.scheduleAtFixedRate(demoModeDeleteTask, new Date(), 5*60*1000); // every 5 minutes
    }
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    if ( Config.enableAutoUpdateStats ) {
      UpdateStatsTimerTask updateStatsTimerTask = new UpdateStatsTimerTask();
      timer.scheduleAtFixedRate(updateStatsTimerTask, today.getTime(), 24*60*60*1000); // every 1 day
    }

  }

  public static String formatFeedback ( String format, String text ) {
    if ( format.equals ( "error" ) ) {
      return "<span class=\"error\">" + text + "</span>";
    }
    else if ( format.equals ( "warning" ) ) {
      return "<span class=\"warning\">" + text + "</span>";
    }
    else if ( format.equals ( "affirmation" ) ) {
      return "<span class=\"affirmation\">" + text + "</span>";
    }
    else {
      return "<span class=\"affirmation\">" + text + "</span>";
    }
  }

  // takes a string and shortens it, e.g., translates "this is a test" to "this is a..." if numChar == 9
  // if the string contains one or more "<br>"'s then the text is shortened before and after the <br> separately
  public static String shortenText ( String text, int numChars ) {
    String s = new String ( text );

    if ( s.indexOf("<br>") > 0 ) {
      StringBuffer sb = new StringBuffer ( "" );
      while ( s.indexOf("<br>") > 0 ) {
        int pos = s.indexOf("<br>");
        String part = Config.shortenText( s.substring(0,pos), numChars );
        sb.append(part+"<br>");
        s = s.substring(pos+4);
      }
      if ( !s.equals("") ) { sb.append ( Config.shortenText(s, numChars) ); }
      s = sb.toString();
    }
    else {
      if ( s.length () > numChars ) {
        s = s.substring(0,numChars) + "...";
      }
    }
    return s;
  }

  public static String translateExportDelimiters ( String content, String delimiter ) {
    StringExt s = new StringExt ( content );
    String substituteDelimiter = ",";
    if ( delimiter.equals ( Config.tab ) ) { substituteDelimiter = " "; }
    else if ( delimiter.equals ( ";" ) ) { substituteDelimiter = ","; }
    else if ( delimiter.equals ( "," ) ) { substituteDelimiter = ";"; }
    else if ( delimiter.equals ( "|" ) ) { substituteDelimiter = ";"; }

    return s.substitute( delimiter, substituteDelimiter );
  }

  public static boolean isAdminPassword ( String password ) {
    User userAdmin = new User ( Config.appDataDir, "admin" );
    if ( userAdmin.exists() ) {
      return userAdmin.passwordIsValid ( password );
    }
    else {
      return false;
    }
  }

  // (re-)read messages from language file
  public static void updateMessages () {
    messages = new Hashtable();
    try {
      File languageFile = new File ( appDataDir + "messages/", languageFileName );
      if (languageFile.exists ()) {
        SAXBuilder builder = new SAXBuilder ( true );


//        Document doc = builder.build ( languageFile );
//      was replaced 2003-12-01 by:
        BufferedReader in = new BufferedReader(
                              new InputStreamReader(
                                new FileInputStream(languageFile),
                                Config.xmlEncoding));
        Document doc = builder.build ( in, "file://" + appDataDir + "messages/" );                    
                 
                             
        Element root = doc.getRootElement();

        List messagesList = root.getChildren( "message" );
        Iterator iMessage = messagesList.iterator();
        while (iMessage.hasNext()) {
          Element messageElement = (Element) iMessage.next();
          messages.put(messageElement.getAttributeValue( "id" ), messageElement.getTextTrim());
        }
      } // end: if (languageFile.exists ())
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  public static String msg ( int id ) {
    return (String) messages.get(Integer.toString(id));
  }

  public static void prepareSurveyAuthCache ( String surveyId ) {
    if ( !authorizationCache.containsKey(surveyId) ) { // try to load authorization file
      loadSurveyAuthCache(surveyId);
    }
  }

  // this is an un-documented feature that can be used if surveys have a large
  // number of members. instead of having to parse a huge XML file upon every
  // entry request, the file "authorizationCache.txt" is loaded into memory
  // the file format is: "one PID per line, no spaces etc."
  public static void loadSurveyAuthCache ( String surveyId ) {
    File surveyAuthCacheFile = new File ( appDataDir + "surveys" + Config.FILE_SEPARATOR + surveyId + Config.FILE_SEPARATOR + "authorizationCache.txt" );
    if ( surveyAuthCacheFile.exists() ) {
      Hashtable surveyAuthCache = new Hashtable();
      try {
        // read data from file
        FileReader fr = new FileReader(surveyAuthCacheFile);
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        do {
          line = br.readLine();
          if ( line != null && !line.equals("") ) {
            surveyAuthCache.put(line.trim().toLowerCase(),"1");
          }
        }
        while ( line != null );

        br.close();
        fr.close();

        authorizationCache.put(surveyId,surveyAuthCache);
      }
      catch ( Exception e ) {
        e.printStackTrace();
      }
    }
  }

  public static boolean isInAuthorizationCache ( String surveyId, String userId ) {
    prepareSurveyAuthCache(surveyId);

    if ( authorizationCache.containsKey(surveyId) ) {
      Hashtable surveyAuthCache = (Hashtable) authorizationCache.get (surveyId);
      if (surveyAuthCache.containsKey(userId.toLowerCase())) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }
}