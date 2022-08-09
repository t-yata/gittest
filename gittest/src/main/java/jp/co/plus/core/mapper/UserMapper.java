package jp.co.plus.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.plus.core.dto.UserBean;

@Mapper
public interface UserMapper {

	public UserBean findByUsername(String username);

	public int updateSecret(String username, String secret);

}