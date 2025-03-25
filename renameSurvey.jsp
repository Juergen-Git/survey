<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect("index.jsp"); return; }

  String surveyName;
  if ( request.getParameter ( "surveyName" ) != null ) {
    surveyName = request.getParameter ( "surveyName" );
  }
  else {
    surveyName = survey.getName ();
  }

  boolean invalidSurveyName = false;

  if ( request.getParameter ( "save" ) != null ) {
    // check if name is valid (valid characters & not taken yet) otherwise forward back to survey creation UI
//    Regex r = new Regex( Config.regexValidSurveyName );

    if ( Pattern.matches ( Config.regexValidSurveyName, surveyName ) && !surveyName.trim().equals("") ) {
      survey.setName ( surveyName );
      survey.save ();

      response.sendRedirect("index.jsp");
      return;
    }
    else {
      invalidSurveyName = true;
    }
  }

  String pageTitle = Config.msg(237);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="document.form.surveyName.focus()" />
<%
  if ( invalidSurveyName ) {
    out.println ( "<font color=\"red\"><b>"+Config.msg(238)+"</b></font><br>" );
  }
%>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<b><%=Config.msg(239)%></b>
<font color="#999999"><%=Config.msg(240)%></font><br>
<input type="text" name="surveyName" value="<%=surveyName%>" size="<%=Config.maxLenSurveyName+10%>" maxlength="<%=Config.maxLenSurveyName%>" tabIndex="1">
<font color="#999999">(<%=Config.minLenSurveyName%> <%=Config.msg(56)%> <%=Config.maxLenSurveyName%> <%=Config.msg(57)%>)</font>
<br>
<br>
<%
  if ( surveyId != null )
    out.print ( "<input type=\"hidden\" name=\"surveyId\" value=\"" + surveyId + "\">\n" );
%>
<input type="submit" name="save" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />
