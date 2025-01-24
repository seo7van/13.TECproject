package com.tecProject.tec.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecProject.tec.domain.Code;
import com.tecProject.tec.repository.TranslationRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

	private final TranslationRepository translationRepository;
	private final ObjectMapper objectMapper; // JSON 파싱용
	private final HttpServletRequest request;
	private final IpService ipService; //IpService 추가

	public TranslationService(
			TranslationRepository translationRepository, 
			ObjectMapper objectMapper, 
			HttpServletRequest request,
			IpService ipService) {
        this.translationRepository = translationRepository;
        this.objectMapper = objectMapper;
        this.request = request;
        this.ipService = ipService;
	}
	
	//회원 여부 및 IP 요청 제한 확인 후 번역
	public String translateSentenceWithChecks(String originSentence, String language, String username, HttpServletRequest request) {
        //회원 여부 확인
        boolean isMember = ipService.isMember(username);
        System.out.println("회원 여부 확인: " + (isMember ? "회원" : "비회원"));

        //IP 요청 제한 확인
        if (!ipService.isRequestAllowed(request, username)) {
            throw new IllegalStateException("요청 제한 초과");
        }
        //번역 로직 실행
        return translateSentence(originSentence, language);
    }
	
    //문장을 번역하면서 특수문자를 유지
    public String translateSentence(String originSentence, String language) {

        //특수문자와 단어를 구분하는 정규식
        String[] parts = originSentence.split("(?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9])");
        
        //결과를 담을 StringBuilder
        StringBuilder translatedSentence = new StringBuilder();

        //각 부분을 번역 또는 그대로 둠
        for (String part : parts) {
            if (part.matches("[a-zA-Z0-9]+")) { //영어 단어라면
                String translatedWord = translationRepository.findByOriginCode(part)
                        .map(Code::getTranslateCode) //번역 결과 가져오기
                        .map(translateCode -> getTranslationForLanguage(translateCode, language, part)) //언어별 번역 처리
                        .orElse(part); //번역 결과가 없으면 원본 사용
                translatedSentence.append(translatedWord);
            } else {
                translatedSentence.append(part); //특수문자는 그대로 유지
            }
        }
        //번역된 문장을 반환
        return translatedSentence.toString();
    }
    
    //JSON 데이터를 언어별로 처리하는 헬퍼 메서드
    private String getTranslationForLanguage(String translateCode, String language, String defaultValue) {
        try {
            var jsonNode = objectMapper.readTree(translateCode);
            return jsonNode.has(language) ? jsonNode.get(language).asText() : defaultValue;
            
        } catch (Exception e) {
            // JSON 파싱 실패 시 기본값 반환
            return defaultValue;
        }
    }
    
    //아이피 주소 가져오는 메서드(테스트용)
    public String getClientIp() {

        String ipAddress = request.getHeader("X-Forwarded-For"); //프록시 사용하는 경우
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr(); //일반적인 경우
        }
        return ipAddress; //사용자 아이피 주소 반환
    }

    //사용자요청 횟수 제한 메서드 ipAddress :제한 여부 true: 요청 가능, false: 요청 제한
    public boolean isRequestAllowed(String ipAddress) {
        System.out.println("요청 받은 IP: " + ipAddress);
        return true; //현재는 요청을 무조건 허용
    }
}