package org.training.spring.ioc.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Bean {

	private final String id;
	private final Object value;

}
