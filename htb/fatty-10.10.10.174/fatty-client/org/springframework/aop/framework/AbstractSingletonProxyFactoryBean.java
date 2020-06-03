/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class AbstractSingletonProxyFactoryBean
/*     */   extends ProxyConfig
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Object target;
/*     */   @Nullable
/*     */   private Class<?>[] proxyInterfaces;
/*     */   @Nullable
/*     */   private Object[] preInterceptors;
/*     */   @Nullable
/*     */   private Object[] postInterceptors;
/*  58 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private transient ClassLoader proxyClassLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(Object target) {
/*  79 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
/*  89 */     this.proxyInterfaces = proxyInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreInterceptors(Object[] preInterceptors) {
/* 100 */     this.preInterceptors = preInterceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostInterceptors(Object[] postInterceptors) {
/* 110 */     this.postInterceptors = postInterceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 119 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyClassLoader(ClassLoader classLoader) {
/* 129 */     this.proxyClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 134 */     if (this.proxyClassLoader == null) {
/* 135 */       this.proxyClassLoader = classLoader;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 142 */     if (this.target == null) {
/* 143 */       throw new IllegalArgumentException("Property 'target' is required");
/*     */     }
/* 145 */     if (this.target instanceof String) {
/* 146 */       throw new IllegalArgumentException("'target' needs to be a bean reference, not a bean name as value");
/*     */     }
/* 148 */     if (this.proxyClassLoader == null) {
/* 149 */       this.proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */     }
/*     */     
/* 152 */     ProxyFactory proxyFactory = new ProxyFactory();
/*     */     
/* 154 */     if (this.preInterceptors != null) {
/* 155 */       for (Object interceptor : this.preInterceptors) {
/* 156 */         proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(interceptor));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 161 */     proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(createMainInterceptor()));
/*     */     
/* 163 */     if (this.postInterceptors != null) {
/* 164 */       for (Object interceptor : this.postInterceptors) {
/* 165 */         proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(interceptor));
/*     */       }
/*     */     }
/*     */     
/* 169 */     proxyFactory.copyFrom(this);
/*     */     
/* 171 */     TargetSource targetSource = createTargetSource(this.target);
/* 172 */     proxyFactory.setTargetSource(targetSource);
/*     */     
/* 174 */     if (this.proxyInterfaces != null) {
/* 175 */       proxyFactory.setInterfaces(this.proxyInterfaces);
/*     */     }
/* 177 */     else if (!isProxyTargetClass()) {
/*     */       
/* 179 */       Class<?> targetClass = targetSource.getTargetClass();
/* 180 */       if (targetClass != null) {
/* 181 */         proxyFactory.setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.proxyClassLoader));
/*     */       }
/*     */     } 
/*     */     
/* 185 */     postProcessProxyFactory(proxyFactory);
/*     */     
/* 187 */     this.proxy = proxyFactory.getProxy(this.proxyClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TargetSource createTargetSource(Object target) {
/* 197 */     if (target instanceof TargetSource) {
/* 198 */       return (TargetSource)target;
/*     */     }
/*     */     
/* 201 */     return (TargetSource)new SingletonTargetSource(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessProxyFactory(ProxyFactory proxyFactory) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 217 */     if (this.proxy == null) {
/* 218 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 220 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getObjectType() {
/* 226 */     if (this.proxy != null) {
/* 227 */       return this.proxy.getClass();
/*     */     }
/* 229 */     if (this.proxyInterfaces != null && this.proxyInterfaces.length == 1) {
/* 230 */       return this.proxyInterfaces[0];
/*     */     }
/* 232 */     if (this.target instanceof TargetSource) {
/* 233 */       return ((TargetSource)this.target).getTargetClass();
/*     */     }
/* 235 */     if (this.target != null) {
/* 236 */       return this.target.getClass();
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSingleton() {
/* 243 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract Object createMainInterceptor();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AbstractSingletonProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */