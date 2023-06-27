package mainCode;

public interface UIRfidListener {

    public void modeChangedListener(final Mode mode);

    public void envChangedListener(final Env env);

    public void addressChangedListener(final String address);

}
