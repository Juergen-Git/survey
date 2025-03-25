<%@ include file="/globalAuthRequired.jsp" %><%
  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect("index.jsp"); return; }

  String password1 = "";
  if ( request.getParameter ( "password1" ) != null ) { password1 = request.getParameter ( "password1" ); }
  String password2 = "";
  if ( request.getParameter ( "password2" ) != null ) { password2 = request.getParameter ( "password2" ); }

  String onLoad = "document.form.password1.focus();";
  String errorMessage = "";

  if ( request.getParameter ( "save" ) != null ) {
    if ( Pattern.matches ( Config.regexValidUserPassword, password1 ) ) {
      if ( password1.equals (password2) ) {
        User user = new User ( Config.appDataDir, pid );
        if ( user.exists () ) {
          user.setPassword ( password1 );
          user.save ();

          response.sendRedirect("index.jsp");
          return;
        } // end:  if ( user.exists () )
      }
      else {
        errorMessage = Config.msg(16);
      }
    }
    else {
      errorMessage = Config.msg(17);
    }
  } // end: if ( request.getParameter ( "save" ) != null )

  String pageTitle = Config.msg(45);
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
    <td align="right"><b><%=Config.msg(46)%></b></td>
    <td>
      <input type="password" name="password1" value="" size="15" maxlength="15">
    </td>
  </tr>
  <tr>
    <td><b><%=Config.msg(47)%></b></td>
    <td>
      <input type="password" name="password2" value="" size="15" maxlength="15">
    </td>
  </tr>
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
