package edu.vt.ward.survey;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class SurveyMetaData {
  private static String surveyMetaDataDTD = "surveyMetaData.dtd";
  private static String surveyMetaDataXML = "metadata.xml";
  private String id; // survey id, e.g. 982340320349
  private String appDir; // absolute directory for the data of this web application (including trailing slash)
  private String surveyDir; // absolute dir of the survey's directory (including trailing slash)
  private String surveyMetaDataFilePath; // absolute path to survey definition file

//  protected String ownerId = "";
  protected String name = "";
  protected String adminEmail = "";
  Vector admins = new Vector ();
  Vector members = new Vector ();

  protected String accessResultsRestriction = "owner"; // can be: "owner", "anyone", "password"
  protected String resultsPassword = "";
  protected String entryRestriction = "public"; // can be: "public", "password", "vt", "members"
  protected String entryPassword = "";
  protected boolean oneEntryOnly = false; // can be: "1" or "0"
  protected String opened = ""; // in the format: "yyyy-MM-dd HH:mm:ss"
  protected String closed = "";

  private boolean numEntriesCalculated = false; // true, if number of entries has been determined once
  private long numEntries = 0;
  private long numEntriesYoungerThan7days = 0;
  private long numEntriesYoungerThan24hours = 0;

  public boolean singleEntry = false; // this is a hack to avoid that percentages are shown if you view exactly one entry

  public String getSurveyDir () {
    return this.surveyDir;
  }

  public String getSurveyMetaDataFilePath () {
    return this.surveyMetaDataFilePath;
  }

  private void createSurveyDataDir ( String appDir ) {
    File surveyDataDirFile = new File ( appDir + "surveys" + Config.FILE_SEPARATOR );
    if ( !surveyDataDirFile.isDirectory() ) {
      surveyDataDirFile.mkdir();
    }
  }

  public SurveyMetaData ( String appDir, String surveyId ) {
    this.id = new String ( surveyId );
    this.appDir = new String ( appDir );
    this.createSurveyDataDir( appDir );

    this.surveyDir = new String ( appDir + "surveys" + Config.FILE_SEPARATOR + this.id + Config.FILE_SEPARATOR );
    this.surveyMetaDataFilePath = new String ( surveyDir + surveyMetaDataXML );
    if ( this.exists() )
      this.load ();
  }

  public boolean exists () {
    File surveyFile = new File ( surveyMetaDataFilePath );
    return surveyFile.exists ();
  }

  // constructor that generates a new survey and sets the owner ID
  public SurveyMetaData ( String appDir, String surveyId, String ownerId ) {
    this.id = new String ( surveyId );
    this.appDir = new String ( appDir );
    this.createSurveyDataDir( appDir );

    this.surveyDir = new String ( appDir + "surveys" + Config.FILE_SEPARATOR + this.id + Config.FILE_SEPARATOR );
    this.surveyMetaDataFilePath = new String ( surveyDir + surveyMetaDataXML );

//    this.setOwnerId ( ownerId );
    this.addAdminPID( ownerId );
  }

  public String getId () {
    return this.id;
  }

  /* deprecated. use "admin" tag instead */
//  public String getOwnerId () {
//    return this.ownerId;
//  }

  /* deprecated. use "admin" tag instead */
//  public void setOwnerId ( String ownerId ) {
//    this.ownerId = ownerId;
//  }

  public String getName () {
    return this.name;
  }

  public void setName ( String name ) {
    this.name = name;
  }

  public String getAdminEmail () {
    return this.adminEmail;
  }

  public void setAdminEmail ( String adminEmail ) {
    this.adminEmail = adminEmail;
  }

  public void addAdminPID ( String adminPID ) {
    this.admins.add ( adminPID );
  }

  public void removeAdminPID ( String adminPID ) {
    this.admins.remove ( adminPID );
  }

  public boolean isAdmin ( String adminPID ) {
    return this.admins.contains ( adminPID );
  }

  public Iterator getAdminsSorted () {
    TreeSet t = new TreeSet ( java.text.Collator.getInstance() );
    for (int i = 0; i < this.admins.size (); i++ )
      t.add ( this.admins.get ( i ) );

    return t.iterator ();
  }

  public int getNumAdmins () {
    return this.admins.size();
  }

  public String getAccessResultsRestriction () {
    return this.accessResultsRestriction;
  }

  public void setAccessResultsRestriction ( String accessResultsRestriction ) {
    this.accessResultsRestriction = accessResultsRestriction;
  }

  public String getResultsPassword () {
    return this.resultsPassword;
  }

  public void setResultsPassword ( String resultsPassword ) {
    this.resultsPassword = resultsPassword;
  }

  public String getEntryRestriction () {
    return this.entryRestriction;
  }

  public void setEntryRestriction ( String entryRestriction ) {
    this.entryRestriction = entryRestriction;
  }

  public String getEntryPassword () {
    return this.entryPassword;
  }

  public void setEntryPassword ( String entryPassword ) {
    this.entryPassword = entryPassword;
  }

  public boolean getOneEntryOnly () {
    return this.oneEntryOnly;
  }

  public void setOneEntryOnly ( boolean oneEntryOnly ) {
    this.oneEntryOnly = oneEntryOnly;
  }

  public String getOpened () {
    return this.opened;
  }

  public void setOpened ( String opened ) {
    this.opened = opened;
  }

  public String getClosed () {
    return this.closed;
  }

  public void setClosed ( String closed ) {
    this.closed = closed;
  }

  public String getStatusAsText () {
    if ( this.getOpened ().equals("") ) {
      return "<font color=\"#ff9900\">"+Config.msg(322)+"</font>";
    }
    else {
      if ( this.getClosed ().equals("") ) {
        return "<font color=\"#009900\">"+Config.msg(323)+" " + this.getOpened () + "</font>";
      }
      else {
        return "<font color=\"#999999\">"+Config.msg(324)+" " + this.getClosed () + "</font>";
      }
    }
  }

  // returns true if the survey is currently open and the closed date is not yet over
  public boolean isAcceptingDataEntry () {
    return
    ( !this.getOpened().equals("")
       &&
      ( this.getClosed().equals("")
        ||
        DateUtil.parseDate ( this.getClosed(), DateUtil.ISO4601DateFormat ).compareTo ( new java.util.Date () ) >= 0
      )
    );
  }

  public boolean isOpen () {
    return ( this.getOpened () != null && this.getClosed () == null );
  }

  public boolean isClosed () {
    return ( this.getClosed () != null );
  }

  public boolean isReadyForOpen () {
/*
    boolean readyForOpen = true;

    if (sections.size() == 0) {
      readyForOpen = false;
    }
    else {
      // test if all sections have at least one question
      for (int i = 0; i < sections.size (); i++) {
        if ( ((Section) sections.get(i)).getNumQuestions() == 0)
          readyForOpen = false;
      }
    }
    if (

    return readyForOpen;
*/
    return false;
  }

  public void addMemberPID ( String memberPID ) {
    this.members.add ( memberPID );
  }

  public void removeMemberPID ( String memberPID ) {
    this.members.remove ( memberPID );
  }

  public boolean isMember ( String memberPID ) {
    if ( this.members.contains ( memberPID ) ) {
      return true;
    }
    else {
      if ( Config.isInAuthorizationCache(this.id, memberPID) ) {
        return true;
      }
    }
    return false;
  }

  public Iterator getMembersSorted () {
    TreeSet t = new TreeSet ( java.text.Collator.getInstance() );
    for (int i = 0; i < this.members.size (); i++ )
      t.add ( this.members.get ( i ) );

    return t.iterator ();
  }

  public int getNumMembers () {
    return this.members.size();
  }

  public void load () {
    try {
      File surveyFile = new File ( surveyMetaDataFilePath );
      if (surveyFile.exists ()) {
        SAXBuilder builder = new SAXBuilder ( true );
        Document doc = builder.build ( surveyFile );
        Element root = doc.getRootElement();

//        try { this.setOwnerId ( root.getAttributeValue ( "ownerId" ) ); }
//        catch ( Exception e ) { };

        try { this.setName ( root.getChild ( "name" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setAdminEmail ( root.getChild ( "adminEmail" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try {
          List adminPIDs = root.getChild ( "admins" ).getChildren( "adminPID" );
          Iterator iadminPID = adminPIDs.iterator();
          while (iadminPID.hasNext()) {
            this.addAdminPID ( ((Element) iadminPID.next()).getTextTrim() );
          }
        }
        catch ( Exception e ) {
          try { this.addAdminPID ( root.getAttributeValue ( "ownerId" ) ); }
          catch ( Exception ex ) { };
        }

        try { this.setOpened ( root.getChild ( "opened" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setClosed ( root.getChild ( "closed" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setAccessResultsRestriction ( root.getChild ( "accessResultsRestriction" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setResultsPassword ( root.getChild ( "resultsPassword" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setEntryRestriction ( root.getChild ( "entryRestriction" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setEntryPassword ( root.getChild ( "entryPassword" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try {
          this.setOneEntryOnly ( root.getChild ( "oneEntryOnly" ).getTextTrim().equals("1") );
        }
        catch ( Exception e ) { }

        try {
          List memberPIDs = root.getChild ( "members" ).getChildren( "memberPID" );
          Iterator imemberPID = memberPIDs.iterator();
          while (imemberPID.hasNext()) {
            this.addMemberPID ( ((Element) imemberPID.next()).getTextTrim() );
          }
        }
        catch ( Exception e ) { }

      }
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  public void save () {
    try {
      File surveyDirFile = new File ( surveyDir );
      if ( !surveyDirFile.exists () ) {
        surveyDirFile.mkdir ();
      }

      Element survey = new Element ( "survey" );
//      if ( this.getNumAdmins() == 0 ) { // deprecated
//        survey.setAttribute ( new Attribute ( "ownerId", this.getOwnerId () ) );
//      }
      survey.addContent ( new Element ( "name" ).setText ( this.getName () ) );
      survey.addContent ( new Element ( "adminEmail" ).setText ( this.getAdminEmail () ) );

      Element adminsE = new Element ( "admins" );
      for ( int i = 0; i < admins.size(); i++ ) {
        adminsE.addContent ( new Element ( "adminPID" ).setText ( (String) admins.get(i) ) );
      }
      survey.addContent ( adminsE );

      survey.addContent ( new Element ( "opened" ).setText ( this.getOpened () ) );
      survey.addContent ( new Element ( "closed" ).setText ( this.getClosed () ) );
      survey.addContent ( new Element ( "accessResultsRestriction" ).setText ( this.getAccessResultsRestriction () ) );
      survey.addContent ( new Element ( "resultsPassword" ).setText ( this.getResultsPassword () ) );
      survey.addContent ( new Element ( "entryRestriction" ).setText ( this.getEntryRestriction () ) );
      survey.addContent ( new Element ( "entryPassword" ).setText ( this.getEntryPassword () ) );

      Element membersE = new Element ( "members" );
      for ( int i = 0; i < members.size(); i++ ) {
        membersE.addContent ( new Element ( "memberPID" ).setText ( (String) members.get(i) ) );
      }
      survey.addContent ( membersE );

      if ( this.getOneEntryOnly() ) {
        survey.addContent ( new Element ( "oneEntryOnly" ).setText ( "1" ) );
      }
      else {
        survey.addContent ( new Element ( "oneEntryOnly" ).setText ( "0" ) );
      }

      Document doc = new Document ( survey );
      DocType docType = new DocType ( "survey", "../" + surveyMetaDataDTD );
      doc.setDocType ( docType );
//      FileWriter writer = new FileWriter ( surveyMetaDataFilePath );
      BufferedWriter writer = new BufferedWriter(
                      new OutputStreamWriter(
                      new FileOutputStream(
                      new File ( surveyMetaDataFilePath )),Config.xmlEncoding));


      XMLOutputter xmlOutputter = new XMLOutputter ( "  ", true );
      xmlOutputter.setEncoding(Config.xmlEncoding);
      String output = xmlOutputter.outputString ( doc );
      writer.write ( output, 0, output.length () );
      writer.close ();
    }
    catch ( Exception e ) {
      e.printStackTrace ();
    }
  }

  // creates a deep copy of this survey
  public void createCopy ( String newSurveyId, String newSurveyName ) throws IOException {
    File surveyDirFile = new File ( surveyDir );
    String newSurveyDir = this.appDir + "surveys" + Config.FILE_SEPARATOR + newSurveyId + Config.FILE_SEPARATOR;
    File newSurveyDirFile = new File ( newSurveyDir );
    if ( !newSurveyDirFile.exists () ) {
      newSurveyDirFile.mkdir();
      FileActions.copy ( new File ( surveyDir + SurveyEntryForm.surveyEntryFormXML),
                         new File ( newSurveyDir + SurveyEntryForm.surveyEntryFormXML ) );
      FileActions.copy ( new File ( surveyDir + surveyMetaDataXML),
                         new File ( newSurveyDir + surveyMetaDataXML ) );
//      FileActions.copy ( surveyDirFile, newSurveyDirFile );

      // reset opened/closed
      SurveyMetaData newSurvey = new SurveyMetaData ( this.appDir, newSurveyId );
      newSurvey.setOpened ( "" );
      newSurvey.setClosed ( "" );
      newSurvey.save ();
    }
  }

  // deletes the whole survey's directory
  public void delete () {
    File surveyDirFile = new File ( surveyDir );
    if ( surveyDirFile.exists () ) {
      FileActions.delete ( surveyDirFile );
    }
  }

  // done for performance reasons, so that number of entries doesn't have to be recalculated every time
  public void recalculateNumEntries () {
    java.util.Date date = new java.util.Date ();
    long currentTime = date.getTime();
    this.numEntries = 0;
    this.numEntriesYoungerThan7days = 0;
    this.numEntriesYoungerThan24hours = 0;

    File surveyDirFile = new File ( this.getSurveyDir () );
    java.io.File[] entryFileList = surveyDirFile.listFiles( new FilenameFilterEntryFile () );
    if (entryFileList != null) {
      for ( int i = 0; i < entryFileList.length; i++ ) {
        if ( currentTime - entryFileList[i].lastModified() < 7*24*60*60*1000 ) {
          this.numEntriesYoungerThan7days++;
        }
        if ( currentTime - entryFileList[i].lastModified() < 24*60*60*1000 ) {
          this.numEntriesYoungerThan24hours++;
        }
      }
      this.numEntries = entryFileList.length;
    }
    this.numEntriesCalculated = true;
  }

  public long getNumEntries () {
    if ( !this.numEntriesCalculated ) { this.recalculateNumEntries(); }
    return this.numEntries;
  }

  public long getNumEntriesYoungerThan7days () {
    if ( !this.numEntriesCalculated ) { this.recalculateNumEntries(); }
    return this.numEntriesYoungerThan7days;
  }

  public long getNumEntriesYoungerThan24hours () {
    if ( !this.numEntriesCalculated ) { this.recalculateNumEntries(); }
    return this.numEntriesYoungerThan24hours;
  }

}