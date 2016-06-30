package beans;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Author: Eduard Dedu
 * eduardcdedu@gmail.com
 * Created on: 10/06/16
 */

@Named
@ApplicationScoped
public class SessionBean {
    private String sessionId;
    private String sessionIdSignature;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionIdSignature(String sessionIdSignature) {
        this.sessionIdSignature = sessionIdSignature;
    }


}
