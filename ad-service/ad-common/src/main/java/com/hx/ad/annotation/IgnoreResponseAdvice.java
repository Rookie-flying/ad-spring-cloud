package com.hx.ad.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Target翻译中文为目标，即该注解可以声明在哪些目标元素之前，也可理解为注释类型的程序元素的种类。
// ElementType.TYPE：该注解只能声明在一个类前。
// ElementType.METHOD：该注解只能声明在一个类的方法前。
// Retention 翻译成中文为保留，可以理解为如何保留，即告诉编译程序如何处理，也可理解为注解类的生命周期。
// RetentionPolicy.RUNTIME  : 注解保留在程序运行期间。
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface IgnoreResponseAdvice {
}
