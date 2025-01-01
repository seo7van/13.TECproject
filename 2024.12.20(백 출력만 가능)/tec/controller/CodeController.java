package com.tecProject.tec.controller;

import com.tecProject.tec.service.CodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code") // 번역 API 주소
public class CodeController {
	
	private final CodeService codeService;
	
    // 서비스 사용 준비
    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }
/*    
    // 사용자가 원본 코드를 보내면 번역된 결과를 반환
    @GetMapping
    public ResponseEntity<String> getTranslation(@RequestParam("origin") String originCode) {
        String translatedCode = codeService.getTranslatedCode(originCode);
        return ResponseEntity.ok(translatedCode); // 번역 결과 반환
    }
*/	
    // 문장 번역 API
    @GetMapping
    public ResponseEntity<String> getTranslation(@RequestParam("origin") String originSentence) {
        String translatedSentence = codeService.translateSentence(originSentence);
        return ResponseEntity.ok(translatedSentence);
    }

}

