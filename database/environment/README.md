# Piranha Database - Environment driver

A JDBC driver that looks for environment variables to delegate to a another JDBC driver.

E.g. if you set the JDBC url in your application to be jdbc:environment:0 it will look for environment variables of the format:

```shell
  PIRANHA_ENVIRONMENT.0.URL=
  PIRANHA_ENVIRONMENT.0.PROPERTY.1=name=value
  PIRANHA_ENVIRONMENT.0.PROPERTY.2=name2=value2
```
