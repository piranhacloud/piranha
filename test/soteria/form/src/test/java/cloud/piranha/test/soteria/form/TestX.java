package cloud.piranha.test.soteria.form;

import javax.naming.InitialContext;
import javax.naming.NamingException;


public class TestX {

    public static void main(String[] args) throws NamingException {
       javax.naming.Context context = new InitialContext();

       context.bind("foo", "bar");

    }

}
