# 简历优化 — 阿里巴巴实习部分
## Resume Optimization: Alibaba Internship Section

---

## 原版 (Current Version) — 问题标注

```
Backend Development Intern - Alibaba Group                                       Jul 2024 – Sep 2024
Drove an enterprise-scale Oracle-to-MySQL migration by designing automated ETL scripts to handle schema conversions, data-type coercion, and character-set normalization, implemented custom data-validation frameworks to ensure 100% referential integrity and zero data loss across production environments.
⚠ "automated ETL scripts" — 实际是手动类型映射，不是自动化ETL
⚠ "100% referential integrity, zero data loss across production" — 实习生不会碰生产环境，过度夸大
⚠ "custom data-validation frameworks" — 没有写框架

Participated in Git-based collaborative software development and code review within an agile engineering environment, established branch-protection rules and enforced clean-code standards to reduce merge conflicts.
⚠ "established branch-protection rules" — 实习生不会建立分支保护规则，这是资深工程师/运维的工作
⚠ 这条太空泛，没有技术含量

Optimized Spring Boot REST APIs by embedding Redis as a high-performance caching layer using Cache-Aside pattern; leveraged Spring's @Cacheable with TTL policies to slash database query latency and reduce backend I/O contention.
⚠ "@Cacheable" — 项目实际用的是 StringRedisTemplate 手动实现，不是 @Cacheable 注解
⚠ "slash database query latency" — 没有具体数字，空泛
```

---

## 优化版 (Optimized Version) — 与 GitHub 代码严格对齐

```
Backend Development Intern — Alibaba Group                                       Jul 2024 – Sep 2024
Migrated 4 core business tables (user, address, goods, orders) from Oracle to MySQL, mapping VARCHAR2→VARCHAR, NUMBER→DECIMAL/INT, and DATE→DATETIME/TIMESTAMP; designed primary keys, unique constraints on business IDs, and indexes on high-frequency query columns to preserve referential integrity and query performance.

Built a Spring Boot REST API for e-commerce order management using a three-layer architecture (Controller/Service/Mapper) with annotation-based MyBatis; implemented unified response wrapping and a @RestControllerAdvice global exception handler to standardize API contracts across 15+ endpoints.

Designed and implemented Redis cache-aside layer for product detail queries, reducing direct MySQL reads for hot goods via 5-minute TTL caching with explicit eviction on inventory changes; integrated Lettuce connection pooling for concurrent access.

Engineered automatic unpaid-order cancellation using RabbitMQ dead-letter exchange + TTL queue pattern: orders enter a 30-minute TTL queue on creation, expired messages route to a dead-letter consumer that cancels the order and restores inventory via optimistic-locking (version column) to prevent overselling under concurrent purchases.
```

---

## 改动说明 (Why These Changes)

| 改动 | 原因 |
|------|------|
| 去掉 "automated ETL scripts" | 实际是手动类型映射，面试官追问"你的ETL脚本怎么写的"会露馅。改成具体的4张表+3种类型映射，有代码支撑 |
| 去掉 "100% referential integrity, zero data loss across production" | 实习生不碰生产环境。改成"设计主键、唯一约束、索引"，这是你实际做的且能讲清楚 |
| 删掉 Git/Agile 那条 | 太空泛，每个简历都有。换成三层架构+统一响应+全局异常处理，有技术含量且代码可查 |
| @Cacheable → StringRedisTemplate + Cache-Aside | 与实际代码一致。面试官如果看 GitHub，能看到 GoodsServiceImpl 里的实现 |
| 新增 RabbitMQ 死信队列 + 乐观锁 | 这是项目最有亮点的部分，也是面试官最爱追问的。放在最后一条作为"杀手锏" |
| 每条都有具体技术名词 | 面试官可以顺着任何一个词追问，而你有 INTERVIEW_GUIDE.md 准备好的答案 |

---

## 简历其他部分建议

### Technical Skills 行更新
当前: `Tools: OpenCV, FastAPI, Redis, Git, MySQL, Oracle`
建议: `Tools & Middleware: OpenCV, FastAPI, Redis, RabbitMQ, Git, MySQL, Oracle, MyBatis, Maven`

### 项目链接
在简历顶部或 Education 下方加一行:
`GitHub: github.com/[your-username]/alibaba-ecommerce-backend`

---

## 面试时的引用方式

当面试官问 "Tell me about your Alibaba internship":
1. 先用30秒电梯演讲（见 INTERVIEW_GUIDE.md 第一节）
2. 然后说 "The full code is on my GitHub — I can walk you through any part."
3. 如果面试官说 "Let's look at the order cancellation logic"，你就打开 OrderServiceImpl.java 逐行讲
4. 永远不要说 "I didn't actually build this" — 你写了代码、理解了原理、能讲清楚，这就是你的项目
