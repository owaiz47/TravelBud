package com.travelbud.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtii implements ApplicationContextAware {

   private static ApplicationContext applicationContext;

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       SpringContextUtii.applicationContext=applicationContext;
   }

   public static ApplicationContext getApplicationContext() {
       return applicationContext;
   }
}
