# 🧱 Eslite – Secure Vault Management System 🔐

> **Privacy & Security First** — A Spring Boot project built to protect sensitive data with **encryption**, **hashing**, and **zero-trust design**.

---

## 🚀 Overview

**Eslite** is a **secure vault system** built with **Spring Boot** and **Java 17**, designed to demonstrate strong security principles:

- 🔐 AES-256-GCM encryption for sensitive data
- 🧂 PBKDF2 key derivation for strong encryption keys
- 🔑 JWT-based authentication for stateless API sessions
- 🧱 BCrypt password hashing for user credentials
- 🧭 Role-based access control and clean security config

This project is ideal for:
- Security-conscious developers 🧠
- Demonstrations of **data privacy and encryption**
- Building blocks for **secure backend services**

---

## 🧠 Features

### 🔑 **Authentication & Authorization**
- Secure login and signup with **BCrypt-hashed passwords**
- **JWT tokens** (HS512) for API authentication
- Verification tokens for account activation

### 🧱 **Vault System**
- Store and manage sensitive entries
- Each record is **encrypted using AES-256-GCM**
- Keys derived from user secrets via **PBKDF2**

### 🔒 **Data Protection**
- All user passwords are **irreversible hashes**
- All vault data is **confidential even if DB is leaked**
- Secrets never stored in plaintext

### 🧰 **Security Config**
- CSRF disabled for REST APIs (stateless)
- CORS configured for specific frontend origins
- Stateless JWT filter for all secured endpoints

---

## 🧪 Tech Stack

| Layer | Technology |
|-------|-------------|
| 🧱 Backend | Spring Boot 3.x |
| 🔐 Security | Spring Security, JWT |
| 🧂 Crypto | AES-256-GCM, PBKDF2, BCrypt |
| 💾 Database | MySQL / PostgreSQL |
| 🧰 Build | Maven |
| ☁️ Deployment | Any Java-compatible server |

---

## 🛡️ Security Architecture

| Component | Implementation |
|-----------|----------------|
| **Passwords** | Hashed with BCrypt |
| **Vault Data** | Encrypted with AES-256-GCM |
| **Key Derivation** | PBKDF2WithHmacSHA256 (salt + 65k iterations) |
| **Authentication** | JWT tokens (HS512) |
| **Authorization** | Role-based |
| **Transport Security** | Use HTTPS in production |
| **Logs** | Debug logging disabled in production |

---

## 📦 Project Structure

```
src/
 ├── main/
 │   ├── java/com/devIntern/eslite/
 │   │   ├── controller/      # REST Controllers
 │   │   ├── model/           # Entities (User, Vault, etc.)
 │   │   ├── service/         # Business Logic
 │   │   ├── AESUtil/         # Encryption Helpers
 │   │   └── security/        # JWT & Config
 │   └── resources/
 │       ├── application.properties
 │       └── static/
 └── test/                    # Unit tests
```

---

## ⚙️ Setup Instructions

### ✅ Prerequisites
- Java 17+
- Maven 3+
- MySQL/PostgreSQL
- (Optional) Docker

### 🧱 Run Locally

```bash
# 1. Clone repository
git clone https://github.com/yourusername/eslite.git
cd eslite

# 2. Configure environment
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Update DB credentials and secrets

# 3. Build
mvn clean install

# 4. Run
mvn spring-boot:run
```

### 🧪 API Testing
Use **Postman** or **cURL**:
- `POST /auth/register`
- `POST /auth/login`
- `POST /vault/create`
- `GET /vault/all`

---

## 🧱 Environment Variables

| Variable | Description |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | Secret for token signing |
| `FRONTEND_URL` | Allowed CORS origin |

**⚠️ Never commit secrets** — use `.env` and `.gitignore`.

---

## 🔐 Security Considerations

✅ **Safe if DB is stolen:**  
Data is **encrypted and hashed**, unreadable without keys.

✅ **JWT tokens signed securely:**  
Use **long random secret** and rotate regularly.

⚠️ **Outdated library warning:**  
Consider upgrading `jjwt` to `0.11.5` with **RSA keys** for better security.

⚠️ **Production tips:**
- Disable DEBUG logs
- Use HTTPS
- Store keys in **Vault / Secrets Manager**
- Add **rate limiting** and **refresh tokens**

---

## 🧠 Roadmap

- [ ] Upgrade JWT to v0.11.5
- [ ] Implement refresh token system
- [ ] Add rate limiting
- [ ] Integrate monitoring & audit logs
- [ ] Dockerize

---

## 🧾 License

This project is licensed under the **MIT License** — feel free to use and modify.

---

## 👨‍💻 Author

**Ashish Kumar**  
Security-focused Java Developer  
📧 ashishchaudhary9065@gmail.com  
🌐 [LinkedIn](https://www.linkedin.com/in/ashish-kumar-0333b8373) • [GitHub](https://github.com/ashishchaudhary9523)

---

> “Security is not a feature — it’s a mindset.”
