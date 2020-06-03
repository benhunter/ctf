/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.netty.http.HttpResources;
/*     */ import reactor.netty.resources.ConnectionProvider;
/*     */ import reactor.netty.resources.LoopResources;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReactorResourceFactory
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*     */   private boolean useGlobalResources = true;
/*     */   @Nullable
/*     */   private Consumer<HttpResources> globalResourcesConsumer;
/*     */   private Supplier<ConnectionProvider> connectionProviderSupplier = () -> ConnectionProvider.elastic("webflux");
/*     */   private Supplier<LoopResources> loopResourcesSupplier = () -> LoopResources.create("webflux-http");
/*     */   @Nullable
/*     */   private ConnectionProvider connectionProvider;
/*     */   @Nullable
/*     */   private LoopResources loopResources;
/*     */   private boolean manageConnectionProvider = false;
/*     */   private boolean manageLoopResources = false;
/*     */   
/*     */   public void setUseGlobalResources(boolean useGlobalResources) {
/*  74 */     this.useGlobalResources = useGlobalResources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseGlobalResources() {
/*  82 */     return this.useGlobalResources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGlobalResourcesConsumer(Consumer<HttpResources> consumer) {
/*  93 */     this.useGlobalResources = true;
/*  94 */     this
/*  95 */       .globalResourcesConsumer = (this.globalResourcesConsumer != null) ? this.globalResourcesConsumer.andThen(consumer) : consumer;
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
/*     */   public void setConnectionProviderSupplier(Supplier<ConnectionProvider> supplier) {
/* 107 */     this.connectionProviderSupplier = supplier;
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
/*     */   public void setLoopResourcesSupplier(Supplier<LoopResources> supplier) {
/* 119 */     this.loopResourcesSupplier = supplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionProvider(ConnectionProvider connectionProvider) {
/* 128 */     this.connectionProvider = connectionProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionProvider getConnectionProvider() {
/* 135 */     Assert.state((this.connectionProvider != null), "ConnectionProvider not initialized yet");
/* 136 */     return this.connectionProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoopResources(LoopResources loopResources) {
/* 145 */     this.loopResources = loopResources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoopResources getLoopResources() {
/* 152 */     Assert.state((this.loopResources != null), "LoopResources not initialized yet");
/* 153 */     return this.loopResources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 159 */     if (this.useGlobalResources) {
/* 160 */       Assert.isTrue((this.loopResources == null && this.connectionProvider == null), "'useGlobalResources' is mutually exclusive with explicitly configured resources");
/*     */       
/* 162 */       HttpResources httpResources = HttpResources.get();
/* 163 */       if (this.globalResourcesConsumer != null) {
/* 164 */         this.globalResourcesConsumer.accept(httpResources);
/*     */       }
/* 166 */       this.connectionProvider = (ConnectionProvider)httpResources;
/* 167 */       this.loopResources = (LoopResources)httpResources;
/*     */     } else {
/*     */       
/* 170 */       if (this.loopResources == null) {
/* 171 */         this.manageLoopResources = true;
/* 172 */         this.loopResources = this.loopResourcesSupplier.get();
/*     */       } 
/* 174 */       if (this.connectionProvider == null) {
/* 175 */         this.manageConnectionProvider = true;
/* 176 */         this.connectionProvider = this.connectionProviderSupplier.get();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 183 */     if (this.useGlobalResources) {
/* 184 */       HttpResources.disposeLoopsAndConnections();
/*     */     } else {
/*     */       
/*     */       try {
/* 188 */         ConnectionProvider provider = this.connectionProvider;
/* 189 */         if (provider != null && this.manageConnectionProvider) {
/* 190 */           provider.dispose();
/*     */         }
/*     */       }
/* 193 */       catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 198 */         LoopResources resources = this.loopResources;
/* 199 */         if (resources != null && this.manageLoopResources) {
/* 200 */           resources.dispose();
/*     */         }
/*     */       }
/* 203 */       catch (Throwable throwable) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ReactorResourceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */