package edu.vt.ward.survey;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.util.*;

public class User {
  private static String userDTD = "user.dtd";
  private String pid;
  private String appDir; // absolute directory for the data of this web application (including trailing slash)
  private String userDir; // absolute dir of the user's directory (including trailing slash)
  private String userFilePath; // absolute path to user file
  private Element sourceElement;

  public User ( String appDir, String pid ) {
    this.pid = new String ( pid );
    this.appDir = new String ( appDir );
    File userDataDirFile = new File ( appDir + "users" + Config.FILE_SEPARATOR );
    if ( !userDataDirFile.isDirectory() ) {
      userDataDirFile.mkdir();
    }

    userDir = new String ( appDir + "users" + Config.FILE_SEPARATOR + pid + Config.FILE_SEPARATOR );
    userFilePath = new String ( userDir + pid + ".xml" );

    File userFile = new File ( userFilePath );
    if ( userFile.exists () ) { // read from users file
      this.load ();
    }
  }

  public boolean exists () {
    File userFile = new File ( userFilePath );
    return userFile.exists ();
  }

  public void create () {
    if ( !this.exists () ) {
      // create user dir
      File userDirFile = new File ( userDir );
      userDirFile.mkdir ();

      // create basic structure
      sourceElement = new Element ( "user" ).addContent ( new Element ( "surveys" ) );

      // create user file
      this.save ();

      // write timestamp to log file
      try {
        FileOutputStream logFileOut = new FileOutputStream ( Config.accountCreationLogFilePath, true ); // append to the file
        Calendar calendar = Calendar.getInstance();
        String content = new String ( DateUtil.formatDate ( DateUtil.ISO4601DateFormat, calendar.getTime() ) + " " + pid + "\n" );

        logFileOut.write ( content.getBytes() );
        logFileOut.close ();
      }
      catch ( Exception e ) {
        // ignore exceptions (probably concurrent access to the log file...)
      }

      // send e-mail notification to service admin
      if ( !Config.adminEmail.equals("") && Config.enableEmailAdminOnNewUser ) {
        SurveyEmail surveyEmail = new SurveyEmail (
          Config.adminEmail,
          pid + " "+Config.msg(325)+" " + Config.serviceName + " "+Config.msg(326),
          pid + " "+Config.msg(325)+" " + Config.serviceName + " "+Config.msg(326) );
        surveyEmail.save ();
      }
    }
  }

  public void remove () {
    if ( this.exists () && !this.pid.equals("admin") ) {
      // remove user dir
      File userDirFile = new File ( userDir );
      FileActions.delete ( userDirFile );
    }
  }

  public void setPassword ( String password ) {
    // encrypt password before storing it in the XML file
    String passwordEncrypted = Jcrypt.crypt ( Config.cryptSalt, password );

    Attribute passwordEncryptedAttribute = this.sourceElement.getAttribute( "password" );
    if ( passwordEncryptedAttribute == null ) {
      this.sourceElement.setAttribute ( new Attribute ( "password", passwordEncrypted ) );
    }
    else {
      passwordEncryptedAttribute.setValue( passwordEncrypted );
    }
  }

  public boolean passwordIsValid ( String password ) {
    if ( password == null ) { return false; }

    String passwordEncrypted = Jcrypt.crypt ( Config.cryptSalt, password );
    Attribute passwordEncryptedAttribute = this.sourceElement.getAttribute( "password" );

    if ( passwordEncryptedAttribute == null ) {
      return false;
    }
    else if ( passwordEncrypted.equals((String) passwordEncryptedAttribute.getValue()) ) {
      return true;
    }
    else {
      return false;
    }
  }

  public void setUsePolicyAccepted () {
    Attribute a = this.sourceElement.getAttribute( "usePolicyAccepted" );
    if ( a == null ) {
      this.sourceElement.setAttribute ( new Attribute ( "usePolicyAccepted", "1" ) );
    }
    else {
      a.setValue("1");
    }
  }

  public boolean getUsePolicyAccepted () {
    Attribute a = this.sourceElement.getAttribute( "usePolicyAccepted" );
    if ( a != null ) {
      String usePolicyAccepted = (String) a.getValue();
      return usePolicyAccepted != null && usePolicyAccepted.equals("1");
    }
    else {
      return false;
    }
  }

  public void addSurvey ( String surveyId ) {
    Element survey = new Element ( "survey" );
    survey.setAttribute ( new Attribute ( "id", surveyId ) );
    this.sourceElement.getChild ( "surveys" ).addContent ( survey );
  }

  public void removeSurvey ( String surveyId ) {
    Element newSurveysElement = new Element ("surveys");
    List surveyList = this.sourceElement.getChild ( "surveys" ).getChildren();
    Iterator iSurvey = surveyList.iterator();
    while (iSurvey.hasNext()) {
      Element survey = (Element) iSurvey.next();
      if ( !surveyId.equals ( survey.getAttributeValue( "id" ) ) ) {
        newSurveysElement.addContent( (new Element ( "survey" )).setAttribute("id",survey.getAttributeValue( "id" )) );
      }
    }
    this.sourceElement.removeChild ( "surveys" );
    this.sourceElement.addContent(newSurveysElement);
  }

  private String getXML ()
    throws IOException {

    Document doc = new Document ( this.sourceElement );
    DocType userDocType = new DocType ( "user", "../" + userDTD );
    doc.setDocType ( userDocType );

    XMLOutputter xmlOutput = new XMLOutputter( "  " , true );
    xmlOutput.setEncoding(Config.xmlEncoding);
    return xmlOutput.outputString ( doc );
  }

  public List getSurveyIdList () {
    List surveyIdList = new ArrayList ();

    List surveyList = this.sourceElement.getChild ( "surveys" ).getChildren( "survey" );
    Iterator iSurvey = surveyList.iterator();
    while (iSurvey.hasNext()) {
      Element survey = (Element) iSurvey.next();
      surveyIdList.add ( survey.getAttributeValue( "id" ) );
    }

    return surveyIdList;
  }

  private void load () {
    try {
      SAXBuilder builder = new SAXBuilder ( true );
      Document doc = builder.build ( userFilePath );
      this.sourceElement = doc.getRootElement();
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public boolean save () {
    try {
//      FileWriter writer = new FileWriter ( userFilePath );
      BufferedWriter writer = new BufferedWriter(
                      new OutputStreamWriter(
                      new FileOutputStream(
                      new File ( userFilePath )),Config.xmlEncoding));


      String userFileXML = this.getXML ();
      writer.write ( userFileXML, 0, userFileXML.length () );
      writer.close ();
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    return true;
  }
}
