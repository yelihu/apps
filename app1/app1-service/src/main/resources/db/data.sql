-- 初始化用户数据
INSERT INTO application.user (`username`, `password`, `real_name`, `email`, `phone`, `status`) VALUES
('admin', '$2a$10$uST3Md6w0QQxK.XsHC8Xeu5lmGbjf2EbNB7zp7hfHwpC9Yvo5.JXC', '系统管理员', 'admin@example.com', '13800138000', 1),
('test', '$2a$10$uST3Md6w0QQxK.XsHC8Xeu5lmGbjf2EbNB7zp7hfHwpC9Yvo5.JXC', '测试用户', 'test@example.com', '13800138001', 1);