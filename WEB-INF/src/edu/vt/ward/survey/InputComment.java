package edu.vt.ward.survey;

import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public class InputComment extends Question {
  private String comment = ""; // contains the text that gets displayed as the comment
  private boolean isHTML = false; // true, if the comment is HTML rather than plain text

  public InputComment (String surveyId) {
    super (surveyId);
  }

  public InputComment (String surveyId, String comment ) {
    super (surveyId, comment );
  }

  public InputComment (String surveyId, Element sourceElement ) {
    super (surveyId, sourceElement ); // assign questionText

    Element InputCommentElem = sourceElement.getChild ( "inputComment" );

    try { this.setComment ( HTMLUtils.newLineDecode ( InputCommentElem.getTextTrim () ) ); }
    catch ( Exception e ) { }

    try { this.setIsHTML ( InputCommentElem.getAttributeValue( "isHTML" ).equals ( "1" ) ); }
    catch ( Exception e ) { }
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return Config.msg(257);
  }

  public String getComment () {
    return this.comment;
  }

  public void setComment ( String comment ) {
    this.comment = comment;
  }

  public boolean getIsHTML () {
    return this.isHTML;
  }

  public void setIsHTML ( boolean isHTML ) {
    this.isHTML = isHTML;
  }

  public String getHTML ( SurveyMetaData survey, int mode, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();

    s.append ( super.getHTML ( survey, mode, sectionNr, questionNr ) ); // get HTML for text of the question
    if ( this.getIsHTML () ) {
      s.append ( this.getComment () );
    }
    else {
      s.append ( HTMLUtils.newLine2Br ( HTMLUtils.encode ( this.getComment () ) ) );
      s.append ( "<br>\n");
    }

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsExport )
      s.append ( this.getTableCloseHTML () + this.getTableCloseHTML () );

    if ( mode == SurveyEntryForm.modeResultsSummary )
      s.append ( this.getTableCloseHTML () );

    return s.toString();
  }

  public Element getXML ( int mode ) {

    Element q = super.getXML ( mode );
    Element inputComment = new Element ( "inputComment" );
    if ( mode == SurveyEntryForm.modeEdit ) {
      if ( this.getIsHTML () ) {
        inputComment.setAttribute ( new Attribute ( "isHTML", "1" ) );
      }
      else {
        inputComment.setAttribute ( new Attribute ( "isHTML", "0" ) );
      }
      inputComment.addContent ( HTMLUtils.newLineEncode ( HTMLUtils.xmlFilter(this.getComment ()) ) );
    }

//    if ( mode == SurveyEntryForm.modeEntry ) {
//      inputComment.addContent ( this.getComment () );
//    }

    q.addContent ( inputComment );

    return q;
  }

  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    StringBuffer s = new StringBuffer ();

//    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
    s.append ( super.getEditFormBeginHTML ( sectionNr, questionNr, type, false ) );

    s.append ( "<b>"+Config.msg(260)+":</b> \n" );
    s.append ( "<input type=\"radio\" name=\"texttype\" value=\"plain\"" );
    if ( !this.getIsHTML () ) { s.append ( " checked" ); }
    s.append ( "> "+Config.msg(258)+" &nbsp;&nbsp;&nbsp;" );
    s.append ( "<input type=\"radio\" name=\"texttype\" value=\"html\"" );
    if ( this.getIsHTML () ) { s.append ( " checked" ); }
    s.append ( "> "+Config.msg(259)+"<br>" );
    s.append ( "<textarea name=\"comment\" wrap=\"virtual\" cols=\"60\" rows=\"15\">" + HTMLUtils.encode ( this.getComment () ) + "</textarea>\n" );
    s.append ( "<br><br>" );
    s.append ( super.getDividerQuestionHTML () );
    s.append ( super.getEditFormEndHTML () );

    return s.toString ();
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    super.makeEditFormChanges ( request );

    try {
      this.setComment ( (String) request.getParameter ( "comment" ) );
    }
    catch ( Exception e ) { // response to user input error
      return "invalidComment";
    }

    try {
      if ( ((String) request.getParameter ( "texttype" )).equals ( "html" ) ) {
        this.setIsHTML ( true );
      }
      else {
        this.setIsHTML ( false );
      }
    }
    catch ( Exception e ) { // response to user input error
      return "invalidTextType";
    }

    return null; // no user input error occurred
  }

  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    return null; // no input error occurred
  }

}
