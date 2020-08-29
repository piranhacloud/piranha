package cloud.piranha.session.hazelcast;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HazelcastInitializerTest {
    
    @Test
    void testInitializer() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addInitializer(new HazelcastInitializer());
        webApp.initialize();
        assertTrue(webApp.getHttpSessionManager() instanceof HazelcastHttpSessionManager);
    }
}
