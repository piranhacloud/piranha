# Migration

The Piranha codebase is migrating towards Jakarta EE 9 and as such some projects
that are used might or might not be up to speed yet. This document tracks what
projects are not up to speed and where it impacts the Piranha codebase.

## Piranha Nano

1. Tests usings Apache Wicket have been disabled.

## Piranha Upload - Apache FileUpload

1. Project has been commented out until either a) project catches up or b) the
Apache source code can be changed on the fly and compiled into this module.
