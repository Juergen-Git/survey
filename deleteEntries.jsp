<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { 
  	response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; 
  }

  if ( request.getParameter ( "deleteEntries" ) != null ) {
    // delete all entries
    // loop through all entry files
    File surveyDirFile = new File ( survey.getSurveyDir () );
    java.io.File[] entryFileList = surveyDirFile.listFiles( new FilenameFilterEntryFile () );
    for ( int i = 0; i < entryFileList.length; i++ ) {
      entryFileList[i].delete();
    }
    // loop through all entry exists files
    java.io.File[] entryExistsFileList = surveyDirFile.listFiles( new FilenameFilterEntryExistsFile () );
    for ( int i = 0; i < entryExistsFileList.length; i++ ) {
      entryExistsFileList[i].delete();
    }

    // delete summary entry form
    SurveyEntryForm resultsSummary = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, SurveyEntryForm.resultsSummaryXML, false );
    resultsSummary.delete ();

    response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return;
  }

  String pageTitle = Config.msg(58);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
<input type="hidden" name="surveyId" value="<%=surveyId%>">
<%
  if ( survey.getNumEntries() == 0 ) {
%>
  <%=Config.msg(59)%> &quot;<%=survey.getName()%>&quot; <%=Config.msg(60)%><br>
  <br>
<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
<%
  }
  else {
%>
  <%=Config.msg(334)%> <a href="viewResultsDetails.jsp?surveyId=<%=surveyId%>"><%=Config.msg(249)%></a>.
	<br>
	<br>
	<font color="#ff0000"><b><%=Config.msg(61)%></b></font>
  <%=survey.getNumEntries ()%> <%
  if (survey.getNumEntries()>1) { out.print( Config.msg(62) ); } else { out.print( Config.msg(63)); }
%> <%=Config.msg(64)%> &quot;<%=survey.getName()%>&quot; <%=Config.msg(65)%><br>
  <br>
<input type="submit" name="deleteEntries" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
<%
  } // end: else: if ( survey.getNumEntries() == 0 )
%>
</form>
<survey:pageFooter />