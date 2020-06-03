/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanWrapperImpl
/*     */   extends AbstractNestablePropertyAccessor
/*     */   implements BeanWrapper
/*     */ {
/*     */   @Nullable
/*     */   private CachedIntrospectionResults cachedIntrospectionResults;
/*     */   @Nullable
/*     */   private AccessControlContext acc;
/*     */   
/*     */   public BeanWrapperImpl() {
/*  85 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(boolean registerDefaultEditors) {
/*  95 */     super(registerDefaultEditors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(Object object) {
/* 103 */     super(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(Class<?> clazz) {
/* 111 */     super(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWrapperImpl(Object object, String nestedPath, Object rootObject) {
/* 122 */     super(object, nestedPath, rootObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanWrapperImpl(Object object, String nestedPath, BeanWrapperImpl parent) {
/* 133 */     super(object, nestedPath, parent);
/* 134 */     setSecurityContext(parent.acc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanInstance(Object object) {
/* 145 */     this.wrappedObject = object;
/* 146 */     this.rootObject = object;
/* 147 */     this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
/* 148 */     setIntrospectionClass(object.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWrappedInstance(Object object, @Nullable String nestedPath, @Nullable Object rootObject) {
/* 153 */     super.setWrappedInstance(object, nestedPath, rootObject);
/* 154 */     setIntrospectionClass(getWrappedClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setIntrospectionClass(Class<?> clazz) {
/* 163 */     if (this.cachedIntrospectionResults != null && this.cachedIntrospectionResults.getBeanClass() != clazz) {
/* 164 */       this.cachedIntrospectionResults = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CachedIntrospectionResults getCachedIntrospectionResults() {
/* 173 */     if (this.cachedIntrospectionResults == null) {
/* 174 */       this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
/*     */     }
/* 176 */     return this.cachedIntrospectionResults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecurityContext(@Nullable AccessControlContext acc) {
/* 184 */     this.acc = acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AccessControlContext getSecurityContext() {
/* 193 */     return this.acc;
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
/*     */   @Nullable
/*     */   public Object convertForProperty(@Nullable Object value, String propertyName) throws TypeMismatchException {
/* 209 */     CachedIntrospectionResults cachedIntrospectionResults = getCachedIntrospectionResults();
/* 210 */     PropertyDescriptor pd = cachedIntrospectionResults.getPropertyDescriptor(propertyName);
/* 211 */     if (pd == null) {
/* 212 */       throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
/*     */     }
/*     */     
/* 215 */     TypeDescriptor td = cachedIntrospectionResults.getTypeDescriptor(pd);
/* 216 */     if (td == null) {
/* 217 */       td = cachedIntrospectionResults.addTypeDescriptor(pd, new TypeDescriptor(property(pd)));
/*     */     }
/* 219 */     return convertForProperty(propertyName, (Object)null, value, td);
/*     */   }
/*     */   
/*     */   private Property property(PropertyDescriptor pd) {
/* 223 */     GenericTypeAwarePropertyDescriptor gpd = (GenericTypeAwarePropertyDescriptor)pd;
/* 224 */     return new Property(gpd.getBeanClass(), gpd.getReadMethod(), gpd.getWriteMethod(), gpd.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected BeanPropertyHandler getLocalPropertyHandler(String propertyName) {
/* 230 */     PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
/* 231 */     return (pd != null) ? new BeanPropertyHandler(pd) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanWrapperImpl newNestedPropertyAccessor(Object object, String nestedPath) {
/* 236 */     return new BeanWrapperImpl(object, nestedPath, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NotWritablePropertyException createNotWritablePropertyException(String propertyName) {
/* 241 */     PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
/* 242 */     throw new NotWritablePropertyException(getRootClass(), getNestedPath() + propertyName, matches
/* 243 */         .buildErrorMessage(), matches.getPossibleMatches());
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyDescriptor[] getPropertyDescriptors() {
/* 248 */     return getCachedIntrospectionResults().getPropertyDescriptors();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
/* 253 */     BeanWrapperImpl nestedBw = (BeanWrapperImpl)getPropertyAccessorForPropertyPath(propertyName);
/* 254 */     String finalPath = getFinalPath(nestedBw, propertyName);
/* 255 */     PropertyDescriptor pd = nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(finalPath);
/* 256 */     if (pd == null) {
/* 257 */       throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
/*     */     }
/*     */     
/* 260 */     return pd;
/*     */   }
/*     */   
/*     */   private class BeanPropertyHandler
/*     */     extends AbstractNestablePropertyAccessor.PropertyHandler
/*     */   {
/*     */     private final PropertyDescriptor pd;
/*     */     
/*     */     public BeanPropertyHandler(PropertyDescriptor pd) {
/* 269 */       super(pd.getPropertyType(), (pd.getReadMethod() != null), (pd.getWriteMethod() != null));
/* 270 */       this.pd = pd;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResolvableType getResolvableType() {
/* 275 */       return ResolvableType.forMethodReturnType(this.pd.getReadMethod());
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeDescriptor toTypeDescriptor() {
/* 280 */       return new TypeDescriptor(BeanWrapperImpl.this.property(this.pd));
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public TypeDescriptor nested(int level) {
/* 286 */       return TypeDescriptor.nested(BeanWrapperImpl.this.property(this.pd), level);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object getValue() throws Exception {
/* 292 */       Method readMethod = this.pd.getReadMethod();
/* 293 */       if (System.getSecurityManager() != null) {
/* 294 */         AccessController.doPrivileged(() -> {
/*     */               ReflectionUtils.makeAccessible(readMethod);
/*     */               return null;
/*     */             });
/*     */         try {
/* 299 */           return AccessController.doPrivileged(() -> readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[])null), BeanWrapperImpl.this
/* 300 */               .acc);
/*     */         }
/* 302 */         catch (PrivilegedActionException pae) {
/* 303 */           throw pae.getException();
/*     */         } 
/*     */       } 
/*     */       
/* 307 */       ReflectionUtils.makeAccessible(readMethod);
/* 308 */       return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[])null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object value) throws Exception {
/* 316 */       Method writeMethod = (this.pd instanceof GenericTypeAwarePropertyDescriptor) ? ((GenericTypeAwarePropertyDescriptor)this.pd).getWriteMethodForActualAccess() : this.pd.getWriteMethod();
/* 317 */       if (System.getSecurityManager() != null) {
/* 318 */         AccessController.doPrivileged(() -> {
/*     */               ReflectionUtils.makeAccessible(writeMethod);
/*     */               return null;
/*     */             });
/*     */         try {
/* 323 */           AccessController.doPrivileged(() -> writeMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), new Object[] { value }), BeanWrapperImpl.this
/* 324 */               .acc);
/*     */         }
/* 326 */         catch (PrivilegedActionException ex) {
/* 327 */           throw ex.getException();
/*     */         } 
/*     */       } else {
/*     */         
/* 331 */         ReflectionUtils.makeAccessible(writeMethod);
/* 332 */         writeMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), new Object[] { value });
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeanWrapperImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */