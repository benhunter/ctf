/*    */ package org.springframework.scripting.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LangNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init() {
/* 44 */     registerScriptBeanDefinitionParser("groovy", "org.springframework.scripting.groovy.GroovyScriptFactory");
/* 45 */     registerScriptBeanDefinitionParser("bsh", "org.springframework.scripting.bsh.BshScriptFactory");
/* 46 */     registerScriptBeanDefinitionParser("std", "org.springframework.scripting.support.StandardScriptFactory");
/* 47 */     registerBeanDefinitionParser("defaults", new ScriptingDefaultsParser());
/*    */   }
/*    */   
/*    */   private void registerScriptBeanDefinitionParser(String key, String scriptFactoryClassName) {
/* 51 */     registerBeanDefinitionParser(key, (BeanDefinitionParser)new ScriptBeanDefinitionParser(scriptFactoryClassName));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/config/LangNamespaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */