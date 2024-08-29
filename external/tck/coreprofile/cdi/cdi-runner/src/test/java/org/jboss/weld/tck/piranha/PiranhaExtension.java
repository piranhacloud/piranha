package org.jboss.weld.tck.piranha;

import org.jboss.arquillian.container.spi.client.container.DeploymentExceptionTransformer;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * Registers the exception transformer to properly identify deployment failures.
 *
 */
public class PiranhaExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(DeploymentExceptionTransformer.class, PiranhaDeploymentExceptionTransformer.class);
    }

}
