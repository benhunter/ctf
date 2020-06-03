/*     */ package org.springframework.web.server.session;
/*     */ 
/*     */ import java.time.Clock;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.IdGenerator;
/*     */ import org.springframework.util.JdkIdGenerator;
/*     */ import org.springframework.web.server.WebSession;
/*     */ import reactor.core.publisher.Mono;
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
/*     */ public class InMemoryWebSessionStore
/*     */   implements WebSessionStore
/*     */ {
/*  47 */   private static final IdGenerator idGenerator = (IdGenerator)new JdkIdGenerator();
/*     */ 
/*     */   
/*  50 */   private int maxSessions = 10000;
/*     */   
/*  52 */   private Clock clock = Clock.system(ZoneId.of("GMT"));
/*     */   
/*  54 */   private final Map<String, InMemoryWebSession> sessions = new ConcurrentHashMap<>();
/*     */   
/*  56 */   private final ExpiredSessionChecker expiredSessionChecker = new ExpiredSessionChecker();
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
/*     */   public void setMaxSessions(int maxSessions) {
/*  68 */     this.maxSessions = maxSessions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxSessions() {
/*  76 */     return this.maxSessions;
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
/*     */   public void setClock(Clock clock) {
/*  89 */     Assert.notNull(clock, "Clock is required");
/*  90 */     this.clock = clock;
/*  91 */     removeExpiredSessions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Clock getClock() {
/*  98 */     return this.clock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, WebSession> getSessions() {
/* 108 */     return Collections.unmodifiableMap((Map)this.sessions);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<WebSession> createWebSession() {
/* 114 */     Instant now = this.clock.instant();
/* 115 */     this.expiredSessionChecker.checkIfNecessary(now);
/* 116 */     return Mono.fromSupplier(() -> new InMemoryWebSession(now));
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<WebSession> retrieveSession(String id) {
/* 121 */     Instant now = this.clock.instant();
/* 122 */     this.expiredSessionChecker.checkIfNecessary(now);
/* 123 */     InMemoryWebSession session = this.sessions.get(id);
/* 124 */     if (session == null) {
/* 125 */       return Mono.empty();
/*     */     }
/* 127 */     if (session.isExpired(now)) {
/* 128 */       this.sessions.remove(id);
/* 129 */       return Mono.empty();
/*     */     } 
/*     */     
/* 132 */     session.updateLastAccessTime(now);
/* 133 */     return Mono.just(session);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> removeSession(String id) {
/* 139 */     this.sessions.remove(id);
/* 140 */     return Mono.empty();
/*     */   }
/*     */   
/*     */   public Mono<WebSession> updateLastAccessTime(WebSession session) {
/* 144 */     return Mono.fromSupplier(() -> {
/*     */           Assert.isInstanceOf(InMemoryWebSession.class, session);
/*     */           ((InMemoryWebSession)session).updateLastAccessTime(this.clock.instant());
/*     */           return session;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExpiredSessions() {
/* 159 */     this.expiredSessionChecker.removeExpiredSessions(this.clock.instant());
/*     */   }
/*     */   
/*     */   private class InMemoryWebSession
/*     */     implements WebSession
/*     */   {
/* 165 */     private final AtomicReference<String> id = new AtomicReference<>(String.valueOf(InMemoryWebSessionStore.idGenerator.generateId()));
/*     */     
/* 167 */     private final Map<String, Object> attributes = new ConcurrentHashMap<>();
/*     */     
/*     */     private final Instant creationTime;
/*     */     
/*     */     private volatile Instant lastAccessTime;
/*     */     
/* 173 */     private volatile Duration maxIdleTime = Duration.ofMinutes(30L);
/*     */     
/* 175 */     private final AtomicReference<InMemoryWebSessionStore.State> state = new AtomicReference<>(InMemoryWebSessionStore.State.NEW);
/*     */ 
/*     */     
/*     */     public InMemoryWebSession(Instant creationTime) {
/* 179 */       this.creationTime = creationTime;
/* 180 */       this.lastAccessTime = this.creationTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getId() {
/* 185 */       return this.id.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, Object> getAttributes() {
/* 190 */       return this.attributes;
/*     */     }
/*     */ 
/*     */     
/*     */     public Instant getCreationTime() {
/* 195 */       return this.creationTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public Instant getLastAccessTime() {
/* 200 */       return this.lastAccessTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMaxIdleTime(Duration maxIdleTime) {
/* 205 */       this.maxIdleTime = maxIdleTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public Duration getMaxIdleTime() {
/* 210 */       return this.maxIdleTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 215 */       this.state.compareAndSet(InMemoryWebSessionStore.State.NEW, InMemoryWebSessionStore.State.STARTED);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStarted() {
/* 220 */       return (((InMemoryWebSessionStore.State)this.state.get()).equals(InMemoryWebSessionStore.State.STARTED) || !getAttributes().isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public Mono<Void> changeSessionId() {
/* 225 */       String currentId = this.id.get();
/* 226 */       InMemoryWebSessionStore.this.sessions.remove(currentId);
/* 227 */       String newId = String.valueOf(InMemoryWebSessionStore.idGenerator.generateId());
/* 228 */       this.id.set(newId);
/* 229 */       InMemoryWebSessionStore.this.sessions.put(getId(), this);
/* 230 */       return Mono.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Mono<Void> invalidate() {
/* 235 */       this.state.set(InMemoryWebSessionStore.State.EXPIRED);
/* 236 */       getAttributes().clear();
/* 237 */       InMemoryWebSessionStore.this.sessions.remove(this.id.get());
/* 238 */       return Mono.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Mono<Void> save() {
/* 244 */       checkMaxSessionsLimit();
/*     */ 
/*     */       
/* 247 */       if (!getAttributes().isEmpty()) {
/* 248 */         this.state.compareAndSet(InMemoryWebSessionStore.State.NEW, InMemoryWebSessionStore.State.STARTED);
/*     */       }
/*     */       
/* 251 */       if (isStarted()) {
/*     */         
/* 253 */         InMemoryWebSessionStore.this.sessions.put(getId(), this);
/*     */ 
/*     */         
/* 256 */         if (((InMemoryWebSessionStore.State)this.state.get()).equals(InMemoryWebSessionStore.State.EXPIRED)) {
/* 257 */           InMemoryWebSessionStore.this.sessions.remove(getId());
/* 258 */           return Mono.error(new IllegalStateException("Session was invalidated"));
/*     */         } 
/*     */       } 
/*     */       
/* 262 */       return Mono.empty();
/*     */     }
/*     */     
/*     */     private void checkMaxSessionsLimit() {
/* 266 */       if (InMemoryWebSessionStore.this.sessions.size() >= InMemoryWebSessionStore.this.maxSessions) {
/* 267 */         InMemoryWebSessionStore.this.expiredSessionChecker.removeExpiredSessions(InMemoryWebSessionStore.this.clock.instant());
/* 268 */         if (InMemoryWebSessionStore.this.sessions.size() >= InMemoryWebSessionStore.this.maxSessions) {
/* 269 */           throw new IllegalStateException("Max sessions limit reached: " + InMemoryWebSessionStore.this.sessions.size());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isExpired() {
/* 276 */       return isExpired(InMemoryWebSessionStore.this.clock.instant());
/*     */     }
/*     */     
/*     */     private boolean isExpired(Instant now) {
/* 280 */       if (((InMemoryWebSessionStore.State)this.state.get()).equals(InMemoryWebSessionStore.State.EXPIRED)) {
/* 281 */         return true;
/*     */       }
/* 283 */       if (checkExpired(now)) {
/* 284 */         this.state.set(InMemoryWebSessionStore.State.EXPIRED);
/* 285 */         return true;
/*     */       } 
/* 287 */       return false;
/*     */     }
/*     */     
/*     */     private boolean checkExpired(Instant currentTime) {
/* 291 */       return (isStarted() && !this.maxIdleTime.isNegative() && currentTime
/* 292 */         .minus(this.maxIdleTime).isAfter(this.lastAccessTime));
/*     */     }
/*     */     
/*     */     private void updateLastAccessTime(Instant currentTime) {
/* 296 */       this.lastAccessTime = currentTime;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ExpiredSessionChecker
/*     */   {
/*     */     private static final int CHECK_PERIOD = 60000;
/*     */ 
/*     */     
/* 307 */     private final ReentrantLock lock = new ReentrantLock();
/*     */     
/* 309 */     private Instant checkTime = InMemoryWebSessionStore.this.clock.instant().plus(60000L, ChronoUnit.MILLIS);
/*     */ 
/*     */     
/*     */     public void checkIfNecessary(Instant now) {
/* 313 */       if (this.checkTime.isBefore(now)) {
/* 314 */         removeExpiredSessions(now);
/*     */       }
/*     */     }
/*     */     
/*     */     public void removeExpiredSessions(Instant now) {
/* 319 */       if (InMemoryWebSessionStore.this.sessions.isEmpty()) {
/*     */         return;
/*     */       }
/* 322 */       if (this.lock.tryLock())
/*     */         try {
/* 324 */           Iterator<InMemoryWebSessionStore.InMemoryWebSession> iterator = InMemoryWebSessionStore.this.sessions.values().iterator();
/* 325 */           while (iterator.hasNext()) {
/* 326 */             InMemoryWebSessionStore.InMemoryWebSession session = iterator.next();
/* 327 */             if (session.isExpired(now)) {
/* 328 */               iterator.remove();
/* 329 */               session.invalidate();
/*     */             } 
/*     */           } 
/*     */         } finally {
/*     */           
/* 334 */           this.checkTime = now.plus(60000L, ChronoUnit.MILLIS);
/* 335 */           this.lock.unlock();
/*     */         }  
/*     */     }
/*     */     
/*     */     private ExpiredSessionChecker() {} }
/*     */   
/*     */   private enum State {
/* 342 */     NEW, STARTED, EXPIRED;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/session/InMemoryWebSessionStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */