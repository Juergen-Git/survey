<%@ include file="/globalNoAuthRequired.jsp" %><%

  if ( (String) request.getParameter ( "backToMenu" ) != null ) { response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; }

  if ( surveyId == null ) {
    response.sendRedirect("error.jsp?errorId=unknownSurveyId");
    return;
  }

  SurveyMetaData survey = new SurveyMetaData ( Config.appDataDir, surveyId );
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  // make sure survey exists and had been open at some point in time
  if ( !survey.exists() || !entryForm.exists () || survey.getOpened ().equals("") ) {
    response.sendRedirect("error.jsp?surveyId="+surveyId+"&errorId=notexistSurvey");
    return;
  }
//  else {
//    mysession.setAttribute(null,  "surveyId", surveyId );
//  }

  String pid = (String) mysession.getAttribute(null,  "pid" );
  // is the person that is currently logged on the owner of this survey? (if so, don't require password)
  if ( !survey.getAccessResultsRestriction().equals("anyone") ) {
    if ( survey.getAccessResultsRestriction().equals("password") ) {
      // test if password is in the session
      String surveyPassword = (String) mysession.getAttribute(surveyId,  "surveyPassword" );
      if ( surveyPassword == null || !surveyPassword.equals (survey.getResultsPassword()) ) {
        response.sendRedirect("askSurveyPassword.jsp?surveyId="+surveyId+"&returnTo="+java.net.URLEncoder.encode("viewResults.jsp?surveyId="+surveyId) );
        return;
      }
    }
    else {
      if ( pid == null || !survey.isAdmin (pid) ) {
        response.sendRedirect ( "login.jsp?r=1" );
        return;
      }
    }
  } // end: if ( !survey.getAccessResultsRestriction().equals("anyone") )


  if ( Config.resultSummaryForms.containsKey(surveyId) ) {
    entryForm = (SurveyEntryForm) Config.resultSummaryForms.get(surveyId);
  }
  else {
    entryForm.createResultsSummary ();
  }

//  entryForm.saveResultsSummary ();

  String pageTitle = Config.msg(245);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );

%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
  <input type="hidden" name="surveyId" value="<%=surveyId%>">
  <input type="submit" name="backToMenu" value="<%=Config.msg(242)%>" class="button">
</form>
<%=Config.msg(246)%> <b><%=survey.getNumEntries ()%></b> <%=Config.msg(247)%>. <a href="viewResultsDetails.jsp?surveyId=<%=surveyId%>"><%=Config.msg(248)%></b></a><br>
<br>
<%
  out.print ( entryForm.getHTML ( survey, SurveyEntryForm.modeResultsSummary ) );
%>
<survey:pageFooter />