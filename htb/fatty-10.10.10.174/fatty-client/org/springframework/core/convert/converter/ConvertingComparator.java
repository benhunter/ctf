/*     */ package org.springframework.core.convert.converter;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.comparator.Comparators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConvertingComparator<S, T>
/*     */   implements Comparator<S>
/*     */ {
/*     */   private final Comparator<T> comparator;
/*     */   private final Converter<S, T> converter;
/*     */   
/*     */   public ConvertingComparator(Converter<S, T> converter) {
/*  49 */     this(Comparators.comparable(), converter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConvertingComparator(Comparator<T> comparator, Converter<S, T> converter) {
/*  58 */     Assert.notNull(comparator, "Comparator must not be null");
/*  59 */     Assert.notNull(converter, "Converter must not be null");
/*  60 */     this.comparator = comparator;
/*  61 */     this.converter = converter;
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
/*     */   public ConvertingComparator(Comparator<T> comparator, ConversionService conversionService, Class<? extends T> targetType) {
/*  73 */     this(comparator, new ConversionServiceConverter<>(conversionService, targetType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(S o1, S o2) {
/*  79 */     T c1 = this.converter.convert(o1);
/*  80 */     T c2 = this.converter.convert(o2);
/*  81 */     return this.comparator.compare(c1, c2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ConvertingComparator<Map.Entry<K, V>, K> mapEntryKeys(Comparator<K> comparator) {
/*  91 */     return new ConvertingComparator<>(comparator, Map.Entry::getKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ConvertingComparator<Map.Entry<K, V>, V> mapEntryValues(Comparator<V> comparator) {
/* 101 */     return new ConvertingComparator<>(comparator, Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConversionServiceConverter<S, T>
/*     */     implements Converter<S, T>
/*     */   {
/*     */     private final ConversionService conversionService;
/*     */ 
/*     */     
/*     */     private final Class<? extends T> targetType;
/*     */ 
/*     */     
/*     */     public ConversionServiceConverter(ConversionService conversionService, Class<? extends T> targetType) {
/* 116 */       Assert.notNull(conversionService, "ConversionService must not be null");
/* 117 */       Assert.notNull(targetType, "TargetType must not be null");
/* 118 */       this.conversionService = conversionService;
/* 119 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T convert(S source) {
/* 125 */       return (T)this.conversionService.convert(source, this.targetType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/converter/ConvertingComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */