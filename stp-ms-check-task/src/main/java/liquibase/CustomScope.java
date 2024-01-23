package liquibase;

import java.util.Map;

public class CustomScope extends Scope{

    public CustomScope(Scope rootScope, Map<String, Object> scopeValues) {
        super(rootScope, scopeValues);
    }
}
