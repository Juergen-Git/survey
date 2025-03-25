<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { 
  	response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; 
  }

  String adminEmail;
  if ( request.getParameter ( "adminEmail" ) != null ) {
    adminEmail = request.getParameter ( "adminEmail" );
  }
  else {
    adminEmail = survey.getAdminEmail ();
  }

  boolean invalidAdminEmail = false;

  if ( request.getParameter ( "save" ) != null ) {
    // check if name is valid (valid characters & not taken yet) otherwise forward back to survey creation UI
//    Regex r = new Regex( Config.regexValidEmail );

    if ( adminEmail.equals("") || Pattern.matches ( Config.regexValidEmail, adminEmail ) ) {
      survey.setAdminEmail ( adminEmail );
      survey.save ();

      response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId);
      return;
    }
    else {
      invalidAdminEmail = true;
    }
  }

  String pageTitle = Config.msg(74);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<%
  if ( invalidAdminEmail ) {
    out.println ( "<font color=\"red\"><b>"+Config.msg(75)+"</b></font><br>" );
  }
%>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
<input type="hidden" name="surveyId" value="<%=surveyId%>">
<p>
<%=Config.msg(76)%><br>
<br>
<%=Config.msg(77)%> <b><%=Config.msg(78)%> &quot;<%=survey.getName ()%>&quot;</b>
</p>
<b><%=Config.msg(79)%></b>
<font color="#999999">(<%=Config.msg(80)%>)</font><br>
<input type="text" name="adminEmail" value="<%=adminEmail%>" size="25" maxlength="<%=Config.maxLenEmail%>">
<br>
<br>
<input type="submit" name="save" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />
