package cloud.piranha.tck.core;

import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class CustomJsonbSerializationITFix implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(CustomJsonbSerializationITFix.class);
    }

    public void removeService(@Observes BeforeDeploy event) {
        Archive<?> archive = event.getDeployment().getArchive();
        if (archive instanceof WebArchive webArchive) {
            if (webArchive.getName().equals("CustomJsonbSerializationIT.war")) {
                webArchive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
            }
        }
    }

}
