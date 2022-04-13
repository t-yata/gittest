package jp.co.plus.demo.form;

import java.util.List;

import jp.co.plus.demo.dto.UserListBean;
import lombok.Data;

@Data
public class UserListForm{
	
	//結果
	List<UserListBean> userList;

	//ページングカウント
	private int pagingCount;
	
	//前ページの存在チェック
	private int backPageCheck;
	
	//次ページの存在チェック
	private int nextPageCheck;

}
