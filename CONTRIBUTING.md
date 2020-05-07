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
mvn verify
```

If you do not want the tests to run use:

```
mvn -DskipTests -DskipITs verify 
```

### Running a singular test

To run a singular test pass in `-Dtest=expression`, see the `surefire` plugin
documentation for more information.

### Running our more complex tests

Our more complex tests are in the `test` profile which we do not release as part
of a release because these modules only test functionality.

You can run our more complex tests using:

```
mvn -P test verify
```

### Run the TCKs

To run all the TCKs use:

```
mvn verify -P tck
```

Or go into the directory of the TCK you want to run and use:

```
mvn verify
```

## Code conventions

We have compiled a list of code conventions we try to adhere to, 
see [Code Conventions](CODE_CONVENTIONS.md).

## Release process

Any committer can start a release by pushing a tag with the following format:

```
  release-BRANCH-VERSION
```

where BRANCH is the branch that should be used to cut the release from and VERSION
is the version number the release should be tagged with.
