# Guppy Environment driver

A JDBC driver that looks for system properties to delegate to a another JDBC driver.

E.g. if you set the JDBC url in your application to be jdbc:property:0 it will 
look for system properties of the format:

```shell
  property.0.url
  property.0.property.1=name=value
  property.0.property.2=name2=value2
```
