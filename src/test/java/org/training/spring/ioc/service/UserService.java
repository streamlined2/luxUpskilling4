package org.training.spring.ioc.service;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
@EqualsAndHashCode
public class UserService {

	private MailService mailService;

}
