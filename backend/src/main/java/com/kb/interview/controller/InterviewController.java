package com.kb.interview.controller;

import com.kb.interview.dto.coverletter.CoverLetterAnswer;
import com.kb.interview.dto.coverletter.CoverLetterResponse;
import com.kb.interview.dto.coverletter.CoverLetterTTSRequest;
import com.kb.interview.dto.question.CoverLetterQuestionRequest;
import com.kb.interview.dto.question.CoverLetterQuestionResponse;
import com.kb.interview.dto.question.CoverLetterSubQuestionRequest;
import com.kb.interview.dto.report.Report;
import com.kb.interview.dto.tech.TechQuestion;
import com.kb.interview.dto.tech.TechQuestionResponse;
import com.kb.interview.service.InterviewService;
import com.kb.interview.service.ReportService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "InterviewController", tags = "면접")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview")
public class InterviewController {
    private final InterviewService interviewService;
    private final ReportService reportService;

    @PostMapping("/cover-letter/question")
    public ResponseEntity<List<CoverLetterQuestionResponse>> createQuestionOfCoverLetter(@RequestBody CoverLetterQuestionRequest request) {
        Report report = reportService.createReport(request.getMno(), request.getCompanyName(), request.getJob());

        List<CoverLetterQuestionResponse> response = interviewService.createQuestions(report.getRno(), request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cover-letter/sub-question")
    public ResponseEntity<CoverLetterQuestionResponse> createSubQuestionOfCoverLetter(@RequestBody CoverLetterSubQuestionRequest request) {
        CoverLetterQuestionResponse response = interviewService.createSubQuestion(request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cover-letter/question/tts")
    public ResponseEntity<HttpStatus> executeCoverLetterTTS(@RequestBody CoverLetterTTSRequest request) {
        interviewService.executeCoverLetterTTS(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/tech/question")
    public ResponseEntity<List<TechQuestionResponse>> getTechQuestions() {
        return ResponseEntity.ok(interviewService.getTechQuestions());
    }

    @GetMapping("/tech/question/${bno}")
    public ResponseEntity<TechQuestionResponse> getTechQuestion(@PathVariable("bno") int bno) {
        return ResponseEntity.ok(interviewService.getTechQuestion(bno));
    }
}