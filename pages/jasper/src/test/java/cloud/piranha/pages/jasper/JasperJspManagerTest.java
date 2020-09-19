package cloud.piranha.pages.jasper;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletRegistration;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class JasperJspManagerTest {

    @Test
    void testAddJsp() {
        JasperJspManager manager = new JasperJspManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setJspManager(manager);
        manager.addJspFile(webApp, "TestServlet", "file.jsp");
        Collection<String> mappings = webApp.getMappings("TestServlet");
        assertNotNull(mappings);
        assertTrue(mappings.contains("file.jsp"));
    }

    @Test
    void testGetJspConfigDescriptor() {
        assertNull(new JasperJspManager().getJspConfigDescriptor());
    }

}
