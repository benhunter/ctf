/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ProxyConfig
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8409359707199703185L;
/*     */   private boolean proxyTargetClass = false;
/*     */   private boolean optimize = false;
/*     */   boolean opaque = false;
/*     */   boolean exposeProxy = false;
/*     */   private boolean frozen = false;
/*     */   
/*     */   public void setProxyTargetClass(boolean proxyTargetClass) {
/*  61 */     this.proxyTargetClass = proxyTargetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProxyTargetClass() {
/*  68 */     return this.proxyTargetClass;
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
/*     */ 
/*     */   
/*     */   public void setOptimize(boolean optimize) {
/*  83 */     this.optimize = optimize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptimize() {
/*  90 */     return this.optimize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpaque(boolean opaque) {
/* 100 */     this.opaque = opaque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpaque() {
/* 108 */     return this.opaque;
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
/*     */   public void setExposeProxy(boolean exposeProxy) {
/* 121 */     this.exposeProxy = exposeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExposeProxy() {
/* 129 */     return this.exposeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean frozen) {
/* 139 */     this.frozen = frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 146 */     return this.frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyFrom(ProxyConfig other) {
/* 155 */     Assert.notNull(other, "Other ProxyConfig object must not be null");
/* 156 */     this.proxyTargetClass = other.proxyTargetClass;
/* 157 */     this.optimize = other.optimize;
/* 158 */     this.exposeProxy = other.exposeProxy;
/* 159 */     this.frozen = other.frozen;
/* 160 */     this.opaque = other.opaque;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     StringBuilder sb = new StringBuilder();
/* 166 */     sb.append("proxyTargetClass=").append(this.proxyTargetClass).append("; ");
/* 167 */     sb.append("optimize=").append(this.optimize).append("; ");
/* 168 */     sb.append("opaque=").append(this.opaque).append("; ");
/* 169 */     sb.append("exposeProxy=").append(this.exposeProxy).append("; ");
/* 170 */     sb.append("frozen=").append(this.frozen);
/* 171 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ProxyConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */