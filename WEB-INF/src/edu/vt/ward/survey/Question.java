package edu.vt.ward.survey;

import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public abstract class Question implements Cloneable {
  protected String surveyId;
  private String text = ""; // contains question text etc.
  boolean showDivider = true; // true if the divider between questions should be displayed
  boolean exportInclude = true; // true if the question is to be included in an export data command
  boolean exportExpand = false; // true if the results are to be shown one column per option (instead of default: one column per question)

  Question (String surveyId) {
  	this.surveyId = surveyId;
  }

  Question (String surveyId, String text ) {
  	this.surveyId = surveyId;
    this.setText ( text );
  }

  Question (String surveyId, Element sourceElement ) {
  	this.surveyId = surveyId;
    try {
      this.setShowDivider ( sourceElement.getAttributeValue( "showDivider" ) );
    }
    catch ( Exception e ) { }
    try {
      this.setExportInclude ( sourceElement.getAttributeValue( "exportInclude" ).equals("1") );
    }
    catch ( Exception e ) { }
    try {
      this.setExportExpand ( sourceElement.getAttributeValue( "exportExpand" ).equals("1") );
    }
    catch ( Exception e ) { }
    try {
      this.setText ( HTMLUtils.newLineDecode( sourceElement.getChild ( "questionText" ).getTextTrim() ) );
    }
    catch ( Exception e ) { }
  }

  public String getSurveyId() { return surveyId; }
  
  public String getText () {
    return text;
  }

  public void setText ( String text ) {
    this.text = text;
  }

  public boolean getShowDivider () {
    return showDivider;
  }

  public String getShowDividerAsString () {
    if ( showDivider ) {
      return "1";
    }
    else {
      return "0";
    }
  }

  public void setShowDivider ( boolean showDivider ) {
    this.showDivider = showDivider;
  }

  public void setShowDivider ( String showDivider ) {
    if ( showDivider != null && showDivider.equals ( "1" ) ) {
      this.showDivider = true;
    }
    else {
      this.showDivider = false;
    }
  }

  public boolean getExportInclude () {
    return exportInclude;
  }

  public void setExportInclude ( boolean exportInclude ) {
    this.exportInclude = exportInclude;
  }

  public boolean getExportExpand () {
    return exportExpand;
  }

  public void setExportExpand ( boolean exportExpand ) {
    this.exportExpand = exportExpand;
  }

  public Question copy () {
    try {
      return (Question) super.clone ();				// return the clone
    } catch (CloneNotSupportedException e) {
       throw new InternalError();
    }
  }

  public String getHTML ( SurveyMetaData survey, int mode, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsExport ) {
      s.append ( "<a name=\"q_" + sectionNr + "_" + questionNr + "\"></a>" );
      s.append ( getTableOpenHTML ( mode, sectionNr, questionNr ) );
    }

    if ( mode == SurveyEntryForm.modeResultsSummary ) {
      s.append ( "<table cellspacing=\"0\" cellpadding=\"3\" border=\"0\" width=\"100%\"><tr><td class=\"globalsettings\">" );
    }

    if ( !this.getText ().equals ( "" ) )
      s.append ( "<b>" + this.getText () + "</b><br>\n" );

    return s.toString ();
  }

  public Element getXML ( int mode ) {
    Element question = new Element ( "question" );
    if ( this.getText () != null ) {
      if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsSummary ) {
        Element questionText = new Element ( "questionText" ).setText ( HTMLUtils.newLineEncode( HTMLUtils.xmlFilter(text) ) );
        question.addContent ( questionText );
        question.setAttribute ( new Attribute ( "showDivider", HTMLUtils.xmlFilter(this.getShowDividerAsString ()) ) );
        if ( this.getExportInclude() ) { question.setAttribute ( new Attribute ( "exportInclude", "1" ) ); }
        else { question.setAttribute ( new Attribute ( "exportInclude", "0" ) ); }
        if ( this.getExportExpand() ) { question.setAttribute ( new Attribute ( "exportExpand", "1" ) ); }
        else { question.setAttribute ( new Attribute ( "exportExpand", "0" ) ); }
      }
    }

    return question;
  }

  public String getEditCopyDeleteUpDownButtonHTML ( int sectionNr, int questionNr ) {
    return EditFormButtons.getEditCopyDeleteButtonHTML ( "editQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, Config.msg(298),
                                                         "copyQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, Config.msg(299),
                                                         "deleteQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, Config.msg(300))
           + "&nbsp;&nbsp;" + this.getUpDownButtonHTML ( sectionNr, questionNr );
  }

  public String getAddAboveButtonsHTML ( int sectionNr, int questionNr ) {
    return EditFormButtons.getAddAboveButtonsHTML ( "addQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr,
                                                    "editQuestion.jsp?surveyId=" + this.surveyId + "&type=comment&section=" + sectionNr + "&question=" + questionNr );
  }

  public String getUpButtonHTML ( int sectionNr, int questionNr ) {
    return EditFormButtons.getUpButtonHTML ( "upQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, Config.msg(301) );
  }

  public String getDownButtonHTML ( int sectionNr, int questionNr ) {
    return EditFormButtons.getDownButtonHTML ( "downQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr, Config.msg(302) );
  }

  public String getUpDownButtonHTML ( int sectionNr, int questionNr ) {
    return this.getUpButtonHTML ( sectionNr, questionNr ) + this.getDownButtonHTML ( sectionNr, questionNr );
  }

  public static String getAddButtonsHTML (String surveyId, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();

    s.append ( "<a href=\"addQuestion.jsp?surveyId=" + surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\" style=\"font-size:80%\">"+Config.msg(303)+"</a>&nbsp; " );
    s.append ( "<a href=\"editQuestion.jsp?surveyId=" + surveyId + "&type=comment&amp;section=" + sectionNr + "&question=" + questionNr + "\" style=\"font-size:80%\">"+Config.msg(304)+"</a><br>" );

    return s.toString ();
  }

  // this method is overwritten by all sub classes
  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    return getQuestionTextHTML ();
  }

  public String getTableOpenHTML ( int mode, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ( "" );

    if ( this.getShowDivider () )
      s.append ( "<br>" );

    s.append ( "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#cccccc\">" );
    s.append ( "<table cellspacing=\"1\" cellpadding=\"2\" border=\"0\" width=\"100%\"><tr><td bgcolor=\"#6699cc\">");
    s.append ( "<img src=\"images/trans.gif\" width=\"600\" height=\"1\" border=\"0\" alt=\"\"><br>" );
    s.append ( "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td style=\"color : #ffffff\">" );

    if ( mode == SurveyEntryForm.modeEdit )
      s.append ( this.getQuestionTypeTextHTML () );
    if ( mode == SurveyEntryForm.modeResultsExport ) {
      if ( !this.getClass().getName().equals("edu.vt.ward.survey.InputComment") ) {
        s.append ( "<input type=\"checkbox\" name=\"include_" + sectionNr + "_" + questionNr + "\" value=\"1\"");
        if ( this.getExportInclude() )
          s.append ( " checked" );
        s.append ( "> "+Config.msg(305) );
      }
      else {
        s.append ( "&nbsp;" );
      }

      if ( this.getClass().getName ().equals( "edu.vt.ward.survey.InputRadio" ) ||
              this.getClass().getName ().equals( "edu.vt.ward.survey.InputCheckbox" ) ) {
        s.append ( "&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" name=\"expand_" + sectionNr + "_" + questionNr + "\" value=\"1\"");
        if ( this.getExportExpand() )
          s.append ( " checked" );
        s.append ( "> "+Config.msg(306) );
      }
      else {
        s.append ( "&nbsp;" );
      }

    }

    s.append ( "</td><td nowrap align=\"right\">" );
    if ( mode == SurveyEntryForm.modeEdit ) {
      s.append ( this.getEditCopyDeleteUpDownButtonHTML ( sectionNr, questionNr ) );
      s.append ( "&nbsp;&nbsp;" );
      s.append ( this.getAddAboveButtonsHTML ( sectionNr, questionNr ) );
    }
    s.append ( "</td></tr></table>" );
    s.append ( "</td></tr><tr><td class=\"globalsettings\">" );

    return s.toString ();
  }

  // this method is overwritten by all extended classes
  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return Config.msg(307);
  }

  public String getTableCloseHTML () {
    return "</td></tr></table>\n";
  }

  public String getQuestionTextHTML () {
    StringBuffer s = new StringBuffer ();

    s.append ( "<b>"+Config.msg(308)+"</b> <font color=\"#999999\">"+Config.msg(277)+"</font><br>\n" );
    s.append ( "<textarea name=\"prompt\" wrap=\"physical\" cols=\"60\" rows=\"3\">" );
    s.append ( HTMLUtils.encode ( this.getText () ) );
    s.append ( "</textarea><br><br>\n" );

    s.append ( this.getDividerQuestionHTML () );

    return s.toString ();
  }

  public String getEditFormBeginHTML ( int sectionNr, int questionNr, String type ) {
    return getEditFormBeginHTML ( sectionNr, questionNr, type, true );
  }

  public String getEditFormBeginHTML ( int sectionNr, int questionNr, String type, boolean showPrompt ) {
    StringBuffer s = new StringBuffer ("");
    s.append ( "<form action=\"editQuestion.jsp?section=" + sectionNr + "&question=" + questionNr );
    if ( type != null ) { s.append ( "&type=" + type ); }
    s.append ( "\" method=\"post\">\n" );
    s.append ( "<input type=\"hidden\" name=\"surveyId\" value=\"" + this.getSurveyId () + "\">\n" );

    if ( showPrompt ) { s.append( getQuestionTextHTML () ); }

    return s.toString();
  }

  public String getDividerQuestionHTML () {
    StringBuffer s = new StringBuffer ();

    s.append ( "<b>"+Config.msg(309)+"</b><br>" );
    s.append ( "<font color=\"#999999\">"+Config.msg(310)+"</font><br>\n" );
    s.append ( "<input type=\"radio\" name=\"showDivider\" value=\"1\"" );
    if ( this.getShowDividerAsString ().equals ( "1" ) )
      s.append ( " checked" );
    s.append ( "> "+Config.msg(311)+"<br>\n" );
    s.append ( "<input type=\"radio\" name=\"showDivider\" value=\"0\"" );
    if ( this.getShowDividerAsString ().equals ( "0" ) )
      s.append ( " checked" );
    s.append ( "> "+Config.msg(312)+"<br>\n" );
    s.append ( "<br>\n" );

    return s.toString ();
  }

  public String getEditFormEndHTML () {
    return "<input type=\"submit\" class=\"button\" name=\"save\" value=\""+Config.msg(23)+"\">&nbsp;&nbsp;<input type=\"submit\" class=\"button\" name=\"cancel\" value=\""+Config.msg(24)+"\">\n</form>\n";
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    try {
      this.setShowDivider ( (String) request.getParameter ( "showDivider" ) );
    }
    catch ( Exception e ) { // response to user input error
    }

    String prompt = (String) request.getParameter ( "prompt" );
    if ( prompt != null ) {
      this.setText ( prompt );
    }

    return null; // no input error occurred
  }

  // this method is overwritten by all extended classes
  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    return null;
  }

  // place holder; gets overwritten
  public void updateResultsSummary ( Question entryQuestion ) {
  }

  // this method is overwritten by all extended classes
  public String getResultsDetailsHTML ( Question entryQuestion ) {
    return "";
  }

  // place holder
  public String getExport ( Question entryQuestion, String delimiter ) {
    return "";
  }

  static public Question getNewQuestion (String surveyId, String type ) {
    if ( type.equals ( "comment" ) ) {
      return (Question) new InputComment (surveyId);
    }
    else if ( type.equals ( "radio" ) ) {
      return (Question) new InputRadio (surveyId);
    }
    else if ( type.equals ( "checkbox" ) ) {
      return (Question) new InputCheckbox (surveyId);
    }
    else if ( type.equals ( "textline" ) ) {
      return (Question) new InputTextline (surveyId);
    }
    else if ( type.equals ( "textarea" ) ) {
      return (Question) new InputTextarea (surveyId);
    }
    else {
      return null;
    }
  }
}