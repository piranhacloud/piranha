package cloud.piranha.microprofile.jwt.tck.extension;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class ConfigPropertiesAdder implements ApplicationArchiveProcessor {

    @Override
    public void process(Archive<?> archive, TestClass testClass) {
        if (!(archive instanceof WebArchive)) {
            return;
        }

        process((WebArchive) archive);
    }

    public void process(WebArchive webArchive) {
        Node metaInfConfig = webArchive.get("/META-INF/microprofile-config.properties");
        
        if (metaInfConfig == null) {
            if (webArchive.get("/WEB-INF/classes/publicKey.pem") != null) {
                webArchive.addAsResource("META-INF/public-key.properties", "META-INF/microprofile-config.properties");
            } else {
                webArchive.addAsResource("META-INF/microprofile-config.properties");
            }
        } else {
            webArchive.addAsResource(metaInfConfig.getAsset(), "META-INF/microprofile-config.properties");
            webArchive.delete("/META-INF/microprofile-config.properties");
        }
        
        System.out.printf("WebArchive: %s\n", webArchive.toString(true));
    }
}