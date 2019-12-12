
# Piranha OmniFaces JWT integration

The Piranha OmniFaces JWT module delivers you with OmniFaces JWT
integration.

Note you will first need to enable CDI integration, like [Weld](../piranha-weld/README.md) or [OpenWebBeans](../piranha-openwebbeans/README.md) integration.

Then add the following dependency to your project.

      <dependency>
          <groupId>com.manorrock.piranha</groupId>
          <artifactId>piranha-microprofile-omnifaces-jwt</artifactId>
          <version>x.y.z</version>
      </dependency>

Make sure you use the same version of Piranha itself.

Note that this example does not set the &lt;scope&gt; but depending on what you
are using Piranha for you might need to add a &lt;scope&gt;.
