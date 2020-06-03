/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*     */ public abstract class AbstractPrototypeBasedTargetSource
/*     */   extends AbstractBeanFactoryBasedTargetSource
/*     */ {
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
/*  51 */     super.setBeanFactory(beanFactory);
/*     */ 
/*     */     
/*  54 */     if (!beanFactory.isPrototype(getTargetBeanName())) {
/*  55 */       throw new BeanDefinitionStoreException("Cannot use prototype-based TargetSource against non-prototype bean with name '" + 
/*     */           
/*  57 */           getTargetBeanName() + "': instances would not be independent");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object newPrototypeInstance() throws BeansException {
/*  66 */     if (this.logger.isDebugEnabled()) {
/*  67 */       this.logger.debug("Creating new instance of bean '" + getTargetBeanName() + "'");
/*     */     }
/*  69 */     return getBeanFactory().getBean(getTargetBeanName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyPrototypeInstance(Object target) {
/*  77 */     if (this.logger.isDebugEnabled()) {
/*  78 */       this.logger.debug("Destroying instance of bean '" + getTargetBeanName() + "'");
/*     */     }
/*  80 */     if (getBeanFactory() instanceof ConfigurableBeanFactory) {
/*  81 */       ((ConfigurableBeanFactory)getBeanFactory()).destroyBean(getTargetBeanName(), target);
/*     */     }
/*  83 */     else if (target instanceof DisposableBean) {
/*     */       try {
/*  85 */         ((DisposableBean)target).destroy();
/*     */       }
/*  87 */       catch (Throwable ex) {
/*  88 */         this.logger.warn("Destroy method on bean with name '" + getTargetBeanName() + "' threw an exception", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/*  99 */     throw new NotSerializableException("A prototype-based TargetSource itself is not deserializable - just a disconnected SingletonTargetSource or EmptyTargetSource is");
/*     */   }
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
/*     */   protected Object writeReplace() throws ObjectStreamException {
/* 112 */     if (this.logger.isDebugEnabled()) {
/* 113 */       this.logger.debug("Disconnecting TargetSource [" + this + "]");
/*     */     }
/*     */     
/*     */     try {
/* 117 */       Object target = getTarget();
/* 118 */       return (target != null) ? new SingletonTargetSource(target) : 
/* 119 */         EmptyTargetSource.forClass(getTargetClass());
/*     */     }
/* 121 */     catch (Exception ex) {
/* 122 */       String msg = "Cannot get target for disconnecting TargetSource [" + this + "]";
/* 123 */       this.logger.error(msg, ex);
/* 124 */       throw new NotSerializableException(msg + ": " + ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/AbstractPrototypeBasedTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */