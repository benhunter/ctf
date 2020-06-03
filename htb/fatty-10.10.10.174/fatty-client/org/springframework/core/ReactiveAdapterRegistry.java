/*     */ package org.springframework.core;
/*     */ 
/*     */ import io.reactivex.BackpressureStrategy;
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Single;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ import rx.Completable;
/*     */ import rx.Observable;
/*     */ import rx.RxReactiveStreams;
/*     */ import rx.Single;
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
/*     */ public class ReactiveAdapterRegistry
/*     */ {
/*     */   @Nullable
/*     */   private static volatile ReactiveAdapterRegistry sharedInstance;
/*     */   private final boolean reactorPresent;
/*  57 */   private final List<ReactiveAdapter> adapters = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReactiveAdapterRegistry() {
/*  65 */     ClassLoader classLoader = ReactiveAdapterRegistry.class.getClassLoader();
/*     */ 
/*     */     
/*  68 */     boolean reactorRegistered = false;
/*  69 */     if (ClassUtils.isPresent("reactor.core.publisher.Flux", classLoader)) {
/*  70 */       (new ReactorRegistrar()).registerAdapters(this);
/*  71 */       reactorRegistered = true;
/*     */     } 
/*  73 */     this.reactorPresent = reactorRegistered;
/*     */ 
/*     */     
/*  76 */     if (ClassUtils.isPresent("rx.Observable", classLoader) && 
/*  77 */       ClassUtils.isPresent("rx.RxReactiveStreams", classLoader)) {
/*  78 */       (new RxJava1Registrar()).registerAdapters(this);
/*     */     }
/*     */ 
/*     */     
/*  82 */     if (ClassUtils.isPresent("io.reactivex.Flowable", classLoader)) {
/*  83 */       (new RxJava2Registrar()).registerAdapters(this);
/*     */     }
/*     */ 
/*     */     
/*  87 */     if (ClassUtils.isPresent("java.util.concurrent.Flow.Publisher", classLoader)) {
/*  88 */       (new ReactorJdkFlowAdapterRegistrar()).registerAdapter(this);
/*     */     }
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
/*     */   public boolean hasAdapters() {
/* 101 */     return !this.adapters.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerReactiveType(ReactiveTypeDescriptor descriptor, Function<Object, Publisher<?>> toAdapter, Function<Publisher<?>, Object> fromAdapter) {
/* 112 */     if (this.reactorPresent) {
/* 113 */       this.adapters.add(new ReactorAdapter(descriptor, toAdapter, fromAdapter));
/*     */     } else {
/*     */       
/* 116 */       this.adapters.add(new ReactiveAdapter(descriptor, toAdapter, fromAdapter));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ReactiveAdapter getAdapter(Class<?> reactiveType) {
/* 126 */     return getAdapter(reactiveType, null);
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
/*     */   public ReactiveAdapter getAdapter(@Nullable Class<?> reactiveType, @Nullable Object source) {
/* 140 */     if (this.adapters.isEmpty()) {
/* 141 */       return null;
/*     */     }
/*     */     
/* 144 */     Object sourceToUse = (source instanceof Optional) ? ((Optional)source).orElse(null) : source;
/* 145 */     Class<?> clazz = (sourceToUse != null) ? sourceToUse.getClass() : reactiveType;
/* 146 */     if (clazz == null) {
/* 147 */       return null;
/*     */     }
/* 149 */     for (ReactiveAdapter adapter : this.adapters) {
/* 150 */       if (adapter.getReactiveType() == clazz) {
/* 151 */         return adapter;
/*     */       }
/*     */     } 
/* 154 */     for (ReactiveAdapter adapter : this.adapters) {
/* 155 */       if (adapter.getReactiveType().isAssignableFrom(clazz)) {
/* 156 */         return adapter;
/*     */       }
/*     */     } 
/* 159 */     return null;
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
/*     */   public static ReactiveAdapterRegistry getSharedInstance() {
/* 174 */     ReactiveAdapterRegistry registry = sharedInstance;
/* 175 */     if (registry == null) {
/* 176 */       synchronized (ReactiveAdapterRegistry.class) {
/* 177 */         registry = sharedInstance;
/* 178 */         if (registry == null) {
/* 179 */           registry = new ReactiveAdapterRegistry();
/* 180 */           sharedInstance = registry;
/*     */         } 
/*     */       } 
/*     */     }
/* 184 */     return registry;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReactorRegistrar
/*     */   {
/*     */     private ReactorRegistrar() {}
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 193 */       registry.registerReactiveType(
/* 194 */           ReactiveTypeDescriptor.singleOptionalValue(Mono.class, Mono::empty), source -> (Publisher)source, Mono::from);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 199 */       registry.registerReactiveType(
/* 200 */           ReactiveTypeDescriptor.multiValue(Flux.class, Flux::empty), source -> (Publisher)source, Flux::from);
/*     */ 
/*     */ 
/*     */       
/* 204 */       registry.registerReactiveType(
/* 205 */           ReactiveTypeDescriptor.multiValue(Publisher.class, Flux::empty), source -> (Publisher)source, source -> source);
/*     */ 
/*     */ 
/*     */       
/* 209 */       registry.registerReactiveType(
/* 210 */           ReactiveTypeDescriptor.singleOptionalValue(CompletableFuture.class, () -> {
/*     */               CompletableFuture<?> empty = new CompletableFuture();
/*     */               empty.complete(null);
/*     */               return empty;
/*     */             }), source -> Mono.fromFuture((CompletableFuture)source), source -> Mono.from(source).toFuture());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RxJava1Registrar
/*     */   {
/*     */     private RxJava1Registrar() {}
/*     */ 
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 225 */       registry.registerReactiveType(
/* 226 */           ReactiveTypeDescriptor.multiValue(Observable.class, Observable::empty), source -> RxReactiveStreams.toPublisher((Observable)source), RxReactiveStreams::toObservable);
/*     */ 
/*     */ 
/*     */       
/* 230 */       registry.registerReactiveType(
/* 231 */           ReactiveTypeDescriptor.singleRequiredValue(Single.class), source -> RxReactiveStreams.toPublisher((Single)source), RxReactiveStreams::toSingle);
/*     */ 
/*     */ 
/*     */       
/* 235 */       registry.registerReactiveType(
/* 236 */           ReactiveTypeDescriptor.noValue(Completable.class, Completable::complete), source -> RxReactiveStreams.toPublisher((Completable)source), RxReactiveStreams::toCompletable);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RxJava2Registrar
/*     */   {
/*     */     private RxJava2Registrar() {}
/*     */ 
/*     */     
/*     */     void registerAdapters(ReactiveAdapterRegistry registry) {
/* 247 */       registry.registerReactiveType(
/* 248 */           ReactiveTypeDescriptor.multiValue(Flowable.class, Flowable::empty), source -> (Publisher)source, Flowable::fromPublisher);
/*     */ 
/*     */ 
/*     */       
/* 252 */       registry.registerReactiveType(
/* 253 */           ReactiveTypeDescriptor.multiValue(Observable.class, Observable::empty), source -> ((Observable)source).toFlowable(BackpressureStrategy.BUFFER), source -> Flowable.fromPublisher(source).toObservable());
/*     */ 
/*     */ 
/*     */       
/* 257 */       registry.registerReactiveType(
/* 258 */           ReactiveTypeDescriptor.singleRequiredValue(Single.class), source -> ((Single)source).toFlowable(), source -> Flowable.fromPublisher(source).toObservable().singleElement().toSingle());
/*     */ 
/*     */ 
/*     */       
/* 262 */       registry.registerReactiveType(
/* 263 */           ReactiveTypeDescriptor.singleOptionalValue(Maybe.class, Maybe::empty), source -> ((Maybe)source).toFlowable(), source -> Flowable.fromPublisher(source).toObservable().singleElement());
/*     */ 
/*     */ 
/*     */       
/* 267 */       registry.registerReactiveType(
/* 268 */           ReactiveTypeDescriptor.noValue(Completable.class, Completable::complete), source -> ((Completable)source).toFlowable(), source -> Flowable.fromPublisher(source).toObservable().ignoreElements());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReactorJdkFlowAdapterRegistrar
/*     */   {
/*     */     private ReactorJdkFlowAdapterRegistrar() {}
/*     */ 
/*     */ 
/*     */     
/*     */     void registerAdapter(ReactiveAdapterRegistry registry) {
/*     */       try {
/* 282 */         String publisherName = "java.util.concurrent.Flow.Publisher";
/* 283 */         Class<?> publisherClass = ClassUtils.forName(publisherName, getClass().getClassLoader());
/*     */         
/* 285 */         String adapterName = "reactor.adapter.JdkFlowAdapter";
/* 286 */         Class<?> flowAdapterClass = ClassUtils.forName(adapterName, getClass().getClassLoader());
/*     */         
/* 288 */         Method toFluxMethod = flowAdapterClass.getMethod("flowPublisherToFlux", new Class[] { publisherClass });
/* 289 */         Method toFlowMethod = flowAdapterClass.getMethod("publisherToFlowPublisher", new Class[] { Publisher.class });
/* 290 */         Object emptyFlow = ReflectionUtils.invokeMethod(toFlowMethod, null, new Object[] { Flux.empty() });
/*     */         
/* 292 */         registry.registerReactiveType(
/* 293 */             ReactiveTypeDescriptor.multiValue(publisherClass, () -> emptyFlow), source -> (Publisher)ReflectionUtils.invokeMethod(toFluxMethod, null, new Object[] { source }), publisher -> ReflectionUtils.invokeMethod(toFlowMethod, null, new Object[] { publisher }));
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 298 */       catch (Throwable throwable) {}
/*     */     }
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
/*     */   private static class ReactorAdapter
/*     */     extends ReactiveAdapter
/*     */   {
/*     */     ReactorAdapter(ReactiveTypeDescriptor descriptor, Function<Object, Publisher<?>> toPublisherFunction, Function<Publisher<?>, Object> fromPublisherFunction) {
/* 317 */       super(descriptor, toPublisherFunction, fromPublisherFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Publisher<T> toPublisher(@Nullable Object source) {
/* 322 */       Publisher<T> publisher = super.toPublisher(source);
/* 323 */       return isMultiValue() ? (Publisher<T>)Flux.from(publisher) : (Publisher<T>)Mono.from(publisher);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/ReactiveAdapterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */