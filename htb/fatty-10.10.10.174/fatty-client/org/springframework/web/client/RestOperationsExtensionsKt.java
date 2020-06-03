/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import kotlin.Metadata;
/*     */ import kotlin.jvm.internal.Intrinsics;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.RequestEntity;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 2, d1 = {"\000:\n\000\n\002\030\002\n\000\n\002\020\000\n\002\030\002\n\000\n\002\030\002\n\000\n\002\030\002\n\000\n\002\030\002\n\002\020\016\n\000\n\002\020\021\n\000\n\002\020$\n\002\030\002\n\002\b\017\032?\020\000\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0062\006\020\007\032\0020\b2\016\b\002\020\t\032\b\022\002\b\003\030\0010\nH\b\032X\020\000\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\006\020\007\032\0020\b2\016\b\002\020\t\032\b\022\002\b\003\030\0010\n2\022\020\f\032\n\022\006\b\001\022\0020\0030\r\"\0020\003H\b¢\006\002\020\016\032Q\020\000\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\006\020\007\032\0020\b2\016\b\002\020\t\032\b\022\002\b\003\030\0010\n2\020\020\f\032\f\022\004\022\0020\013\022\002\b\0030\017H\b\032+\020\000\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\n\020\t\032\006\022\002\b\0030\020H\b\032'\020\021\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\006H\b\032@\020\021\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\022\020\f\032\n\022\006\b\001\022\0020\0030\r\"\0020\003H\b¢\006\002\020\022\0329\020\021\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\020\020\f\032\f\022\004\022\0020\013\022\002\b\0030\017H\b\032(\020\023\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\006H\b¢\006\002\020\024\032<\020\023\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\022\020\f\032\n\022\006\b\001\022\0020\0030\r\"\0020\003H\b¢\006\002\020\025\032>\020\023\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\024\020\f\032\020\022\004\022\0020\013\022\006\022\004\030\0010\0030\017H\b¢\006\002\020\026\0324\020\027\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0062\n\b\002\020\030\032\004\030\0010\003H\b¢\006\002\020\031\032H\020\027\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\n\b\002\020\030\032\004\030\0010\0032\022\020\f\032\n\022\006\b\001\022\0020\0030\r\"\0020\003H\b¢\006\002\020\032\032F\020\027\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\n\b\002\020\030\032\004\030\0010\0032\020\020\f\032\f\022\004\022\0020\013\022\002\b\0030\017H\b¢\006\002\020\033\0323\020\034\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0062\n\b\002\020\030\032\004\030\0010\003H\b\032L\020\034\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\n\b\002\020\030\032\004\030\0010\0032\022\020\f\032\n\022\006\b\001\022\0020\0030\r\"\0020\003H\b¢\006\002\020\035\032E\020\034\032\b\022\004\022\002H\0020\001\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\n\b\002\020\030\032\004\030\0010\0032\020\020\f\032\f\022\004\022\0020\013\022\002\b\0030\017H\b\0324\020\036\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0062\n\b\002\020\030\032\004\030\0010\003H\b¢\006\002\020\031\032H\020\036\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\n\b\002\020\030\032\004\030\0010\0032\022\020\f\032\n\022\006\b\001\022\0020\0030\r\"\0020\003H\b¢\006\002\020\032\032F\020\036\032\004\030\001H\002\"\n\b\000\020\002\030\001*\0020\003*\0020\0042\006\020\005\032\0020\0132\n\b\002\020\030\032\004\030\0010\0032\020\020\f\032\f\022\004\022\0020\013\022\002\b\0030\017H\b¢\006\002\020\033¨\006\037"}, d2 = {"exchange", "Lorg/springframework/http/ResponseEntity;", "T", "", "Lorg/springframework/web/client/RestOperations;", "url", "Ljava/net/URI;", "method", "Lorg/springframework/http/HttpMethod;", "requestEntity", "Lorg/springframework/http/HttpEntity;", "", "uriVariables", "", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Lorg/springframework/http/HttpMethod;Lorg/springframework/http/HttpEntity;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity;", "", "Lorg/springframework/http/RequestEntity;", "getForEntity", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity;", "getForObject", "(Lorg/springframework/web/client/RestOperations;Ljava/net/URI;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/util/Map;)Ljava/lang/Object;", "patchForObject", "request", "(Lorg/springframework/web/client/RestOperations;Ljava/net/URI;Ljava/lang/Object;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/lang/Object;Ljava/util/Map;)Ljava/lang/Object;", "postForEntity", "(Lorg/springframework/web/client/RestOperations;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity;", "postForObject", "spring-web"})
/*     */ public final class RestOperationsExtensionsKt
/*     */ {
/*     */   private static final <T> T getForObject(@NotNull RestOperations $receiver, String url, Object... uriVariables) throws RestClientException {
/*  38 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.getForObject(url, (Class)Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
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
/*     */   private static final <T> T getForObject(@NotNull RestOperations $receiver, String url, Map<String, ?> uriVariables) throws RestClientException {
/*  52 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.getForObject(url, (Class)Object.class, uriVariables);
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
/*     */   private static final <T> T getForObject(@NotNull RestOperations $receiver, URI url) throws RestClientException {
/*  66 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.getForObject(url, (Class)Object.class);
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
/*     */   private static final <T> ResponseEntity<T> getForEntity(@NotNull RestOperations $receiver, URI url) throws RestClientException {
/*  79 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.getForEntity(url, Object.class), "getForEntity(url, T::class.java)"); return $receiver.getForEntity(url, Object.class);
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
/*     */   private static final <T> ResponseEntity<T> getForEntity(@NotNull RestOperations $receiver, String url, Object... uriVariables) throws RestClientException {
/*  93 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.getForEntity(url, Object.class, Arrays.copyOf(uriVariables, uriVariables.length)), "getForEntity(url, T::class.java, *uriVariables)"); return $receiver.getForEntity(url, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
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
/*     */   private static final <T> ResponseEntity<T> getForEntity(@NotNull RestOperations $receiver, String url, Map<String, ?> uriVariables) throws RestClientException {
/* 106 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.getForEntity(url, Object.class, uriVariables), "getForEntity(url, T::class.java, uriVariables)"); return $receiver.getForEntity(url, Object.class, uriVariables);
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
/*     */   private static final <T> T patchForObject(@NotNull RestOperations $receiver, String url, Object request, Object... uriVariables) throws RestClientException {
/* 120 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.patchForObject(url, request, (Class)Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
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
/*     */   private static final <T> T patchForObject(@NotNull RestOperations $receiver, String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
/* 134 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.patchForObject(url, request, (Class)Object.class, uriVariables);
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
/*     */   private static final <T> T patchForObject(@NotNull RestOperations $receiver, URI url, Object request) throws RestClientException {
/* 147 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.patchForObject(url, request, (Class)Object.class);
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
/*     */   private static final <T> T postForObject(@NotNull RestOperations $receiver, String url, Object request, Object... uriVariables) throws RestClientException {
/* 162 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.postForObject(url, request, (Class)Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
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
/*     */   private static final <T> T postForObject(@NotNull RestOperations $receiver, String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
/* 177 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.postForObject(url, request, (Class)Object.class, uriVariables);
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
/*     */   private static final <T> T postForObject(@NotNull RestOperations $receiver, URI url, Object request) throws RestClientException {
/* 191 */     Intrinsics.reifiedOperationMarker(4, "T"); return $receiver.postForObject(url, request, (Class)Object.class);
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
/*     */   private static final <T> ResponseEntity<T> postForEntity(@NotNull RestOperations $receiver, String url, Object request, Object... uriVariables) throws RestClientException {
/* 206 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.postForEntity(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length)), "postForEntity(url, reque…lass.java, *uriVariables)"); return $receiver.postForEntity(url, request, Object.class, Arrays.copyOf(uriVariables, uriVariables.length));
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
/*     */   private static final <T> ResponseEntity<T> postForEntity(@NotNull RestOperations $receiver, String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
/* 221 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.postForEntity(url, request, Object.class, uriVariables), "postForEntity(url, reque…class.java, uriVariables)"); return $receiver.postForEntity(url, request, Object.class, uriVariables);
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
/*     */   private static final <T> ResponseEntity<T> postForEntity(@NotNull RestOperations $receiver, URI url, Object request) throws RestClientException {
/* 235 */     Intrinsics.reifiedOperationMarker(4, "T"); Intrinsics.checkExpressionValueIsNotNull($receiver.postForEntity(url, request, Object.class), "postForEntity(url, request, T::class.java)"); return $receiver.postForEntity(url, request, Object.class);
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
/*     */   private static final <T> ResponseEntity<T> exchange(@NotNull RestOperations $receiver, String url, HttpMethod method, HttpEntity<?> requestEntity, Object... uriVariables) throws RestClientException {
/* 249 */     Intrinsics.needClassReification(); Intrinsics.checkExpressionValueIsNotNull($receiver.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$1(), Arrays.copyOf(uriVariables, uriVariables.length)), "exchange(url, method, re…e<T>() {}, *uriVariables)"); return (ResponseEntity)$receiver.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$1(), Arrays.copyOf(uriVariables, uriVariables.length));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\013\n\000\n\002\030\002\n\000*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001¨\006\002"}, d2 = {"org/springframework/web/client/RestOperationsExtensionsKt$exchange$1", "Lorg/springframework/core/ParameterizedTypeReference;", "spring-web"})
/*     */   public static final class RestOperationsExtensionsKt$exchange$1
/*     */     extends ParameterizedTypeReference<T> {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final <T> ResponseEntity<T> exchange(@NotNull RestOperations $receiver, String url, HttpMethod method, HttpEntity<?> requestEntity, Map<String, ?> uriVariables) throws RestClientException {
/* 263 */     Intrinsics.needClassReification(); Intrinsics.checkExpressionValueIsNotNull($receiver.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$2(), uriVariables), "exchange(url, method, re…ce<T>() {}, uriVariables)"); return (ResponseEntity)$receiver.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$2(), uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\013\n\000\n\002\030\002\n\000*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001¨\006\002"}, d2 = {"org/springframework/web/client/RestOperationsExtensionsKt$exchange$2", "Lorg/springframework/core/ParameterizedTypeReference;", "spring-web"})
/*     */   public static final class RestOperationsExtensionsKt$exchange$2
/*     */     extends ParameterizedTypeReference<T> {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final <T> ResponseEntity<T> exchange(@NotNull RestOperations $receiver, URI url, HttpMethod method, HttpEntity<?> requestEntity) throws RestClientException {
/* 277 */     Intrinsics.needClassReification(); Intrinsics.checkExpressionValueIsNotNull($receiver.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$3()), "exchange(url, method, re…zedTypeReference<T>() {})"); return (ResponseEntity)$receiver.exchange(url, method, requestEntity, new RestOperationsExtensionsKt$exchange$3());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\013\n\000\n\002\030\002\n\000*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001¨\006\002"}, d2 = {"org/springframework/web/client/RestOperationsExtensionsKt$exchange$3", "Lorg/springframework/core/ParameterizedTypeReference;", "spring-web"})
/*     */   public static final class RestOperationsExtensionsKt$exchange$3
/*     */     extends ParameterizedTypeReference<T> {}
/*     */ 
/*     */ 
/*     */   
/*     */   private static final <T> ResponseEntity<T> exchange(@NotNull RestOperations $receiver, RequestEntity<?> requestEntity) throws RestClientException {
/* 290 */     Intrinsics.needClassReification(); Intrinsics.checkExpressionValueIsNotNull($receiver.exchange(requestEntity, new RestOperationsExtensionsKt$exchange$4()), "exchange(requestEntity, …zedTypeReference<T>() {})"); return (ResponseEntity)$receiver.exchange(requestEntity, new RestOperationsExtensionsKt$exchange$4());
/*     */   }
/*     */   
/*     */   @Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\000\013\n\000\n\002\030\002\n\000*\001\000\b\n\030\0002\b\022\004\022\0028\0000\001¨\006\002"}, d2 = {"org/springframework/web/client/RestOperationsExtensionsKt$exchange$4", "Lorg/springframework/core/ParameterizedTypeReference;", "spring-web"})
/*     */   public static final class RestOperationsExtensionsKt$exchange$4 extends ParameterizedTypeReference<T> {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/RestOperationsExtensionsKt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */