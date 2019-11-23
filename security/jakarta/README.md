
# Manorrock Piranha Jakarta Security Base integration

The Manorrock Piranha Jakarta Security Base module delivers you with Jakarta Security Base integration.

Jakarta Security Base does not implement Jakarta Security it self, but provides the base, which is Jakarta Authentication and Jakarta Authorization, that Jakarta Security needs.

While Jakarta Security Base can be used by itself, it's low-level and not advised for direct use for ordinary applications. 


Add the following dependency to your project to use it.

      <dependency>
          <groupId>com.manorrock.piranha</groupId>
          <artifactId>piranha-authentication-elios</artifactId>
          <version>x.y.z</version>
      </dependency>

Make sure you use the same version of Manorrock Piranha itself.

Note that this example does not set the &lt;scope&gt; but depending on what you
are using Manorrock Piranha for you might need to add a &lt;scope&gt;.
