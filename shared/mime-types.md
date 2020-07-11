# Adding more mime types to your web application

The default set of supported mime-types is kept small because we want to make
sure the runtime does not do any unnecessary processing. If the mime type your
application is using is not in the default set you have 2 options to enable
more mime types.

1. Add mime type to web.xml
2. Add mime type using a WebApplicationExtension

## Add mime type to web.xml

If you have the source of the application this is the preferred way. Add a
`<mime-type>` block defining the mime type you want added to your web.xml and
it will be added to the MimeTypeManager.

## Add mime type using a WebApplicationExtension

If you do not have the source you can create a WebApplicationExtension that
calls the MimeTypeManager API to add the mime type programmatically. And then
if you are using Piranha Micro you can use the `--extension` on the command
line to add it.

[Home](../overview.md)
