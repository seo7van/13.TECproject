package com.tecProject.tec.controller;

import com.tecProject.tec.domain.Code;
import com.tecProject.tec.service.CodeService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code") // 공통 URL
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
    @GetMapping("/translate")
    public ResponseEntity<String> getTranslation(@RequestParam(name = "origin") String originSentence) {
        String translatedSentence = codeService.translateSentence(originSentence);
        return ResponseEntity.ok(translatedSentence);
    }

    // 모든 번역 데이터 가져옴
    @GetMapping("/all")
    public ResponseEntity<List<Code>> getAllCodes() {
    	return ResponseEntity.ok(codeService.getAllCodes());
    }

    // 특정 번역 데이터를 가져옴 (id로 찾음)
    @GetMapping("/{id}")
    public ResponseEntity<Code> getCodeById(@PathVariable Integer id) {
    	return ResponseEntity.ok(codeService.getCodeById(id));
    }

    // 새로운 번역 데이터 추가
    @PostMapping
    public ResponseEntity<Code> createCode(@RequestBody Code code) {
    	return ResponseEntity.ok(codeService.createCode(code));
    }

    // 기존 번역 데이터 수정
    @PutMapping("/{id}")
    public ResponseEntity<Code> updateCode(@PathVariable Integer id, @RequestBody Code updatedCode) {
    	return ResponseEntity.ok(codeService.updateCode(id, updatedCode));
    }

    // 번역 데이터 삭제 (id로 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCode(@PathVariable Integer id) {
    	codeService.deleteCode(id); // id로 데이터를 삭제
        return ResponseEntity.ok("번역 데이터가 삭제되었습니다.");
    }
}

