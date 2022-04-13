package jp.co.plus.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jp.co.plus.core.dto.UserBean;
import jp.co.plus.core.service.LoginAccountDetails;

public final class LoginAccountHolder {

	public static UserBean get() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		LoginAccountDetails principal = (LoginAccountDetails) auth.getPrincipal();

		return principal.getAccount();
	}
}
