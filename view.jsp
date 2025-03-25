<%@ include file="/globalNoAuthRequired.jsp" %><%

  if ( (String) request.getParameter ( "backToMenu" ) != null ) { 
  	response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; 
  }

  String entryId = null;
  if ( request.getParameter ( "entryid" ) != null ) {
    entryId = (String) request.getParameter ( "entryid" );
  }
  else {
    entryId = (String) mysession.getAttribute(surveyId,  "entryId" );
  }

  if ( surveyId == null || entryId == null ) {
    response.sendRedirect("error.jsp");
    return;
  }

  SurveyMetaData survey = new SurveyMetaData ( Config.appDataDir, surveyId );
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  // make sure survey exists and had been open at some point in time
  if ( !survey.exists() || !entryForm.exists () || survey.getOpened ().equals("") ) {
    response.sendRedirect("error.jsp?surveyId="+surveyId);
    return;
  }
//  else {
//    mysession.setAttribute(null,  "surveyId", surveyId );
//  }

  boolean entryExists = entryForm.readEntryResults ( entryId );
  // make sure survey exists and had been open at some point in time
  if ( !survey.exists() || !entryForm.exists () || survey.getOpened ().equals("") || !entryExists ) {
    response.sendRedirect("error.jsp?surveyId="+surveyId);
    return;
  }
  else {
    mysession.setAttribute(surveyId,  "entryId", entryId );
  }

  String pid = (String) mysession.getAttribute(null,  "pid" );
  // is the person that is currently logged on the owner of this survey? (if so, don't require password)
  if ( !survey.getAccessResultsRestriction().equals("anyone") ) {
    if ( survey.getAccessResultsRestriction().equals("password") ) {
      // test if password is in the session
      String surveyPassword = (String) mysession.getAttribute(surveyId,  "surveyPassword" );
      if ( surveyPassword == null || !surveyPassword.equals (survey.getResultsPassword()) ) {
        response.sendRedirect("askSurveyPassword.jsp?surveyId="+surveyId+"&returnTo=" + java.net.URLEncoder.encode ( "view.jsp?surveyId=" + surveyId + "&entryid=" + entryId ) );
        return;
      }
    }
    else {
      if ( pid == null || !survey.isAdmin (pid) ) {
        response.sendRedirect ( "login.jsp?surveyId="+surveyId+"&returnTo=" + java.net.URLEncoder.encode ( "view.jsp?surveyId=" + surveyId + "&entryid=" + entryId ) );
        return;
      }
    }
  } // end: if ( !survey.getAccessResultsRestriction().equals("anyone") )

//  if ( surveyId != null ) { mysession.setAttribute(surveyId,  "surveyId", surveyId ); }

  String pageTitle = Config.msg(241);
  if ( pid != null ) {
    mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; <a href=\"viewResults.jsp?surveyId="+surveyId+"\">"+Config.msg(229)+"</a> &gt; " + pageTitle );
  }
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
 <input type="hidden" name="surveyId" value="<%=surveyId%>">
 <input type="submit" name="backToMenu" value="<%=Config.msg(242)%>" class="button">
</form>
<%=Config.msg(243)%> <b><%=DateUtil.formatDate( DateUtil.ISO4601DateFormat, new Date ( Long.parseLong(entryId)))%></b>
&nbsp;&nbsp;<a href="viewResults.jsp?surveyId=<%=surveyId%>"><%=Config.msg(244)%></a>
&nbsp;&nbsp;<a href="deleteEntry.jsp?surveyId=<%=surveyId%>&entryid=<%=entryId%>"><%=Config.msg(333)%></a><br>
<br>
<%
  survey.singleEntry = true;
  out.print( entryForm.getHTML ( survey, SurveyEntryForm.modeResultsSummary ) );
%>
<survey:pageFooter />