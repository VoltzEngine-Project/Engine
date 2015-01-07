package com.builtbroken.mc.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes(value= {"com.builtbroken.mc.core.annotation.TestAnnotation"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Processor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
        processingEnv.getMessager().printMessage(Kind.WARNING, "WARNING TEST!!!");
        processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, "MANDATORY WARNING TEST!!!");
        processingEnv.getMessager().printMessage(Kind.ERROR, "ERROR TEST!!!");
        
        System.out.println("TEST");
        
        for (TypeElement element : annotations)
        {
            System.out.println(element.getQualifiedName());
            System.err.println(element.getQualifiedName());
        }
        return true;
    }

}
