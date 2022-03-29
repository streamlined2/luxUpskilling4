package org.training.spring.ioc.bean;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PrinterBean implements Printer {

	private String name;
	private String greeting;
	private String message;

	@Override
	public void print() {
		System.out.format("%s, %s %s%n", name, greeting, message);
	}

}
