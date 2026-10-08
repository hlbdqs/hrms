# Git 协作开发与持续集成规范（实验三）

## 一、分支策略（Git Flow 简化版）

| 分支 | 用途 | 说明 |
|---|---|---|
| `main` | 生产分支 | 稳定可发布，只接受 `develop` 或 hotfix 的 PR 合并 |
| `develop` | 集成分支 | 日常开发主线，各功能分支合并到此处 |
| `feature/*` | 功能分支 | 每个功能/实验一个分支，如 `feature/employee-crud` |

典型流程：

```bash
# 1. 从 develop 拉取最新代码，创建功能分支
git checkout develop
git pull origin develop
git checkout -b feature/employee-crud

# 2. 开发 + 提交
git add .
git commit -m "feat: 员工 CRUD 接口"

# 3. 推送远程并提 PR
git push origin feature/employee-crud
# 在 GitHub 上创建 PR：feature/employee-crud -> develop

# 4. 合并后切回 develop 并拉取
git checkout develop
git pull origin develop
```

## 二、提交信息规范

采用 `type: 描述` 格式：

- `feat:` 新功能
- `fix:` 修复 bug
- `docs:` 文档
- `refactor:` 重构
- `test:` 测试
- `chore:` 构建/工具

## 三、Pull Request 与 Code Review

1. 每个功能分支完成后，提交 PR（使用 `.github/PULL_REQUEST_TEMPLATE.md` 模板）。
2. **AI 辅助评审**：用 AI 生成 PR 摘要、潜在 bug 提示，提高评审效率。
3. **人工评审为最终责任方**：AI 结论仅作参考，合并前须人工确认。
4. 合并前必须通过 CI（自动构建 + 测试）。

## 四、持续集成（GitHub Actions）

`.github/workflows/ci.yml` 在 push / PR 时自动执行：

- 后端：JDK 17 + `mvn test`（编译 + 单元测试）
- 前端：Node 20 + `npm install` + `npm run build`

## 五、常用命令

```bash
git status                 # 查看工作区状态
git diff                   # 查看改动
git log --oneline --graph  # 查看提交历史
git merge <branch>         # 合并分支
git rebase <branch>        # 变基
# 解决冲突：编辑冲突文件 -> git add -> git commit
```
