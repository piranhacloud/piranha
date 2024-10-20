README
======

The `runner` module runs the REST TCK tests so make sure you are in its 
directory when you need to debug any tests.

To run a single test, use the following command:

```
mvn -Dit.test=TestName verify
```

To run a single test in debug mode, use the following command:

```
mvn -Dmaven.failsafe.debug -Dit.test=TestName verify 
```

To run a single test in debug mode with a specific port, use the following 
command:

```
mvn -Dmaven.failsafe.debug -Dmaven.failsafe.debug.port=5005 -Dit.test=TestName verify
```

To run a single test in debug mode with a specific port and suspend, use the 
following command:

```
mvn -Dmaven.failsafe.debug -Dmaven.failsafe.debug.port=5005 -Dmaven.failsafe.debug.suspend=y -Dit.test=TestName verify
```

To run a single test and put Piranha in debug and susspend mode use the 
following command:

```
mvn -Dpiranha.debug=true -Dpiranha.suspend=true -Dit.test=TestName verify
```
