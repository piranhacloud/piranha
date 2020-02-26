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

Note our more complex tests are in the `test` profile because we do NOT release
them as part of a release, but they are activated automatically for local
development.

### Running a singular test

To run a singular test pass in `-Dtest=expression`, see the `surefire` plugin
documentation for more information.

### Run the TCKs

To run all the TCKs use:

```
mvn clean verify -Ptck
```

Or go into the directory of the TCK you want to run and use:

```
mvn clean verify
```
