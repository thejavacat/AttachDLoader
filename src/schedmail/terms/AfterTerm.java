package schedmail.terms;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

@SuppressWarnings("serial")
public class AfterTerm extends SearchTerm {
	private Date when;
	
	public AfterTerm(Date when) {
		this.when = when;
	}
	
	public boolean match(Message arg0) {
		try {
			return arg0.getReceivedDate().after(when);
		}
		catch (MessagingException e) {
			return false;
		}
	}
}