package com.tecProject.tec.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecProject.tec.domain.Code;
import com.tecProject.tec.repository.CodeRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CodeService {
	
	private final CodeRepository codeRepository;
	private final ObjectMapper objectMapper; // JSON 파싱용
	
	//리포지토리 사용 준비
	public CodeService(CodeRepository codeRepository, ObjectMapper objectMapper) {
        this.codeRepository = codeRepository;
        this.objectMapper = objectMapper;
    }
    
    // 문장을 번역하면서 특수문자를 유지
    public String translateSentence(String originSentence, String language) {
        // 특수문자와 단어를 구분하는 정규식
        String[] parts = originSentence.split("(?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9])");
        
        // 결과를 담을 StringBuilder
        StringBuilder translatedSentence = new StringBuilder();

        // 각 부분을 번역 또는 그대로 둠
        for (String part : parts) {
            if (part.matches("[a-zA-Z0-9]+")) { // 영어 단어라면
                String translatedWord = codeRepository.findByOriginCode(part)
                        .map(Code::getTranslateCode) // 번역 결과 가져오기
                        .map(translateCode -> getTranslationForLanguage(translateCode, language, part)) // 언어별 번역 처리
                        .orElse(part); // 번역 결과가 없으면 원본 사용
                translatedSentence.append(translatedWord);
            } else {
                translatedSentence.append(part); // 특수문자는 그대로 유지
            }
        }
        // 번역된 문장을 반환
        return translatedSentence.toString();
    }
<<<<<<< Updated upstream
    // JSON 데이터를 언어별로 처리하는 헬퍼 메서드
    private String getTranslationForLanguage(String translateCode, String language, String defaultValue) {
        try {
            var jsonNode = objectMapper.readTree(translateCode);
            return jsonNode.has(language) ? jsonNode.get(language).asText() : defaultValue;
        } catch (Exception e) {
            // JSON 파싱 실패 시 기본값 반환
            return defaultValue;
        }
    }
    // 데이터베이스에서 모든 데이터 가져옴
    public List<Code> getAllCodes() {
        return codeRepository.findAll();
=======
    
    public List<Code> getAllCodes() {
        return codeRepository.findAll(); // 데이터베이스에서 모든 데이터를 가져옵니다
>>>>>>> Stashed changes
    }
    
    // 특정 번역 데이터를 id로 가져옴
    public Code getCodeById(Integer id) {
        return codeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("번역 데이터를 찾을 수 없습니다."));
    }
    // 새로운 번역 데이터 추가
    public Code createCode(Code code) {
        return codeRepository.save(code); // 새로운 데이터를 저장
    }
    
    // 번역 데이터를 수정
    public Code updateCode(Integer id, Code updatedCode) {
        Code existingCode = getCodeById(id); // 기존 데이터 가져옴
        existingCode.setOriginCode(updatedCode.getOriginCode()); // 새로운 데이터 덮어씌움
        existingCode.setTranslateCode(updatedCode.getTranslateCode());
        return codeRepository.save(existingCode); // 수정된 데이터 저장
    }

    public void deleteCode(Integer id) { // 번역 데이터 삭제
        codeRepository.deleteById(id); // 데이터베이스에서 데이터 삭제
    }
}

