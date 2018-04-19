
# Manorrock Piranha JNDI

The Manorrock Piranha JNDI module delivers you with JNDI integration.

Add the following dependency to your project to use it.

      <dependency>
          <groupId>com.manorrock.piranha</groupId>
          <artifactId>piranha-jndi</artifactId>
          <version>x.y.z</version>
      </dependency>

Make sure you use the same version of Manorrock Piranha itself.

Then you either will have to pass in the InitialContextFactory using a property

      -Djava.naming.factory.initial=com.manorrock.piranha.jndi.DefaultInitialContextFactory

or programmatically set it:

      System.getProperties().put("java.naming.factory.initial", 
        "com.manorrock.piranha.jndi.DefaultInitialContextFactory");

Note that this example does not set the &lt;scope&gt; but depending on what you
are using Manorrock Piranha for you might need to add a &lt;scope&gt;.
