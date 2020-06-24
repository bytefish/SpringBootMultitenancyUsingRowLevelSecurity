// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.datasource;

import com.zaxxer.hikari.HikariDataSource;
import de.bytefish.multitenancy.core.ThreadLocalStorage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TenantAwareHikariDataSource extends HikariDataSource {

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();

        try (Statement sql = connection.createStatement()) {
            String tenantName = ThreadLocalStorage.getTenantName();

            sql.execute("SET app.current_tenant = '" + tenantName + "'");
        }

        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = super.getConnection(username, password);

        try (Statement sql = connection.createStatement()) {
            sql.execute("SET app.current_tenant = '" + ThreadLocalStorage.getTenantName() + "'");
        }

        return connection;
    }

}
