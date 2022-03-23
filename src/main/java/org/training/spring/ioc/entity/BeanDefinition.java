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
	public static final Comparator<BeanDefinition> REFERENCE_COUNTER_COMPARATOR = new ReferenceCounterComparator();

	private String id;
	private String className;
	private Class<?> classReference;
	private final Map<String, String> dependencies = new HashMap<>();
	private final Map<String, String> refDependencies = new HashMap<>();

	public BeanDefinition addDependency(String name, String value) {
		dependencies.put(name, value);
		return this;
	}

	public BeanDefinition addRefDependency(String name, String value) {
		refDependencies.put(name, value);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id: ").append(id).append(",").append(" class name: ").append(className).append(", values: ");
		dependencies.forEach((key, value) -> builder.append(key).append("=").append(value).append(","));
		builder.append(" references: ");
		refDependencies.forEach((key, value) -> builder.append(key).append("=").append(value).append(","));
		return builder.toString();
	}

	private static class ReferenceCounterComparator implements Comparator<BeanDefinition> {

		@Override
		public int compare(BeanDefinition o1, BeanDefinition o2) {
			if (o1.refDependencies.size() < o2.refDependencies.size()) {
				return -1;
			} else if (o1.refDependencies.size() > o2.refDependencies.size()) {
				return 1;
			}
			return ID_CLASSNAME_COMPARATOR.compare(o1, o2);
		}

	}

}
