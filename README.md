## Git Workflow

- main: code production (stable)
- develop: code tổng hợp
- feature/*: code từng chức năng

Quy trình:
1. Tạo branch từ develop:
   git checkout develop
   git checkout -b feature/auth

2. Code xong → commit:
   git commit -m "feat: login API"

3. Push:
   git push origin feature/auth

4. Tạo Pull Request → merge vào develop
