<%@ include file="/globalAuthRequired.jsp" %><%
  String entryId = null;
  if ( request.getParameter ( "entryid" ) != null ) {
    entryId = (String) request.getParameter ( "entryid" );
  }
  else {
    entryId = (String) mysession.getAttribute(surveyId,  "entryId" );
  }
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  boolean entryExists = entryForm.readEntryResults ( entryId );

  if ( surveyId == null || entryId == null || !entryExists) {
    response.sendRedirect("error.jsp");
    return;
  }

  if ( (String) request.getParameter ( "cancel" ) != null ) {
	  response.sendRedirect("viewResultsDetails.jsp?surveyId="+surveyId); return;
	}

  if ( request.getParameter ( "deleteEntry" ) != null ) {
    SurveyEntryForm entry = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, "entry." + entryId, true );
    String entryAuthUser = entry.getEntryAuthUser();
    // delete entry
    File entryFile = new File ( survey.getSurveyDir (), "entry."+entryId );
    try { entryFile.delete(); } catch ( Exception e) {}

    // delete entry exists file
    if ( entryAuthUser != null && !entryAuthUser.equals("") ) {
      File entryExistsFile = new File ( survey.getSurveyDir (), "entryexists."+entryAuthUser );
      entryExistsFile.delete();
    }

    // delete summary entry form
    SurveyEntryForm resultsSummary = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, SurveyEntryForm.resultsSummaryXML, false );
    resultsSummary.delete ();

    response.sendRedirect("viewResultsDetails.jsp?surveyId="+surveyId); return;
  }
  mysession.setAttribute(surveyId, "entryId", entryId);
  String pageTitle = Config.msg(330);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
  <input type="hidden" name="surveyId" value="<%=surveyId%>">
  <font color="#ff0000"><b><%=Config.msg(61)%></b></font>
  <%=Config.msg(331)%> &quot;<%=DateUtil.formatDate( DateUtil.ISO4601DateFormat, new Date ( Long.parseLong(entryId) ) )%>&quot; <%=Config.msg(65)%><br>
  <br>
<input type="submit" name="deleteEntry" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />