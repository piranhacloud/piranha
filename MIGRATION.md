# Migration

The Piranha codebase is migrating towards Servlet 5 / Jakarta EE 9 and as such
some projects that are used might or might not be up to speed yet. This document
tracks what projects are not up to speed and where it impacts the Piranha
codebase.

## Piranha Nano

1. Tests usings Apache Wicket have been disabled.

## Piranha Upload - Apache FileUpload

1. Project has been commented out until either a) project catches up or b) the
Apache source code can be changed on the fly and compiled into this module.

## Piranha Extension - Servlet

1. Commented out the Piranha Upload - Apache FileUpload as it is currently
   unavailable.

## Piranha Extension - Jakarta EE

1. Commented out the Piranha Upload - Apache FileUpload as it is currently
   unavailable.

## Piranha CDI - OpenWebBeans Integration

1. Commented out as the OpenWebBeans project is not yet up to speed.

## Piranha Extension - Micro

1. Commented out the Piranha Upload - Apache FileUpload as it is currently
   unavailable.

## Piranha Extension - Web Profile

1. Commented out the Piranha Upload - Apache FileUpload as it is currently
   unavailable.

## Piranha Test - Project

All tests need to be checked and if needed migrated.

