<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { 
  	 response.sendRedirect ( "manageSurveyMenu.jsp?surveyId=" + surveyId ); return; 
  }

  if ( request.getParameter ( "save" ) != null ) {
    // use new survey title from the HTML form
    String resultsPassword = request.getParameter ( "resultsPassword" );
    survey.setResultsPassword ( resultsPassword );
    survey.save ();

    response.sendRedirect("manageSurveyMenu.jsp?surveyId=" + surveyId); return;
  }

  String pageTitle = "Edit password required for viewing results";
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">My Surveys</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
  <input type="hidden" name="surveyId" value="<%=surveyId%>">
<b>Password:</b><br>
<input type="password" name="resultsPassword" size="15" maxLength="10" value="<%=HTMLUtils.encode(survey.getResultsPassword())%>">
<font color="#999999">(case-sensitive; if left blank anyone may view the survey results)</font>
  <br>
  <br>
  <input type="submit" name="save" value="&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="Cancel" class="button">
</form>
<survey:pageFooter />