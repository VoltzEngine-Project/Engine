package com.builtbroken.mc.processor;

import com.builtbroken.mc.core.annotation.TestAnnotation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value= {"com.builtbroken.mc.core.annotation.TestAnnotation"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Processor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
        for (Element e : env.getElementsAnnotatedWith(TestAnnotation.class))
        {
            if (e.getKind() == ElementKind.CLASS)
            {
                TypeElement classElement = (TypeElement) e;
                PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

                try
                {
                    JavaFileObject jfo = processingEnv.getFiler().createSourceFile(classElement.getQualifiedName());

                    BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                    bw.append("package " + packageElement.getQualifiedName() + ";");
                    bw.newLine();
                    bw.newLine();



                    // rest of generated class contents
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

}
