# Contributing

How can I contribute to Piranha? Well it all depends on the level of 
involvement you are aiming for. The following comes to mind:

1. Write blog entries with example applications
2. File issues for bugs you encounter.
3. Answer questions that people file at the issue tracker.
4. Fork the repository and issue pull requests.

Which ever level you pick we welcome you!

## Important notice

Note if you file issues, answer questions and/or issue pull requests you agree
that those contributions will be owned by Manorrock.com and that Manorrock.com 
can use those contributions in any manner Manorrock.com so desires.

## Building and testing Piranha locally

### Building Piranha and running the tests

```
mvn clean install
```

If you do not want the tests to run use:

```
mvn -DskipTests clean install 
```

### Running a singular test

To run a singular test pass in `-Dtest=expression`, see the `surefire` plugin
documentation for more information.

### Running our more complex tests

Our more complex tests are in the `test` profile which we do not release as part
of a release because these modules only test functionality.

You can run our more complex tests using:

```
mvn -P test clean install
```

### Run the external tests (including TCKs)

To run all the external tests use:

```
mvn -P external clean install
```

Or go into the directory of the external test you want to run and use:

```
mvn clean install
```

For the Servlet TCK if you want to run a singular test use 
`-Drun.test=expression`. See the example below.

```
mvn -Drun.test=com/sun/ts/tests/servlet/spec/errorpage/URLClient.java#servletToDifferentErrorPagesTest verify
```

## Problems

Support for Java modules has a feature gap in Eclipse and as such a workaround
needs to be employed to make it work properly (June 2021).
