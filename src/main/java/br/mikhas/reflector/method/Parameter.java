package br.mikhas.reflector.method;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import br.mikhas.reflector.AnnotatedElement;
import br.mikhas.reflector.annotation.AnnotationProxy;
import br.mikhas.reflector.annotation.CoreAnnotationProxy;

public class Parameter implements AnnotatedElement {
	private final Class<?> type;
	private final AnnotationProxy[] annotations;
	private final HashMap<Annotation, AnnotationProxy> annotationsMap;
	private final int index;

	public Parameter(int index, Class<?> type, Annotation[] annotations) {
		this.index = index;
		this.type = type;

		this.annotations = new AnnotationProxy[annotations.length];
		this.annotationsMap = new HashMap<Annotation, AnnotationProxy>(
				this.annotations.length);

		for (int i = 0; i < annotations.length; i++) {
			this.annotations[i] = new CoreAnnotationProxy(annotations[i]);
			this.annotationsMap.put(annotations[i], this.annotations[i]);
		}
	}

	public int index() {
		return index;
	}

	public Class<?> type() {
		return type;
	}

	public AnnotationProxy annotation(Class<? extends Annotation> annotation) {
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i].getClass() == annotation) {
				return annotations[i];
			}
		}
		return null;
	}

	public AnnotationProxy[] annotations() {
		AnnotationProxy[] aps = new AnnotationProxy[annotations.length];
		for (int i = 0; i < annotations.length; i++) {
			aps[i] = annotations[i];
		}
		return annotations;
	}

	public boolean isAnnotated(Class<? extends Annotation> annotation) {
		return this.annotationsMap.containsKey(annotation);
	}

	public boolean isAnnotated() {
		return this.annotationsMap.size() > 0;
	}

}
