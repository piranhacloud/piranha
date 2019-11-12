package com.manorrock.piranha.runner.war;

import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;

import java.util.Set;

import org.jboss.shrinkwrap.api.Archive;
import org.omnifaces.exousia.modules.def.DefaultPolicy;
import org.omnifaces.exousia.modules.def.DefaultPolicyConfigurationFactory;

import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.DefaultWebApplicationServer;
import com.manorrock.piranha.api.HttpServer;
import com.manorrock.piranha.api.WebApplication;
import com.manorrock.piranha.authentication.elios.AuthenticationInitializer;
import com.manorrock.piranha.authorization.exousia.AuthorizationInitializer;
import com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer;
import com.manorrock.piranha.security.jakarta.JakartaSecurityInitializer;
import com.manorrock.piranha.security.soteria.SoteriaInitializer;
import com.manorrock.piranha.servlet.ServletFeature;
import com.manorrock.piranha.shrinkwrap.ShrinkWrapResource;
import com.manorrock.piranha.weld.WeldInitializer;

public class PiranhaServerDeployer {
    
    private HttpServer httpServer;
    
    public Set<String> start(Archive<?> applicationArchive, ClassLoader classLoader) {
        WebApplication webApplication = getWebApplication(applicationArchive, classLoader);
        
        DefaultWebApplicationServer webApplicationServer = new DefaultWebApplicationServer();
        webApplication.addFeature(new ServletFeature());
        webApplicationServer.addWebApplication(webApplication);
        
        webApplication.addInitializer(WeldInitializer.class.getName());
        
        webApplication.setAttribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class);
        webApplication.setAttribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class);
        
        webApplication.addInitializer(AuthorizationPreInitializer.class.getName());
        webApplication.addInitializer(AuthenticationInitializer.class.getName());
        webApplication.addInitializer(AuthorizationInitializer.class.getName());
        webApplication.addInitializer(JakartaSecurityInitializer.class.getName());
        
        webApplication.addInitializer(SoteriaInitializer.class.getName());
        
        webApplicationServer.initialize();
        webApplicationServer.start();
        
        httpServer = new DefaultHttpServer(8080, webApplicationServer);
        httpServer.start();
        
        return webApplication.getServletRegistrations().keySet();
    }
    
    WebApplication getWebApplication(Archive<?> archive, ClassLoader newClassLoader) {
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(newClassLoader);
        webApplication.addResource(new ShrinkWrapResource(archive));
        
        return webApplication;
    }

    public void stop() {
        // TODO Auto-generated method stub
        
    }
    
}
