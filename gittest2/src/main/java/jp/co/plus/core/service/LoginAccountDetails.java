package jp.co.plus.core.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import jp.co.plus.core.dto.UserBean;
import lombok.Getter;

public class LoginAccountDetails extends User {

	private static final long serialVersionUID = 1L;

	@Getter
	private final UserBean account;

	public LoginAccountDetails(UserBean account) {
		super(account.getUsername(), account.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
		this.account = account;
	}
}