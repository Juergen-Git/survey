<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { 
  	response.sendRedirect ( "editExitPage.jsp?surveyId="+surveyId ); return; 
  }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( request.getParameter ( "save" ) != null ) {
    // use new survey title from the HTML form
    String exitPageText = request.getParameter ( "exitPageText" );
    entryForm.setExitPageText ( exitPageText );
    if ( ((String) request.getParameter ( "texttype" )).equals ( "html" ) ) {
      entryForm.setExitPageTextIsHTML ( true );
    }
    else {
      entryForm.setExitPageTextIsHTML ( false );
    }
    entryForm.save ();

    response.sendRedirect("editExitPage.jsp?surveyId="+surveyId); return;
  }

  String pageTitle = Config.msg(327);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; <a href=\"editEntryForm.jsp?surveyId="+surveyId+"\">"+Config.msg(94)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
<input type="hidden" name="surveyId" value="<%=surveyId%>">
<b><%=Config.msg(328)%></b> <font color="#999999"><%=Config.msg(329)%></font><br>
<input type="radio" name="texttype" value="plain"<%
  if ( !entryForm.getExitPageTextIsHTML () ) { out.print ( " checked" ); }
%>> <%=Config.msg(258)%> &nbsp;&nbsp;&nbsp
<input type="radio" name="texttype" value="html"<%
    if ( entryForm.getExitPageTextIsHTML () ) { out.print ( " checked" ); }
%>> <%=Config.msg(259)%><br>
  <textarea name="exitPageText" wrap="virtual" cols="60" rows="15"><%=HTMLUtils.encode ( entryForm.getExitPageText () )%></textarea>
  <br>
  <br>
  <input type="submit" name="save" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />