<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  String type = (String) request.getParameter ( "type" );
  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));

  String pageTitle = Config.msg(7);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">" + Config.msg(8) + "</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; <a href=\"editEntryForm.jsp?surveyId="+surveyId+"\">" + Config.msg(9) + "</a> &gt; " + pageTitle );
%>
<survey:pageHeader title="<%=pageTitle%>" />
<table border="0" cellspacing="6" cellpadding="3" width="302">
  <tr>
    <td nowrap align="left" valign="top"><a href="editQuestion.jsp?surveyId=<%=surveyId%>&section=<%=sectionNr%>&question=<%=questionNr%>&type=radio"><%=Config.msg(10)%><br>
      <img src="images/questiontype_radio.gif" width="39" height="60" border="0" alt="<%=Config.msg(10)%>"></a><br>
    </td>
  </tr>
  <tr>
    <td align="left" valign="top" nowrap><a href="editQuestion.jsp?surveyId=<%=surveyId%>&section=<%=sectionNr%>&question=<%=questionNr%>&type=checkbox"><%=Config.msg(11)%><br>
      <img src="images/questiontype_checkbox.gif" width="39" height="60" border="0" alt="<%=Config.msg(11)%>"></a></td>
  </tr>
  <tr>
    <td align="left" valign="top" ><a href="editQuestion.jsp?surveyId=<%=surveyId%>&section=<%=sectionNr%>&question=<%=questionNr%>&type=textline"><%=Config.msg(12)%><br>
      <img src="images/questiontype_textline.gif" width="120" height="32" border="0" alt="<%=Config.msg(12)%>"></a></td>
  </tr>
  <tr>
    <td class="menuiteminactive" align="left" valign="top" nowrap><a href="editQuestion.jsp?surveyId=<%=surveyId%>&section=<%=sectionNr%>&question=<%=questionNr%>&type=textarea">
      <%=Config.msg(13)%><br>
      <img src="images/questiontype_textarea.gif" width="145" height="48" border="0" alt="<%=Config.msg(13)%>"></a></td>
  </tr>
</table>
<survey:pageFooter />