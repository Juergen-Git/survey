/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.vt.ward.survey;

import java.util.Hashtable;
import javax.servlet.http.HttpSession;

/**
 * @author Jochen Rode
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MySession {

	private HttpSession session;
	
	public MySession(HttpSession session) {
		this.session = session;
	}
	
	public Object getAttribute(String surveyId, String key) {
        //	get global values not associated with survey straight from the session
		if ((surveyId == null) || (surveyId.length() == 0) || (key.equals("surveyId"))) {
			return session.getAttribute(key);
		}
		Hashtable myHashtable = (Hashtable) session.getAttribute(surveyId);
		if (myHashtable == null) {
			return null;
		}
		return myHashtable.get(key);
	}

	public void setAttribute(String surveyId, String key, Object value) {
		// store global values not associated with survey straight into the session
		if ((surveyId == null) || (surveyId.length() == 0) || (key.equals("surveyId"))) {
			session.setAttribute(key, value);
			return;
		}
		Hashtable myHashtable = (Hashtable) session.getAttribute(surveyId);
		if (myHashtable == null) {
			myHashtable = new Hashtable();
			session.setAttribute(surveyId, myHashtable);
		}
		myHashtable.put(key, value);
	}
	
	public void removeAttribute(String surveyId, String key) {
		if ((surveyId == null) || (surveyId.length() == 0) || (key.equals("surveyId"))) {
			session.removeAttribute(key);
			return;
		}
		Hashtable myHashtable = (Hashtable) session.getAttribute(surveyId);
		if (myHashtable != null) {
			myHashtable.remove(key);
		}
	}

}
