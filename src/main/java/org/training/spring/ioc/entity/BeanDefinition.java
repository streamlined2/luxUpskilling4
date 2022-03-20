package org.training.spring.ioc.entity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = { "id", "className" })
public class BeanDefinition {

	public static final Comparator<BeanDefinition> ID_CLASSNAME_COMPARATOR = Comparator.comparing(BeanDefinition::getId)
			.thenComparing(BeanDefinition::getClassName);

	private String id;
	private String className;
	private final Map<String, String> dependencies = new HashMap<>();
	private final Map<String, String> references = new HashMap<>();

	public BeanDefinition addDependency(String name, String value) {
		dependencies.put(name, value);
		return this;
	}

	public BeanDefinition addReference(String name, String value) {
		references.put(name, value);
		return this;
	}

}
