package edu.vt.ward.survey;

//import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** PageFooter represents a tag for printing the HTML header of a page  Use it like:
 *
 * <PRE>   &lt;survey:pageFooter /&gt;</PRE>
 * @version $Id: PageFooter.java,v 1.1.1.1 2002/12/16 15:41:27 jrode Exp $
 */
public class PageFooter extends TagSupport {

    public PageFooter () {
    }

    /** Called after the tag has been evaluated.  Reset the properties to their defaults.
     */
    public void release() {
        super.release();
    }

    /** Called when the starting tag is encountered.
     * @throws JspException throw back to calling page
     * @return SKIP_BODY
     */
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
//        HttpSession session = pageContext.getSession();
//        ServletContext application = pageContext.getServletContext();

        try {
          pageContext.include ( Config.pageFooterFile  );
          out.println ( "</center>" );
          out.println ( "</body>" );
          out.println ( "</html>" );
        }
        catch ( Exception ex ) {
            throw new JspTagException ( "exception " + ex.getMessage() );
        }
        return SKIP_BODY;
    }
}
