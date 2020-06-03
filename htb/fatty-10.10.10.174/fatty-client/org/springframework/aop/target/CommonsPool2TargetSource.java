/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import org.apache.commons.pool2.ObjectPool;
/*     */ import org.apache.commons.pool2.PooledObject;
/*     */ import org.apache.commons.pool2.PooledObjectFactory;
/*     */ import org.apache.commons.pool2.impl.DefaultPooledObject;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPool;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonsPool2TargetSource
/*     */   extends AbstractPoolingTargetSource
/*     */   implements PooledObjectFactory<Object>
/*     */ {
/*  69 */   private int maxIdle = 8;
/*     */   
/*  71 */   private int minIdle = 0;
/*     */   
/*  73 */   private long maxWait = -1L;
/*     */   
/*  75 */   private long timeBetweenEvictionRunsMillis = -1L;
/*     */   
/*  77 */   private long minEvictableIdleTimeMillis = 1800000L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean blockWhenExhausted = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ObjectPool pool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonsPool2TargetSource() {
/*  95 */     setMaxSize(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxIdle(int maxIdle) {
/* 105 */     this.maxIdle = maxIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxIdle() {
/* 112 */     return this.maxIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinIdle(int minIdle) {
/* 121 */     this.minIdle = minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinIdle() {
/* 128 */     return this.minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxWait(long maxWait) {
/* 137 */     this.maxWait = maxWait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxWait() {
/* 144 */     return this.maxWait;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
/* 154 */     this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimeBetweenEvictionRunsMillis() {
/* 161 */     return this.timeBetweenEvictionRunsMillis;
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
/*     */   public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
/* 173 */     this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMinEvictableIdleTimeMillis() {
/* 180 */     return this.minEvictableIdleTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockWhenExhausted(boolean blockWhenExhausted) {
/* 187 */     this.blockWhenExhausted = blockWhenExhausted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlockWhenExhausted() {
/* 194 */     return this.blockWhenExhausted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void createPool() {
/* 204 */     this.logger.debug("Creating Commons object pool");
/* 205 */     this.pool = createObjectPool();
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
/*     */   protected ObjectPool createObjectPool() {
/* 217 */     GenericObjectPoolConfig config = new GenericObjectPoolConfig();
/* 218 */     config.setMaxTotal(getMaxSize());
/* 219 */     config.setMaxIdle(getMaxIdle());
/* 220 */     config.setMinIdle(getMinIdle());
/* 221 */     config.setMaxWaitMillis(getMaxWait());
/* 222 */     config.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
/* 223 */     config.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
/* 224 */     config.setBlockWhenExhausted(isBlockWhenExhausted());
/* 225 */     return (ObjectPool)new GenericObjectPool(this, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() throws Exception {
/* 234 */     Assert.state((this.pool != null), "No Commons ObjectPool available");
/* 235 */     return this.pool.borrowObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) throws Exception {
/* 243 */     if (this.pool != null) {
/* 244 */       this.pool.returnObject(target);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getActiveCount() throws UnsupportedOperationException {
/* 250 */     return (this.pool != null) ? this.pool.getNumActive() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIdleCount() throws UnsupportedOperationException {
/* 255 */     return (this.pool != null) ? this.pool.getNumIdle() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 264 */     if (this.pool != null) {
/* 265 */       this.logger.debug("Closing Commons ObjectPool");
/* 266 */       this.pool.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PooledObject<Object> makeObject() throws Exception {
/* 277 */     return (PooledObject<Object>)new DefaultPooledObject(newPrototypeInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroyObject(PooledObject<Object> p) throws Exception {
/* 282 */     destroyPrototypeInstance(p.getObject());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validateObject(PooledObject<Object> p) {
/* 287 */     return true;
/*     */   }
/*     */   
/*     */   public void activateObject(PooledObject<Object> p) throws Exception {}
/*     */   
/*     */   public void passivateObject(PooledObject<Object> p) throws Exception {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/CommonsPool2TargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */