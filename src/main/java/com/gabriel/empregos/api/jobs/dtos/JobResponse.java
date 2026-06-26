package com.gabriel.empregos.api.jobs.dtos;

import java.math.BigDecimal;

import com.gabriel.empregos.core.enums.JobLevel;
import com.gabriel.empregos.core.enums.JobType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private String title;
    private String description;
    private String company;
    private String location;
    private JobType jobType;
    private JobLevel jobLevel;
    private BigDecimal salary;


}
