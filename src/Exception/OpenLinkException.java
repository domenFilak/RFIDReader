package Exception;

public class OpenLinkException extends Exception {

    public OpenLinkException(String link) {
        super("Failed to open link " + link + ". Desktop or browse action may not be supported.");
    }

}
