package jp.co.plus.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.plus.demo.dto.EditUserDTO;
import jp.co.plus.demo.dto.UserListDTO;

@Mapper
public interface UserMasterMapper {
	
	//更新回数の取得
    public int updateCount(EditUserDTO editUserDTO);
	
    //ユーザー情報更新
    public int updatePassword(EditUserDTO editUserDTO);

	//ユーザー一覧
    public List<UserListDTO> userlist(UserListDTO UserListDTO);

    //ページング
	public List<UserListDTO> paging(UserListDTO UserListDTO);

}