package edu.vt.ward.survey;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import javax.servlet.http.HttpServletRequest;

public class SurveyEntryForm {
  public static String surveyEntryFormDTD = "surveyEntryForm.dtd";
  public static String surveyEntryFormXML = "entryForm.xml";
  public static String resultsSummaryXML = "resultsSummary.xml";
  public static final int modeEntry = 0; // everything works
  public static final int modeEdit = 1; // contains all the buttons
  public static final int modePreview = 2; // everything, submit button doesn't work
  public static final int modeResultsSummary = 3; // only for internal purposes, counts number of feedbacks on a certain option
  public static final int modeResultsExport = 4; // form for coding the questions and selecting the ones that are to be exported

  private String appDir; // absolute directory of this web application (including trailing slash)
  private String surveyDir; // absolute dir of the survey's directory (including trailing slash)
  private String surveyEntryForm; // absolute path to survey definition file

  protected String id;
  protected String title = "";
  protected String bgColor = "#ffffff";
  protected String textColor = "#000000";
  protected String font = "Verdana, Geneva, Arial, Helvetica, sans-serif";
  protected String fontSize = ""; // "12px";
  protected String header = "";
  protected String footer = "";
  protected String baseHref = "";
  protected String userCSS = "";
  protected boolean showBorder = true; // true, if the default web browser border should be shown
  protected String entryAuthUser = ""; // contains the id of a user who authenticated before submitting a survey
  protected boolean exportIncludeQuestions = true; // true, if the question text should be included in export
  protected String exportDelimiter = ";";
  protected String ip = ""; // IP address of the computer that submitted the survey or the one that last edited the entry form
  protected String browserId = ""; // web browser Id string from the browser that submitted the survey or the one that last edited the entry form
  protected long javascriptEnabled = 0;
  protected long count = 0;
  protected String divider = "<br>";
  protected String introductionText = "";
  protected String exitPageText = Config.msg(313); // \n<a href=\"JavaScript:this.parent.window.close()\">Close this window</a>";
  private boolean exitPageTextIsHTML = true; // true, if the text is HTML rather than plain text
  protected String urlScheme = "http"; // can also be "https"

  Vector sections = new Vector ();

  public SurveyEntryForm ( String appDir, String id, String urlScheme ) {
    this.id = new String ( id );
    this.appDir = new String ( appDir );
    this.urlScheme = urlScheme;
    this.surveyDir = new String ( appDir + "surveys/" + this.id + "/" );
    this.surveyEntryForm = new String ( surveyDir + surveyEntryFormXML );
  }

  public SurveyEntryForm ( String appDir, String id, String urlScheme, boolean loadFromFile ) {
    this ( appDir, id, urlScheme );
    if ( this.exists () && loadFromFile )
      load();

    // if there is no section create one
    if ( this.getNumSections () == 0 ) {
      Section section = new Section ( this );
      this.addSection ( 0, section );
      // add a text field that is preset to contain the survey title
      SurveyMetaData survey = new SurveyMetaData ( appDir, id );
      InputComment titleComment = new InputComment ( "" );
      titleComment.setComment ( "<h1>" + survey.getName() + "</h1>" );
      titleComment.setIsHTML ( true );
      titleComment.setShowDivider ( false );
      section.addQuestion( (Question) titleComment);

      this.save ();
    }
  }

  public SurveyEntryForm ( String appDir, String id, String urlScheme, String fileName, boolean loadFromFile ) {
    this.id = new String ( id );
    this.appDir = new String ( appDir );
    this.urlScheme = urlScheme;
    this.surveyDir = new String ( appDir + "surveys/" + this.id + "/" );
    this.surveyEntryForm = new String ( surveyDir + fileName );

    if ( this.exists () && loadFromFile )
      load();
  }

  public boolean exists () {
    File surveyEntryFormFile = new File ( surveyEntryForm );
    return surveyEntryFormFile.exists ();
  }

  public String getId() { return id; }
  public String getTitle() { return title; }
  public void setTitle( String title ) {  this.title = new String (title); }
  public String getBgColor() { return this.bgColor; }
  public void setBgColor( String bgColor ) {  this.bgColor = new String (bgColor); }
  public String getTextColor() { return this.textColor; }
  public void setTextColor( String textColor ) {  this.textColor = new String (textColor); }
  public String getFont() { return this.font; }
  public void setFont( String font ) {  this.font = new String (font); }
  public String getFontSize() { return this.fontSize; }
  public void setFontSize( String fontSize ) {  this.fontSize = new String (fontSize); }
  public String getHeader() { return this.header; }
  public void setHeader( String header ) {  this.header = new String (header); }
  public String getFooter () { return this.footer; }
  public void setFooter( String footer ) {  this.footer = new String (footer); }
  public String getBaseHref () { return this.baseHref; }
  public void setBaseHref ( String baseHref ) { this.baseHref = baseHref; }
  public String getUserCSS () { return this.userCSS; }
  public void setUserCSS ( String userCSS ) { this.userCSS = userCSS; }
  public boolean getShowBorder () { return this.showBorder; }
  public void setShowBorder ( boolean showBorder ) { this.showBorder = showBorder; }
  public String getEntryAuthUser () { return this.entryAuthUser; }
  public void setEntryAuthUser ( String entryAuthUser ) {  this.entryAuthUser = entryAuthUser; }
  public String getExportDelimiter () { return this.exportDelimiter; }
  public void setExportDelimiter( String exportDelimiter ) {  this.exportDelimiter = new String (exportDelimiter); }
  public String getIp () { return this.ip; }
  public void setIp( String ip ) {  this.ip = new String (ip); }
  public String getBrowserId () { return this.browserId; }
  public void setBrowserId( String browserId ) {
    if ( browserId != null ) {
      this.browserId = new String (browserId);
    }
    else { this.browserId = "unknown"; }
  }
  public long getJavascriptEnabled () { return this.javascriptEnabled; }
  public void setJavascriptEnabled ( long javascriptEnabled ) {  this.javascriptEnabled = javascriptEnabled; }
  public void incrementJavascriptEnabled () {  this.javascriptEnabled++; }

  public long getCount () { return this.count; }
  public void setCount ( long count ) {  this.count = count; }
  public void incrementCount () {  this.count++; }

  public boolean getExportIncludeQuestions () { return this.exportIncludeQuestions; }
  public void setExportIncludeQuestions ( boolean exportIncludeQuestions ) { this.exportIncludeQuestions = exportIncludeQuestions; }
  public String getDivider () { return this.divider; }
  public void setDivider( String divider ) {  this.divider = new String (divider); }
  public String getExitPageText () { return this.exitPageText; }
  public void setExitPageText ( String exitPageText ) { this.exitPageText = exitPageText; }
  public boolean getExitPageTextIsHTML () { return this.exitPageTextIsHTML; }
  public void setExitPageTextIsHTML ( boolean exitPageTextIsHTML ) { this.exitPageTextIsHTML = exitPageTextIsHTML; }

  public String getIntroductionText () { return this.introductionText; }
  public void setIntroductionText ( String introductionText ) { this.introductionText = introductionText; }

  public void addSection ( Section section ) {
    sections.add ( section );
  }

  public void addSection ( int index, Section section ) {
    sections.add ( index, section );
  }

  public void removeSection ( int index ) {
    sections.remove ( index );
  }

  public Section getSection ( int sectionNr ) {
    return (Section) this.sections.get ( sectionNr );
  }

  public int getNumSections () {
    return sections.size ();
  }

  public boolean save () {
    try {
      File surveyDirFile = new File ( this.surveyDir );
      if (surveyDirFile.exists ()) {
//        FileWriter writer = new FileWriter ( new File ( surveyEntryForm ) );
        BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                        new FileOutputStream(
                        new File ( surveyEntryForm )),Config.xmlEncoding));


        String surveyEntryFormXML = this.getXML ( modeEdit );
        writer.write ( surveyEntryFormXML, 0, surveyEntryFormXML.length () );
        writer.close ();
      }
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean saveResultsSummary () {
    try {
      File surveyDirFile = new File ( this.surveyDir );
      if (surveyDirFile.exists ()) {
//        FileWriter writer = new FileWriter ( new File ( this.surveyDir + resultsSummaryXML ) );
        BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                        new FileOutputStream(
                        new File ( this.surveyDir + resultsSummaryXML )),Config.xmlEncoding));


        String surveyEntryFormXML = this.getXML ( modeResultsSummary );
        writer.write ( surveyEntryFormXML, 0, surveyEntryFormXML.length () );
        writer.close ();
      }
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    return true;
  }

  // this code addresses the problem of two entries arriving at virtually the same time
  private String getUnusedSurveyEntryId () {
    String entryId = "";
    Hashtable entryIdUsed = (Hashtable) Config.surveyEntryIdUsed.get(this.id);
    if ( entryIdUsed == null ) {
      entryIdUsed = new Hashtable();
      Config.surveyEntryIdUsed.put(this.id, entryIdUsed);
    }

    // using a hashtable for speed but also making sure that the entry file doesn't
    // exist. This may happen in the (very unlikely) case that the server gets
    // restarted, the hashtable contents lost and the server-time upon restart
    // is changed...
    File entryFile = null;
    do {
      // begin: this code is time-critical and should be as short as possible
      do {
        entryId = String.valueOf ( ( new java.util.Date () ).getTime() );
      } while ( entryIdUsed.containsKey(entryId) );
      entryIdUsed.put(entryId, "1"); // this may mark more entryIds as "used" than really are
      // end: time-critical code

      entryFile = new File ( this.surveyDir + "entry." + entryId );
    } while ( entryFile.exists () );

    return entryId;
  }

  public String saveEntry () {
    try {
      File surveyDirFile = new File ( this.surveyDir );
      if (surveyDirFile.exists ()) {
        String surveyEntryXML = this.getXML ( modeEntry );

        String entryId = getUnusedSurveyEntryId ();
        File entryFile = new File ( this.surveyDir + "entry." + entryId );

//        FileWriter writer = new FileWriter ( entryFile );
        BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                        new FileOutputStream(entryFile),Config.xmlEncoding));


        writer.write ( surveyEntryXML, 0, surveyEntryXML.length () );
        writer.close ();

        return entryId;
      }
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    return "";
  }


  public void load () {
    try {
      File surveyEntryFormFile = new File ( surveyEntryForm );
      if (surveyEntryFormFile.exists ()) {
        SAXBuilder builder = new SAXBuilder ( true );
        Document doc = builder.build ( surveyEntryFormFile );
        Element root = doc.getRootElement();

        try { this.setEntryAuthUser ( root.getAttributeValue( "entryAuthUser" ) ); }
        catch ( Exception e ) { }

        try { this.setShowBorder ( root.getAttributeValue( "showBorder" ).equals ( "1" ) ); }
        catch ( Exception e ) { }

        try { this.setExportDelimiter ( root.getAttributeValue( "exportDelimiter" ) ); }
        catch ( Exception e ) { }

        try { this.setExportIncludeQuestions ( root.getAttributeValue( "exportIncludeQuestions" ).equals ( "1" ) ); }
        catch ( Exception e ) { }

        try { this.setIp ( root.getAttributeValue( "ip" ) ); }
        catch ( Exception e ) { }

        try { this.setBrowserId ( root.getAttributeValue( "browserId" ) ); }
        catch ( Exception e ) { }

        try { this.setJavascriptEnabled ( Long.parseLong( root.getAttributeValue( "javascriptEnabled" ) ) ); }
        catch ( Exception e ) { }

        try { this.setCount ( Long.parseLong( root.getAttributeValue( "count" ) ) ); }
        catch ( Exception e ) { }

        try { this.setTitle ( root.getChild ( "title" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setBgColor ( root.getChild ( "bgColor" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setTextColor ( root.getChild ( "textColor" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setFont ( root.getChild ( "font" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setFontSize ( root.getChild ( "font" ).getAttributeValue( "size" ) ); }
        catch ( Exception e ) { }

        try { this.setHeader ( HTMLUtils.newLineDecode ( root.getChild ( "header" ).getTextTrim() ) ); }
        catch ( Exception e ) { }

        try { this.setFooter ( HTMLUtils.newLineDecode ( root.getChild ( "footer" ).getTextTrim() ) ); }
        catch ( Exception e ) { }

        try { this.setBaseHref ( root.getChild ( "baseHref" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setUserCSS ( root.getChild ( "userCSS" ).getTextTrim() ); }
        catch ( Exception e ) { }

        try { this.setDivider ( HTMLUtils.newLineDecode ( root.getChild ( "divider" ).getTextTrim() ) ); }
        catch ( Exception e ) { }

        try { this.setExitPageText ( HTMLUtils.newLineDecode ( root.getChild ( "exitPageText" ).getTextTrim() ) ); }
        catch ( Exception e ) { }

        try { this.setExitPageTextIsHTML ( root.getChild ( "exitPageText" ).getAttributeValue( "isHTML" ).equals ( "1" ) ); }
        catch ( Exception e ) { }

        try { this.setIntroductionText ( root.getChild ( "introductionText" ).getTextTrim() ); }
        catch ( Exception e ) { }

        List sections = root.getChildren( "section" );
        Iterator iSection = sections.iterator();
        while (iSection.hasNext()) {
          this.addSection ( new Section ( this, (Element) iSection.next() ) );
        }
      }
    }
    catch ( Exception e ) {
//      e.printStackTrace();
    }
  }

  public String getExitPageHTML ( int mode ) {
    StringBuffer s = new StringBuffer ();
    String style = "font-family : " + this.getFont ();
    if ( !this.getFontSize ().equals ( "" ) ) {
      style += "; font-size : " + this.getFontSize ();
    }
    String styleColors = "background-color : " + this.getBgColor() + "; color : " + this.getTextColor ();

    if ( mode != modeEdit ) {
      s.append ( "<html>\n" );
      s.append ( "<head>\n" );
      s.append ( "  <title>" + this.getTitle () + "</title>\n" );
      s.append ( "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Config.xmlEncoding + "\">\n" );
      s.append ( "  <style>\n" );
      s.append ( "    body, p, td { " + style + " }\n" );
//      s.append ( "    h1 { font-size : 28px; font-weight : normal }\n" );
      s.append ( "  </style>\n" );
      if ( !this.getBaseHref().equals("") ) {
        s.append ( "  <base href=\"" + this.getBaseHref() + "\">\n" );
      }
      if ( !this.getUserCSS().equals("") ) {
        s.append ( "  <link type=\"text/css\" rel=\"stylesheet\" media=\"screen\" href=\"" + this.getUserCSS() + "\">\n" );
      }
      s.append ( "</head>\n" );
      s.append ( "<body" );

      if ( !this.getBgColor ().equals ( "" ) )
        s.append ( " bgColor=\"" + bgColor + "\"" );
      if ( !this.getTextColor ().equals ( "" ) )
        s.append ( " text=\"" + this.getTextColor () + "\"" );

      s.append ( " onLoad=\"this.window.focus()\"" );
      if ( !this.getShowBorder() )
        s.append ( " leftMargin=\"0\" topMargin=\"0\" marginheight=\"0\" marginwidth=\"0\"" );

      s.append ( ">\n" );

      if ( this.getFont().startsWith("Verdana") ) { s.append ( "<font size=\"2\">\n" ); }
      s.append ( this.getHeader () + "<br>\n" );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font><font size=\"2\">\n" ); }
    } // end: if ( mode != modeEdit )

    if ( mode == modeEdit ) {
      s.append ( "  <style>\n" );
      s.append ( "    .globalsettings { " + style + "; " + styleColors + " }\n" );
      s.append ( "  </style>\n");
      s.append ( "<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\" width=\"100%\">");
      s.append ( "<tr><td colspan=\"2\"><img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br></td></tr>" );
      s.append ( "<tr><td bgcolor=\"#ff9900\"><b>" );
      s.append ( Config.msg(314) );
      s.append ( "</b></td><td bgcolor=\"#ff9900\" nowrap align=\"right\">" + this.getEditGlobalSettingsButtonHTML ( "editExitPage.jsp?surveyId=" + this.getId() ) + "</td></tr></table>" );
      s.append ( "<br>\n" );
      s.append ( Config.msg(315)+"<br><br>\n" );
      s.append ( "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#cccccc\">" );
      s.append ( "<table cellspacing=\"1\" cellpadding=\"2\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#6699cc\">");
      s.append ( "<img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br>" );
      s.append ( "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td style=\"color : #ffffff\">" );
      s.append ( Config.msg(316) );
      s.append ( "</td><td nowrap align=\"right\">" + this.getExitPageSettingsButtonHTML () + "</td></tr></table>" );
      s.append ( "</td></tr><tr><td class=\"globalsettings\">" );
    }

    if ( this.getExitPageTextIsHTML () ) {
      s.append ( this.getExitPageText () );
    }
    else {
      s.append ( HTMLUtils.newLine2Br ( HTMLUtils.encode ( this.getExitPageText () ) ) );
    }

    if ( mode == modeEdit ) {
      s.append ( "</td></tr></table></td></tr></table>\n" );
    }

    if ( mode != modeEdit ) {
      s.append ( "<br><br><br><a href=\""+Config.urlScheme+"://" + Config.hostName + Config.rootURL + "\"><img src=\""+Config.urlScheme+"://" + Config.hostName + Config.rootURL + "images/createdwith.gif\" border=\"0\" alt=\""+Config.msg(317)+" " + Config.serviceName + "\"></a><br>" );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font><font size=\"2\">\n" ); }
      s.append ( this.getFooter () );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font>\n" ); }
      s.append ( "</body>\n" );
      s.append ( "</html>\n" );
    }

    return s.toString ();
  }

  public String getHTML ( SurveyMetaData survey, int mode ) {
    StringBuffer s = new StringBuffer ();
    String style = "font-family : " + this.getFont ();
    if ( !this.getFontSize ().equals ( "" ) ) {
      style += "; font-size : " + this.getFontSize ();
    }

    String styleColors = "background-color : " + this.getBgColor() + "; color : " + this.getTextColor ();

    if ( mode == modeEntry || mode == modePreview ) {
      s.append ( "<html>\n" );
      s.append ( "<head>\n" );
      s.append ( "  <title>" + this.getTitle () + "</title>\n" );
      s.append ( "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n" );
      s.append ( "  <style>\n" );
      s.append ( "    body, p, td { " + style + " }\n" );
      s.append ( "  </style>\n" );
      if ( !this.getBaseHref().equals("") ) {
        s.append ( "  <base href=\"" + this.getBaseHref() + "\">\n" );
      }
      if ( !this.getUserCSS().equals("") ) {
        s.append ( "  <link type=\"text/css\" rel=\"stylesheet\" media=\"screen\" href=\"" + this.getUserCSS() + "\">\n" );
      }
      s.append ( "</head>\n" );
      s.append ( "<body" );

      if ( !this.getBgColor ().equals ( "" ) )
        s.append ( " bgColor=\"" + bgColor + "\"" );
      if ( !this.getTextColor ().equals ( "" ) )
        s.append ( " text=\"" + this.getTextColor () + "\"" );
      s.append ( " onLoad=\"this.window.focus()\"" );

      if ( !this.getShowBorder() )
        s.append ( " leftMargin=\"0\" topMargin=\"0\" marginheight=\"0\" marginwidth=\"0\"" );

      s.append ( ">\n" );

      if ( this.getFont().startsWith("Verdana") ) { s.append ( "<font size=\"2\">\n" ); }
      s.append ( this.getHeader () + "<br>\n" );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font><font size=\"2\">\n" ); }
    } // end: if ( mode != modeEdit )

    if ( mode == modeEdit || mode == modeResultsSummary || mode == modeResultsExport ) {
      s.append ( "  <style>\n" );
      s.append ( "    .globalsettings { " + style + "; " + styleColors + " }\n" );
      s.append ( "  </style>\n");
    }

    if ( mode == modeEntry ) {
      s.append ( "<form action=\""+this.urlScheme+"://" + Config.hostName + Config.rootURL + "entry.jsp\" method=\"post\" name=\"form\" onSubmit=\"return false\">\n" );
      s.append ( "<input type=\"hidden\" name=\"surveyId\" value=\"" + this.getId () + "\">\n" );
    }
    if ( mode == modePreview ) {
      s.append ( "<form name=\"form\">\n" );
    }

    if ( mode == modeEdit || mode == modeResultsExport ) {
      s.append ( "<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\" width=\"100%\">");
      s.append ( "<tr><td colspan=\"2\"><img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br></td></tr>" );
      s.append ( "<tr><td bgcolor=\"#ff9900\"><b>" );
      s.append ( Config.msg(318) );
      s.append ( "</b></td><td bgcolor=\"#ff9900\" nowrap align=\"right\">" );
    }
    if ( mode == modeEdit ) {
      s.append ( this.getEditGlobalSettingsButtonHTML ( "editEntryForm.jsp?surveyId=" + this.getId() ) );
    }
    if ( mode == modeEdit || mode == modeResultsExport ) {
      s.append( "</td></tr></table>" );
    }

    if ( mode == modeEdit || mode == modeResultsExport ) {
      s.append ( "<br>\n" );
    }

    s.append ( this.getSection ( 0 ).getHTML ( survey, mode, 0 ) );

    if ( mode == modeEntry ) {
      s.append ( "<br>\n" );
      s.append ( "<script language=\"JavaScript\"><!--\n" );
      s.append ( "document.write (\"<input type=\\\"hidden\\\" name=\\\"javascriptEnabled\\\" value=\\\"1\\\">\");\n" );
      s.append ( "document.write (\"<input type=\\\"hidden\\\" name=\\\"save\\\" value=\\\""+Config.msg(319)+"\\\">\");\n" );
      s.append ( "document.write (\"<input type=\\\"button\\\" value=\\\""+Config.msg(319)+"\\\" onClick=\\\"document.form.submit();\\\">\");\n" );
      s.append ( "// -->\n" );
      s.append ( "</script>\n" );
      s.append ( "<noscript>\n" );
      s.append ( "  <input type=\"submit\" name=\"save\" value=\""+Config.msg(319)+"\">\n" );
      s.append ( "</noscript>\n" );
      s.append ( "</form>\n" );
    }
    if ( mode == modePreview ) {
      s.append ( "</form>");
      s.append ( "<form name=\"form2\"><input type=\"button\" name=\"test\" value=\""+Config.msg(319)+"\"></form>\n" );
      // javascript to disable the submit button
      s.append ("<script language=\"JavaScript\" type=\"text/javascript\"><!--\n");
      s.append ("document.form2.test.disabled = true;\n" );
      s.append ("//-->\n</script>\n");
    }

    if ( mode == modeEdit || mode == modeResultsExport ) {
      s.append ( "<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\" width=\"100%\">");
      s.append ( "<tr><td colspan=\"2\"><img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br></td></tr>" );
      s.append ( "<tr><td colspan=\"2\" bgcolor=\"#ff9900\">&nbsp;</td></tr></table>" );
    }

    if ( mode == modeEntry || mode == modePreview ) {
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font><font size=\"2\">\n" ); }
      s.append ( this.getFooter () );
      if ( this.getFont().startsWith("Verdana") ) { s.append ( "</font>\n" ); }
      s.append ( "</body>\n" );
      s.append ( "</html>\n" );
    }

    return s.toString ();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ();
    s.append ( this.getSection ( 0 ).getExport ( entryQuestion, delimiter ) );
    return s.toString ();
  }

  // generate the DOM for the survey entry form and return the root element
  private Element getDOM ( int mode ) {
    Element survey = new Element ( "survey" );

    if ( mode == modeEdit ) { // || mode == this.modeResultsSummary ) {
      if ( this.getShowBorder () ) { survey.setAttribute ( new Attribute ( "showBorder", "1" ) ); }
      else { survey.setAttribute ( new Attribute ( "showBorder", "0" ) ); }
      survey.setAttribute ( new Attribute ( "exportDelimiter", this.getExportDelimiter() ) );
      if ( this.getExportIncludeQuestions () ) { survey.setAttribute ( new Attribute ( "exportIncludeQuestions", "1" ) ); }
      else { survey.setAttribute ( new Attribute ( "exportIncludeQuestions", "0" ) ); }
    }

    if ( mode == modeEdit || mode == modeEntry ) {
      if ( this.getEntryAuthUser() != null && !this.getEntryAuthUser().equals("") )
        survey.setAttribute ( new Attribute ( "entryAuthUser", this.getEntryAuthUser() ) );

      survey.setAttribute ( new Attribute ( "ip", this.getIp() ) );
      survey.setAttribute ( new Attribute ( "browserId", this.getBrowserId() ) );
    }

    if ( mode == modeEntry || mode == modeResultsSummary ) {
      survey.setAttribute ( new Attribute ( "javascriptEnabled", Long.toString( this.getJavascriptEnabled() ) ) );
    }

    if ( mode == modeResultsSummary ) {
      survey.setAttribute ( new Attribute ( "count", Long.toString( this.getCount() ) ) );
    }

    if ( mode == modeEdit || mode == modeResultsSummary ) {
      survey.addContent ( new Element ( "title" ).setText ( HTMLUtils.xmlFilter(this.getTitle ()) ) );
      survey.addContent ( new Element ( "bgColor" ).setText ( HTMLUtils.xmlFilter(this.getBgColor ()) ) );
      survey.addContent ( new Element ( "textColor" ).setText ( HTMLUtils.xmlFilter(this.getTextColor ()) ) );
      Element font = new Element ( "font" ).setText ( HTMLUtils.xmlFilter(this.getFont ()) );
      font.setAttribute ( new Attribute ( "size", HTMLUtils.xmlFilter(this.getFontSize ()) ) );
      survey.addContent ( font );
      survey.addContent ( new Element ( "header" ).setText ( HTMLUtils.newLineEncode ( HTMLUtils.xmlFilter(this.getHeader ()) ) ) );
      survey.addContent ( new Element ( "footer" ).setText ( HTMLUtils.newLineEncode ( HTMLUtils.xmlFilter(this.getFooter ()) ) ) );
      survey.addContent ( new Element ( "baseHref" ).setText ( HTMLUtils.xmlFilter(this.getBaseHref ()) ) );
      survey.addContent ( new Element ( "userCSS" ).setText ( HTMLUtils.xmlFilter(this.getUserCSS ()) ) );
      survey.addContent ( new Element ( "divider" ).setText ( HTMLUtils.newLineEncode ( HTMLUtils.xmlFilter(this.getDivider ()) ) ) );

      Element exitPageText = new Element ( "exitPageText" );
      if ( mode != modeResultsSummary ) {
        exitPageText.setText ( HTMLUtils.newLineEncode ( HTMLUtils.xmlFilter(this.getExitPageText ()) ) );
      }
      if ( this.getExitPageTextIsHTML () ) {
        exitPageText.setAttribute ( new Attribute ( "isHTML", "1" ) );
      }
      else {
        exitPageText.setAttribute ( new Attribute ( "isHTML", "0" ) );
      }
      survey.addContent ( exitPageText );
      survey.addContent ( new Element ( "introductionText" ).setText ( HTMLUtils.xmlFilter(this.getIntroductionText ()) ) );
    }

    for ( int i = 0; i < sections.size(); i++ ) {
      Element section = ((Section) sections.get(i)).getXML ( mode );
      survey.addContent ( section );
    }

    return survey;
  }

  // generate an XML document from a given root element and return the XML code as a string
  public String getXML ( int mode )
    throws IOException {

    Document doc = new Document ( this.getDOM ( mode ) );
    DocType surveyDocType = new DocType ( "survey", "../" + surveyEntryFormDTD );
    doc.setDocType ( surveyDocType );

    XMLOutputter xmlOutput = new XMLOutputter( "  ", true );
    xmlOutput.setEncoding(Config.xmlEncoding);
    return xmlOutput.outputString ( doc );
  }

  private String getExitPageSettingsButtonHTML () {
    return " <a href=\"editExitPageSettings.jsp?surveyId=" + this.getId() + "\" style=\"font-size:80%\">"+Config.msg(321)+"</a>\n";
  }

  private String getEditGlobalSettingsButtonHTML ( String returnTo ) {
    return " <a href=\"editGlobalSettings.jsp?surveyId=" + this.getId() + "&returnTo=" + java.net.URLEncoder.encode(returnTo) + "\" style=\"font-size:80%\">"+Config.msg(320)+"</a>\n";
  }


  // goes through all entry files and updates the "count" attribute for each option, text field etc.
  public void createResultsSummary () {
    // loop through all entry files
    File surveyDirFile = new File ( this.surveyDir );
    java.io.File[] entryFileList = surveyDirFile.listFiles( new FilenameFilterEntryFile () );

    for ( int i = 0; i < entryFileList.length; i++ ) {
      SurveyEntryForm entry = new SurveyEntryForm ( this.appDir, this.id, this.urlScheme, entryFileList [i].getName(), true );

      if ( entry.getJavascriptEnabled() == 1 ) {
        this.incrementJavascriptEnabled ();
      }
      this.incrementCount ();
      this.getSection ( 0 ).updateResultsSummary ( entry.getSection ( 0 ) );
    }
  }

  public void delete () {
    File surveyEntryFormFile = new File ( surveyEntryForm );
    surveyEntryFormFile.delete ();
    if (surveyEntryForm.substring(surveyEntryForm.length()-resultsSummaryXML.length()).equals(resultsSummaryXML)) {
      Config.resultSummaryForms.remove(this.id);
    }
  }

  public String makeEntryChanges ( HttpServletRequest request ) {
    try {
      for ( int i = 0; i < sections.size(); i++ ) {
        String errorId = ((Section) sections.get(i)).makeEntryChanges ( i, request );
        if ( errorId != null )
          throw new Exception ();
      }
      this.setIp ( request.getRemoteAddr() );
      this.setBrowserId( (String) request.getHeader ( "User-Agent") );
      if ( request.getParameter("javascriptEnabled") != null ) { this.setJavascriptEnabled( 1 ); }
      else { this.setJavascriptEnabled( 0 ); }
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }

  // reads an entry file; returns true if it could read the file
  public boolean readEntryResults ( String entryId ) {
    SurveyEntryForm entry = new SurveyEntryForm ( this.appDir, this.id, this.urlScheme, "entry." + entryId, true );
    if ( entry.exists () ) {
      this.getSection ( 0 ).updateResultsSummary ( entry.getSection ( 0 ) );
      return true;
    }
    else {
      return false;
    }
  }

  public boolean isReadyForOpen () {
    boolean ready = false;
    for ( int i=0; ready == false && i < this.getSection( 0 ).getNumQuestions(); i++ ) {
      Question q = this.getSection( 0 ).getQuestion(i);
      if ( !q.getClass().getName().equals("edu.vt.ward.survey.InputComment") )
        ready = true;
    }
    return ready;
  }

}
