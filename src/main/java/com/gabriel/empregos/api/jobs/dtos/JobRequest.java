package com.gabriel.empregos.api.jobs.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.gabriel.empregos.core.enums.JobLevel;
import com.gabriel.empregos.core.enums.JobType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    @Size(min = 10, max = 255)
    private String description;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String location;

    @NotNull
    private JobType jobType;

    @NotNull
    private JobLevel jobLevel;

    @NotNull
    private BigDecimal salary;

    @NotEmpty
    private List<Long> skills;

}
