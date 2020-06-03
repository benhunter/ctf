/*     */ package org.springframework.context.config;
/*     */ 
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.jmx.support.MBeanServerFactoryBean;
/*     */ import org.springframework.jmx.support.WebSphereMBeanServerFactoryBean;
/*     */ import org.springframework.jndi.JndiObjectFactoryBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MBeanServerBeanDefinitionParser
/*     */   extends AbstractBeanDefinitionParser
/*     */ {
/*     */   private static final String MBEAN_SERVER_BEAN_NAME = "mbeanServer";
/*     */   private static final String AGENT_ID_ATTRIBUTE = "agent-id";
/*     */   private static final boolean weblogicPresent;
/*     */   private static final boolean webspherePresent;
/*     */   
/*     */   static {
/*  57 */     ClassLoader classLoader = MBeanServerBeanDefinitionParser.class.getClassLoader();
/*  58 */     weblogicPresent = ClassUtils.isPresent("weblogic.management.Helper", classLoader);
/*  59 */     webspherePresent = ClassUtils.isPresent("com.ibm.websphere.management.AdminServiceFactory", classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
/*  65 */     String id = element.getAttribute("id");
/*  66 */     return StringUtils.hasText(id) ? id : "mbeanServer";
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
/*  71 */     String agentId = element.getAttribute("agent-id");
/*  72 */     if (StringUtils.hasText(agentId)) {
/*  73 */       RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(MBeanServerFactoryBean.class);
/*  74 */       rootBeanDefinition.getPropertyValues().add("agentId", agentId);
/*  75 */       return (AbstractBeanDefinition)rootBeanDefinition;
/*     */     } 
/*  77 */     AbstractBeanDefinition specialServer = findServerForSpecialEnvironment();
/*  78 */     if (specialServer != null) {
/*  79 */       return specialServer;
/*     */     }
/*  81 */     RootBeanDefinition bd = new RootBeanDefinition(MBeanServerFactoryBean.class);
/*  82 */     bd.getPropertyValues().add("locateExistingServerIfPossible", Boolean.TRUE);
/*     */ 
/*     */     
/*  85 */     bd.setRole(2);
/*  86 */     bd.setSource(parserContext.extractSource(element));
/*  87 */     return (AbstractBeanDefinition)bd;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static AbstractBeanDefinition findServerForSpecialEnvironment() {
/*  92 */     if (weblogicPresent) {
/*  93 */       RootBeanDefinition bd = new RootBeanDefinition(JndiObjectFactoryBean.class);
/*  94 */       bd.getPropertyValues().add("jndiName", "java:comp/env/jmx/runtime");
/*  95 */       return (AbstractBeanDefinition)bd;
/*     */     } 
/*  97 */     if (webspherePresent) {
/*  98 */       return (AbstractBeanDefinition)new RootBeanDefinition(WebSphereMBeanServerFactoryBean.class);
/*     */     }
/*     */     
/* 101 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/config/MBeanServerBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */