package org.hoongoin.interviewbank.interview.service;

import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionCommandService {

	private final InterviewRepository interviewRepository;

}
