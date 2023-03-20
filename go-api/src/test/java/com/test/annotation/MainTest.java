package com.test.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MainTest {
    @Test
    public void testInherited() {

        Annotation[] annotations1 = MyInheritedClass.class.getAnnotations();
        System.out.println(Arrays.stream(annotations1).anyMatch(l -> l.annotationType().equals(IsInheritedAnnotation.class)));
        System.out.println(Arrays.stream(annotations1).noneMatch(l -> l.annotationType().equals(NoInherritedAnnotation.class)));

        System.out.println("***********");

        Annotation[] annotations2 = MyInheritedClassUseInterface.class.getAnnotations();
        System.out.println(Arrays.stream(annotations2).noneMatch(l -> l.annotationType().equals(IsInheritedAnnotation.class)));
        System.out.println(Arrays.stream(annotations2).noneMatch(l -> l.annotationType().equals(NoInherritedAnnotation.class)));

        System.out.println("***********");

        Annotation[] annotations3 = IInheritedInterface.class.getAnnotations();
        System.out.println(Arrays.stream(annotations3).anyMatch(l -> l.annotationType().equals(IsInheritedAnnotation.class)));
        System.out.println(Arrays.stream(annotations3).anyMatch(l -> l.annotationType().equals(NoInherritedAnnotation.class)));

        System.out.println("***********");

        Annotation[] annotations4 = IInheritedInterfaceChild.class.getAnnotations();
        System.out.println(Arrays.stream(annotations4).noneMatch(l -> l.annotationType().equals(IsInheritedAnnotation.class)));
        System.out.println(Arrays.stream(annotations4).noneMatch(l -> l.annotationType().equals(NoInherritedAnnotation.class)));

        System.out.println("***********");
    }
}

