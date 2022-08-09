package jp.co.plus.demo.dto;

import lombok.Data;

@Data
public class EditUserDTO{

	//ユーザー名
	private String log_id;

	//パスワード
	private String password;
	
	//パスワード再入力
	private String rePassword;
	
	//更新回数
	private int update_count;

}
