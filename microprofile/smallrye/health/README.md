
# Piranha MicroProfile - SmallRye - Health

The Piranha MicroProfile - SmallRye - Health  module delivers you with SmallRye Health
integration.

Note you will first need to either enable CDI integration, like 
[Weld](../piranha-weld/README.md) or [OpenWebBeans](../piranha-openwebbeans/README.md) integration.

Then add the following dependency to your project.

      <dependency>
          <groupId>cloud.piranha.microprofile.smallrye</groupId>
          <artifactId>piranha-microprofile-smallrye-health</artifactId>
          <version>x.y.z</version>
      </dependency>

Make sure you use the same version of Piranha itself.

Note that this example does not set the &lt;scope&gt; but depending on what you
are using Piranha for you might need to add a &lt;scope&gt;.
