package com.techelevator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.io.IOException;
import java.sql.SQLException;

public class TenmoDaoTest {

    public static SingleConnectionDataSource dataSource;
    @BeforeClass
    public static void setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        /* The following line disables autocommit for connections
         * returned by this DataSource. This allows us to rollback
         * any changes after each test */
        dataSource.setAutoCommit(false);
    }
    @Before
    public void loadTestData() throws IOException, SQLException {
        // Spring provides a convenience class called ScriptUtils for running external SQL scripts.
        // You'll find the test-data.sql script file in the test/resources folder.
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("tenmo-test-data.sql"));
    }

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }
}
