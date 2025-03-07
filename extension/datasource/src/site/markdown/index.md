# Piranha DataSource Extension

The DataSource extension delivers you with a default DataSource in a Piranha runtime.

## Default DataSource Configuration

The default DataSource is configured with the following details:

- **Name**: `java:comp/DefaultDataSource`
- **Class**: `org.h2.jdbcx.JdbcDataSource`
- **URL**: `jdbc:h2:mem:test;DB_CLOSE_DELAY=-1`

## Override the default DataSource using web.xml

You override the default DataSource using the snippet below:

```xml
    <data-source>
        <name>java:comp/DefaultDataSource</name>
        <class-name>my.CustomDataSource</class-name>
        <url>jdbc:custom://localhost:3306/mydb</url>
        <user>myuser</user>
        <password>mypassword</password>
        <property>
            <name>myProperty</name>
            <value>myValue</value>
        </property>
    </data-source>
```

For runtimes that do not include the Piranha web.xml extension you will need to add it if you want
to override the default DataSource as described above. You can do this by adding the
following dependency to your pom.xml:

```xml
<dependency>
    <groupId>cloud.piranha.extension</groupId>
    <artifactId>piranha-extension-webxml</artifactId>
    <version>${piranha.version}</version>
</dependency>
```

## Register Any Other DataSource

You can register any other DataSource by using a similar approach. Below is an example of configuring a PostgreSQL DataSource:

```xml
    <data-source>
        <name>java:comp/PostgreSQLDataSource</name>
        <class-name>org.postgresql.ds.PGSimpleDataSource</class-name>
        <url>jdbc:postgresql://localhost:5432/mydb</url>
        <user>myuser</user>
        <password>mypassword</password>
        <property>
            <name>ssl</name>
            <value>true</value>
        </property>
    </data-source>
```

Make sure to replace the values with your actual PostgreSQL database details.
