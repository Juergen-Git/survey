<%@ include file="/globalNoAuthRequired.jsp" %><%
  String cookietrail = (String) mysession.getAttribute(surveyId,  "cookietrail" );
  String pid = (String) mysession.getAttribute(null,  "pid" );
  if ( cookietrail == null ) { cookietrail = ""; }
  else { mysession.removeAttribute ( surveyId, "cookietrail" ); }
%>
<table width="730" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="1%" rowspan="10"><img src="images/trans.gif" width="15" height="1" border="0" alt=""><br></td>
    <td width="1%"><img src="images/trans.gif" width="1" height="1" border="0" alt=""><br></td>
    <td width="60%"><img src="images/trans.gif" width="500" height="10" border="0" alt=""><br></td>
    <td width="1%"><img src="images/trans.gif" width="1" height="1" border="0" alt=""><br></td>
    <td width="1%"><img src="images/trans.gif" width="1" height="1" border="0" alt=""><br></td>
    <td width="1%"><img src="images/trans.gif" width="1" height="1" border="0" alt=""><br></td>
    <td width="35%" rowspan="10"><img src="images/trans.gif" width="15" height="1" border="0" alt=""><br></td>
  </tr>
  <tr>
    <td colspan="2"><img src="images/trans.gif" width="562" height="20" border="0" alt=""><br></td>
    <td colspan="2" align="right" valign="bottom"><img src="images/surveylogo_part1.gif" width="87" height="25" border="0" alt=""><br></td>
    <td><img src="images/trans.gif" width="1" height="1" border="0" alt=""><br></td>
  </tr>
  <tr>
    <td bgcolor="#ffffff" valign="top" align="left" rowspan="2"><img src="images/edge_upper_left.gif" width="12" height="20" border="0" alt=""><br></td>
    <td bgcolor="#ffffff" valign="top"><img src="images/trans.gif" width="635" height="1" border="0" alt=""><br></td>
    <td bgcolor="#ffffff" valign="top" align="right"><img src="images/surveylogo_part2.gif" width="67" height="2" border="0" alt=""><br></td>
    <td bgcolor="#ffffff" valign="top" align="right" rowspan="2"><img src="images/surveylogo_part3.gif" width="20" height="14" border="0" alt=""><br></td>
    <td bgcolor="#ffffff" valign="top" rowspan="2" align="right"><img src="images/edge_upper_right.gif" width="12" height="20" border="0" alt=""><br></td>
  </tr>
  <tr>
    <td bgcolor="#ffffff" valign="top" class="breadcrumbtrail"><%
      if ( cookietrail != "" ) { out.print ( "<b>"+Config.msg(149)+"</b> " + cookietrail ); } %>&nbsp;</td>
    <td bgcolor="#ffffff" valign="top" align="right"><% if ( pid != null ) { %><a  target="_top" href="<%=Config.rootURL%>logout.jsp"><%=Config.msg(150)%></a><% } %>&nbsp;</td>
  </tr>
  <tr>
    <td bgcolor="#ffffff"><img src="images/trans.gif" width="12" height="<%
    if ( mysession.getAttribute(surveyId, "shortScreen") != null ) { out.print( "1" ); }
    else { out.print( "310" ); }
    %>" border="0" alt=""><br></td>
    <td bgcolor="#ffffff" colspan="3" valign="top">
