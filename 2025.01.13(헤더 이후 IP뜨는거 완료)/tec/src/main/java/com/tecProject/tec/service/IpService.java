package com.tecProject.tec.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tecProject.tec.auth.IpUtil;
import com.tecProject.tec.auth.JWTUtil;
import com.tecProject.tec.domain.Ip;
import com.tecProject.tec.domain.User;
import com.tecProject.tec.repository.IpRepository;
import com.tecProject.tec.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IpService {
	
	 private final IpRepository ipRepository;
	 private final UserRepository userRepository;
	 private final IpUtil ipUtil;
	 private final JWTUtil jwtUtil;
	 
	    private static final int NON_MEMBER_LIMIT = 30; //비회원 요청 제한
	    private static final int MEMBER_LIMIT = 50;     //회원 요청 제한
	    

	    public String checkMembershipAndIp(String token, HttpServletRequest request) {
	        String username = "비회원"; //기본값: 비회원
	        String userType = "ROLE_GUEST"; //기본값: 비회원 역할
	        boolean isMember = false; //기본값: 비회원

	        //1. JWT 토큰 검증 및 사용자 정보 추출
	        if (token != null && token.startsWith("Bearer ")) {
	            try {
	                token = token.replace("Bearer ", "");
	                username = jwtUtil.getUsername(token); //사용자 이름 추출
	                userType = jwtUtil.getRole(token); //사용자 역할 추출
	                isMember = "ROLE_ADMIN".equals(userType) || "ROLE_USER".equals(userType); //회원 여부 확인
	            } catch (Exception e) {
	                //JWT 검증 실패 시 비회원으로 처리
	                System.err.println("JWT 처리 오류: " + e.getMessage());
	            }
	        } else {
	            System.out.println("Authorization 헤더가 없거나 잘못된 형식입니다. 비회원으로 처리합니다.");
	        }

	        //2. 클라이언트 IP 가져오기
	        String ipAddress = ipUtil.getClientIp(request);

	        //3. 결과 출력 (디버깅 용도)
	        System.out.println("요청 사용자 이름: " + username);
	        System.out.println("요청 사용자 역할: " + userType);
	        System.out.println("회원 여부: " + (isMember ? "회원" : "비회원"));
	        System.out.println("클라이언트 IP: " + ipAddress);

	        //4. 결과 메시지 반환
	        return String.format("클라이언트 IP: %s, 사용자: %s, 회원 여부: %s",
	                ipAddress, username, isMember ? "회원" : "비회원");
	    }
	
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//		//회원 여부 및 IP 요청 제한 확인 후 번역
//		public String translateSentenceWithChecks(String originSentence, String language, String username, HttpServletRequest request) {
//	        //회원 여부 확인
//	        boolean isMember = ipService.isMember(username);
//	        System.out.println("회원 여부 확인: " + (isMember ? "회원" : "비회원"));
//
//	        //IP 요청 제한 확인
//	        if (!ipService.isRequestAllowed(request, username)) {
//	            throw new IllegalStateException("요청 제한 초과");
//	        }
//	        //번역 로직 실행
//	        return translateSentence(originSentence, language);
//	    }
	    
	    
	    
        //클라이언트의 IP 주소가 요청 가능한지 확인하고 요청 횟수를 업데이트
	    @Transactional
	    public boolean isRequestAllowed(HttpServletRequest request, String username) {
	    
	    	
	    	//IpUtil을 사용해 클라이언트의 IP 가져오기
	    	String ipAddressString = ipUtil.getClientIp(request); // IpUtil의 인스턴스 메서드 호출
	        
	        //회원 여부 확인
	        User user = userRepository.findByUsername(username);
	        boolean isMember = user != null && Boolean.TRUE.equals(user.getIsMember());
	        System.out.println("회원 여부 확인: " + (isMember ? "회원" : "비회원"));
	        
	        //IP 정보를 데이터베이스에서 가져옴
	        Optional<Ip> optionalIpResponse = ipRepository.findByIpAddress(ipAddressString);

	        //IP 정보가 없으면 새로 생성
	        Ip ipResponse = optionalIpResponse.orElseGet(() -> createNewIpResponse(ipAddressString));

	        //현재 요청 제한을 초과했는지 확인, 회원인지 여부에 따라 요청 제한 설정
	        int maxRequestLimit = isMember ? MEMBER_LIMIT : NON_MEMBER_LIMIT;
	        
	        if (ipResponse.getRequestCount() >= maxRequestLimit) {
	            return false; // 요청 제한 초과
	        }

	        //요청 가능: 요청 횟수를 증가, 마지막 요청 시간을 갱신
	        ipResponse.setRequestCount(ipResponse.getRequestCount() + 1);
	        ipResponse.setLastRequest(LocalDateTime.now());
	        ipRepository.save(ipResponse);

	        return true; //요청 가능
	    }

	    private Ip createNewIpResponse(String ipAddress) {
	        Ip newIpResponse = new Ip();
	        newIpResponse.setIpAddress(ipAddress);
	        newIpResponse.setRequestCount(0); // 초기 요청 횟수는 0
	        newIpResponse.setLastRequest(LocalDateTime.now()); // 현재 시간으로 초기화
	        return ipRepository.save(newIpResponse);
	    }
	    
	    //회원 여부 확인 메서드
	    public boolean isMember(String username) {
	        User user = userRepository.findByUsername(username);
	        if (user == null) {
	            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
	        }
	        return user.getIsMember() != null && user.getIsMember();
	    }

	    //클라이언트의 요청 제한 정보를 초기화
	    @Transactional
	    public void resetRequestCount(HttpServletRequest request) {
	    	
	    	String ipAddressString = ipUtil.getClientIp(request);
	    	
	    	Optional<Ip> optionalIpResponse = ipRepository.findByIpAddress(ipAddressString);
	        
	        if (optionalIpResponse.isPresent()) {
	            Ip ipResponse = optionalIpResponse.get();
	            ipResponse.setRequestCount(0); //요청 횟수 초기화
	            ipRepository.save(ipResponse);
	        }
	    }

	    // 클라이언트 IP 가져오기 (프록시 및 기본 환경 지원)
	    public String getClientIp(HttpServletRequest request) {
	        String ip = extractIpFromHeaders(request); // 헤더에서 IP 가져오기
	        if (ip == null || ip.isEmpty()) {
	            ip = request.getRemoteAddr(); // 기본 클라이언트 IP 가져오기
	        }
	        // 여러 IP가 전달된 경우 첫 번째 IP를 반환
	        if (ip != null && ip.contains(",")) {
	            ip = ip.split(",")[0].trim();
	        }
	        return ip;
	    }
	    
	    // 헤더에서 클라이언트 IP 추출
	    private String extractIpFromHeaders(HttpServletRequest request) {
	        String[] headersToCheck = {
	            "X-Forwarded-For", // 프록시 환경에서 클라이언트 IP
	            "Proxy-Client-IP", // Apache Httpd 서버
	            "WL-Proxy-Client-IP", // 웹로직
	            "HTTP_CLIENT_IP", // 일부 프록시 서버
	            "HTTP_X_FORWARDED_FOR" // 기타 프록시 서버
	        };

	        for (String header : headersToCheck) {
	            String ip = request.getHeader(header);
	            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
	                return ip; // 유효한 IP가 있으면 반환
	            }
	        }
	        return null; // 유효한 IP가 없으면 null 반환
	    }
	    
	    
//	    //아이피 주소 가져오는 메서드(테스트용)
//	    public String getClientIp() {
//
//	        String ipAddress = request.getHeader("X-Forwarded-For"); //프록시 사용하는 경우
//	        if (ipAddress == null || ipAddress.isEmpty()) {
//	            ipAddress = request.getRemoteAddr(); //일반적인 경우
//	        }
//	        return ipAddress; //사용자 아이피 주소 반환
//	    }
//
//	    //사용자요청 횟수 제한 메서드 ipAddress :제한 여부 true: 요청 가능, false: 요청 제한
//	    public boolean isRequestAllowed(String ipAddress) {
//	        System.out.println("요청 받은 IP: " + ipAddress);
//	        return true; //현재는 요청을 무조건 허용
	    
	    
    
//
//	private final IpResponseRepository ipRepository;
//	
//	//서비스 생성자
//	public IpResponseService(IpResponseRepository ipRepository) {
//		this.ipRepository=ipRepository;
//	}
//	
//	//사용자의 IP주소를 가져오는 메서드
//	public String getClientIp() {
//        //현재 사용자의 IP 주소를 반환
//        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//                .getRequest().getRemoteAddr();
//    }
//
//    //사용자가 요청 가능한지 확인하는 메서드
//    public boolean isRequestAllowed(String ipAddress) {
//        //데이터베이스에서 IP 정보를 찾습니다.
//        Optional<IpResponse> ipOpt = ipRepository.findByIpAddress(ipAddress);
//
//        if (ipOpt.isPresent()) {
//            IpResponse ip = ipOpt.get();
//
//            //요청 제한 (예: 10번 이상 요청한 경우)
//            if (ip.getRequestCount() >= 10) {
//                return false; //요청 불가능
//            }
//
//            //요청 횟수 증가 후 데이터베이스에 저장
//            ip.setRequestCount(ip.getRequestCount() + 1);
//            ip.setLastRequest(LocalDateTime.now());
//            ipRepository.save(ip);
//        } else {
//            //처음 요청하는 IP인 경우 새로 저장
//            ipRepository.save(new IpResponse(null, ipAddress, 1, LocalDateTime.now()));
//        }
//
//        return true; //요청 가능
//    }
}
