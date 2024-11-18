package com.kb.interview.dto.question;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoverLetterQuestionRequest {
    private int questionCount;
    private int clno;
}
