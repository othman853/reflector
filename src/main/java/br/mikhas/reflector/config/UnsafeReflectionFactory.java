package br.mikhas.reflector.config;

import java.lang.reflect.Field;

public class UnsafeReflectionFactory extends JdkReflectionFactory {

	@Override
	public FieldAccessor getFieldAcessor(Field field) {
		if (UnsafeGetter.isUnsafeAvailable()) {
			return new UnsafeFieldAccessor(field);
		} else
			return super.getFieldAcessor(field);
	}

}
