/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ReactiveAdapter;
/*     */ import org.springframework.core.ReactiveAdapterRegistry;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.task.SyncTaskExecutor;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.codec.ServerSentEvent;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.context.request.async.DeferredResult;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ class ReactiveTypeHandler
/*     */ {
/*     */   private static final long STREAMING_TIMEOUT_VALUE = -1L;
/*  77 */   private static Log logger = LogFactory.getLog(ReactiveTypeHandler.class);
/*     */   
/*     */   private final ReactiveAdapterRegistry adapterRegistry;
/*     */   
/*     */   private final TaskExecutor taskExecutor;
/*     */   
/*     */   private final ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */   private boolean taskExecutorWarning;
/*     */ 
/*     */   
/*     */   public ReactiveTypeHandler() {
/*  89 */     this(ReactiveAdapterRegistry.getSharedInstance(), (TaskExecutor)new SyncTaskExecutor(), new ContentNegotiationManager());
/*     */   }
/*     */   
/*     */   ReactiveTypeHandler(ReactiveAdapterRegistry registry, TaskExecutor executor, ContentNegotiationManager manager) {
/*  93 */     Assert.notNull(registry, "ReactiveAdapterRegistry is required");
/*  94 */     Assert.notNull(executor, "TaskExecutor is required");
/*  95 */     Assert.notNull(manager, "ContentNegotiationManager is required");
/*  96 */     this.adapterRegistry = registry;
/*  97 */     this.taskExecutor = executor;
/*  98 */     this.contentNegotiationManager = manager;
/*     */     
/* 100 */     this.taskExecutorWarning = (executor instanceof org.springframework.core.task.SimpleAsyncTaskExecutor || executor instanceof SyncTaskExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReactiveType(Class<?> type) {
/* 109 */     return (this.adapterRegistry.getAdapter(type) != null);
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
/*     */   @Nullable
/*     */   public ResponseBodyEmitter handleValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mav, NativeWebRequest request) throws Exception {
/* 123 */     Assert.notNull(returnValue, "Expected return value");
/* 124 */     ReactiveAdapter adapter = this.adapterRegistry.getAdapter(returnValue.getClass());
/* 125 */     Assert.state((adapter != null), () -> "Unexpected return value: " + returnValue);
/*     */     
/* 127 */     ResolvableType elementType = ResolvableType.forMethodParameter(returnType).getGeneric(new int[0]);
/* 128 */     Class<?> elementClass = elementType.toClass();
/*     */     
/* 130 */     Collection<MediaType> mediaTypes = getMediaTypes(request);
/* 131 */     Optional<MediaType> mediaType = mediaTypes.stream().filter(MimeType::isConcrete).findFirst();
/*     */     
/* 133 */     if (adapter.isMultiValue()) {
/* 134 */       if (mediaTypes.stream().anyMatch(MediaType.TEXT_EVENT_STREAM::includes) || ServerSentEvent.class
/* 135 */         .isAssignableFrom(elementClass)) {
/* 136 */         logExecutorWarning(returnType);
/* 137 */         SseEmitter emitter = new SseEmitter(Long.valueOf(-1L));
/* 138 */         (new SseEmitterSubscriber(emitter, this.taskExecutor)).connect(adapter, returnValue);
/* 139 */         return emitter;
/*     */       } 
/* 141 */       if (CharSequence.class.isAssignableFrom(elementClass)) {
/* 142 */         logExecutorWarning(returnType);
/* 143 */         ResponseBodyEmitter emitter = getEmitter(mediaType.orElse(MediaType.TEXT_PLAIN));
/* 144 */         (new TextEmitterSubscriber(emitter, this.taskExecutor)).connect(adapter, returnValue);
/* 145 */         return emitter;
/*     */       } 
/* 147 */       if (mediaTypes.stream().anyMatch(MediaType.APPLICATION_STREAM_JSON::includes)) {
/* 148 */         logExecutorWarning(returnType);
/* 149 */         ResponseBodyEmitter emitter = getEmitter(MediaType.APPLICATION_STREAM_JSON);
/* 150 */         (new JsonEmitterSubscriber(emitter, this.taskExecutor)).connect(adapter, returnValue);
/* 151 */         return emitter;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 156 */     DeferredResult<Object> result = new DeferredResult();
/* 157 */     (new DeferredResultSubscriber(result, adapter, elementType)).connect(adapter, returnValue);
/* 158 */     WebAsyncUtils.getAsyncManager((WebRequest)request).startDeferredResultProcessing(result, new Object[] { mav });
/*     */     
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<MediaType> getMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
/* 167 */     Collection<MediaType> mediaTypes = (Collection<MediaType>)request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, 0);
/*     */ 
/*     */     
/* 170 */     return CollectionUtils.isEmpty(mediaTypes) ? this.contentNegotiationManager
/* 171 */       .resolveMediaTypes(request) : mediaTypes;
/*     */   }
/*     */   
/*     */   private ResponseBodyEmitter getEmitter(final MediaType mediaType) {
/* 175 */     return new ResponseBodyEmitter(Long.valueOf(-1L))
/*     */       {
/*     */         protected void extendResponse(ServerHttpResponse outputMessage) {
/* 178 */           outputMessage.getHeaders().setContentType(mediaType);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private void logExecutorWarning(MethodParameter returnType) {
/* 185 */     if (this.taskExecutorWarning && logger.isWarnEnabled()) {
/* 186 */       synchronized (this) {
/* 187 */         if (this.taskExecutorWarning) {
/* 188 */           String executorTypeName = this.taskExecutor.getClass().getSimpleName();
/* 189 */           logger.warn("\n!!!\nStreaming through a reactive type requires an Executor to write to the response.\nPlease, configure a TaskExecutor in the MVC config under \"async support\".\nThe " + executorTypeName + " currently in use is not suitable under load.\n-------------------------------\nController:\t" + returnType
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 194 */               .getContainingClass().getName() + "\nMethod:\t\t" + returnType
/* 195 */               .getMethod().getName() + "\nReturning:\t" + 
/* 196 */               ResolvableType.forMethodParameter(returnType).toString() + "\n!!!");
/*     */           
/* 198 */           this.taskExecutorWarning = false;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class AbstractEmitterSubscriber
/*     */     implements Subscriber<Object>, Runnable
/*     */   {
/*     */     private final ResponseBodyEmitter emitter;
/*     */     
/*     */     private final TaskExecutor taskExecutor;
/*     */     
/*     */     @Nullable
/*     */     private Subscription subscription;
/* 214 */     private final AtomicReference<Object> elementRef = new AtomicReference();
/*     */     
/*     */     @Nullable
/*     */     private Throwable error;
/*     */     
/*     */     private volatile boolean terminated;
/*     */     
/* 221 */     private final AtomicLong executing = new AtomicLong();
/*     */     
/*     */     private volatile boolean done;
/*     */     
/*     */     protected AbstractEmitterSubscriber(ResponseBodyEmitter emitter, TaskExecutor executor) {
/* 226 */       this.emitter = emitter;
/* 227 */       this.taskExecutor = executor;
/*     */     }
/*     */     
/*     */     public void connect(ReactiveAdapter adapter, Object returnValue) {
/* 231 */       Publisher<Object> publisher = adapter.toPublisher(returnValue);
/* 232 */       publisher.subscribe(this);
/*     */     }
/*     */     
/*     */     protected ResponseBodyEmitter getEmitter() {
/* 236 */       return this.emitter;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onSubscribe(Subscription subscription) {
/* 241 */       this.subscription = subscription;
/* 242 */       this.emitter.onTimeout(() -> {
/*     */             if (ReactiveTypeHandler.logger.isTraceEnabled()) {
/*     */               ReactiveTypeHandler.logger.trace("Connection timeout for " + this.emitter);
/*     */             }
/*     */             terminate();
/*     */             this.emitter.complete();
/*     */           });
/* 249 */       this.emitter.onError(this.emitter::completeWithError);
/* 250 */       subscription.request(1L);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(Object element) {
/* 255 */       this.elementRef.lazySet(element);
/* 256 */       trySchedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onError(Throwable ex) {
/* 261 */       this.error = ex;
/* 262 */       this.terminated = true;
/* 263 */       trySchedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onComplete() {
/* 268 */       this.terminated = true;
/* 269 */       trySchedule();
/*     */     }
/*     */     
/*     */     private void trySchedule() {
/* 273 */       if (this.executing.getAndIncrement() == 0L) {
/* 274 */         schedule();
/*     */       }
/*     */     }
/*     */     
/*     */     private void schedule() {
/*     */       try {
/* 280 */         this.taskExecutor.execute(this);
/*     */       }
/* 282 */       catch (Throwable ex) {
/*     */         try {
/* 284 */           terminate();
/*     */         } finally {
/*     */           
/* 287 */           this.executing.decrementAndGet();
/* 288 */           this.elementRef.lazySet(null);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 295 */       if (this.done) {
/* 296 */         this.elementRef.lazySet(null);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 301 */       boolean isTerminated = this.terminated;
/*     */       
/* 303 */       Object element = this.elementRef.get();
/* 304 */       if (element != null) {
/* 305 */         this.elementRef.lazySet(null);
/* 306 */         Assert.state((this.subscription != null), "No subscription");
/*     */         try {
/* 308 */           send(element);
/* 309 */           this.subscription.request(1L);
/*     */         }
/* 311 */         catch (Throwable ex) {
/* 312 */           if (ReactiveTypeHandler.logger.isTraceEnabled()) {
/* 313 */             ReactiveTypeHandler.logger.trace("Send for " + this.emitter + " failed: " + ex);
/*     */           }
/* 315 */           terminate();
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 320 */       if (isTerminated) {
/* 321 */         this.done = true;
/* 322 */         Throwable ex = this.error;
/* 323 */         this.error = null;
/* 324 */         if (ex != null) {
/* 325 */           if (ReactiveTypeHandler.logger.isTraceEnabled()) {
/* 326 */             ReactiveTypeHandler.logger.trace("Publisher for " + this.emitter + " failed: " + ex);
/*     */           }
/* 328 */           this.emitter.completeWithError(ex);
/*     */         } else {
/*     */           
/* 331 */           if (ReactiveTypeHandler.logger.isTraceEnabled()) {
/* 332 */             ReactiveTypeHandler.logger.trace("Publisher for " + this.emitter + " completed");
/*     */           }
/* 334 */           this.emitter.complete();
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 339 */       if (this.executing.decrementAndGet() != 0L) {
/* 340 */         schedule();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void terminate() {
/* 347 */       this.done = true;
/* 348 */       if (this.subscription != null)
/* 349 */         this.subscription.cancel(); 
/*     */     }
/*     */     
/*     */     protected abstract void send(Object param1Object) throws IOException;
/*     */   }
/*     */   
/*     */   private static class SseEmitterSubscriber
/*     */     extends AbstractEmitterSubscriber {
/*     */     SseEmitterSubscriber(SseEmitter sseEmitter, TaskExecutor executor) {
/* 358 */       super(sseEmitter, executor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void send(Object element) throws IOException {
/* 363 */       if (element instanceof ServerSentEvent) {
/* 364 */         ServerSentEvent<?> event = (ServerSentEvent)element;
/* 365 */         ((SseEmitter)getEmitter()).send(adapt(event));
/*     */       } else {
/*     */         
/* 368 */         getEmitter().send(element, MediaType.APPLICATION_JSON);
/*     */       } 
/*     */     }
/*     */     
/*     */     private SseEmitter.SseEventBuilder adapt(ServerSentEvent<?> sse) {
/* 373 */       SseEmitter.SseEventBuilder builder = SseEmitter.event();
/* 374 */       String id = sse.id();
/* 375 */       String event = sse.event();
/* 376 */       Duration retry = sse.retry();
/* 377 */       String comment = sse.comment();
/* 378 */       Object data = sse.data();
/* 379 */       if (id != null) {
/* 380 */         builder.id(id);
/*     */       }
/* 382 */       if (event != null) {
/* 383 */         builder.name(event);
/*     */       }
/* 385 */       if (data != null) {
/* 386 */         builder.data(data);
/*     */       }
/* 388 */       if (retry != null) {
/* 389 */         builder.reconnectTime(retry.toMillis());
/*     */       }
/* 391 */       if (comment != null) {
/* 392 */         builder.comment(comment);
/*     */       }
/* 394 */       return builder;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class JsonEmitterSubscriber
/*     */     extends AbstractEmitterSubscriber
/*     */   {
/*     */     JsonEmitterSubscriber(ResponseBodyEmitter emitter, TaskExecutor executor) {
/* 402 */       super(emitter, executor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void send(Object element) throws IOException {
/* 407 */       getEmitter().send(element, MediaType.APPLICATION_JSON);
/* 408 */       getEmitter().send("\n", MediaType.TEXT_PLAIN);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TextEmitterSubscriber
/*     */     extends AbstractEmitterSubscriber
/*     */   {
/*     */     TextEmitterSubscriber(ResponseBodyEmitter emitter, TaskExecutor executor) {
/* 416 */       super(emitter, executor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void send(Object element) throws IOException {
/* 421 */       getEmitter().send(element, MediaType.TEXT_PLAIN);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DeferredResultSubscriber
/*     */     implements Subscriber<Object>
/*     */   {
/*     */     private final DeferredResult<Object> result;
/*     */     
/*     */     private final boolean multiValueSource;
/*     */     private final ReactiveTypeHandler.CollectedValuesList values;
/*     */     
/*     */     DeferredResultSubscriber(DeferredResult<Object> result, ReactiveAdapter adapter, ResolvableType elementType) {
/* 435 */       this.result = result;
/* 436 */       this.multiValueSource = adapter.isMultiValue();
/* 437 */       this.values = new ReactiveTypeHandler.CollectedValuesList(elementType);
/*     */     }
/*     */     
/*     */     public void connect(ReactiveAdapter adapter, Object returnValue) {
/* 441 */       Publisher<Object> publisher = adapter.toPublisher(returnValue);
/* 442 */       publisher.subscribe(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription subscription) {
/* 447 */       this.result.onTimeout(subscription::cancel);
/* 448 */       subscription.request(Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object element) {
/* 453 */       this.values.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/* 458 */       this.result.setErrorResult(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 463 */       if (this.values.size() > 1 || this.multiValueSource) {
/* 464 */         this.result.setResult(this.values);
/*     */       }
/* 466 */       else if (this.values.size() == 1) {
/* 467 */         this.result.setResult(this.values.get(0));
/*     */       } else {
/*     */         
/* 470 */         this.result.setResult(null);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class CollectedValuesList
/*     */     extends ArrayList<Object>
/*     */   {
/*     */     private final ResolvableType elementType;
/*     */ 
/*     */ 
/*     */     
/*     */     CollectedValuesList(ResolvableType elementType) {
/* 485 */       this.elementType = elementType;
/*     */     }
/*     */     
/*     */     public ResolvableType getReturnType() {
/* 489 */       return ResolvableType.forClassWithGenerics(List.class, new ResolvableType[] { this.elementType });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ReactiveTypeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */