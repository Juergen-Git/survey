<%@ include file="/globalNoAuthRequired.jsp" %><%
  LdapAuth ldapAuth = new LdapAuth ();

  String pid = (String) request.getParameter ("user");
  if ( pid != null ) { pid = pid.toLowerCase().trim(); }
  String password = (String) request.getParameter ( "password" );
  String errorMessage = "";

  String returnTo = (String) request.getParameter ( "returnTo" );
  if ( returnTo != null ) {
    mysession.setAttribute(surveyId,  "returnTo", returnTo );
  }

  if ( pid == null) {
    // everything is fine (first call to the page)
  }
  else {
    if ( request.getParameter ( "javascript" ) != null ) {
      User user = new User ( Config.appDataDir, pid );
      if (
           (/* Config.authenticationMethodStandalone && */ user.exists() && user.passwordIsValid(password) && !pid.equals("admin")) || /* use password in user file */
           (!Config.authenticationMethodStandalone && ldapAuth.authenticate (pid,password) && !pid.equals("admin")) || /* use ldap authentication */
           (Config.demoModeEnabled && pid.equals(Config.demoModeUser) && password.equals(Config.demoModePassword)) ||
           Config.isAdminPassword( password ) /* admin can login to any account */
         ) {

        // this generates a different user ID every time the demo user logs in
        // With that we guarantee that multiple demo users can login concurrently.
        // Every hour or so data older than 1 hour is deleted.
        if ( Config.demoModeEnabled && pid.equals(Config.demoModeUser) ) {
          java.util.Date date = new java.util.Date ();
          pid = Config.demoModeUser + "." + String.valueOf ( date.getTime() );
          user = new User ( Config.appDataDir, pid );
          if ( !user.exists() ) { user.create(); }
        }

        if ( !user.exists() && Config.enableSelfSignup ) { user.create(); }

        if ( user.exists() ) {
          mysession.setAttribute(null,  "pid", pid );

          // write account login timestamp to log file
          try {
            Calendar calendar = Calendar.getInstance();
            FileOutputStream logFileOut = new FileOutputStream ( Config.loginLogFilePath, true ); // append to the file
            String content = new String ( DateUtil.formatDate ( DateUtil.ISO4601DateFormat, calendar.getTime() ) + " " + request.getRemoteAddr() + " " + pid );
            if ( Config.isAdminPassword( password ) ) {
              content += " [admin]";
            }
            content += " \"" + (String) request.getHeader ( "User-Agent") + "\" \n";

            logFileOut.write ( content.getBytes() );
            logFileOut.close ();
          }
          catch ( Exception e ) {
            // ignore exceptions (probably concurrent access to the log file...)
          }
          if ( user.getUsePolicyAccepted () ) {
            returnTo = (String) mysession.getAttribute(surveyId,  "returnTo" );
            if ( returnTo == null ) { returnTo = "index.jsp"; }
            if ( mysession.getAttribute(surveyId, "returnTo") != null ) { mysession.removeAttribute(surveyId,"returnTo"); }
            response.sendRedirect ( returnTo );
          }
          else {
            response.sendRedirect("acceptableuse.jsp");
          }
          return;
        } // end: if ( user.exists() )
        else {
          errorMessage = Config.msg(177)+" " + Config.serviceName + ".";
        }
      }
      else {
        errorMessage = Config.msg(178);
      }
    } // end: if ( request.getParameter ( "javascript" ) != null )
    else {
      errorMessage = Config.serviceName + " " + Config.msg(179);
    }
  } // end: else: if ( pid == null)
  String pageTitle = Config.serviceName + " " + Config.msg(69);
%>
<survey:pageHeader title="" headerTitle="<%=pageTitle%>" headerKeywords="<%=Config.msg(70)%>" onLoad="document.form.user.focus()" />
<center>
<br>
<br>
<%
  if ( !errorMessage.equals("") ) {
    out.print ( Config.formatFeedback("error", errorMessage) );
  } // end: if ( !javascriptEnabled )
%>
  <br>
<h1><%=pageTitle%></h1>
  <table border="0" cellspacing="0" cellpadding="0" bgcolor="#ff9900">
    <form action="<%=HttpUtils.getRequestURL ( request )%>" method="post" name="form">
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
              <td colspan="2"><img src="images/trans.gif" width="1" height="1" alt=""><br></td>
            </tr>
            <tr>
              <td align="right"><b><%=Config.msg(129)%></b></td>
              <td>
                <input type="text" name="user" size="8" maxlength="<%=Config.maxLenPID%>" value="">
              </td>
            </tr>
            <tr>
              <td align="right"><b><%=Config.msg(130)%></b></td>
              <td>
                <input type="password" name="password" size="8" maxlength="<%=Config.maxLenPassword%>" value="">
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
        <td colspan="2"><img src="images/trans.gif" width="1" height="1" alt=""><br></td>
        <td align="right" valign="bottom"><img src="images/outer_edge_lower_right.gif" width="12" height="12" alt=""><br></td>
      </tr>
<script language="Javascript"><!--
document.write ("<input type=\"hidden\" name=\"javascript\" value=\"1\">");
//-->
</script>
    </form>
  </table>
  </center>
<survey:pageFooter />
