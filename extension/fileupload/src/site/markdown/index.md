# Piranha File Upload Extension

The File Upload extension delivers the ability for a Piranha runtime to support
file upload. This extension is available by default for the following runtimes:

1. Piranha Server
1. Piranha Servlet
1. Piranha Web Profile

## Configuration parameters

The following configuration parameters are available:

1. `cloud.piranha.extension.fileupload.outputDirectory` - the directory where
   the file upload will store temporary files. The default is the location as
   
1. `cloud.piranha.extension.fileupload.fileSizeTreshold` - the file size
   threshold (in bytes) before the runtime will create a temporary file on the
   filesystem to store the upload. The default is 10240 (10 KB).

## Setting the configuration parameters using web.xml

You can set the configuration parameter in the web.xml as illustrated below:

```xml
    <context-param>
        <param-name>cloud.piranha.extension.fileupload.fileSizeTreshold</param-name>
        <param-value>1048576</param-value>
    </context-param>
```
