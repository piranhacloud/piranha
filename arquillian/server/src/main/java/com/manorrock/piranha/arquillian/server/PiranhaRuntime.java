package com.manorrock.piranha.arquillian.server;

import java.util.Set;

import org.jboss.shrinkwrap.api.Archive;

public interface PiranhaRuntime {
    
    Set<String> start(Archive<?> applicationArchive, ClassLoader classLoader);
    
    public void stop();
     
    

}
