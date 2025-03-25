package edu.vt.ward.survey;

import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public class InputTextarea extends Question {
  private static int minCols = 10;
  private static int maxCols = 100;
  private static int minRows = 2;
  private static int maxRows = 50;

  private int cols = 60;
  private int rows = 3;
  private String value = ""; // contains the text the user inputs
  private long count = 0;

  public InputTextarea (String surveyId) {
    super (surveyId);
  }

  public InputTextarea (String surveyId, String text, int cols, int rows ) {
    super (surveyId, text );
    this.setCols ( cols );
    this.setRows ( rows );
  }

  public InputTextarea (String surveyId,  Element sourceElement ) {
    super (surveyId, sourceElement ); // assign questionText

    Element InputTextareaElem = sourceElement.getChild ( "inputTextarea" );

    try { this.setValue ( HTMLUtils.newLineDecode( InputTextareaElem.getTextTrim () ) ); }
    catch ( Exception e ) { this.setValue("Test"); }

    try { this.setCols ( Integer.parseInt ( InputTextareaElem.getAttributeValue( "cols" ) ) ); }
    catch ( Exception e ) { }

    try { this.setRows ( Integer.parseInt ( InputTextareaElem.getAttributeValue( "rows" ) ) ); }
    catch ( Exception e ) { }

    try { this.setCount ( Long.parseLong ( InputTextareaElem.getAttributeValue( "count" ) ) ); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return Config.msg(262); //"Short Answer - multiple lines";
  }

  public String getValue () {
    return this.value;
  }

  public void setValue ( String value ) {
    this.value = value;
  }

  public int getCols () {
    return this.cols;
  }

  public void setCols ( int cols ) {
    this.cols = cols;
  }

  public int getRows () {
    return this.rows;
  }

  public void setRows ( int rows ) {
    this.rows = rows;
  }

  public long getCount () {
    return this.count;
  }

  public void setCount ( long count ) {
    this.count = count;
  }

  public void incrementCount () {
    this.count++;
  }

  public String getHTML ( SurveyMetaData survey, int mode, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();

    s.append ( super.getHTML ( survey, mode, sectionNr, questionNr ) ); // get HTML for text of the question

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeResultsExport ) {
      s.append ( "<textarea name=\"q_" + sectionNr + "_" + questionNr + "\" wrap=\"virtual\" cols=\"" + String.valueOf( this.getCols () ) + "\" rows=\"" + String.valueOf( this.getRows () ) + "\" ></textarea>\n" );
    }

    if ( mode == SurveyEntryForm.modeResultsSummary ) {

      if ( survey.singleEntry ) {
        if ( this.getCount() > 0 ) {
          s.append ( "<span class=\"highlightresponses\">" + this.getValue () + "</span>" );
        }
        else {
          s.append ( "<span class=\"highlightresponses\"><i>"+Config.msg(263)+"</i></span>" );
        }
      }
      else {
        s.append ( "<span class=\"highlightresponses\"><b>" );
        s.append ( String.valueOf ( this.getCount () ) + " ");

        if ( this.getCount() == 1 ) { s.append ( Config.msg(264)); } else { s.append ( Config.msg(265) ); }
        s.append ( "</b></span>" );
        if ( this.getCount() > 0 ) {
          s.append (" &nbsp;&nbsp;<a href=\"viewResultsQuestion.jsp?surveyId=" + this.getSurveyId() + "&section=" + sectionNr + "&question=" + questionNr + "\">"+Config.msg(266)+"</a>&nbsp; <a href=\"viewResultsDetails.jsp?surveyId=" + this.getSurveyId() + "&section=" + sectionNr + "&question=" + questionNr + "\">"+Config.msg(267)+"</a>");
        }
      } // end: else: if ( survey.singleEntry )
    }
    s.append ( "<br>\n" );

    // javascript to disable the form elements when in edit or export mode
    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsExport ) {
      s.append ("<script language=\"JavaScript\" type=\"text/javascript\"><!--\n");
      s.append ("document.form.q_" + sectionNr + "_" + questionNr + ".disabled = true;\n" );
      s.append ("//-->\n</script>\n");
    }

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsExport )
      s.append ( this.getTableCloseHTML () + this.getTableCloseHTML () );

    if ( mode == SurveyEntryForm.modeResultsSummary )
      s.append ( this.getTableCloseHTML () );


    return s.toString();
  }

  public Element getXML ( int mode ) {

    Element q = super.getXML ( mode );
    Element inputTextarea = new Element ( "inputTextarea" );
    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsSummary ) {
      inputTextarea.setAttribute ( new Attribute ( "cols", String.valueOf ( this.getCols () ) ) );
      inputTextarea.setAttribute ( new Attribute ( "rows", String.valueOf ( this.getRows () ) ) );
    }
    if ( mode == SurveyEntryForm.modeResultsSummary ) {
      inputTextarea.setAttribute ( new Attribute ( "count", String.valueOf ( this.getCount () ) ) );
    }

    if ( mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modeResultsSummary ) {
      inputTextarea.addContent ( HTMLUtils.newLineEncode( HTMLUtils.xmlFilter(this.getValue ()) ) );
    }

    q.addContent ( inputTextarea );

    return q;
  }

  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    StringBuffer s = new StringBuffer ();

    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
    s.append ( super.getEditFormBeginHTML ( sectionNr, questionNr, type ) );

    if ( inputErrorId != null && inputErrorId.equals ( "invalidCols" ) )
      s.append ( "<br><font color=\"#ff0000\"><b>"+Config.msg(268)+"</b></font> "+Config.msg(269)+"<br><br>" );
    s.append ( "<b>"+Config.msg(270)+"</b>\n" );
    s.append ( "<input type=\"text\" name=\"cols\" size=\"3\" maxlength=\"3\" value=\"" + this.getCols () + "\"> " );
    s.append ( "<font color=\"#999999\">(" + minCols + " "+Config.msg(56)+" " + maxCols + ")</font>" );
    s.append ( "<br><br>" );

    if ( inputErrorId != null && inputErrorId.equals ( "invalidRows" ) )
      s.append ( "<br><font color=\"#ff0000\"><b>"+Config.msg(268)+"</b></font> "+Config.msg(271)+"<br><br>" );
    s.append ( "<b>"+Config.msg(272)+"</b>\n" );
    s.append ( "<input type=\"text\" name=\"rows\" size=\"2\" maxlength=\"2\"  value=\"" + this.getRows () + "\"> " );
    s.append ( "<font color=\"#999999\">("+ minRows +" "+Config.msg(56)+" " + maxRows + ")</font>" );
    s.append ( "<br><br>" );

    s.append ( "</textarea><br><br>\n" );
    s.append ( super.getEditFormEndHTML () );

    return s.toString ();
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    super.makeEditFormChanges ( request );

    try {
      int cols = Integer.parseInt ( (String) request.getParameter ( "cols" ) );
      if ( cols >= minCols && cols <= maxCols )
        this.setCols ( cols );
      else
        throw new NumberFormatException ();
    }
    catch ( Exception NumberFormatException ) { return "invalidCols"; }

    try {
      int rows = Integer.parseInt ( (String) request.getParameter ( "rows" ) );
      if ( rows >= minRows && rows <= maxRows )
        this.setRows ( rows );
      else
        throw new NumberFormatException ();
    }
    catch ( Exception NumberFormatException ) { return "invalidRows"; }

    return null; // no user input error occurred
  }

  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    try {
      this.setValue ( (String) request.getParameter ( "q_" + sectionNr + "_" + questionNr ) );
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }

  public void updateResultsSummary ( Question entryQuestion ) {
    if ( !((InputTextarea) entryQuestion).getValue ().equals("") ) { // increment count for this question
      this.incrementCount();
      this.setValue( ((InputTextarea) entryQuestion).getValue () );
    }
  }

  public String getResultsDetailsHTML ( Question entryQuestion ) {
    return ((InputTextarea) entryQuestion).getValue ();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    return Config.translateExportDelimiters ( ((InputTextarea) entryQuestion).getValue (), delimiter);
  }

}
