# Migration

The Piranha codebase has migrated to Servlet 5 / Jakarta EE 9 and as such some
projects that are used might or might not be up to speed yet. This document
tracks what projects are not up to speed and where it impacts the Piranha
codebase.

## Piranha Nano

1. Tests using Apache Wicket have been disabled.

## Piranha CDI - OpenWebBeans Integration

1. Commented out as the Apache OpenWebBeans project is not yet up to speed.

## Piranha Test - Project

## The following projects are currently not up to speed with respect to 
Jakarta EE 9 and as such the tests for these have been turned off.

1. Apache OpenWebBeans (test/openwebbeans)
1. Apache Struts (test/struts)
1. OmniFaces JWT (test/omnifaces-jwt)
1. Smallrye Health (test/smallrye-health)
1. Spring MVC (test/springmvc)
1. Vaadin (test/vaadin)
1. Wicket (test/wicket)
