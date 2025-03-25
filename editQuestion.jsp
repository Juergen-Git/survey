<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId ); return; }

  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));
  String type = (String) request.getParameter ( "type" );
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  String inputErrorId = null;

  // save the changes if the user selected "save"
  if ( (String) request.getParameter ( "save" ) != null ) {

    // create *new* question ?!
    if ( type != null ) {
      entryForm.getSection ( sectionNr ).addQuestion ( questionNr, Question.getNewQuestion (surveyId, type ) );
    }

    inputErrorId = entryForm.getSection ( sectionNr ).getQuestion ( questionNr ).makeEditFormChanges ( request );
    if ( inputErrorId == null ) {
      entryForm.setIp ( request.getRemoteAddr() );
      entryForm.setBrowserId( (String) request.getHeader ( "User-Agent") );

      entryForm.save ();
      response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId+"#q_" + sectionNr + "_" + questionNr );
      return;
    }
  }

  String pageTitle = Config.msg(113);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; <a href=\"editEntryForm.jsp?surveyId="+surveyId+"\">"+Config.msg(9)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<%
  out.print ( entryForm.getSection ( sectionNr ).getQuestion ( questionNr, type ).getEditFormHTML ( sectionNr, questionNr, type, inputErrorId ) );
%>
<survey:pageFooter />