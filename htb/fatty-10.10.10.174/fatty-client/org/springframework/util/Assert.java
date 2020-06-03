/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Assert
/*     */ {
/*     */   public static void state(boolean expression, String message) {
/*  72 */     if (!expression) {
/*  73 */       throw new IllegalStateException(message);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void state(boolean expression, Supplier<String> messageSupplier) {
/*  93 */     if (!expression) {
/*  94 */       throw new IllegalStateException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void state(boolean expression) {
/* 105 */     state(expression, "[Assertion failed] - this state invariant must be true");
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
/*     */   public static void isTrue(boolean expression, String message) {
/* 117 */     if (!expression) {
/* 118 */       throw new IllegalArgumentException(message);
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
/*     */   
/*     */   public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
/* 135 */     if (!expression) {
/* 136 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void isTrue(boolean expression) {
/* 147 */     isTrue(expression, "[Assertion failed] - this expression must be true");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isNull(@Nullable Object object, String message) {
/* 158 */     if (object != null) {
/* 159 */       throw new IllegalArgumentException(message);
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
/*     */   public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
/* 175 */     if (object != null) {
/* 176 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void isNull(@Nullable Object object) {
/* 186 */     isNull(object, "[Assertion failed] - the object argument must be null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notNull(@Nullable Object object, String message) {
/* 197 */     if (object == null) {
/* 198 */       throw new IllegalArgumentException(message);
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
/*     */   public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
/* 214 */     if (object == null) {
/* 215 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notNull(@Nullable Object object) {
/* 225 */     notNull(object, "[Assertion failed] - this argument is required; it must not be null");
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
/*     */   public static void hasLength(@Nullable String text, String message) {
/* 238 */     if (!StringUtils.hasLength(text)) {
/* 239 */       throw new IllegalArgumentException(message);
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
/*     */ 
/*     */   
/*     */   public static void hasLength(@Nullable String text, Supplier<String> messageSupplier) {
/* 257 */     if (!StringUtils.hasLength(text)) {
/* 258 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hasLength(@Nullable String text) {
/* 269 */     hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
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
/*     */   public static void hasText(@Nullable String text, String message) {
/* 283 */     if (!StringUtils.hasText(text)) {
/* 284 */       throw new IllegalArgumentException(message);
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
/*     */ 
/*     */   
/*     */   public static void hasText(@Nullable String text, Supplier<String> messageSupplier) {
/* 302 */     if (!StringUtils.hasText(text)) {
/* 303 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hasText(@Nullable String text) {
/* 314 */     hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
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
/*     */   public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
/* 327 */     if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch
/* 328 */       .contains(substring)) {
/* 329 */       throw new IllegalArgumentException(message);
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
/*     */   
/*     */   public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
/* 346 */     if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch
/* 347 */       .contains(substring)) {
/* 348 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void doesNotContain(@Nullable String textToSearch, String substring) {
/* 358 */     doesNotContain(textToSearch, substring, () -> "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
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
/*     */   public static void notEmpty(@Nullable Object[] array, String message) {
/* 371 */     if (ObjectUtils.isEmpty(array)) {
/* 372 */       throw new IllegalArgumentException(message);
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
/*     */   
/*     */   public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
/* 389 */     if (ObjectUtils.isEmpty(array)) {
/* 390 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(@Nullable Object[] array) {
/* 401 */     notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
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
/*     */   public static void noNullElements(@Nullable Object[] array, String message) {
/* 413 */     if (array != null) {
/* 414 */       for (Object element : array) {
/* 415 */         if (element == null) {
/* 416 */           throw new IllegalArgumentException(message);
/*     */         }
/*     */       } 
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
/*     */   
/*     */   public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
/* 435 */     if (array != null) {
/* 436 */       for (Object element : array) {
/* 437 */         if (element == null) {
/* 438 */           throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void noNullElements(@Nullable Object[] array) {
/* 450 */     noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
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
/*     */   public static void notEmpty(@Nullable Collection<?> collection, String message) {
/* 463 */     if (CollectionUtils.isEmpty(collection)) {
/* 464 */       throw new IllegalArgumentException(message);
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
/*     */ 
/*     */   
/*     */   public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
/* 482 */     if (CollectionUtils.isEmpty(collection)) {
/* 483 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(@Nullable Collection<?> collection) {
/* 494 */     notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
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
/*     */   public static void notEmpty(@Nullable Map<?, ?> map, String message) {
/* 507 */     if (CollectionUtils.isEmpty(map)) {
/* 508 */       throw new IllegalArgumentException(message);
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
/*     */   
/*     */   public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
/* 525 */     if (CollectionUtils.isEmpty(map)) {
/* 526 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(@Nullable Map<?, ?> map) {
/* 537 */     notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
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
/*     */   
/*     */   public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message) {
/* 553 */     notNull(type, "Type to check against must not be null");
/* 554 */     if (!type.isInstance(obj)) {
/* 555 */       instanceCheckFailed(type, obj, message);
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
/*     */   
/*     */   public static void isInstanceOf(Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
/* 572 */     notNull(type, "Type to check against must not be null");
/* 573 */     if (!type.isInstance(obj)) {
/* 574 */       instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
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
/*     */   public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
/* 586 */     isInstanceOf(type, obj, "");
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
/*     */   
/*     */   public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, String message) {
/* 602 */     notNull(superType, "Super type to check against must not be null");
/* 603 */     if (subType == null || !superType.isAssignableFrom(subType)) {
/* 604 */       assignableCheckFailed(superType, subType, message);
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
/*     */   
/*     */   public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
/* 621 */     notNull(superType, "Super type to check against must not be null");
/* 622 */     if (subType == null || !superType.isAssignableFrom(subType)) {
/* 623 */       assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
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
/*     */   public static void isAssignable(Class<?> superType, Class<?> subType) {
/* 635 */     isAssignable(superType, subType, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String msg) {
/* 640 */     String className = (obj != null) ? obj.getClass().getName() : "null";
/* 641 */     String result = "";
/* 642 */     boolean defaultMessage = true;
/* 643 */     if (StringUtils.hasLength(msg)) {
/* 644 */       if (endsWithSeparator(msg)) {
/* 645 */         result = msg + " ";
/*     */       } else {
/*     */         
/* 648 */         result = messageWithTypeName(msg, className);
/* 649 */         defaultMessage = false;
/*     */       } 
/*     */     }
/* 652 */     if (defaultMessage) {
/* 653 */       result = result + "Object of class [" + className + "] must be an instance of " + type;
/*     */     }
/* 655 */     throw new IllegalArgumentException(result);
/*     */   }
/*     */   
/*     */   private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String msg) {
/* 659 */     String result = "";
/* 660 */     boolean defaultMessage = true;
/* 661 */     if (StringUtils.hasLength(msg)) {
/* 662 */       if (endsWithSeparator(msg)) {
/* 663 */         result = msg + " ";
/*     */       } else {
/*     */         
/* 666 */         result = messageWithTypeName(msg, subType);
/* 667 */         defaultMessage = false;
/*     */       } 
/*     */     }
/* 670 */     if (defaultMessage) {
/* 671 */       result = result + subType + " is not assignable to " + superType;
/*     */     }
/* 673 */     throw new IllegalArgumentException(result);
/*     */   }
/*     */   
/*     */   private static boolean endsWithSeparator(String msg) {
/* 677 */     return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
/*     */   }
/*     */   
/*     */   private static String messageWithTypeName(String msg, @Nullable Object typeName) {
/* 681 */     return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
/* 686 */     return (messageSupplier != null) ? messageSupplier.get() : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/Assert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */