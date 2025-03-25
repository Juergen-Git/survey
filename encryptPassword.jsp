<%@ include file="/globalNoAuthRequired.jsp" %><%
  String password1 = "";
  if ( request.getParameter ( "password1" ) != null ) { password1 = request.getParameter ( "password1" ); }
  String password2 = "";
  if ( request.getParameter ( "password2" ) != null ) { password2 = request.getParameter ( "password2" ); }

  String errorMessage = "";
  if ( request.getParameter ( "encrypt" ) != null ) {
    if ( Pattern.matches ( Config.regexValidUserPassword, password1 ) ) {
      if ( password1.equals (password2) ) {
        // that's fine
      }
      else {
        errorMessage = Config.msg(16);
      }
    }
    else {
      errorMessage = Config.msg(17);
    }
  } // end: if ( request.getParameter ( "encrypt" ) != null )
%><html>
<head>
  <title><%=Config.msg(123)%></title>
</head>
<body onLoad="document.form.password1.focus();">
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<table border="0" cellspacing="3" cellpadding="3">
<%
  if ( !errorMessage.equals("") ) { %>
  <tr>
    <td>&nbsp;</td>
    <td><font color="#cc0000"><b><%=errorMessage%></b></td>
  </tr>
<%
  }
  else {
    if ( password1 != null && !password1.equals("") ) { %>
  <tr>
    <td align="right"><b><%=Config.msg(124)%></b></td>
    <td>
      <font color="#009900"><b><%=Jcrypt.crypt ( Config.cryptSalt, password1 )%></b></td>
    </td>
  </tr>
<%
    }
  }
%>
  <tr>
    <td align="right"><b><%=Config.msg(21)%></b></td>
    <td>
      <input type="password" name="password1" value="" size="15" maxlength="15">
    </td>
  </tr>
  <tr>
    <td><b><%=Config.msg(22)%></b></td>
    <td>
      <input type="password" name="password2" value="" size="15" maxlength="15">
    </td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td>
      <br>
      <input type="submit" name="encrypt" value="&nbsp;&nbsp;&nbsp;Encrypt&nbsp;&nbsp;&nbsp;">
    </td>
  </tr>
</table>
</form>
</body>
</html>