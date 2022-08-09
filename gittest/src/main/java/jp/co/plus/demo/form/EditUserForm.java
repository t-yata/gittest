package jp.co.plus.demo.form;

import lombok.Data;

@Data
public class EditUserForm{

	//ユーザー名
	private String userName;

	//パスワード
	private String password;
	
	//パスワード再入力
	private String re_password;
	
	//更新回数
	private int updateCount;

}
