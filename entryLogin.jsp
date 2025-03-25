<%@ include file="/globalNoAuthRequired.jsp" %><%
  LdapAuth ldapAuth = new LdapAuth ();

  String user = request.getParameter ("user");
  if ( user != null ) { user = user.toLowerCase(); }
  String password = request.getParameter ( "password" );

  if ( surveyId == null ) {
    response.sendRedirect("error.jsp?errorId=unknownSurveyId");
    return;
  }

  SurveyMetaData survey = new SurveyMetaData ( Config.appDataDir, surveyId );
  if ( !survey.exists () ) {
    response.sendRedirect("error.jsp?surveyId="+surveyId+"&errorId=unknownSurveyId");
    return;
  }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, request.getScheme(), true );

  boolean invalidLogin = false;
  boolean javascriptEnabled = true;
  String errorMessage = "";

  String returnTo = (String) request.getParameter ( "returnTo" );
  if ( returnTo != null ) {
    mysession.setAttribute(surveyId,  "returnTo", returnTo );
  }
  else {
    returnTo = (String) mysession.getAttribute(surveyId,  "returnTo" );
  }
  if ( returnTo == null ) { returnTo = "error.jsp?surveyId="+surveyId; }


  if ( user != null || password != null ) {
    if ( survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members") ) {
      if ( ldapAuth.authenticate (user,password) && (survey.getEntryRestriction().equals("vt") || survey.isMember(user)) ) {

        // check if user already submitted an entry before
        File entryExistsFile = new File ( survey.getSurveyDir() + "entryexists." + user );
        if ( survey.getOneEntryOnly() && entryExistsFile.exists() ) {
          errorMessage = Config.msg(127);
        }
        else {
          mysession.setAttribute(surveyId,  "entryPID", user );
          if ( mysession.getAttribute(surveyId, "returnTo") != null ) { mysession.removeAttribute(surveyId, "returnTo"); }
          response.sendRedirect(returnTo);
          return;
        }
      }
      else { invalidLogin = true; }
    }
    else if ( survey.getEntryRestriction().equals("password") ) {
      if ( password != null && password.equals(survey.getEntryPassword()) ) {
        mysession.setAttribute(surveyId,  "entryPassword", password );
        response.sendRedirect(returnTo);
        return;
      }
      else { invalidLogin = true; }
    }
  } // end: if ( user != null || password != null )

  if ( invalidLogin ) { errorMessage = Config.msg(128); }

%><html>
<head>
<%
  if ( !entryForm.getBaseHref().equals("") ) {
    out.print ( "  <base href=\"" + entryForm.getBaseHref() + "\">\n" );
  }
  if ( !entryForm.getUserCSS().equals("") ) {
    out.print ( "  <link type=\"text/css\" rel=\"stylesheet\" href=\"" + entryForm.getUserCSS() + "\">\n" );
  }
%>
</head>
<body<%
  if ( !entryForm.getBgColor ().equals ( "" ) )
    out.print ( " bgColor=\"" + entryForm.getBgColor () + "\"" );
  if ( !entryForm.getTextColor ().equals ( "" ) )
    out.print ( " text=\"" + entryForm.getTextColor () + "\"" );

  if ( survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members") )
    out.print ( " onLoad=\"document.form.user.focus()\"" );
  if ( survey.getEntryRestriction().equals("password") )
    out.print ( " onLoad=\"document.form.password.focus()\"" );

  if ( !entryForm.getShowBorder() )
    out.print ( " leftMargin=\"0\" topMargin=\"0\" marginheight=\"0\" marginwidth=\"0\"" );
%>>
  <font size="2">
  <%=entryForm.getHeader ()%>
  </font>
  <font size="2">

  <form action="<%=HttpUtils.getRequestURL ( request )%>" method="post" name="form">
  <input type="hidden" name="surveyId" value="<%=surveyId%>">
  <table border="0" cellspacing="0" cellpadding="5" bgcolor="#ffffff" align="center">
  <tr><td>
    <table border="0" cellspacing="1" cellpadding="5" bgcolor="#eeeeee">
      <% if ( !errorMessage.equals("") ) { %>
      <tr><td colspan="2"><b><font color="#ff0000"><%=errorMessage%></font></b><br></td></tr>
      <% } // end: if ( invalidLogin )
      %>
<%
  if ( survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members") ) {
%>
      <tr>
        <td align="right"><b><%=Config.msg(129)%></b></td>
        <td><input type="text" name="user" size="8" maxlength="<%=Config.maxLenPID%>" value=""></td>
      </tr>
<%
  } // end:
%>
      <tr>
        <td align="right"><b><%=Config.msg(130)%></b></td>
        <td><input type="password" name="password" size="8" maxlength="<%=Config.maxLenPassword%>" value=""></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type="submit" name="login" value="<%=Config.msg(131)%>"></td>
      </tr>
    </table>
<script language="Javascript"><!--
document.write ("<input type=\"hidden\" name=\"javascript\" value=\"1\">");
//-->
</script>
  </td></tr>
  </table>
  </form>
  </font>
  <font size="2">
  <%=entryForm.getFooter ()%>
  </font>
</body>
</html>