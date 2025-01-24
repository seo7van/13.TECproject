package com.tecProject.tec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveHistoryDTO {
	private String requestCode;
	private String responseCode;
	private String typeCode;
}
