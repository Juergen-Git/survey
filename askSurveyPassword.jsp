<%@ include file="/globalNoAuthRequired.jsp" %><%
  String returnTo = (String) request.getParameter ( "returnTo" );
  if ( returnTo != null ) {
    mysession.setAttribute(surveyId,  "returnTo", returnTo );
  }

  String password = (String) request.getParameter ("password");
  boolean invalidLogin;

  if ( password == null) {
    invalidLogin = false;
  }
  else {
    SurveyMetaData survey = new SurveyMetaData ( Config.appDataDir, surveyId );

    if ( survey.getResultsPassword().equals(password) ) {
      // add this password to the hashtable of survey passwords
      mysession.setAttribute(surveyId,  "surveyPassword", password );

      returnTo = (String) mysession.getAttribute(surveyId,  "returnTo" );
      if ( returnTo == null ) { returnTo = "viewResults.jsp?surveyId="+surveyId; }
      mysession.removeAttribute ( surveyId, "returnTo" );
      response.sendRedirect ( returnTo ); return;
    }
    else {
      invalidLogin = true;
    }
  }
%>
<survey:pageHeader title="" onLoad="document.form.password.focus()" />
<br>
<br>
<center>
  <h1><%=Config.msg(41)%></h1>
  <table border="0" cellspacing="0" cellpadding="0" bgcolor="#ff9900">
    <form action="<%=HttpUtils.getRequestURL ( request )%>" method="post" name="form">
      <input type="hidden" name="surveyId" value="<%=surveyId%>">
      <tr>
        <td align="left" valign="top"><img src="images/outer_edge_upper_left.gif" width="12" height="12" alt=""><br></td>
        <td bgcolor="#ff9900" colspan="2"><img src="images/trans.gif" width="12" height="12" alt=""><br></td>
        <td align="right" valign="top"><img src="images/outer_edge_upper_right.gif" width="12" height="12" alt=""><br></td>
      </tr>
      <tr>
        <td align="left" valign="top">&nbsp;</td>
        <td bgcolor="#ff9900" colspan="2">
          <table border="0" cellspacing="0" cellpadding="7" bgcolor="#ff9900">
            <tr>
              <td colspan="2"><img src="images/trans.gif" width="1" height="1" alt=""><br>
                <% if ( invalidLogin ) {
%>
                <b><%=Config.msg(42)%></b><br>
                      <br>
                <% } // end: if ( invalidLogin )
%>
              </td>
            </tr>
            <tr>
              <td align="right"><b><%=Config.msg(43)%></b></td>
              <td>
                <input type="password" name="password" size="25" maxlength="<%=Config.maxLenResultsPassword%>" value="">
              </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>
                <input type="submit" name="login" value="<%=Config.msg(44)%>">
              </td>
            </tr>
          </table>
        </td>
        <td align="right" valign="top">&nbsp;</td>
      </tr>
      <tr>
        <td align="left" valign="bottom"><img src="images/outer_edge_lower_left.gif" width="12" height="12" alt=""><br></td>
        <td colspan="2">&nbsp;</td>
        <td align="right" valign="bottom"><img src="images/outer_edge_lower_right.gif" width="12" height="12" alt=""><br></td>
      </tr>
    </form>
  </table>
</center>
<survey:pageFooter />
