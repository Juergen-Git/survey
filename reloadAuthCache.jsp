<%@ include file="/globalAuthRequired.jsp" %><%
  Config.loadSurveyAuthCache(surveyId);

  Hashtable surveyAuthCache = (Hashtable) Config.authorizationCache.get(surveyId);
  Enumeration users = surveyAuthCache.keys();

  int i = 0;
  while ( users.hasMoreElements() ) {
    out.print("\""+(String)users.nextElement()+"\"<br>\n");
    i++;
  }
  out.print("<br><br>"+i);
%>