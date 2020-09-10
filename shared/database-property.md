# Piranha Database Property driver

This JDBC driver can be used to configure your database using system
properties.

E.g. if you set the JDBC url in your application to be jdbc:property:0
it will look for system properties of the format:

```
  property.0.url
  property.0.property.1=name=value
  property.0.property.2=name2=value2
```

[Home](../overview.md)
