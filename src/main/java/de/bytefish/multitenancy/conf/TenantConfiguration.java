package de.bytefish.multitenancy.conf;


public class TenantConfiguration {

    private final String name;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public TenantConfiguration(String name, String dbUrl, String dbUser, String dbPassword) {
        this.name = name;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getName() {
        return name;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

}
