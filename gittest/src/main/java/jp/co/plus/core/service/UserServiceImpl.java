package jp.co.plus.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.plus.core.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

	@Override
	@Transactional
	public int updateSecret(String username, String secret) {
        return userMapper.updateSecret(username,secret);
	}

}
