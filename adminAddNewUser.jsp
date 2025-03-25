<%@ include file="/globalAuthRequired.jsp" %><%
  if ( !pid.equals("admin") ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect("index.jsp"); return; }

  String userPID = "";
  if ( request.getParameter ( "userPID" ) != null ) { userPID = request.getParameter ( "userPID" ); }
  String password1 = "";
  if ( request.getParameter ( "password1" ) != null ) { password1 = request.getParameter ( "password1" ); }
  String password2 = "";
  if ( request.getParameter ( "password2" ) != null ) { password2 = request.getParameter ( "password2" ); }

  String onLoad = "document.form.userPID.focus();";
  String errorMessage = "";

  if ( request.getParameter ( "save" ) != null ) {
    LdapAuth ldapAuth = null;
    if ( !Config.authenticationMethodStandalone ) {
      ldapAuth = new LdapAuth ();
    }

    if ( !Config.authenticationMethodStandalone || Pattern.matches ( Config.regexValidUserPassword, password1 ) ) {
      if ( !Config.authenticationMethodStandalone || password1.equals (password2) ) {
        if ( Config.authenticationMethodStandalone || ldapAuth.userExists ( userPID ) ) {
          User user = new User ( Config.appDataDir, userPID );
          if ( !user.exists () ) {
            user.create();
            user.setPassword ( password1 );
            user.save ();

            response.sendRedirect("index.jsp");
            return;
          }
          else {
            errorMessage = Config.msg(14);
          }
        }
        else {
          errorMessage = Config.msg(15);
        }
      }
      else {
        errorMessage = Config.msg(16);
        onLoad = "document.form.password1.focus();";
      }
    }
    else {
      errorMessage = Config.msg(17);
      onLoad = "document.form.password1.focus();";
    }
  } // end: if ( request.getParameter ( "save" ) != null )

  String pageTitle = Config.msg(18);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="<%=onLoad%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<%
  if ( !errorMessage.equals("") ) {
    out.print ( Config.formatFeedback("error", errorMessage ) );
  }
%>
<table border="0" cellspacing="3" cellpadding="3">
  <tr>
    <td align="right"><b><%=Config.msg(20)%></b><br><br></td>
    <td>
      <input type="text" name="userPID" value="<%=userPID%>" size="<%=Config.maxLenPID%>" maxlength="<%=Config.maxLenPID%>">
      <br><br>
    </td>
  </tr>
<%
  if ( Config.authenticationMethodStandalone ) {
%>
  <tr>
    <td align="right"><b><%=Config.msg(21)%></b></td>
    <td>
      <input type="password" name="password1" value="" size="15" maxlength="<%=Config.maxLenPassword%>">
    </td>
  </tr>
  <tr>
    <td><b><%=Config.msg(22)%></b></td>
    <td>
      <input type="password" name="password2" value="" size="15" maxlength="<%=Config.maxLenPassword%>">
    </td>
  </tr>
<%
  } // end: if ( Config.authenticationMethodStandalone )
%>
  <tr>
    <td align="right">&nbsp;</td>
    <td>
      <br>
      <input type="submit" name="save" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
    </td>
  </tr>
</table>
<br>
</form>
<survey:pageFooter />
