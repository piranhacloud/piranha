
# Manorrock Piranha Hazelcast HTTP Session Integration

The Manorrock Piranha Hazelcast HTTP Session integration module is a module that
can be used to have Hazelcast be the session manager.

## How to use?

    webApplication.setHttpSessionManager(new HazelcastHttpSessionManager());
        
This snippet above makes your web application use the Hazelcast HTTP session
manager.