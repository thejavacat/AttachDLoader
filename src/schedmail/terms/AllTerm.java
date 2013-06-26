package schedmail.terms;

import javax.mail.Message;
import javax.mail.search.SearchTerm;

@SuppressWarnings("serial")
public class AllTerm extends SearchTerm {
	public boolean match(Message arg0) {
		return true;
	}
}