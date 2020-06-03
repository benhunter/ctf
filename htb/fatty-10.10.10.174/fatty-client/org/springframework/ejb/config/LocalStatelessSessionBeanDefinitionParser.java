/*    */ package org.springframework.ejb.config;
/*    */ 
/*    */ import org.w3c.dom.Element;
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
/*    */ class LocalStatelessSessionBeanDefinitionParser
/*    */   extends AbstractJndiLocatingBeanDefinitionParser
/*    */ {
/*    */   protected String getBeanClassName(Element element) {
/* 36 */     return "org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/config/LocalStatelessSessionBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */