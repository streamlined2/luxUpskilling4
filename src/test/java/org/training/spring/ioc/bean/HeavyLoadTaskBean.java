package org.training.spring.ioc.bean;

import java.math.BigInteger;

import org.training.spring.ioc.annotation.Log;

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
@EqualsAndHashCode
@ToString
public class HeavyLoadTaskBean implements TaskA, TaskB {

	@Log(message = "computation of 1000000 square roots took (msec) ")
	public double taskA() {
		double result = 0;
		for (int k = 2; k < 1000000; k++) {
			result += Math.sqrt(k);
		}
		return result;
	}

	@Log(message = "calculation of factorial of 500 took (msec) ")
	public BigInteger taskB() {
		return factorial(BigInteger.valueOf(500));
	}

	private BigInteger factorial(BigInteger n) {
		if (BigInteger.ZERO.equals(n) || BigInteger.ONE.equals(n)) {
			return BigInteger.ONE;
		}
		return n.multiply(factorial(n.subtract(BigInteger.ONE)));
	}

}
