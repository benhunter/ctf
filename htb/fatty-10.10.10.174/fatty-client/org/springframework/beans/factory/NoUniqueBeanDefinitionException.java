/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoUniqueBeanDefinitionException
/*     */   extends NoSuchBeanDefinitionException
/*     */ {
/*     */   private final int numberOfBeansFound;
/*     */   @Nullable
/*     */   private final Collection<String> beanNamesFound;
/*     */   
/*     */   public NoUniqueBeanDefinitionException(Class<?> type, int numberOfBeansFound, String message) {
/*  50 */     super(type, message);
/*  51 */     this.numberOfBeansFound = numberOfBeansFound;
/*  52 */     this.beanNamesFound = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoUniqueBeanDefinitionException(Class<?> type, Collection<String> beanNamesFound) {
/*  61 */     super(type, "expected single matching bean but found " + beanNamesFound.size() + ": " + 
/*  62 */         StringUtils.collectionToCommaDelimitedString(beanNamesFound));
/*  63 */     this.numberOfBeansFound = beanNamesFound.size();
/*  64 */     this.beanNamesFound = beanNamesFound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoUniqueBeanDefinitionException(Class<?> type, String... beanNamesFound) {
/*  73 */     this(type, Arrays.asList(beanNamesFound));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoUniqueBeanDefinitionException(ResolvableType type, Collection<String> beanNamesFound) {
/*  83 */     super(type, "expected single matching bean but found " + beanNamesFound.size() + ": " + 
/*  84 */         StringUtils.collectionToCommaDelimitedString(beanNamesFound));
/*  85 */     this.numberOfBeansFound = beanNamesFound.size();
/*  86 */     this.beanNamesFound = beanNamesFound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoUniqueBeanDefinitionException(ResolvableType type, String... beanNamesFound) {
/*  96 */     this(type, Arrays.asList(beanNamesFound));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfBeansFound() {
/* 107 */     return this.numberOfBeansFound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Collection<String> getBeanNamesFound() {
/* 118 */     return this.beanNamesFound;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/NoUniqueBeanDefinitionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */