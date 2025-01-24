package com.tecProject.tec.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.domain.Code;
import com.tecProject.tec.service.AdminService;

@RestController
@RequestMapping("/admin/code")
public class AdminController {

	private final AdminService adminService;
	
    // 서비스 사용 준비
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    // 자동완성 키워드 검색 기능
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSeggestions(@RequestParam("query") String query) {
    	List<String> suggestions = adminService.getSuggestions(query);
    	return ResponseEntity.ok(suggestions);
    }
    
    // 특정 키워드의 상세 번역 정보 조회
    @GetMapping("/details")
    public ResponseEntity<Code> getDetails(@RequestParam("keyword") String keyword) {
    	return adminService.getCodeDetails(keyword)
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }

    // 번역데이터 저장 또는 수정 기능
    @PostMapping("/addKeyword")
    public ResponseEntity<?> saveCode(@RequestBody Code code) {
        if (code.getOriginCode() == null || code.getOriginCode().trim().isEmpty()) {        	
            return ResponseEntity.badRequest().body("originCode는 null이 될 수 없습니다.");
        }
        if (code.getTranslateCode() == null || code.getTranslateCode().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("translateCode는 null이 될 수 없습니다.");
        }
        
    	Code savedCode = adminService.saveCode(code);
    	return ResponseEntity.ok(savedCode);
    }
}