package jp.co.plus.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.plus.core.dto.UserBean;
import jp.co.plus.core.mapper.UserMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//findByUsernameで見つけてきたユーザ情報をmyUserに入れる
        UserBean myUser = userMapper.findByUsername(username);

        if (myUser == null) {
        	throw new UsernameNotFoundException("not found");
        }
        //UserDetailsにreturn
        return new LoginAccountDetails(myUser);
	}


}
