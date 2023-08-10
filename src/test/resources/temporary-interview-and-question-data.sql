-- TemporaryInterviewEntity Data
INSERT INTO temporary_interview (id, title, account_id, job_category_id, interview_period, career_year)
VALUES (1, 'Interview 1', 1, 1, 'EXPECTED_INTERVIEW', 'NEWCOMER'),
       (2, 'Interview 2', 1, 1, 'TWENTY_THREE1H', 'ONE_YEAR'),
       (3, 'Interview 3', 1, 1, 'TWENTY_TWO2H', 'TWO_YEAR'),
       (4, 'Interview 4', 1, 1, 'TWENTY_TWO1H', 'THREE_YEAR'),
       (5, 'Interview 5', 1, 1, 'TWENTY_ONE2H', 'FOUR_YEAR');

-- TemporaryQuestionEntity Data
INSERT INTO temporary_question (id, content, temporary_interview_id)
VALUES (1, 'This is question 1 for interview 1', 1),
       (2, 'This is question 2 for interview 1', 1),
       (3, 'This is question 1 for interview 2', 2),
       (4, 'This is question 2 for interview 2', 2),
       (5, 'This is question 1 for interview 3', 3);
