# E-commerce Order Management Backend

> Spring Boot REST API for an e-commerce order system — built during Alibaba Backend Development Internship (Jul–Sep 2024).

This project integrates four core backend capabilities into one cohesive service:

1. **Oracle → MySQL schema migration** — four business tables (user, address, goods, orders) migrated from Oracle `VARCHAR2/NUMBER/DATE` to MySQL `VARCHAR/DECIMAL/DATETIME`.
2. **CRUD REST API** — three-layer architecture (Controller → Service → Mapper) with MyBatis annotation-based data access.
3. **Redis caching** — Cache-Aside pattern on hot goods detail, reducing MySQL read load.
4. **RabbitMQ delayed order cancellation** — dead-letter exchange + TTL queue to auto-cancel unpaid orders after 30 minutes and restore inventory.

---

## Tech Stack

| Layer        | Technology |
|--------------|------------|
| Language     | Java 17 |
| Framework    | Spring Boot 2.7.18 |
| ORM          | MyBatis (annotation-based) |
| Database     | MySQL 8.0 |
| Cache        | Redis (Spring Data Redis / Lettuce) |
| Message Queue| RabbitMQ (Spring AMQP) |
| Build        | Maven |
| Validation   | Jakarta Bean Validation |

---

## Architecture

```
┌────────────┐     HTTP / JSON      ┌──────────────────────────────────────────┐
│  Frontend  │ ───────────────────▶ │            Spring Boot App               │
│  (Vue.js)  │ ◀─────────────────── │                                          │
└────────────┘   {code,msg,data}    │  ┌──────────┐  ┌──────────┐  ┌────────┐ │
                                    │  │Controller│→│ Service  │→│ Mapper │ │
                                    │  └──────────┘  └────┬─────┘  └───┬────┘ │
                                    │                     │             │      │
                                    │         ┌───────────┼───┐         │      │
                                    │         ▼           ▼   ▼         ▼      │
                                    │    ┌────────┐ ┌─────────┐ ┌──────────┐  │
                                    │    │ Redis  │ │RabbitMQ │ │  MySQL   │  │
                                    │    │(cache) │ │  (MQ)   │ │  (DB)    │  │
                                    │    └────────┘ └─────────┘ └──────────┘  │
                                    └──────────────────────────────────────────┘
```

### Order Creation Flow

```
Client → OrderController.create()
         │
         ├─ validate user / goods / address
         ├─ deduct inventory (optimistic lock, retry once on conflict)
         ├─ INSERT order (state = PENDING_PAY)
         └─ send message → RabbitMQ TTL queue
                                   │
                            (30 min TTL expires)
                                   │
                                   ▼
                          Dead-Letter Exchange → DLX Queue
                                   │
                                   ▼
                          OrderTimeoutConsumer
                          ├─ check state == PENDING_PAY?
                          ├─ YES → cancel order + restore stock
                          └─ NO  → skip (already paid)
```

---

## Database Schema

Four tables migrated from Oracle to MySQL. Key type mappings:

| Oracle Type | MySQL Type | Used For |
|-------------|------------|----------|
| `VARCHAR2(n BYTE)` | `VARCHAR(n)` | IDs, names, URLs |
| `NUMBER(p,s)` | `DECIMAL(p,s)` | prices, balances |
| `NUMBER` (no precision) | `INT` | inventory, sale_volume |
| `DATE` | `DATETIME` / `TIMESTAMP` | create_time, update_time |

- **user** — user profile, wallet balance
- **address** — shipping addresses linked to user
- **goods** — product catalog with inventory + optimistic-lock `version`
- **orders** — order lifecycle: `PENDING_PAY → PAID → SHIPPED → COMPLETED`, or `CANCELLED`

Full DDL: [`sql/schema.sql`](sql/schema.sql) · Seed data: [`sql/data.sql`](sql/data.sql)

---

## API Endpoints

### Users
| Method | Path | Description |
|--------|------|-------------|
| GET    | `/api/users/{userId}` | Get user by ID |
| GET    | `/api/users?page=&size=` | Paginated user list |
| POST   | `/api/users` | Create user |
| PUT    | `/api/users` | Update user |
| DELETE | `/api/users/{userId}` | Delete user |

### Addresses
| Method | Path | Description |
|--------|------|-------------|
| GET    | `/api/addresses/{addressId}` | Get address |
| GET    | `/api/addresses/user/{userId}` | List user's addresses |
| POST   | `/api/addresses` | Create address |
| PUT    | `/api/addresses` | Update address |
| DELETE | `/api/addresses/{addressId}` | Delete address |

### Goods
| Method | Path | Description |
|--------|------|-------------|
| GET    | `/api/goods/{goodsId}` | Get goods detail (**Redis cached**) |
| GET    | `/api/goods?page=&size=` | Paginated goods list |
| POST   | `/api/goods` | Add goods |

### Orders
| Method | Path | Description |
|--------|------|-------------|
| POST   | `/api/orders` | Create order (deducts stock + schedules timeout) |
| GET    | `/api/orders/{orderId}` | Get order detail |
| GET    | `/api/orders/user/{userId}` | List user's orders |
| POST   | `/api/orders/{orderId}/pay` | Pay for order |
| POST   | `/api/orders/{orderId}/cancel` | Cancel unpaid order |

All responses use the unified format:
```json
{ "code": 200, "message": "success", "data": { ... } }
```

---

## Key Design Decisions

### 1. Cache-Aside Pattern for Goods
- **Read**: check Redis → miss → query MySQL → write Redis with 5-min TTL → return.
- **Write**: update MySQL → delete Redis key (evict).
- Why Cache-Aside? Simplest to reason about; application controls cache lifecycle; no write-through complexity.

### 2. Delayed Queue via Dead-Letter Exchange (not the delayed-message plugin)
- Messages enter a TTL queue (`x-message-ttl = 30 min`).
- On expiry, they are routed via `x-dead-letter-exchange` to a DLX queue.
- Consumer on DLX queue performs the cancellation.
- Why? No dependency on the commercial RabbitMQ delayed plugin; works on standard RabbitMQ installation.

### 3. Optimistic Locking for Inventory
- `goods.version` field increments on each stock deduction.
- `UPDATE ... WHERE version = ?` returns 0 rows on conflict → retry once → fail with "out of stock".
- Prevents overselling under concurrent orders without heavy row locks.

### 4. Unified Response + Global Exception Handler
- Every endpoint returns `Result<T>` with `code`, `message`, `data`.
- `CommonException` (business error) + `@RestControllerAdvice` ensures consistent error JSON, no stack traces leaked to frontend.

---

## How to Run

### Prerequisites
- JDK 17+
- Maven 3.6+
- MySQL 8.0
- Redis 6+
- RabbitMQ 3.8+ (with management plugin recommended)

### Steps

```bash
# 1. Start MySQL, Redis, RabbitMQ
#    (Docker example)
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0
docker run -d -p 6379:6379 redis:7-alpine
docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# 2. Create database and tables
mysql -u root -proot < sql/schema.sql
mysql -u root -proot < sql/data.sql

# 3. Update application.yml if your credentials differ
#    src/main/resources/application.yml

# 4. Build and run
mvn clean package
java -jar target/ecommerce-backend-1.0.0.jar

# 5. Test
curl http://localhost:8080/api/goods/G3001
```

---

## Project Structure

```
src/main/java/com/alibaba/internship/
├── EcommerceApplication.java      # Spring Boot entry point
├── common/                        # Result, ResultCode, CommonException, GlobalExceptionHandler
├── config/                        # RedisConfig, RabbitMQConfig
├── entity/                        # User, Address, Goods, Order
├── dto/                           # OrderCreateRequest
├── mapper/                        # MyBatis interfaces (annotation-based)
├── service/                       # Service interfaces
│   └── impl/                      # Service implementations (Redis + MQ logic)
├── controller/                    # REST controllers
└── mq/                            # OrderTimeoutProducer, OrderTimeoutConsumer
```

---

## License

This project is for educational and portfolio demonstration purposes.
