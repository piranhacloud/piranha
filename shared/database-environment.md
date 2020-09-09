# Piranha Database Environment driver

This JDBC driver can be used to configure your database using environment 
variables.

E.g. if you set the JDBC url in your application to be jdbc:environment:0 
it will look for environment variables of the format:

```
  PIRANHA_ENVIRONMENT.0.URL=url
  PIRANHA_ENVIRONMENT.0.PROPERTY.1=name=value
  PIRANHA_ENVIRONMENT.0.PROPERTY.2=name2=value2
```

[Home](../overview.md)
