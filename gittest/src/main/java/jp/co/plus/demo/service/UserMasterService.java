package jp.co.plus.demo.service;

import java.util.List;

import jp.co.plus.demo.dto.EditUserDTO;
import jp.co.plus.demo.dto.UserListDTO;

public interface UserMasterService {

	// ユーザー名取得の処理
	public String getUserName();
	
	//更新回数取得の処理
	public int getUpdateCount(EditUserDTO editUserDTO);

	// ユーザー情報更新の処理
	public String userupdate(EditUserDTO editUserDTO) throws Exception;

	// ユーザー一覧表示処理
	public List<UserListDTO> userlist(UserListDTO userListDTO);
	
	//ページングの処理
	public List<UserListDTO> paging(UserListDTO userListDTO);

}