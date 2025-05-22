-- 1. Chat 더미 데이터 (Summary 참조용)
INSERT INTO chat (id, created_at)
VALUES (1, NOW());

-- 2. User 더미 데이터 (Post 참조용)
INSERT INTO user (user_id, password, nickname, profile_img, res_time, created_at, last_summary_date)
VALUES ('user1', 'password123', '닉네임1', 1, 1000, NOW(), NOW());

-- 3. Summary 더미 데이터 (Post 참조용)
INSERT INTO summary (summary_id, chat_id, title, content, created_at, retry_count)
VALUES (1, 1, '요약 제목입니다', '이것은 요약의 본문 내용입니다.', NOW(), 0);

-- 4. Post 더미 데이터 (User, Summary 참조)
INSERT INTO post (user_id, summary_id, is_public)
VALUES 
('user1', 1, true),
('user1', 1, false);
