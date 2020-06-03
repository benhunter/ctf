/*    */ package org.springframework.scripting.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.TypedStringValue;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ class ScriptingDefaultsParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   private static final String REFRESH_CHECK_DELAY_ATTRIBUTE = "refresh-check-delay";
/*    */   private static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*    */   
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 43 */     BeanDefinition bd = LangNamespaceUtils.registerScriptFactoryPostProcessorIfNecessary(parserContext.getRegistry());
/* 44 */     String refreshCheckDelay = element.getAttribute("refresh-check-delay");
/* 45 */     if (StringUtils.hasText(refreshCheckDelay)) {
/* 46 */       bd.getPropertyValues().add("defaultRefreshCheckDelay", Long.valueOf(refreshCheckDelay));
/*    */     }
/* 48 */     String proxyTargetClass = element.getAttribute("proxy-target-class");
/* 49 */     if (StringUtils.hasText(proxyTargetClass)) {
/* 50 */       bd.getPropertyValues().add("defaultProxyTargetClass", new TypedStringValue(proxyTargetClass, Boolean.class));
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/config/ScriptingDefaultsParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */