<%@ include file="/globalAuthRequired.jsp" %><%
  // read all survey meta data (parameter "true")
  User user = new User ( Config.appDataDir, pid );

  if ( (String) request.getParameter ( "accept" ) != null ) {
    user.setUsePolicyAccepted();
    user.save ();
    response.sendRedirect("index.jsp"); return;
  }
  if ( (String) request.getParameter ( "decline" ) != null ) { response.sendRedirect("login.jsp"); return; }

  SurveyMetaDataList surveyMetaDataList = new SurveyMetaDataList ( Config.appDataDir, user.getSurveyIdList () );

  String pageTitle = Config.msg(1);
  mysession.setAttribute(surveyId,  "cookietrail", pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />


<%=Config.msg(2)%>
<%
  if ( Config.acceptableUsePolicyURL != null && !Config.acceptableUsePolicyURL.equals("") ) {
  }
%>
<a target="_new" href="<%=Config.acceptableUsePolicyURL%>"><%=Config.msg(3)%></a>.<br>
<br>
<%=Config.msg(4)%>
<form>
<textarea wrap="virtual" cols="60" rows="15">
<jsp:include page="acceptableuse.html" />
</textarea>
</form>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
 <input type="submit" name="accept" value="<%=Config.msg(5)%>" class="button">&nbsp;&nbsp;&nbsp;
 <input type="submit" name="decline" value="<%=Config.msg(6)%>" class="button">
</form>
<survey:pageFooter />