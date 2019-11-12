package com.manorrock.piranha.runner.war;

import javax.annotation.Priority;

import org.jboss.weld.environment.deployment.discovery.BeanArchiveBuilder;
import org.jboss.weld.environment.deployment.discovery.BeanArchiveHandler;

@Priority(10)
public class PiranhaBeanArchiveHandler implements BeanArchiveHandler {

    @Override
    public BeanArchiveBuilder handle(String beanArchiveReference) {
        return new PiranhaBeanArchiveBuilder();
    }

}
