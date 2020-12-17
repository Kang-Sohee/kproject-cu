package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("DochaCarInfoDto")
public class DochaHolidayDto {

	private String holIdx;
	private String rtIdx;
	private String crIdx;
	private String holidayStartDt;
	private String holidayEndDt;
	private String holidayName;

}
