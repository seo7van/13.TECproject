package com.tecProject.tec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tecProject.tec.domain.User;
import com.tecProject.tec.repository.UserRepository;
import com.tecProject.tec.security.UserDetail;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Override
    public UserDetail loadUserByUsername(String userId) throws UsernameNotFoundException {
        
		// 데이터베이스에서 사용자 검색
        User user = userRepository.findByUserId(userId) 
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));
        
        return new UserDetail(user); //인증된 사용자 반환
    }
	
	// 새로운 사용자 등록
    public void userInsert(User user) {
        userRepository.save(user);
    }
}
