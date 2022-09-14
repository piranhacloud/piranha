
# Piranha Naming Thread

The Piranha Naming Thread module delivers you with Thread based JNDI
implementation.

Add the following dependency to your project to use it.

      <dependency>
          <groupId>cloud.piranha.naming</groupId>
          <artifactId>piranha-naming-thread</artifactId>
          <version>x.y.z</version>
      </dependency>

And then you either will have to pass in the InitialContextFactory using a property

      -Djava.naming.factory.initial=cloud.piranha.naming.thread.ThreadInitialContextFactory

or programmatically set it:

      System.getProperties().put("java.naming.factory.initial", 
        "cloud.piranha.naming.thread.ThreadInitialContextFactory");
