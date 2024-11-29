package cloud.piranha.arquillian.managed;

import java.io.File;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the ManagedPiranhaContainer class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ManagedPiranhaContainerTest {

    /**
     * Test getContainerConfiguration method.
     */
    @Test
    void testGetConfigurationClass() {
        ManagedPiranhaContainer container = new ManagedPiranhaContainer();
        assertEquals(ManagedPiranhaContainerConfiguration.class, container.getConfigurationClass());
    }

    /**
     * TEst getDefaulProtocol method.
     */
    @Test
    void testGetDefaultProtocol() {
        ManagedPiranhaContainer container = new ManagedPiranhaContainer();
        container.setup(new ManagedPiranhaContainerConfiguration());
        ProtocolDescription protocolDescription = container.getDefaultProtocol();
        assertEquals("Servlet 6.0", protocolDescription.getName());
    }
}
