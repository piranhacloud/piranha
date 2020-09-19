package cloud.piranha.pages.jasper;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationExtensionContext;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContainerInitializer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JasperExtensionTest {
    @Test
    void testExtension() {
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(JasperExtension.class);
        DefaultWebApplication webApp = new DefaultWebApplication();
        context.configure(webApp);
        webApp.initialize();
        webApp.start();
        Optional<ServletContainerInitializer> extension =
            webApp.getInitializers()
                .stream()
                .filter(JasperInitializer.class::isInstance)
                .findFirst();
        assertTrue(extension.isPresent());
    }
}
