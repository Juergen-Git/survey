<%@ include file="/globalNoAuthRequired.jsp" %><%
  String errorId = "";
  if ( request.getParameter ( "errorId" ) != null ) {
    errorId = (String) request.getParameter ( "errorId" );
  }

  String pageTitle = "Error!";
  if ( errorId.equals ("unknownSurveyId") ) {
    pageTitle = "Error! Survey does not exist";
  }
  else if ( errorId.equals ("surveyNotOpen") ) {
     pageTitle = "Error! Survey is not open for data entry";
  }

%><survey:pageHeader title="<%=pageTitle%>" /><%

  if ( errorId.equals ("unknownSurveyId") ) {
%><p>No valid survey has been specified.
<p>
A well-formed web address for a survey is for example:<br>
<%=Config.urlScheme%>://<%=Config.hostName%><%=Config.rootURL%>entry.jsp?id=982256422561<br>
<br>
The number at the end of the web address points to a particular survey.
<%
  }
  else if ( errorId.equals ("surveyNotOpen") ) {
%><p>This survey is currently not open for data entry.
<%
  }
%><survey:pageFooter />