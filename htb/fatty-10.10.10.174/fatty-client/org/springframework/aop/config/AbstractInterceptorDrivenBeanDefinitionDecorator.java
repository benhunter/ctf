/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.aop.framework.ProxyFactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Node;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractInterceptorDrivenBeanDefinitionDecorator
/*     */   implements BeanDefinitionDecorator
/*     */ {
/*     */   public final BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definitionHolder, ParserContext parserContext) {
/*  64 */     BeanDefinitionRegistry registry = parserContext.getRegistry();
/*     */ 
/*     */     
/*  67 */     String existingBeanName = definitionHolder.getBeanName();
/*  68 */     BeanDefinition targetDefinition = definitionHolder.getBeanDefinition();
/*  69 */     BeanDefinitionHolder targetHolder = new BeanDefinitionHolder(targetDefinition, existingBeanName + ".TARGET");
/*     */ 
/*     */     
/*  72 */     BeanDefinition interceptorDefinition = createInterceptorDefinition(node);
/*     */ 
/*     */     
/*  75 */     String interceptorName = existingBeanName + '.' + getInterceptorNameSuffix(interceptorDefinition);
/*  76 */     BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(interceptorDefinition, interceptorName), registry);
/*     */ 
/*     */     
/*  79 */     BeanDefinitionHolder result = definitionHolder;
/*     */     
/*  81 */     if (!isProxyFactoryBeanDefinition(targetDefinition)) {
/*     */       
/*  83 */       RootBeanDefinition proxyDefinition = new RootBeanDefinition();
/*     */       
/*  85 */       proxyDefinition.setBeanClass(ProxyFactoryBean.class);
/*  86 */       proxyDefinition.setScope(targetDefinition.getScope());
/*  87 */       proxyDefinition.setLazyInit(targetDefinition.isLazyInit());
/*     */       
/*  89 */       proxyDefinition.setDecoratedDefinition(targetHolder);
/*  90 */       proxyDefinition.getPropertyValues().add("target", targetHolder);
/*     */       
/*  92 */       proxyDefinition.getPropertyValues().add("interceptorNames", new ManagedList());
/*     */       
/*  94 */       proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
/*  95 */       proxyDefinition.setPrimary(targetDefinition.isPrimary());
/*  96 */       if (targetDefinition instanceof AbstractBeanDefinition) {
/*  97 */         proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition)targetDefinition);
/*     */       }
/*     */       
/* 100 */       result = new BeanDefinitionHolder((BeanDefinition)proxyDefinition, existingBeanName);
/*     */     } 
/*     */     
/* 103 */     addInterceptorNameToList(interceptorName, result.getBeanDefinition());
/* 104 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addInterceptorNameToList(String interceptorName, BeanDefinition beanDefinition) {
/* 109 */     List<String> list = (List<String>)beanDefinition.getPropertyValues().get("interceptorNames");
/* 110 */     Assert.state((list != null), "Missing 'interceptorNames' property");
/* 111 */     list.add(interceptorName);
/*     */   }
/*     */   
/*     */   private boolean isProxyFactoryBeanDefinition(BeanDefinition existingDefinition) {
/* 115 */     return ProxyFactoryBean.class.getName().equals(existingDefinition.getBeanClassName());
/*     */   }
/*     */   
/*     */   protected String getInterceptorNameSuffix(BeanDefinition interceptorDefinition) {
/* 119 */     String beanClassName = interceptorDefinition.getBeanClassName();
/* 120 */     return StringUtils.hasLength(beanClassName) ? 
/* 121 */       StringUtils.uncapitalize(ClassUtils.getShortName(beanClassName)) : "";
/*     */   }
/*     */   
/*     */   protected abstract BeanDefinition createInterceptorDefinition(Node paramNode);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AbstractInterceptorDrivenBeanDefinitionDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */