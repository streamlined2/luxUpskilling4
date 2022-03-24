package org.training.spring.ioc.bean;

import org.training.spring.ioc.annotation.RandomValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RandomDoubleBean {
	
	@RandomValue(min = -100, max = 100)
	private double randomValue;

}
