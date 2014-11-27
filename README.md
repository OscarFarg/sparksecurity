# Spark Security

Tiny security library for SparkJava.

# Example

To use the library add the following lines:

```java
SparkSecurity.setSecurityHandlerClass(SimpleSecurityHandler.class);
SparkSecurity.init();
```

The security handler needs to be implemented to set the correct authentication:
```java
public class SimpleSecurityHandler implements SecurityHandler {

    private static final String AUTH_TOKEN = "AUTH-TOKEN";

    @Override
    public void handle(Request request, SecurityContext context) {
        String authToken = request.headers(AUTH_TOKEN);
        if (StringUtils.equals(authToken, "secrettoken")) {
            AuthenticationImpl authentication = new AuthenticationImpl();
            authentication.addPermission(new PermissionImpl("login"));
            context.setAuthentication(authentication);
        }
    }
}
```

# Caution
The code has not been tested.