package emails;

import com.google.inject.Inject;
import play.Configuration;
import play.mvc.Controller;
import play.mvc.Result;
import java.io.IOException;

public class EmailConta extends Controller {

	private final Configuration configuration;

    private final String host;
    private final int port;
    private final String dbName;

    @Inject
    public EmailConta(Configuration configuration) {
        this.configuration = configuration;
        // initialize config variables
        this.host = configuration.getString("db.default.host");
        this.port = configuration.getInt("db.default.port");
        this.dbName = configuration.getString("db.default.dbname");
    }

   /* public Result createOneAccount() throws IOException {
        return accountService.createOneAccount(request().body().asJson());
    }*/
	
}
