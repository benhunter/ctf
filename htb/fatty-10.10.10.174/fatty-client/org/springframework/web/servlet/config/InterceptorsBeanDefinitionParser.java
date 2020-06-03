/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*    */ import org.springframework.beans.factory.support.ManagedList;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.xml.DomUtils;
/*    */ import org.springframework.web.servlet.handler.MappedInterceptor;
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
/*    */ class InterceptorsBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   @Nullable
/*    */   public BeanDefinition parse(Element element, ParserContext context) {
/* 47 */     context.pushContainingComponent(new CompositeComponentDefinition(element
/* 48 */           .getTagName(), context.extractSource(element)));
/*    */     
/* 50 */     RuntimeBeanReference pathMatcherRef = null;
/* 51 */     if (element.hasAttribute("path-matcher")) {
/* 52 */       pathMatcherRef = new RuntimeBeanReference(element.getAttribute("path-matcher"));
/*    */     }
/*    */     
/* 55 */     List<Element> interceptors = DomUtils.getChildElementsByTagName(element, new String[] { "bean", "ref", "interceptor" });
/* 56 */     for (Element interceptor : interceptors) {
/* 57 */       Object interceptorBean; RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
/* 58 */       mappedInterceptorDef.setSource(context.extractSource(interceptor));
/* 59 */       mappedInterceptorDef.setRole(2);
/*    */       
/* 61 */       ManagedList<String> includePatterns = null;
/* 62 */       ManagedList<String> excludePatterns = null;
/*    */       
/* 64 */       if ("interceptor".equals(interceptor.getLocalName())) {
/* 65 */         includePatterns = getIncludePatterns(interceptor, "mapping");
/* 66 */         excludePatterns = getIncludePatterns(interceptor, "exclude-mapping");
/* 67 */         Element beanElem = DomUtils.getChildElementsByTagName(interceptor, new String[] { "bean", "ref" }).get(0);
/* 68 */         interceptorBean = context.getDelegate().parsePropertySubElement(beanElem, null);
/*    */       } else {
/*    */         
/* 71 */         interceptorBean = context.getDelegate().parsePropertySubElement(interceptor, null);
/*    */       } 
/* 73 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, includePatterns);
/* 74 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, excludePatterns);
/* 75 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(2, interceptorBean);
/*    */       
/* 77 */       if (pathMatcherRef != null) {
/* 78 */         mappedInterceptorDef.getPropertyValues().add("pathMatcher", pathMatcherRef);
/*    */       }
/*    */       
/* 81 */       String beanName = context.getReaderContext().registerWithGeneratedName((BeanDefinition)mappedInterceptorDef);
/* 82 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)mappedInterceptorDef, beanName));
/*    */     } 
/*    */     
/* 85 */     context.popAndRegisterContainingComponent();
/* 86 */     return null;
/*    */   }
/*    */   
/*    */   private ManagedList<String> getIncludePatterns(Element interceptor, String elementName) {
/* 90 */     List<Element> paths = DomUtils.getChildElementsByTagName(interceptor, elementName);
/* 91 */     ManagedList<String> patterns = new ManagedList(paths.size());
/* 92 */     for (Element path : paths) {
/* 93 */       patterns.add(path.getAttribute("path"));
/*    */     }
/* 95 */     return patterns;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/InterceptorsBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */