/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
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
/*     */ public abstract class ObjectUtils
/*     */ {
/*     */   private static final int INITIAL_HASH = 7;
/*     */   private static final int MULTIPLIER = 31;
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final String NULL_STRING = "null";
/*     */   private static final String ARRAY_START = "{";
/*     */   private static final String ARRAY_END = "}";
/*     */   private static final String EMPTY_ARRAY = "{}";
/*     */   private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
/*     */   
/*     */   public static boolean isCheckedException(Throwable ex) {
/*  68 */     return (!(ex instanceof RuntimeException) && !(ex instanceof Error));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCompatibleWithThrowsClause(Throwable ex, @Nullable Class<?>... declaredExceptions) {
/*  79 */     if (!isCheckedException(ex)) {
/*  80 */       return true;
/*     */     }
/*  82 */     if (declaredExceptions != null) {
/*  83 */       for (Class<?> declaredException : declaredExceptions) {
/*  84 */         if (declaredException.isInstance(ex)) {
/*  85 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isArray(@Nullable Object obj) {
/*  98 */     return (obj != null && obj.getClass().isArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(@Nullable Object[] array) {
/* 108 */     return (array == null || array.length == 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(@Nullable Object obj) {
/* 135 */     if (obj == null) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     if (obj instanceof Optional) {
/* 140 */       return !((Optional)obj).isPresent();
/*     */     }
/* 142 */     if (obj instanceof CharSequence) {
/* 143 */       return (((CharSequence)obj).length() == 0);
/*     */     }
/* 145 */     if (obj.getClass().isArray()) {
/* 146 */       return (Array.getLength(obj) == 0);
/*     */     }
/* 148 */     if (obj instanceof Collection) {
/* 149 */       return ((Collection)obj).isEmpty();
/*     */     }
/* 151 */     if (obj instanceof Map) {
/* 152 */       return ((Map)obj).isEmpty();
/*     */     }
/*     */ 
/*     */     
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Object unwrapOptional(@Nullable Object obj) {
/* 168 */     if (obj instanceof Optional) {
/* 169 */       Optional<?> optional = (Optional)obj;
/* 170 */       if (!optional.isPresent()) {
/* 171 */         return null;
/*     */       }
/* 173 */       Object result = optional.get();
/* 174 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 175 */       return result;
/*     */     } 
/* 177 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsElement(@Nullable Object[] array, Object element) {
/* 188 */     if (array == null) {
/* 189 */       return false;
/*     */     }
/* 191 */     for (Object arrayEle : array) {
/* 192 */       if (nullSafeEquals(arrayEle, element)) {
/* 193 */         return true;
/*     */       }
/*     */     } 
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
/* 207 */     return containsConstant(enumValues, constant, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
/* 218 */     for (Enum<?> candidate : enumValues) {
/* 219 */       if (caseSensitive ? candidate.toString().equals(constant) : candidate
/* 220 */         .toString().equalsIgnoreCase(constant)) {
/* 221 */         return true;
/*     */       }
/*     */     } 
/* 224 */     return false;
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
/*     */   public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
/* 236 */     for (E candidate : enumValues) {
/* 237 */       if (candidate.toString().equalsIgnoreCase(constant)) {
/* 238 */         return candidate;
/*     */       }
/*     */     } 
/* 241 */     throw new IllegalArgumentException("Constant [" + constant + "] does not exist in enum type " + enumValues
/* 242 */         .getClass().getComponentType().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, O extends A> A[] addObjectToArray(@Nullable A[] array, @Nullable O obj) {
/* 253 */     Class<?> compType = Object.class;
/* 254 */     if (array != null) {
/* 255 */       compType = array.getClass().getComponentType();
/*     */     }
/* 257 */     else if (obj != null) {
/* 258 */       compType = obj.getClass();
/*     */     } 
/* 260 */     int newArrLength = (array != null) ? (array.length + 1) : 1;
/*     */     
/* 262 */     A[] newArr = (A[])Array.newInstance(compType, newArrLength);
/* 263 */     if (array != null) {
/* 264 */       System.arraycopy(array, 0, newArr, 0, array.length);
/*     */     }
/* 266 */     newArr[newArr.length - 1] = (A)obj;
/* 267 */     return newArr;
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
/*     */   public static Object[] toObjectArray(@Nullable Object source) {
/* 280 */     if (source instanceof Object[]) {
/* 281 */       return (Object[])source;
/*     */     }
/* 283 */     if (source == null) {
/* 284 */       return new Object[0];
/*     */     }
/* 286 */     if (!source.getClass().isArray()) {
/* 287 */       throw new IllegalArgumentException("Source is not an array: " + source);
/*     */     }
/* 289 */     int length = Array.getLength(source);
/* 290 */     if (length == 0) {
/* 291 */       return new Object[0];
/*     */     }
/* 293 */     Class<?> wrapperType = Array.get(source, 0).getClass();
/* 294 */     Object[] newArray = (Object[])Array.newInstance(wrapperType, length);
/* 295 */     for (int i = 0; i < length; i++) {
/* 296 */       newArray[i] = Array.get(source, i);
/*     */     }
/* 298 */     return newArray;
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
/*     */   
/*     */   public static boolean nullSafeEquals(@Nullable Object o1, @Nullable Object o2) {
/* 318 */     if (o1 == o2) {
/* 319 */       return true;
/*     */     }
/* 321 */     if (o1 == null || o2 == null) {
/* 322 */       return false;
/*     */     }
/* 324 */     if (o1.equals(o2)) {
/* 325 */       return true;
/*     */     }
/* 327 */     if (o1.getClass().isArray() && o2.getClass().isArray()) {
/* 328 */       return arrayEquals(o1, o2);
/*     */     }
/* 330 */     return false;
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
/*     */   private static boolean arrayEquals(Object o1, Object o2) {
/* 343 */     if (o1 instanceof Object[] && o2 instanceof Object[]) {
/* 344 */       return Arrays.equals((Object[])o1, (Object[])o2);
/*     */     }
/* 346 */     if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
/* 347 */       return Arrays.equals((boolean[])o1, (boolean[])o2);
/*     */     }
/* 349 */     if (o1 instanceof byte[] && o2 instanceof byte[]) {
/* 350 */       return Arrays.equals((byte[])o1, (byte[])o2);
/*     */     }
/* 352 */     if (o1 instanceof char[] && o2 instanceof char[]) {
/* 353 */       return Arrays.equals((char[])o1, (char[])o2);
/*     */     }
/* 355 */     if (o1 instanceof double[] && o2 instanceof double[]) {
/* 356 */       return Arrays.equals((double[])o1, (double[])o2);
/*     */     }
/* 358 */     if (o1 instanceof float[] && o2 instanceof float[]) {
/* 359 */       return Arrays.equals((float[])o1, (float[])o2);
/*     */     }
/* 361 */     if (o1 instanceof int[] && o2 instanceof int[]) {
/* 362 */       return Arrays.equals((int[])o1, (int[])o2);
/*     */     }
/* 364 */     if (o1 instanceof long[] && o2 instanceof long[]) {
/* 365 */       return Arrays.equals((long[])o1, (long[])o2);
/*     */     }
/* 367 */     if (o1 instanceof short[] && o2 instanceof short[]) {
/* 368 */       return Arrays.equals((short[])o1, (short[])o2);
/*     */     }
/* 370 */     return false;
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
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable Object obj) {
/* 391 */     if (obj == null) {
/* 392 */       return 0;
/*     */     }
/* 394 */     if (obj.getClass().isArray()) {
/* 395 */       if (obj instanceof Object[]) {
/* 396 */         return nullSafeHashCode((Object[])obj);
/*     */       }
/* 398 */       if (obj instanceof boolean[]) {
/* 399 */         return nullSafeHashCode((boolean[])obj);
/*     */       }
/* 401 */       if (obj instanceof byte[]) {
/* 402 */         return nullSafeHashCode((byte[])obj);
/*     */       }
/* 404 */       if (obj instanceof char[]) {
/* 405 */         return nullSafeHashCode((char[])obj);
/*     */       }
/* 407 */       if (obj instanceof double[]) {
/* 408 */         return nullSafeHashCode((double[])obj);
/*     */       }
/* 410 */       if (obj instanceof float[]) {
/* 411 */         return nullSafeHashCode((float[])obj);
/*     */       }
/* 413 */       if (obj instanceof int[]) {
/* 414 */         return nullSafeHashCode((int[])obj);
/*     */       }
/* 416 */       if (obj instanceof long[]) {
/* 417 */         return nullSafeHashCode((long[])obj);
/*     */       }
/* 419 */       if (obj instanceof short[]) {
/* 420 */         return nullSafeHashCode((short[])obj);
/*     */       }
/*     */     } 
/* 423 */     return obj.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable Object[] array) {
/* 431 */     if (array == null) {
/* 432 */       return 0;
/*     */     }
/* 434 */     int hash = 7;
/* 435 */     for (Object element : array) {
/* 436 */       hash = 31 * hash + nullSafeHashCode(element);
/*     */     }
/* 438 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable boolean[] array) {
/* 446 */     if (array == null) {
/* 447 */       return 0;
/*     */     }
/* 449 */     int hash = 7;
/* 450 */     for (boolean element : array) {
/* 451 */       hash = 31 * hash + Boolean.hashCode(element);
/*     */     }
/* 453 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable byte[] array) {
/* 461 */     if (array == null) {
/* 462 */       return 0;
/*     */     }
/* 464 */     int hash = 7;
/* 465 */     for (byte element : array) {
/* 466 */       hash = 31 * hash + element;
/*     */     }
/* 468 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable char[] array) {
/* 476 */     if (array == null) {
/* 477 */       return 0;
/*     */     }
/* 479 */     int hash = 7;
/* 480 */     for (char element : array) {
/* 481 */       hash = 31 * hash + element;
/*     */     }
/* 483 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable double[] array) {
/* 491 */     if (array == null) {
/* 492 */       return 0;
/*     */     }
/* 494 */     int hash = 7;
/* 495 */     for (double element : array) {
/* 496 */       hash = 31 * hash + Double.hashCode(element);
/*     */     }
/* 498 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable float[] array) {
/* 506 */     if (array == null) {
/* 507 */       return 0;
/*     */     }
/* 509 */     int hash = 7;
/* 510 */     for (float element : array) {
/* 511 */       hash = 31 * hash + Float.hashCode(element);
/*     */     }
/* 513 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable int[] array) {
/* 521 */     if (array == null) {
/* 522 */       return 0;
/*     */     }
/* 524 */     int hash = 7;
/* 525 */     for (int element : array) {
/* 526 */       hash = 31 * hash + element;
/*     */     }
/* 528 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable long[] array) {
/* 536 */     if (array == null) {
/* 537 */       return 0;
/*     */     }
/* 539 */     int hash = 7;
/* 540 */     for (long element : array) {
/* 541 */       hash = 31 * hash + Long.hashCode(element);
/*     */     }
/* 543 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nullSafeHashCode(@Nullable short[] array) {
/* 551 */     if (array == null) {
/* 552 */       return 0;
/*     */     }
/* 554 */     int hash = 7;
/* 555 */     for (short element : array) {
/* 556 */       hash = 31 * hash + element;
/*     */     }
/* 558 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int hashCode(boolean bool) {
/* 567 */     return Boolean.hashCode(bool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int hashCode(double dbl) {
/* 576 */     return Double.hashCode(dbl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int hashCode(float flt) {
/* 585 */     return Float.hashCode(flt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int hashCode(long lng) {
/* 594 */     return Long.hashCode(lng);
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
/*     */   public static String identityToString(@Nullable Object obj) {
/* 609 */     if (obj == null) {
/* 610 */       return "";
/*     */     }
/* 612 */     return obj.getClass().getName() + "@" + getIdentityHexString(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getIdentityHexString(Object obj) {
/* 621 */     return Integer.toHexString(System.identityHashCode(obj));
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
/*     */   public static String getDisplayString(@Nullable Object obj) {
/* 634 */     if (obj == null) {
/* 635 */       return "";
/*     */     }
/* 637 */     return nullSafeToString(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nullSafeClassName(@Nullable Object obj) {
/* 647 */     return (obj != null) ? obj.getClass().getName() : "null";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nullSafeToString(@Nullable Object obj) {
/* 658 */     if (obj == null) {
/* 659 */       return "null";
/*     */     }
/* 661 */     if (obj instanceof String) {
/* 662 */       return (String)obj;
/*     */     }
/* 664 */     if (obj instanceof Object[]) {
/* 665 */       return nullSafeToString((Object[])obj);
/*     */     }
/* 667 */     if (obj instanceof boolean[]) {
/* 668 */       return nullSafeToString((boolean[])obj);
/*     */     }
/* 670 */     if (obj instanceof byte[]) {
/* 671 */       return nullSafeToString((byte[])obj);
/*     */     }
/* 673 */     if (obj instanceof char[]) {
/* 674 */       return nullSafeToString((char[])obj);
/*     */     }
/* 676 */     if (obj instanceof double[]) {
/* 677 */       return nullSafeToString((double[])obj);
/*     */     }
/* 679 */     if (obj instanceof float[]) {
/* 680 */       return nullSafeToString((float[])obj);
/*     */     }
/* 682 */     if (obj instanceof int[]) {
/* 683 */       return nullSafeToString((int[])obj);
/*     */     }
/* 685 */     if (obj instanceof long[]) {
/* 686 */       return nullSafeToString((long[])obj);
/*     */     }
/* 688 */     if (obj instanceof short[]) {
/* 689 */       return nullSafeToString((short[])obj);
/*     */     }
/* 691 */     String str = obj.toString();
/* 692 */     return (str != null) ? str : "";
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
/*     */   public static String nullSafeToString(@Nullable Object[] array) {
/* 705 */     if (array == null) {
/* 706 */       return "null";
/*     */     }
/* 708 */     int length = array.length;
/* 709 */     if (length == 0) {
/* 710 */       return "{}";
/*     */     }
/* 712 */     StringBuilder sb = new StringBuilder();
/* 713 */     for (int i = 0; i < length; i++) {
/* 714 */       if (i == 0) {
/* 715 */         sb.append("{");
/*     */       } else {
/*     */         
/* 718 */         sb.append(", ");
/*     */       } 
/* 720 */       sb.append(String.valueOf(array[i]));
/*     */     } 
/* 722 */     sb.append("}");
/* 723 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable boolean[] array) {
/* 736 */     if (array == null) {
/* 737 */       return "null";
/*     */     }
/* 739 */     int length = array.length;
/* 740 */     if (length == 0) {
/* 741 */       return "{}";
/*     */     }
/* 743 */     StringBuilder sb = new StringBuilder();
/* 744 */     for (int i = 0; i < length; i++) {
/* 745 */       if (i == 0) {
/* 746 */         sb.append("{");
/*     */       } else {
/*     */         
/* 749 */         sb.append(", ");
/*     */       } 
/*     */       
/* 752 */       sb.append(array[i]);
/*     */     } 
/* 754 */     sb.append("}");
/* 755 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable byte[] array) {
/* 768 */     if (array == null) {
/* 769 */       return "null";
/*     */     }
/* 771 */     int length = array.length;
/* 772 */     if (length == 0) {
/* 773 */       return "{}";
/*     */     }
/* 775 */     StringBuilder sb = new StringBuilder();
/* 776 */     for (int i = 0; i < length; i++) {
/* 777 */       if (i == 0) {
/* 778 */         sb.append("{");
/*     */       } else {
/*     */         
/* 781 */         sb.append(", ");
/*     */       } 
/* 783 */       sb.append(array[i]);
/*     */     } 
/* 785 */     sb.append("}");
/* 786 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable char[] array) {
/* 799 */     if (array == null) {
/* 800 */       return "null";
/*     */     }
/* 802 */     int length = array.length;
/* 803 */     if (length == 0) {
/* 804 */       return "{}";
/*     */     }
/* 806 */     StringBuilder sb = new StringBuilder();
/* 807 */     for (int i = 0; i < length; i++) {
/* 808 */       if (i == 0) {
/* 809 */         sb.append("{");
/*     */       } else {
/*     */         
/* 812 */         sb.append(", ");
/*     */       } 
/* 814 */       sb.append("'").append(array[i]).append("'");
/*     */     } 
/* 816 */     sb.append("}");
/* 817 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable double[] array) {
/* 830 */     if (array == null) {
/* 831 */       return "null";
/*     */     }
/* 833 */     int length = array.length;
/* 834 */     if (length == 0) {
/* 835 */       return "{}";
/*     */     }
/* 837 */     StringBuilder sb = new StringBuilder();
/* 838 */     for (int i = 0; i < length; i++) {
/* 839 */       if (i == 0) {
/* 840 */         sb.append("{");
/*     */       } else {
/*     */         
/* 843 */         sb.append(", ");
/*     */       } 
/*     */       
/* 846 */       sb.append(array[i]);
/*     */     } 
/* 848 */     sb.append("}");
/* 849 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable float[] array) {
/* 862 */     if (array == null) {
/* 863 */       return "null";
/*     */     }
/* 865 */     int length = array.length;
/* 866 */     if (length == 0) {
/* 867 */       return "{}";
/*     */     }
/* 869 */     StringBuilder sb = new StringBuilder();
/* 870 */     for (int i = 0; i < length; i++) {
/* 871 */       if (i == 0) {
/* 872 */         sb.append("{");
/*     */       } else {
/*     */         
/* 875 */         sb.append(", ");
/*     */       } 
/*     */       
/* 878 */       sb.append(array[i]);
/*     */     } 
/* 880 */     sb.append("}");
/* 881 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable int[] array) {
/* 894 */     if (array == null) {
/* 895 */       return "null";
/*     */     }
/* 897 */     int length = array.length;
/* 898 */     if (length == 0) {
/* 899 */       return "{}";
/*     */     }
/* 901 */     StringBuilder sb = new StringBuilder();
/* 902 */     for (int i = 0; i < length; i++) {
/* 903 */       if (i == 0) {
/* 904 */         sb.append("{");
/*     */       } else {
/*     */         
/* 907 */         sb.append(", ");
/*     */       } 
/* 909 */       sb.append(array[i]);
/*     */     } 
/* 911 */     sb.append("}");
/* 912 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable long[] array) {
/* 925 */     if (array == null) {
/* 926 */       return "null";
/*     */     }
/* 928 */     int length = array.length;
/* 929 */     if (length == 0) {
/* 930 */       return "{}";
/*     */     }
/* 932 */     StringBuilder sb = new StringBuilder();
/* 933 */     for (int i = 0; i < length; i++) {
/* 934 */       if (i == 0) {
/* 935 */         sb.append("{");
/*     */       } else {
/*     */         
/* 938 */         sb.append(", ");
/*     */       } 
/* 940 */       sb.append(array[i]);
/*     */     } 
/* 942 */     sb.append("}");
/* 943 */     return sb.toString();
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
/*     */   public static String nullSafeToString(@Nullable short[] array) {
/* 956 */     if (array == null) {
/* 957 */       return "null";
/*     */     }
/* 959 */     int length = array.length;
/* 960 */     if (length == 0) {
/* 961 */       return "{}";
/*     */     }
/* 963 */     StringBuilder sb = new StringBuilder();
/* 964 */     for (int i = 0; i < length; i++) {
/* 965 */       if (i == 0) {
/* 966 */         sb.append("{");
/*     */       } else {
/*     */         
/* 969 */         sb.append(", ");
/*     */       } 
/* 971 */       sb.append(array[i]);
/*     */     } 
/* 973 */     sb.append("}");
/* 974 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/ObjectUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */