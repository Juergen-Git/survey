package edu.vt.ward.survey;

import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public class InputTextline extends Question {
  private static int minSize = 1;
  private static int maxSize = 100;
  private static int minMaxLength = 1;
  private static int maxMaxLength = 300;
  private int size = 30;
  private int maxLength = 300;
  private String label = "";
  private String value = ""; // contains the text the user inputs
  private long count = 0;

  public InputTextline (String surveyId) {
    super (surveyId);
  }

  public InputTextline (String surveyId, String text, String label ) {
    super (surveyId, text );
    this.setLabel ( label );
  }

  public InputTextline (String surveyId, Element sourceElement ) {
    super (surveyId, sourceElement ); // assign questionText

    Element InputTextlineElem = sourceElement.getChild ( "inputTextline" );

    try { this.setValue ( InputTextlineElem.getTextTrim () ); }
    catch ( Exception e ) { }

    try { this.setLabel ( InputTextlineElem.getAttributeValue( "label" ) ); }
    catch ( Exception e ) { }

    try { this.setSize ( Integer.parseInt ( InputTextlineElem.getAttributeValue( "size" ) ) ); }
    catch ( Exception e ) { }

    try { this.setMaxLength ( Integer.parseInt ( InputTextlineElem.getAttributeValue( "maxLength" ) ) ); }
    catch ( Exception e ) { }

    try { this.setCount ( Long.parseLong ( InputTextlineElem.getAttributeValue( "count" ) ) ); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return Config.msg(273);
  }

  public String getValue () {
    return this.value;
  }

  public void setValue ( String value ) {
    this.value = value;
  }

  public int getSize () {
    return this.size;
  }

  public void setSize ( int size ) {
    this.size = size;
  }

  public int getMaxLength () {
    return this.maxLength;
  }

  public void setMaxLength ( int maxLength ) {
    this.maxLength = maxLength;
  }

  public String getLabel () {
    return this.label;
  }

  public void setLabel ( String newLabel ) {
    this.label = newLabel;
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
    if ( !this.getLabel ().equals ( "" ) ) {
      s.append ( this.getLabel () );
      s.append(" ");
    }

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeResultsExport ) {
      s.append ( "<input type=\"text\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"\"" );
      if ( this.getSize () > 0 )
        s.append ( " size=\"" + String.valueOf ( this.getSize () ) + "\"" );
      if ( this.getMaxLength () > 0 )
        s.append ( " maxLength=\"" + String.valueOf ( this.getMaxLength () ) + "\"" );
      s.append ( ">");
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
        if ( this.getCount() == 1 ) { s.append ( Config.msg(264) ); } else { s.append ( Config.msg(265) ); }
        s.append ( "</b></span>" );
        if ( this.getCount() > 0 ) {
          s.append (" &nbsp;&nbsp;<a href=\"viewResultsQuestion.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\">"+Config.msg(266)+"</a>&nbsp; <a href=\"viewResultsDetails.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "\">"+Config.msg(267)+"</a>");
        }
      } // end: else: if ( survey.singleEntry )
    } // end: if ( mode == SurveyEntryForm.modeResultsSummary )

    s.append ( "<br>\n");

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
    Element inputTextline = new Element ( "inputTextline" );
    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsSummary ) {
      if (label != null)
        inputTextline.setAttribute ( new Attribute ( "label", HTMLUtils.xmlFilter(this.getLabel ()) ) );

      inputTextline.setAttribute ( new Attribute ( "size", String.valueOf ( this.getSize () ) ) );
      inputTextline.setAttribute ( new Attribute ( "maxLength", String.valueOf ( this.getMaxLength () ) ) );
    }
    if ( mode == SurveyEntryForm.modeResultsSummary ) {
      inputTextline.setAttribute ( new Attribute ( "count", String.valueOf ( this.getCount () ) ) );
    }

    if ( mode == SurveyEntryForm.modeEntry ) {
      inputTextline.addContent ( HTMLUtils.xmlFilter(this.getValue ()) );
    }

    q.addContent ( inputTextline );

    return q;
  }

//  public String getEditFormHTML ( int sectionNr, int questionNr, String inputErrorId ) {
//    this.getEditFormHTML ( sectionNr, questionNr, null, inputErrorId );
//  }

  // if "type" is not null, then it means that a new question should be created.
  // therefore send "type" in the form
  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    StringBuffer s = new StringBuffer ();

    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
    s.append ( Config.msg(274) );

    s.append ( super.getEditFormBeginHTML ( sectionNr, questionNr, type ) );

    if ( inputErrorId != null && inputErrorId.equals ( "invalidLabel" ) )
      s.append ( "<br><font color=\"#ff0000\"><b>"+Config.msg(268)+"</b></font> "+Config.msg(275)+"<br><br>" );
    s.append ( "<b>"+Config.msg(276)+"</b> <input type=\"text\" name=\"label\" size=\"30\" maxLength=\"100\" value=\"" );
    if ( this.getLabel () != null )
      s.append ( HTMLUtils.encode ( this.getLabel () ) );
    s.append ( "\"> <font color=\"#999999\">"+Config.msg(277)+"</font><br><br>" );

    if ( inputErrorId != null && inputErrorId.equals ( "invalidSize" ) )
      s.append ( "<br><font color=\"#ff0000\"><b>"+Config.msg(268)+"</b></font> "+Config.msg(278)+"<br><br>" );
    s.append ( "<b>"+Config.msg(279)+"</b>\n" );
    s.append ( "<input type=\"text\" name=\"size\" size=\"3\" maxLength=\"3\" value=\"" + this.getSize () + "\"> " );
    s.append ( "<font color=\"#999999\">(" + minSize + " "+Config.msg(56)+" " + maxSize + ")</font>" );
    s.append ( "<br><br>" );

    if ( inputErrorId != null && inputErrorId.equals ( "invalidMaxLength" ) )
      s.append ( "<br><font color=\"#ff0000\"><b>"+Config.msg(268)+"</b></font> "+Config.msg(280)+"<br><br>" );
    s.append ( "<b>"+Config.msg(281)+"</b>\n" );
    s.append ( "<input type=\"text\" name=\"maxLength\" size=\"3\" maxLength=\"3\"  value=\"" );
    if ( this.getMaxLength () > 0 )
      s.append ( String.valueOf ( this.getMaxLength () ) );
    s.append ( "\"> " );
    s.append ( "<font color=\"#999999\">(" + minMaxLength + " "+Config.msg(56)+" " + maxMaxLength + ")</font>" );
    s.append ( "<br><br>" );
    s.append ( super.getEditFormEndHTML () );

    return s.toString ();
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    super.makeEditFormChanges ( request );

    try {
      this.setLabel ( (String) request.getParameter ( "label" ) );
    }
    catch ( Exception e ) { // response to user input error
      return "invalidLabel";
    }

    try {
      int size = Integer.parseInt ( (String) request.getParameter ( "size" ) );
      if ( size >= minSize && size <= maxSize )
        this.setSize ( size );
      else
        throw new NumberFormatException ();
    }
    catch ( Exception NumberFormatException ) { return "invalidSize"; }

    try {
      String maxLengthStr = (String) request.getParameter ( "maxLength" );
      if ( maxLengthStr == null ) {
        throw new NumberFormatException ();
      }
      else if ( maxLengthStr.equals ( "" ) ) {
        this.setMaxLength ( 0 );
      }
      else {
        int maxLength = Integer.parseInt ( maxLengthStr );
        if ( maxLength >= minMaxLength && maxLength <= maxMaxLength )
          this.setMaxLength ( maxLength );
        else
          throw new NumberFormatException ();
      }
    }
    catch ( Exception NumberFormatException ) { return "invalidMaxLength"; }

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
    if ( !((InputTextline) entryQuestion).getValue ().equals("") ) { // increment count for this question
      this.incrementCount();
      this.setValue( ((InputTextline) entryQuestion).getValue () );
    }
  }

  public String getResultsDetailsHTML ( Question entryQuestion ) {
    return ((InputTextline) entryQuestion).getValue ();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    return Config.translateExportDelimiters ( ((InputTextline) entryQuestion).getValue (), delimiter);
  }

}
