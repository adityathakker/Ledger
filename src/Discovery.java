import java.util.Map;

public interface Discovery {
    boolean register(String url, Ledger server);

    Map<String, Ledger> list();
}
