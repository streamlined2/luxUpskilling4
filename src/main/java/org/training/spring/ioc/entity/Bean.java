package org.training.spring.ioc.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class Bean {

	private String id;
	private Object value;

}
